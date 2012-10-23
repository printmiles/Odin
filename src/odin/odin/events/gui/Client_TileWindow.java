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
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.logging.*;
import javax.swing.*;
import odin.odin.gui.OdinClient;
import odin.odin.object.logging.LoggerFactory;

/**
 * This class is used to tile all internal frames within the DesktopPane. All frames
 * regardless of state (minimised, maximised, iconified etc.) are arranged on screen
 * in the same state.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class Client_TileWindow implements ActionListener
{
  /** The logger to be used for writing messages to the application log */
  private Logger log;

  /**
   * Initialises the class and the internal logger.
   */
  public Client_TileWindow()
  {
    log = LoggerFactory.getLogger(getClass().getName());
  }

  /**
   * The user selection of the JMenuItem within the menu of the parent window.
   * @param ae The generated instance of <code>ActionEvent</code>.
   */
  public void actionPerformed(ActionEvent ae)
  {
    // Find out how many frames we have to deal with
    JInternalFrame[] frames = OdinClient.getOdinWindow().getDesktopPane().getAllFrames();
    int frameCount = frames.length;
    // If there aren't any frames open then stop execution
    if (frameCount == 0)
    {
      return;
    }
    // Work out the rough size of a grid
    int sqrt = (int) Math.sqrt(frameCount);
    int rows = sqrt;
    int cols = sqrt;
    // We need to fix outstanding rounding problems from the int conversion above
    if ((rows * cols) < frameCount)
    {
      cols++;
    }
    if ((rows * cols) < frameCount)
    {
      rows++;
    }
    // Get the JDesktopPane so we can find out its size
    Dimension desktopSize = OdinClient.getOdinWindow().getDesktopPane().getSize();
    int w, h, x, y;
    w = desktopSize.width / cols;
    h = desktopSize.height / rows;
    x = 0;
    y = 0;

    // Iterate through the frames, displaying, resizing and repositioning them
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols && ((i*cols) + j < frameCount); j++)
      {
        JInternalFrame frame = frames[(i*cols)+j];
        if ((frame.isClosed() == false) && (frame.isIcon() == true))
        {
          try
          {
            frame.setIcon(false);
          }
          catch (PropertyVetoException pvX)
          {
            log.throwing("Client_TileWindow", "actionPerformed()", pvX);
            System.err.println("Property has been vetoed.");
          }
        }
        OdinClient.getOdinWindow().getDesktopPane().getDesktopManager().resizeFrame(frame, x, y, w, h);
        x += w;
      }
      y += h;
      x = 0;
    }
  }
}