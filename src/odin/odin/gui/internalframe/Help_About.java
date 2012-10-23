/* Class name: Help_About
 * File name:  Help_About.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    11-Jan-2009 17:47:57
 * Modified:   26-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  26-Jul-2010 Adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 * 0.001  11-Jan-2009 Initial build
 */

package odin.odin.gui.internalframe;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.logging.*;
import javax.swing.*;
import odin.odin.gui.OdinClient;
import odin.odin.object.logging.LoggerFactory;

/**
 * Responsible for providing information about the application and the
 * environment in which it runs. This is part of the help system for the Odin
 * application.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class Help_About extends JInternalFrame
{
  /** The logger to be used for writing messages to the application log */
  private Logger log;

  /**
   * This initialises the class and its associated logger instance. It displays the contents of the
   * about.html file located in the HTML folder of the application directory.
   */
  public Help_About()
  {
    super("About Odin", true, true, true, true);
    super.setLayer(1);
    // Obtain an instance of Logger for the class
    log = LoggerFactory.getLogger(getClass().getName());
    try
    {
      JEditorPane jepHelpText = new JEditorPane();
      jepHelpText.setEditable(false);
      URL urlHelp = new URL("file","localhost","HTML/about.html");
      jepHelpText.setPage(urlHelp);
      JScrollPane jspJEP = new JScrollPane(jepHelpText);
      this.add(jspJEP, BorderLayout.CENTER);
    }
    catch (IOException ioX)
    {
      log.throwing(getClass().getName(), "actionPerformed()", ioX);
      System.err.print(ioX);
    }
    JTextArea jtaEnvironment = new JTextArea();
    jtaEnvironment.setFont(new Font("Sans Serif", Font.PLAIN, 10));
    jtaEnvironment.append("SYSTEM PROPERTIES:");
    jtaEnvironment.append("\n~~~~~~~~~~~~~~~~~~~");
    jtaEnvironment.append("\nJava Vendor:\t" + System.getProperty("java.vendor"));
    jtaEnvironment.append("\nJava Version:\t" + System.getProperty("java.version"));
    jtaEnvironment.append("\nRunning on:\t" + System.getProperty("os.name"));
    jtaEnvironment.append("\nVersion:\t" + System.getProperty("os.version"));
    jtaEnvironment.append("\nArchitecture:\t" + System.getProperty("os.arch"));
    jtaEnvironment.append("\nAs User:\t" + System.getProperty("user.name"));
    jtaEnvironment.append("\nUser's Locale:\t" + System.getProperty("user.language") + "_" + System.getProperty("user.country"));
    jtaEnvironment.setEditable(false);
    jtaEnvironment.setTabSize(4);
    this.add(jtaEnvironment, BorderLayout.SOUTH);
    log.finest("Adding frame to the desktop");
    OdinClient parent = OdinClient.getOdinWindow();
    parent.addToDesktop(this);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.pack();
    this.setSize(500, 400);
    this.setVisible(true);
  }
}