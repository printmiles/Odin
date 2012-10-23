/* Class name: CloseOdin
 * File name:  CloseOdin.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    19-Nov-2008 12:55:10
 * Modified:   26-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  26-Jul-2010 Adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.events.gui;
import java.awt.event.*;
import odin.odin.object.logging.LoggerFactory;

/**
 * This class handles any request to close Odin. It does this
 * by closing all log files gracefully and performs garbage collection prior to
 * the application closing.
 * <p>This class extends the <code>WindowAdapter</code> class and implements
 * the <code>ActionListener</code> interface in order to handle both types of
 * events generate either by the user closing the window, or requesting the
 * application exit through the file menu.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class CloseOdin extends WindowAdapter implements ActionListener
{
  /**
   * The standard constructor for the class.
   */
  public CloseOdin()
  {}

  /**
   * This method is called when the application window is requested to shut.
   * The program then closes the logs gracefully and performs garbage collection
   * before ordering the system to exit with a shutdown code of 1 (successful
   * shutdown).
   * @param e The triggered WindowEvent
   */
  public void windowClosing(WindowEvent e)
  {
    closeApp();
  }

  /**
   * This method is called when the user clicks on File > Exit from the Odin
   * menu bar. The program then closes the logs gracefully and performs garbage
   * collection before ordering the system to exit with a shutdown code of 1
   * (successful shutdown).
   * @param ae The generated ActionEvent
   */
  public void actionPerformed(ActionEvent ae)
  {
    closeApp();
  }

  /**
   * This method is called by both the actionPerformed() and windowClosing()
   * methods. The actual functions are stored in this method to further centralise
   * the code being executed. Any changes to the execution of the program during
   * shutdown should be made here.
   */
  private void closeApp()
  {
    LoggerFactory.closeLogs();
    System.gc();
    System.out.println("Closing application at: " + java.util.Calendar.getInstance().getTime().toString());
    System.exit(1);
  }
}