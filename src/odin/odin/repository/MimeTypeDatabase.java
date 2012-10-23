/* Class name: MimeTypeDatabase
 * File name:  MimeTypeDatabase.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    04-Jul-2011 15:14:26
 * Modified:   04-Jul-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  04-Jul-2011 Initial build
 */
package odin.odin.repository;

import java.io.File;
import java.util.*;
import java.util.logging.*;
import javax.xml.bind.*;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import odin.odin.object.logging.LoggerFactory;
import odin.odin.xml.*;

/**
 * This class is used to emulate the MIME format database within memory.
 * Initialisation should be performed during the application start-up and objects
 * can then be retrieved faster using this than performing similar XQuery searches.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class MimeTypeDatabase
{
  private static Logger log;
  private static Root rootDoc;
  private static HashMap<String,String> hmByFileExt;
  private static HashMap<String,Format> hmByGuid;
  
  /**
   * Initialise loggers and data structures for the class
   */
  public MimeTypeDatabase()
  {
    log = LoggerFactory.getLogger(getClass().getName());
    hmByFileExt = new HashMap<String,String>();
    hmByGuid = new HashMap<String,Format>();
  }
  
  public HashMap<String,String> getFileExtensions() { return hmByFileExt; }
  public HashMap<String,Format> getByGuid() { return hmByGuid; }
  
  /**
   * Obtain the server's root information through JAX-B
   */
  private static void getRootDocument()
  {
    if (rootDoc == null)
    {
      try
      {
        JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
        Unmarshaller jaxbU = jaxbCtxt.createUnmarshaller();
        jaxbU.setEventHandler(new DefaultValidationEventHandler());
        File fRoot = new File("home.root.xml");
        JAXBElement<Root> rElement = jaxbU.unmarshal(new StreamSource(fRoot),Root.class);
        rootDoc = rElement.getValue();
      }
      catch (JAXBException jaxbX)
      {
        log.throwing(new MimeTypeDatabase().getClass().getName(), "getRootDocument", jaxbX);
      }
    }
  }
  
  /**
   * Obtain the Format information given a file extension
   * @param fileExt The file extension we require the Format information for
   * @return The complete formatting information
   */
  public static Format getFormatByFileExtension(String fileExt)
  {
    String guid = hmByFileExt.get(fileExt);
    if (guid == null)
    {
      // If we've entered here then we've discovered a new file type not recognised in the database
      guid = addNewFormat(fileExt,"","");
    }
    Format f = getFormatByGuid(guid);
    return f;
  }
  
  /**
   * Obtain the Format information given a specific identifier.
   * @param guid The specific identifier
   * @return The complete formatting information
   */
  public static Format getFormatByGuid(String guid)
  {
    return hmByGuid.get(guid);
  }
  
  /**
   * Initialises the data structure by iterating through the MIME database directory
   * and converting document to the JAX-B class and then storing it in this structure.
   */
  public static void initialiseDatabase()
  {
    Calendar split1, split2;
    split1 = Calendar.getInstance();
    // Get the Root information document if it isn't already initialised
    if (rootDoc == null) { getRootDocument(); }
    // Set the MIME type directory
    File mimeDir = new File(rootDoc.getLocations().getFormat());
    try
    {
      // Configure the JAXB environment
      JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
      Unmarshaller jaxbU = jaxbCtxt.createUnmarshaller();
      jaxbU.setEventHandler(new DefaultValidationEventHandler());
      // Iterate through the file list and unmarshal the data
      for (File f : mimeDir.listFiles())
      {
        JAXBElement<Format> rElement = jaxbU.unmarshal(new StreamSource(f),Format.class);
        Format format = rElement.getValue();
        
        // Put the Format data into the HashMaps
        hmByGuid.put(format.getGuid(), format);
        hmByFileExt.put(format.getExt(), format.getGuid());
      }
    }
    catch (JAXBException jaxbX)
    {
      log.throwing(new MimeTypeDatabase().getClass().getName(), "initialiseDatabase()", jaxbX);
    }
    split2 = Calendar.getInstance();
    System.out.println("MIME database initialised in " + (split2.getTimeInMillis() - split1.getTimeInMillis()) + "ms");
  }
  
  /**
   * Creates a new entry in the MIME database by creating both an entry within
   * the data structure here as well as a XML within the database folder.
   * @param fileExtension The new file extension
   * @param mimeType The new MIME type
   * @param description The new description
   * @return The guid of the new entry
   */
  public static String addNewFormat(String fileExtension, String mimeType, String description)
  {
    if (fileExtension == null) { fileExtension = ""; }
    if (mimeType == null) { mimeType = ""; }
    if (description == null) { description = ""; }
    if (rootDoc == null) { getRootDocument(); }
    String formatDir = rootDoc.getLocations().getFormat();
    String guid = UUID.randomUUID().toString();
    // Check to see if the guid already exists
    File fFormatInfo = new File(formatDir+guid+".xml");
    while (fFormatInfo.exists())
    {
      guid = UUID.randomUUID().toString();
      fFormatInfo = new File(formatDir+guid+".xml");
    }
    ObjectFactory factory = new ObjectFactory();
    Format newFormat = factory.createFormat();
    newFormat.setGuid(guid);
    newFormat.setExt(fileExtension);
    newFormat.setMime(mimeType);
    newFormat.setDescription(description);
    
    try
    {
      // Marshal the document to the XML repository
      JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
      Marshaller m = jaxbCtxt.createMarshaller();
      m.setProperty("jaxb.formatted.output", Boolean.TRUE);
      m.marshal(newFormat, fFormatInfo);
      // Now add the Format to the HashMaps
      hmByFileExt.put(fileExtension, guid);
      hmByGuid.put(guid, newFormat);
    }
    catch (Exception x)
    {
      log.throwing(new MimeTypeDatabase().getClass().getName(), "addNewFormat(String,String,String)",x);
    }
    finally
    {
      return guid;
    }
  }
}