/* Class name: Client_BulkUpload
 * File name:  Client_BulkUpload.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    31-May-2011 17:39:11
 * Modified:   31-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  31-May-2011 Initial build
 */
package odin.odin.events.gui;

import java.awt.event.*;
import java.util.logging.Logger;
import odin.odin.gui.internalframe.BulkUploader;
import odin.odin.object.logging.LoggerFactory;

/**
 * Used by the Odin GUI to receive events generated from the menu when
 * the user selects the Bulk Upload option.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class Client_BulkUpload implements ActionListener
{
  /** The logger to be used for writing messages to the application log */
  private Logger log;
  
  /**
   * Initialises the class and attaches the current instance of Client so that
   * the dialogue can be displayed within the application window.
   * @param parentWindow The main instance of Client for the application
   */
  public Client_BulkUpload()
  {
    log = LoggerFactory.getLogger(getClass().getName());
    log.finest("Created log for the Client_BulkUploader class");
  }
  
  /**
   * The user selection of the JMenuItem within the menu of the parent window
   * @param ae The generated instance of <code>ActionEvent</code>.
   */
  public void actionPerformed(ActionEvent ae)
  {
    BulkUploader bu = new BulkUploader();
  }
}