/* Class name: LanguageWorker
 * File name:  LanguageWorker.java
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
import java.io.StringWriter;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.SwingWorker;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.WriteOutContentHandler;

/**
 * JavaDoc to follow
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class LanguageWorker extends SwingWorker
{
   private File f;
   private HashMap<String,String> lang;
   private ProgressForm worker;
   private Upload parent;
   private ResourceBundle rbUser;

  public LanguageWorker(File newFile, ProgressForm pf, Upload u, ResourceBundle rb)
  {
    f = newFile;
    lang = new HashMap<String,String>();
    lang.put("lang", "en");
    lang.put("certain", Boolean.toString(false));
    worker = pf;
    parent = u;
    rbUser = rb;
  }

  /**
   * <p>This method interrogates the document specified by the File in the constructor
   * and scans it to attempt and identify the language of the content.</p>
   * <p>The HashMap returned contains two elements:</p>
   * <ul>
   * <li><strong>lang</strong> - The ISO code of the detected language</li>
   * <li><strong>certain</strong> - Whether the detector is confident of the result</li>
   * </ul>
   *
   * @return
   */
  public Void doInBackground()
  {
    worker.setStatus(rbUser.getString("gui.lang.working"));
    worker.increaseProgress();
    try
    {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
      Metadata meta = new Metadata();
      ParseContext pc = new ParseContext();
      StringWriter sw = new StringWriter();
      WriteOutContentHandler woch = new WriteOutContentHandler(sw);
      AutoDetectParser adp = new AutoDetectParser();
      // This populates the AutoDetectParser fileds but closes the BufferedInputStream
      adp.parse(bis, woch, meta, pc);
      // Force it close
      bis.close();
      String content = sw.toString();
      LanguageIdentifier li = new LanguageIdentifier(content);
      lang.put("lang", li.getLanguage());
      lang.put("certain", Boolean.toString(li.isReasonablyCertain()));
      worker.setStatus(rbUser.getString("gui.lang.done"));
    }
    catch (FileNotFoundException fnfX)
    {
      worker.setStatus(rbUser.getString("gui.lang.error"));
      System.err.println(rbUser.getString("gui.lang.err.fileNotFound") + " " + f.getAbsolutePath());
      fnfX.printStackTrace();
    }
    catch (IOException ioX)
    {
      worker.setStatus(rbUser.getString("gui.lang.error"));
      System.err.println(rbUser.getString("gui.lang.err.io"));
      ioX.printStackTrace();
    }
    catch (Exception x)
    {
      worker.setStatus(rbUser.getString("gui.lang.error"));
      System.err.println(rbUser.getString("gui.lang.err.io"));
      x.printStackTrace();
    }
    finally
    {
      worker.increaseProgress();
      parent.setLanguage(lang);
      return null;
    }
  }
}
