/* Class name: LoggerFactory
 * File name:  LoggerFactory.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    17-Nov-2008 15:14:47
 * Modified:   25-Mar-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.002  25-Mar-2011 Adapted code to use HashMap instead of Hashtable
 * 1.001  11-Jul-2010 Code adapted for Odin
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.object.logging;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.*;
import java.util.prefs.*;
import odin.odin.object.OdinPreferences;

/**
 * This class is used to control instances of <code>java.util.logging.Logger</code> used within the application.
 * An instance of <code>Logger</code> can be obtained by passing the class name of the source to the factory
 * via the <code>getLogger(String)</code> method. An instance of <code>java.util.logging.Logger</code> is
 * returned to the receiver that has either been newly instantiated or retrieved from a HashMap depending on
 * whether an instance already exists.
 * <p>The files are saved using the XML format defined by Sun for log records and are stored
 * relative to the directory the code is executed from, in the Logs directory.
 * <p>Log records are named in the format of <code>getClass().getName()-u.log.xml</code> where <code>parentgetClass().getName()</code> is
 * the supplied class name, which should be the fully qualified package and class name and <code>u</code> is a unique number
 * <p>The objective of this class is to reduce the overall number of logs kept within the system and to provide
 * a central point for amending settings such as log levels.
 * @version 1.002
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 * @see java.util.logging.Logger
 */
public class LoggerFactory
{
  /** The central store of all loggers created throughout the application */
  private static HashMap<String, Logger> hmOdinLogs;
  /** The logger to be used for writing messages to the application log */
  private static Logger logSelf;

  /**
   * Used to return an existing instance of Logger if one exists for the supplied class name.
   * Or to create a new instance, configure the FileHandler, set the file location and logging level.
   * @see java.util.logging.Logger
   * @param parentClassName The fully qualified class name identifying the Logger
   * @return The instance of Logger to be used
   */
  public static Logger getLogger(String parentClassName)
  {
    // Check to make sure that all variables are initialised, if not then initialise them
    if (hmOdinLogs == null)
    {
      hmOdinLogs = new HashMap<String, Logger>();
    }
    // Get the Preferences for the application so that we can read the preferred logging level
    Preferences prefLogLevel = OdinPreferences.getInitialPreferences();
    if (logSelf == null)
    {
      String myClassName = "odin.odin.object.logging.LoggerFactory";
      logSelf = Logger.getLogger(myClassName);
      hmOdinLogs.put(myClassName, logSelf);
      try
      {
        FileHandler fhXMLLog = new FileHandler(getLogDir() + myClassName + "-%u.log.xml");
        logSelf.addHandler(fhXMLLog);
        // Get the log level from the preferences. If it can't be obtained then default to All
        logSelf.setLevel(Level.parse((prefLogLevel.get("odin.logLevel", "All")).toUpperCase()));
        logSelf.finest("Created new logger for the LoggerFactory class");
        logSelf.config("Started new log");
      }
      catch (IOException ioX)
      {
        logSelf.throwing("LoggerFactory", "getLogger(String)", ioX);
        System.err.println("Unable to create logger for class: " + myClassName);
      }
    }

    // Does the HashMap already contain a logger for the given getClass().getName()?
    if(hmOdinLogs.containsKey(parentClassName))
    {
      logSelf.finest("Found logger for " + parentClassName + " and returning it to the receiver.");
      return hmOdinLogs.get(parentClassName);
    }
    // If it doesn't then set one up using the stored preferences and
    // run-time properties (to obtain directory information).
    else
    {
      Logger logNewClass = Logger.getLogger(parentClassName);
      if (logSelf == null)
      {
        logSelf = logNewClass;
      }
      logSelf.finest("Could not find a logger for " + parentClassName + ", creating a new one.");
      try
      {
        FileHandler fhXMLLog = new FileHandler(getLogDir() + parentClassName + "-%u.log.xml");
        logNewClass.addHandler(fhXMLLog);
        // Get the log level from the preferences. If it can't be obtained then default to All
        logNewClass.setLevel(Level.parse((prefLogLevel.get("odin.logLevel", "All")).toUpperCase()));
        logNewClass.config("Started new log");
        logSelf.finest("Created new logger for class " + parentClassName + " at location: " + getLogDir() + parentClassName + ".log.xml");
      }
      catch (IOException ioX)
      {
        logSelf.severe("Unable to create logger for class: " + parentClassName);
        logSelf.throwing("LoggerFactory", "getLogger(String)", ioX);
        System.err.println("Unable to create logger for class: " + parentClassName + ". See the log for more information.");
      }

      logSelf.finest("Adding new logger to the HashMap");
      hmOdinLogs.put(parentClassName, logNewClass);
      return logNewClass;
    }
  }

