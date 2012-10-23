/* Class name: OdinPreferences
 * File name:  OdinPreferences.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    17-Nov-2008 15:19:54
 * Modified:   15-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.002  15-Jul-2010 Adapted to include JAXB code reading from root.xml
 * 1.001  12-Jul-2010 Adapted for Odin
 * 1.000  17-Jun-2009 Code finalised and released.
 * 0.001  17-Nov-2008 Initial build
 */

package odin.odin.object;
import odin.odin.xml.Root;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import odin.odin.object.logging.LoggerFactory;
import odin.odin.xml.*;

/**
 * This class is used by others as the central means for access to the application preferences. These
 * are stored in a single XML file used by the whole application. Once instantiated, this class uses
 * instances of the <code>java.util.prefs.Preferences</code> class to retrieve and edit data within another
 * class. The other class then calls the static method <code>updateOdinPrefs</code> to write values
 * into the XML file.
 * @version 1.002
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 * @see java.util.prefs.Preferences
 */
public class OdinPreferences
{
  //FIXME Change the preferences file location to be read from the root information.
  /** The name of the applications preference file located within the main class directory */
  private static final String prefsFileName = "odin.prefs.xml";
  /** The centralised instance of <code>Preferences</code> to be used for the whole application */
  private static Preferences prefsOdin;
  /** The root document which has been unmarshalled through JAXB */
  private static Root rDocument;
  /** The path of the file being used to store Preferences */
  private static String sPrefsPath;
  
  /**
   * Used to return all of the Preferences used across the application.
   * Preferences can then be edited locally within the calling class.
   * @return A new instance of Preferences to be used locally
   */
  public static Preferences getOdinPrefs()
  {
    // Obtain a logger to write progress to
    Logger log = LoggerFactory.getLogger("odin.odin.object.OdinPreferences");
    log.log(Level.FINEST, "Examining preferences to see if it's null or not.", prefsOdin);
    if (prefsOdin == null)
    {
      log.finest("Preferences object was null, creating a new one from the default file (odin.prefs.xml).");
      try
      {
        // Obtain the FileInputStream for the central Preferences file
        BufferedInputStream bisXMLPrefs = new BufferedInputStream(new FileInputStream(sPrefsPath));
        // Providing the file isn't null then import the preferences
        if (bisXMLPrefs != null)
        {
          prefsOdin.importPreferences(bisXMLPrefs);
        }
      }
      catch (Exception x)
      {
        // If any errors are thrown then record them in the logger
        log.throwing("OdinPreferences", "getOdinPrefs()", x);
      }
    }
    // Obtain the Preferences for this class, this allows for everything to be centralised within the file
    prefsOdin = Preferences.userNodeForPackage(OdinPreferences.class);
    // Return the Preferences to the receiver.
    return prefsOdin;
  }
  
