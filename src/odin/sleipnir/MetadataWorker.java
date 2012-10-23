/* Class name: MetadataWorker
 * File name:  MetadataWorker.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    30-Mar-2011 15:53:00
 * Modified:   30-Mar-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  30-Mar-2011 Initial build
 */

package odin.sleipnir;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.*;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.WriteOutContentHandler;

/**
 * JavaDoc to follow
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class MetadataWorker extends SwingWorker
{
  private File f;
  private HashMap<String,HashMap<String,String>> metadata;
  private ProgressForm worker;
  private Upload parent;
  private ResourceBundle rbUser;

  public MetadataWorker(File newFile, ProgressForm pf, Upload u, ResourceBundle rb)
  {
    f = newFile;
    metadata = new HashMap<String,HashMap<String,String>>();
    worker = pf;
    parent = u;
    rbUser = rb;
  }

  /**
   * Examines the meta-data of the given document
   * @return A nested HashMap with two entries. One MIME information in "mime:mime" and the other meta-data in "meta"
   */
  public Void doInBackground()
  {    
    worker.setStatus(rbUser.getString("gui.meta.working"));
    worker.increaseProgress();
    HashMap<String,String> hmMime = new HashMap<String,String>();
    HashMap<String,String> hmMeta = new HashMap<String,String>();
    hmMime.put("mime", "unknown");
    try
    {
      // Open the file
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
      // Setup the Tika classes
      Metadata meta = new Metadata();
      ParseContext pc = new ParseContext();
      WriteOutContentHandler woch = new WriteOutContentHandler(-1);
      AutoDetectParser adp = new AutoDetectParser();
      // This populates the AutoDetectParser and Metadata fields but closes the BufferedInputStream
      adp.parse(bis, woch, meta, pc);
      // Force it close
      bis.close();
      // Re-open so that we can parse the file
      bis = new BufferedInputStream(new FileInputStream(f));
      // Get the detector used for the relevant file type
      Detector d = adp.getDetector();
      // Fetch the document specific media type
      MediaType mt = d.detect(bis, meta);
      // Get the MIME type from the MediaType
      hmMime.put("mime", mt.toString());
      worker.setStatus(rbUser.getString("gui.meta.mime"));
      worker.increaseProgress();
      // Get any parameters from the document
      Map<String,String> params = mt.getParameters();
      // Remove the placeholder value
      hmMeta.remove("meta");
      // Iterate over the parameters and put them in the main hashmap
      Iterator i = params.keySet().iterator();
      while (i.hasNext())
      {
          String k = i.next().toString();
          String v = params.get(k);
          hmMeta.put(k, v);
      }
      // Iterate over the rest of the metadata and put that in the hashmap too
      if (meta.size() > 0)
      {
        for (String name : meta.names())
        {
          String value = meta.get(name);
          hmMeta.put(name, value);
        }
     }
     worker.setStatus(rbUser.getString("gui.meta.done"));
    }
    catch (FileNotFoundException fnfX)
    {
      worker.setStatus(rbUser.getString("gui.meta.err"));
      System.err.println(rbUser.getString("gui.meta.err.fnf") + " " + f.getAbsolutePath());
      fnfX.printStackTrace();
    }
    catch (IOException ioX)
    {
      worker.setStatus(rbUser.getString("gui.meta.err"));
      System.err.println(rbUser.getString("gui.meta.err.read"));
      ioX.printStackTrace();
    }
    catch (TikaException tX)
    {
      System.err.println(rbUser.getString("gui.meta.err.size"));
      System.err.println(rbUser.getString("gui.meta.err.parse"));
      tX.printStackTrace();
    }
    catch (Exception x)
    {
      worker.setStatus(rbUser.getString("gui.meta.err"));
      System.err.println(rbUser.getString("gui.meta.err.read"));
      x.printStackTrace();
    }
    finally
    {
      worker.increaseProgress();
      metadata.put("mime", hmMime);
      metadata.put("meta", hmMeta);
      parent.setMetadata(metadata);
      return null;
    }
  }
}