  /**
   * Examines the application preferences and returns the localised string of the current log file directory.
   * If one cannot be found then it examines the current location the application is being run in and creates
   * a Logs directory specifically for Odin.
   * <p>The returned value is localised to the OS so Windows would return C:\Logs and Unix C:/Logs
   * @return The current directory used for Log file storage.
   */
  private static String getLogDir()
  {
    String strClassDir, strUserDir;
    strClassDir = System.getProperty("java.class.path"); // Returns the current class path
    strUserDir = System.getProperty("user.dir"); // If the class path doesn't return the directory then this will
    if (strClassDir.indexOf(System.getProperty("file.separator")) > -1)
    {
      // See if the class directory contains the full class path (that the string contains a file seperator character)
      // If it does then manipulate the string so that it returns the file directory but without the class file name
      strUserDir = strClassDir.substring(0, (strClassDir.lastIndexOf(System.getProperty("file.separator"))) + 1);
      strUserDir = strUserDir + "Logs" + System.getProperty("file.separator");
    }
    else
    {
      // If the class path only provide the class file name then it is being run from the same directory
      strUserDir = strUserDir + System.getProperty("file.separator") + "Logs" + System.getProperty("file.separator");
    }
    // Get the Preferences for Odin
    Preferences prefLogDir = OdinPreferences.getInitialPreferences();
    // See if there's already an entry for the Log files to be recorded
    // If one exists then use that, if not then use the string we've just created
    strUserDir = prefLogDir.get("odin.jaxb.dir.Log", strUserDir);

    if (!(new File(strUserDir).exists()))  // Check to see whether the logs directory exists
    {
      logSelf.warning("Logs directory doesn't exist, trying to create the directory");
      // If it doesn't then try to create the directory
      boolean success = (new File(strUserDir)).mkdirs();
      if (!success)
      {
        // Could not create the directory so throw an exception
        logSelf.severe("Couldn't create a directory for the loggers, notifying System.err.");
        System.err.println("Unable to create directory: " + strUserDir);
      }
    }
    return strUserDir;
  }

  /**
   * Changes all of the Loggers stored within the classes HashMap to the specified level
   * @param newLevel The new level of logging requested. This must match the string equivalent of the level itself.
   */
  public static void setLogLevel(String newLevel)
  {
    newLevel = newLevel.toUpperCase();
    Level lNew = Level.parse(newLevel.toUpperCase());
    Iterator iLogs = hmOdinLogs.keySet().iterator();
    while (iLogs.hasNext())
    {
      String strLogName = iLogs.next().toString();
      Logger logTemp = hmOdinLogs.get(strLogName);
      logTemp.setLevel(lNew);
      hmOdinLogs.put(strLogName, logTemp);
    }
  }

  /**
   * Close the logs gracefully and ensure that the logs has been closed correctly.
   */
  public static void closeLogs()
  {
    Iterator iKeys = hmOdinLogs.keySet().iterator();
    while (iKeys.hasNext())
    {
      Logger aLog = hmOdinLogs.get(iKeys.next().toString());
      // Write a quick note to the logger before closing it
      aLog.config("Application closing, shutting the logger down.");
      // Turn the logging off
      aLog.setLevel(Level.OFF);
      // Set the logger to null which closes the file
      aLog = null;
    }
  }
}