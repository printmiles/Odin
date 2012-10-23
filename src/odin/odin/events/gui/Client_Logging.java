/* Class name: Client_Logging
 * File name:  Client_Logging.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    13-Jan-2009 20:33:31
 * Modified:   17-Jun-2010
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
import odin.odin.object.OdinPreferences;
import odin.odin.object.logging.LoggerFactory;

/**
 * This class is responsible for altering the level at which all loggers record events
 * within the application.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class Client_Logging implements ActionListener
{
  /** The logging level to be applied */
  private String sLevel;
  /** The logger to be used for writing messages to the application log */
  private Logger log;

  /**
   * Initialises the class and internal logger. Requires the log level which this instance is monitoring.
   * @param logLevel The logging level attached to this instance
   */
  public Client_Logging(String logLevel)
  {
    // Get an instance of logger for recording events
    log = LoggerFactory.getLogger(getClass().getName());
    // Set the log level
    sLevel = logLevel;
  }

  /**
   * The user selection of the JMenuItem within the menu of the parent window.
   * @param ae The generated instance of <code>ActionEvent</code>.
   */
  public void actionPerformed(ActionEvent ae)
  {
    log.finest("Logger level has been requested to be changed to a new level: " + sLevel);
    log.finest("Writing new log level to preferences");
    // Obtain the application preferences
    Preferences prefsLocale = OdinPreferences.getOdinPrefs();
    // Write the log level to the preferences
    prefsLocale.put("odin.logLevel", sLevel);
    OdinPreferences.updateOdinPrefs();
    log.finest("Updating all of the current loggers to work at the new level");
    // Get the LoggerFactory to update all of the instances of Logger used by the application to the new level
    LoggerFactory.setLogLevel(sLevel);
    log.finest("Log level changed in the preferences and on current loggers");
  }
}