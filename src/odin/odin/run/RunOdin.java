/* Class name: RunOdin
 * File name:  RunOdin.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    11-Jul-2010 23:15:43
 * Modified:   25-Mar-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  25-Mar-2011 Removed Preferences and checkPreferences() method
 * 1.000  11-Jul-2010 Initial build
 */

package odin.odin.run;
import odin.odin.gui.OdinClient;
import odin.odin.gui.ModernSplash;
import odin.odin.object.logging.*;
import odin.odin.repository.MimeTypeDatabase;
import java.util.logging.Logger;

/**
 * Used to create the application environment and launch the main GUI. The system will exit with one of the
 * following codes:
 * <ol>
 *   <li>User requested application to shut down (normal shut-down).</li>
 *   <li>Application could not find the database DSN name to connect to (abnormal shut-down).</li>
 *   <li>Application could not find the database type to connect to (abnormal shut-down).</li>
 * </ol>
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class RunOdin
{
  /** The logger to be used for writing messages to the application log */
  private static Logger log;

  /**
   * Executes the main application.
   * <p>It requires no specific arguments and so doesn't support additional
   * arguments being passed to it.
   * @param args Any additional arguments - <em>Not used</em>
   */
  public static void main (String[] args)
  {
    // Create the splash screen
    ModernSplash ms = new ModernSplash();
    log = LoggerFactory.getLogger("odin.odin.run.RunOdin");
    log.finest("Starting application");
    ms.logsComplete();
    MimeTypeDatabase mr = new MimeTypeDatabase();
    MimeTypeDatabase.initialiseDatabase();
    ms.mimeComplete();
    // FIXME Load the reporting side
    ms.reportsComplete();
    OdinClient odinClient = new OdinClient(ms);
  }
}