  /**
   * This class provides exactly the same functions are the getOdinPrefs method but doesn't record anything in the Log
   * <p>It should <b>only</b> be used by the LoggerFactory class to prevent a loop. All other classes should use the
   * getOdinPrefs method to utilise the logger.
   * <p>It also retrieves much of the information from the Server's Root XML document and reads it into the preferences
   * for later reads.
   * @return A new instance of Preferences to be used locally
   */
  public static Preferences getInitialPreferences()
  {
    /* As mentioned in the JavaDoc comments, this is the same code but omitting the use of the log. It doesn't harm if
     * a class other than LoggerFactory uses it, we just lose the logs if something goes wrong.
     */
    if (prefsOdin == null)
    {
      try
      {
        String prefsFilePath = getPrefsPath() + prefsFileName;
        File f = new File(prefsFilePath);
        if (f.exists())
        {
          sPrefsPath = prefsFilePath;
          BufferedInputStream bisXMLPrefs = new BufferedInputStream(new FileInputStream(prefsFilePath));
          if (bisXMLPrefs != null)
          {
            prefsOdin.importPreferences(bisXMLPrefs);
          }
        }
        else
        {
          File fBackup = new File(prefsFileName);
          if (fBackup.exists())
          {
            // Using the backup preferences in the application directory.
            sPrefsPath = prefsFileName;
          }
          BufferedInputStream bisXMLPrefs = new BufferedInputStream(new FileInputStream(prefsFileName));
          prefsOdin.importPreferences(bisXMLPrefs);
        }
      }
      catch (FileNotFoundException fnfX)
      {
        System.err.println("Couldn't retrieve either preferences.");
      }
      catch (Exception x)
      {
        // If any errors are thrown then put them in System.err rather than the Logger
        System.err.println("An error was caught while trying to obtain preferences for the logger.");
        x.printStackTrace();
      }
    }
    prefsOdin = Preferences.userNodeForPackage(OdinPreferences.class);
    // Get hold of the JAXB data contained within the Server Root document to find out where we should store everything.
    getRootDocument();
    prefsOdin.put("odin.jaxb.dir.Archive", rDocument.getLocations().getArchive());
    prefsOdin.put("odin.jaxb.dir.Convert", rDocument.getLocations().getConversion());
    prefsOdin.put("odin.jaxb.dir.Mime", rDocument.getLocations().getFormat());
    prefsOdin.put("odin.jaxb.dir.Log", rDocument.getLocations().getLogging());
    prefsOdin.put("odin.jaxb.dir.Prefs", rDocument.getLocations().getPreference());
    prefsOdin.put("odin.jaxb.dir.Hazard", rDocument.getLocations().getQuarantine());
    prefsOdin.put("odin.jaxb.dir.Store", rDocument.getLocations().getRepository());
    silentUpdate();
    return prefsOdin;
  }
    
  /**
   * Used to update any changes within the preferences to the main XML file.
   */
  public static void updateOdinPrefs()
  {
    // Obtain a logger to write progress to
    Logger log = LoggerFactory.getLogger("odin.odin.object.OdinPreferences");
    try
    {
      log.finest("Exporting preferences to file: " + sPrefsPath);
      // Record all of the preferences in the odin.prefs.xml file
      prefsOdin.exportNode(new FileOutputStream(sPrefsPath));
    }
    catch (Exception x)
    {
      // If an error was encountered during the update then write the details to the log
      log.throwing("OdinPreferences", "updateOdinPrefs()", x);
    }
  }

  /**
   * Used to update the preferences with possibly new values from the root document.
   * This method is only used within this class to prevent recursion from creating
   * a <code>Logger</code> instance.
   * @see java.util.logging.Logger
   */
  private static void silentUpdate()
  {
    try
    {
      // Record all of the preferences in the odin.prefs.xml file
      prefsOdin.exportNode(new FileOutputStream(sPrefsPath));
    }
    catch (Exception x)
    {
      // If an error was encountered during the update then write the details
      // to the System.err as there isn't a log to use.
      System.err.println("Couldn't update the preferences with values from the server's root document.");
      x.printStackTrace();
    }
  }

  /**
   * Used to obtain the path of the preferences file from the root document
   */
  private static String getPrefsPath()
  {
    String filepath = "";
    getRootDocument();
    filepath = rDocument.getLocations().getPreference();
    return filepath;
  }

  /**
   * Used to retrieve the root document bound through JAXB
   */
  private static void getRootDocument()
  {
    if (rDocument == null)
    {
      try
      {
        JAXBContext jaxbCtxt = JAXBContext.newInstance("odin.odin.xml");
        Unmarshaller jaxbU = jaxbCtxt.createUnmarshaller();
        jaxbU.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        File fRoot = new File("home.root.xml");
        JAXBElement<Root> rElement = jaxbU.unmarshal(new StreamSource(fRoot),Root.class);
        rDocument = rElement.getValue();
      }
      catch (JAXBException jaxbX)
      {
        System.err.println("An error was caught while trying to read information from the server's root information document.");
        jaxbX.printStackTrace();
      }
    }
  }

}