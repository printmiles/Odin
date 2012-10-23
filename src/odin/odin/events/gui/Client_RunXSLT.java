/* Class name: Client_TileWindow
 * File name:  Client_TileWindow.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    02-Feb-2009 20:24:46
 * Modified:   26-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  26-Jul-2010 Adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.events.gui;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import odin.odin.gui.OdinClient;
import odin.odin.object.logging.LoggerFactory;

/**
 * This class is used to Run a named XSLT script on all documents within the
 * repository.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class Client_RunXSLT implements ActionListener
{
  /** The logger to be used for writing messages to the application log */
  private Logger log;

  /**
   * Initialises the class and the internal logger.
   */
  public Client_RunXSLT()
  {
    log = LoggerFactory.getLogger(getClass().getName());
  }

  /**
   * The user selection of the JMenuItem within the menu of the parent window.
   * @param ae The generated instance of <code>ActionEvent</code>.
   */
  public void actionPerformed(ActionEvent ae)
  {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Select XSLT script");
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int returnVal = fc.showOpenDialog(OdinClient.getOdinWindow());
    if (returnVal == 1)
    {
      return;
    }
    File fXSLT = fc.getSelectedFile();
    fc.setDialogTitle("Select file to run on:");
    returnVal = fc.showOpenDialog(OdinClient.getOdinWindow());
    if (returnVal == 1)
    {
      return;
    }
    File fXML = fc.getSelectedFile();
    /* Code adapted from: http://blog.msbbc.co.uk/2007/06/simple-saxon-java-example.html
     * [Accessed 3rd-Sept-2010]
     */

    try
    {
      System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
      TransformerFactory tFact = TransformerFactory.newInstance();
      Transformer tf = tFact.newTransformer(new StreamSource(fXSLT));
      tf.transform(new StreamSource(fXML), new StreamResult(fXML));
    }
    catch (TransformerException tX)
    {
      tX.printStackTrace();
    }
  }
}