/* Class name: Client_LnF
 * File name:  Client_LnF.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    13-Jan-2009 20:09:57
 * Modified:   26-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  26-Jul-2010 Adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.events.gui;
import java.awt.event.*;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.*;
import odin.odin.gui.OdinClient;
import odin.odin.object.OdinPreferences;
import odin.odin.object.logging.LoggerFactory;

/**
 * This class is responsible for updating the Look and Feel of the application according to the user's selection.
 * Within the application a menu will contain all of the supported Look and Feels by the installed Java Runtime.
 * An instance of this class is attached to each listed Look and Feel and the user selecting one from the list
 * will update the GUI accordingly.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class Client_LnF implements ActionListener
{
  /** The string representation of the Look and Feel to be used */
  private String lnfName;
  /** The parent window that the Look and Feel should be applied to */
  private OdinClient client;
  /** The logger to be used for writing messages to the application log */
  private Logger log;

  /**
   * Initialises the class and internal logger. It requires the name of the Look
   * and Feel it is being attached to along with the instance of <code>Client</code> to work.
   * @param name The name of the Look and Feel which this particular class instance monitors
   * @param parentClient The instance of <code>Client</code> that will be updated
   */
  public Client_LnF(String name, OdinClient parentClient)
  {
    client = parentClient;
    lnfName = name;
    log = LoggerFactory.getLogger(getClass().getName());
    log.finest("Initialised the logger for the Look and Feel events");
  }

  /**
   * The user selection of the JMenuItem within the menu of the parent window.
   * @param ae The generated instance of <code>ActionEvent</code>.
   */
  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      log.finest("Trying to change Look and Feel to " + lnfName);
      Preferences prefLnF = OdinPreferences.getOdinPrefs();
      prefLnF.put("odin.lnf", lnfName);
      OdinPreferences.updateOdinPrefs();
      UIManager.setLookAndFeel(lnfName);
      SwingUtilities.updateComponentTreeUI(client);
    }
    catch (Exception x)
    {
      log.throwing(getClass().getName(), "actionPerformed(ActionEvent)", x);
      System.err.println("Look and Feel could not be applied to the GUI.");
    }
    log.finest("Successfully change look and feel");
  }
}