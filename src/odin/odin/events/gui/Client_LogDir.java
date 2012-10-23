/* Class name: Client_LogDir
 * File name:  Client_LogDir.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    14-Jan-2009 15:31:27
 * Modified:   26-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  26-Jul-2010 Adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.events.gui;
import java.awt.event.*;
import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import odin.odin.gui.OdinClient;
import odin.odin.object.OdinPreferences;
import odin.odin.object.logging.LoggerFactory;

/**
 * This class is responsible for handling events where the user wants to change
 * the directory used to store log files.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class Client_LogDir implements ActionListener
{
  /** The parent window */
  private OdinClient parent;
  /** The logger to be used for writing messages to the application log */
  private Logger log;

  /**
   * Initialises the class and attaches the current instance of Client so that
   * the dialogue can be displayed within the application window.
   * @param parentWindow The main instance of Client for the application
   */
  public Client_LogDir(OdinClient parentWindow)
  {
    parent = parentWindow;
    log = LoggerFactory.getLogger(getClass().getName());
    log.finest("Create log for the Client_LogDir class");
  }

  /**
   * The user selection of the JMenuItem within the menu of the parent window.
   * @param ae The generated instance of <code>ActionEvent</code>.
   */
  public void actionPerformed(ActionEvent ae)
  {
    log.finest("Setting log directory");
    JFileChooser jFC = new JFileChooser();
    // Get the current preferences file
    Preferences prefDir = OdinPreferences.getOdinPrefs();
    jFC.setDialogTitle("Select a Directory:");
    jFC.setFileSelectionMode(jFC.DIRECTORIES_ONLY);
    File fDir = new File(prefDir.get("odin.dir.Log", System.getProperty("user.dir")));
    jFC.setSelectedFile(fDir);
    log.finest("Displaying FileChooser");
    int returnVal = jFC.showOpenDialog(parent);
    if (returnVal == 1)
    {
      // User cancelled the FileChooser dialog
      log.finest("User cancelled the file chooser");
      return;
    }
    fDir = jFC.getSelectedFile();
    prefDir.put("odin.new.dir.Log", fDir.getPath() + System.getProperty("file.separator"));
    OdinPreferences.updateOdinPrefs();
    JOptionPane.showMessageDialog(OdinClient.getOdinWindow().getDesktopPane(), "The new log directory will be used the next time the application is started.","Log Directory Changed:",JOptionPane.INFORMATION_MESSAGE);
    log.config("Logging directory will be set to " + fDir.getAbsolutePath() + " the next time the application is restarted.");
    /* The code below this point is required to fix a problem. The problem is:
     * After the logging directory has changed and the code progresses to this point
     * (without exiting through the cancel button and the return statement above) halting
     * further execution creates problems when exiting the application. The error log and output
     * displays errors stemming from Threads in progress that are closed unexpectedly. These
     * Threads are from this class and the JFileChooser remaining open in the background despite
     * execution having already halted.
     * The code fixes this problem by removing the pointer to the JFileChooser and forcing the
     * Virtual Machine to perform a garbage collection and thus terminate the outstanding threads.
     */
    // Clean everything up
    jFC = null;
    prefDir = null;
    fDir = null;
    // Force a clean-up
    System.gc();
  }
}