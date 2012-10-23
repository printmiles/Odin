/* Class name: Client_MinimiseAll
 * File name:  Client_MinimiseAll.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    02-Feb-2009 20:25:08
 * Modified:   26-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  26-Jul-2010 Adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.events.gui;
import java.awt.event.*;
import javax.swing.*;
import odin.odin.gui.OdinClient;

/**
 * This class is responsible for iterating through all internal frames shown within
 * the JDesktopPane and minimising each of them.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class Client_MinimiseAll implements ActionListener
{

  /**
   * The standard constructor for the class.
   */
  public Client_MinimiseAll()
  {}

  /**
   * The user selection of the JMenuItem within the menu of the parent window.
   * @param ae The generated instance of <code>ActionEvent</code>.
   */
  public void actionPerformed(ActionEvent ae)
  {
    JInternalFrame[] frames = OdinClient.getOdinWindow().getDesktopPane().getAllFrames();
    for (int i = 0; i < frames.length; i++)
    {
      if (frames[i].isIconifiable())
      {
        try
        {
          frames[i].setIcon(true);
        }
        catch (Exception x)
        {
          System.err.println("Problem minimising one fo the frames in the application.");
          x.printStackTrace();
        }
      }
    }
  }
}