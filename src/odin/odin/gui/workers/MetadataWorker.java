/* Class name: MetadataWorker
 * File name:  MetadataWorker.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    15-May-2011 21:40:00
 * Modified:   03-Jul-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.003  03-Jul-2011 Fixed language bug
 * 0.002  13-Jun-2011 Fixed issues with how status and languages are displayed 
 * 0.001  15-May-2011 Initial build
 */

package odin.odin.gui.workers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;
import odin.odin.object.table.GenericTable;
import odin.odin.repository.SaxonRepository;
import odin.shared.xml.*;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.WriteOutContentHandler;

/**
 * This class is used by Odin to perform the document identification tasks like
 * those found in Sleipnir such as:
 * <ul>
 *   <li>Document type identification</li>
 *   <li>Language identification</li>
 *   <li>Metadata detection</li>
 *   <li>Policy checking</li>
 * </ul>
 * <p>This class can be used by both BulkUploader functions as well as agents running
 * within Odin checking for incorrect metadata.
 * @version 0.003
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class MetadataWorker extends SwingWorker
{
  private File f;
  private ObjectFactory factory;
  private Document.Metadata.Author authorDetails;
  private Document.Metadata.Keywords generalKeywords;
  private static final int PROGRESS_COLUMN = 1;
  private static final int LANGUAGE_COLUMN = 2;
  private static final int COMMENT_COLUMN = 3;
  private GenericTable myTable;
  private int myPosition;
  private TreeMap<String,String> tmLang;
  
  /**
   * Initialise the class with the file to be examined along with any details that
   * need to be applied to it (author and keywords).
   * @param aFile         The file to be examined
   * @param author        The author's details
   * @param keywords      Any keywords to be stored along with the document
   * @param resultsTable  The table to be updated with the threads progress
   * @param myRow         The row within the table relating to this particular thread
   */
  public MetadataWorker(File aFile,
                          Document.Metadata.Author author,
                          Document.Metadata.Keywords keywords,
                          GenericTable resultsTable,
                          int myRow)
  {
    f = aFile;
    factory = new ObjectFactory();
    authorDetails = author;
    generalKeywords = keywords;
    myTable = resultsTable;
    myPosition = myRow;
    tmLang = new TreeMap<String,String>();
    Locale lHome = new Locale("en","GB");
    // Get the complete list of languages supported within Locale.
    String[] listLangs = Locale.getISOLanguages();
    // Iterate through the list and add them to the TreeMaps
    // Start with languages
    for (String lang : listLangs)
    {
      String name = (new Locale(lang)).getDisplayLanguage(lHome);
      tmLang.put(name,lang);
    }
  }
  
  /**
   * Performs the scanning task within a worker thread using <code>SwingWorker</code>.
   * The thread interacts with the table provided in the constructor to provide feedback.
   * @return Nothing is returned (Void)
   * @see javax.swing.SwingWorker
   * @see odin.odin.object.table.GenericTable
   */
  public Void doInBackground()
  { 
    try
    {
      myTable.setValueAt(new Integer(1), myPosition, PROGRESS_COLUMN);
      // Load the file
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
      // Start building the document from the ObjectFactory
      Document thisDoc = factory.createDocument();
      Document.Metadata docMeta = factory.createDocumentMetadata();
      // Initialise the Document with the current user details
      thisDoc.setUsername(System.getProperty("user.name"));
      String localaddress = "127.0.0.1";
      try
      {
        localaddress = java.net.InetAddress.getLocalHost().toString();
      }
      catch (UnknownHostException ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        thisDoc.setIpaddress(localaddress);
      }
      myTable.setValueAt(new Integer(2), myPosition, PROGRESS_COLUMN);
      try
      {
        javax.activation.DataHandler dhDoc = new javax.activation.DataHandler(f.toURI().toURL());
        thisDoc.setDocument(dhDoc);
      }
      catch (MalformedURLException ex)
      {
        ex.printStackTrace();
      }
      myTable.setValueAt(new Integer(3), myPosition, PROGRESS_COLUMN);
      // Initialise the variables for Tika
      Metadata meta = new Metadata();
      ParseContext pc = new ParseContext();
      StringWriter sw = new StringWriter();
      WriteOutContentHandler woch = new WriteOutContentHandler(sw);
      AutoDetectParser adp = new AutoDetectParser();
      // This populates the AutoDetectParser fields but closes the BufferedInputStream
      adp.parse(bis, woch, meta, pc);
      // Force it close
      bis.close();
      // Detect the language
      String content = sw.toString();
      LanguageIdentifier li = new LanguageIdentifier(content);
      Document.Metadata.Locale locale = factory.createDocumentMetadataLocale();
      locale.setLanguage(li.getLanguage());
      locale.setCountry(System.getProperty("user.country"));
      docMeta.setLocale(locale);
      // Close the language detection objects 
      sw = null;
      content = null;
      System.gc();
      
      myTable.setValueAt(new Integer(4), myPosition, PROGRESS_COLUMN);
      java.util.Locale lang = new java.util.Locale(li.getLanguage());
      myTable.setValueAt(lang.getDisplayLanguage(), myPosition, LANGUAGE_COLUMN);
      
      // Re-open so that we can parse the file
      bis = new BufferedInputStream(new FileInputStream(f));
      // Get the detector used for the relevant file type
      Detector d = adp.getDetector();
      // Fetch the document specific media type
      MediaType mt = d.detect(bis, meta);
      // Get the MIME type from the MediaType
      docMeta.setMimetype(mt.toString());
      String fileExt = f.getName();
      if (fileExt.contains("."))
      {
        fileExt = fileExt.substring(fileExt.lastIndexOf(".")+1, fileExt.length());
      }
      else
      {
        fileExt="";
      }
      docMeta.setFileextension(fileExt);
      // Get any parameters from the document
      Map<String,String> params = mt.getParameters();
      // Iterate over the parameters and put them in the main hashmap
      Iterator i = params.keySet().iterator();
      Document.Metadata.Attributes attributes = factory.createDocumentMetadataAttributes();
      // Add the file size as an attribute
      Document.Metadata.Attributes.Attribute temp = factory.createDocumentMetadataAttributesAttribute();
      temp.setKey("File size (b)");
      temp.setValue(""+f.length());
      attributes.getAttribute().add(temp);
      while (i.hasNext())
      {
          String k = i.next().toString();
          String v = params.get(k);
          Document.Metadata.Attributes.Attribute a = factory.createDocumentMetadataAttributesAttribute();
          a.setKey(k);
          a.setValue(v);
          attributes.getAttribute().add(a);
      }
      // Iterate over the rest of the metadata and put that in the hashmap too
      if (meta.size() > 0)
      {
        for (String name : meta.names())
        {
          String value = meta.get(name);
          Document.Metadata.Attributes.Attribute a = factory.createDocumentMetadataAttributesAttribute();
          a.setKey(name);
          a.setValue(value);
          attributes.getAttribute().add(a);
        }
     }
     myTable.setValueAt(new Integer(5), myPosition, PROGRESS_COLUMN);
     // Assemble the document
     docMeta.setAttributes(attributes);
     docMeta.setTitle(f.getName());
     docMeta.setAuthor(authorDetails);
     docMeta.setKeywords(generalKeywords);
     thisDoc.setMetadata(docMeta);
     // FIXME Add code to process and check against the server policies
     // FIXME Add code to check what type of repository should be used
     // Store the document in the repository
     SaxonRepository odin = new SaxonRepository();
     myTable.setValueAt(new Integer(6), myPosition, PROGRESS_COLUMN);
     Response r = odin.storeDocument(thisDoc);
     // FIXME Read the response to make sure it was stored correctly.
     int returnCode = r.getStatus().getCode().intValue();
     myTable.setValueAt(new Integer(returnCode), myPosition, PROGRESS_COLUMN);
     String returnGuid = r.getAppendix().getGuid();
     myTable.setValueAt(returnGuid, myPosition, COMMENT_COLUMN);
    }
    catch (FileNotFoundException fnfX)
    {
      fnfX.printStackTrace();
    }
    catch (IOException ioX)
    {
      ioX.printStackTrace();
    }
    catch (TikaException tX)
    {
      tX.printStackTrace();
    }
    catch (Exception x)
    {
      x.printStackTrace();
    }
    finally
    {
      // Set all of the variables in the GUI
      return null;
    }
  }
}
