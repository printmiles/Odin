/* Class name: OdinClient
 * File name:  OdinClient.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    17-Nov-2008 22:25:48
 * Modified:   26-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  26-Jul-2010 Code adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.gui;
import odin.odin.object.OdinPreferences;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.swing.*;
import odin.odin.events.gui.*;
import odin.odin.object.*;
import odin.odin.object.logging.*;

/**
 * This class is responsible for the general working environment of the Odin application.
 * On initialisation the window will setup the working environment, attach suitable listeners
 * and initialise any related threads required for the normal operation of the overall application.
 * @version 1.000
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class OdinClient extends JFrame
{
  /** The application preferences */
  private Preferences pGui;
  /** The logger to be used for writing messages to the application log */
  private Logger log;
  /** The desktop pane containing all of the child windows */
  private JDesktopPane jdMain;
  /** The menu-bar for the window */
  private JMenuBar jmbMenu;
  /** The centralised instance of the class to be returned once instantiated */
  private static OdinClient odinClient;

  /**
   * Initiates the application and internal logger. Requires the current splash
   * screen to be passed as an argument so that updates can be made.
   * @param ssSplash The splash screen being used to provide feedback to the user during the application load
   */
  public OdinClient(ModernSplash ssSplash)
  {
    // For more information see the constructor above
    log = LoggerFactory.getLogger(getClass().getName());
    log.finest("Setting ResourceBundle.");
    pGui = OdinPreferences.getOdinPrefs();
    odinClient = this;
    log.entering(getClass().getName(), "buildMenu()");
    buildMenu();
    log.entering(getClass().getName(), "buildGUI()");
    buildGUI();
    ssSplash.guiComplete();
    startApp();
    ssSplash.agentsComplete();
    ssSplash.readyComplete();
    ssSplash.closeSplashScreen();
  }

  /**
   * Part of the main constructor. This method is responsible for initialising
   * the menu along with its associated events.
   */
  private void buildMenu()
  {
    jmbMenu = new JMenuBar();
/* ~ ~ ~ ~ ~ ~ FILE MENU ~ ~ ~ ~ ~ ~ */
    JMenu jmFile;
    JMenuItem jmiFile_Exit;
    jmFile = new JMenu("File");
    jmFile.setMnemonic(KeyEvent.VK_F);
    jmiFile_Exit = new JMenuItem("Exit");
    jmiFile_Exit.setMnemonic(KeyEvent.VK_X);
    jmiFile_Exit.addActionListener(new CloseOdin());
    jmFile.add(jmiFile_Exit);
    jmbMenu.add(jmFile);
/* ~ ~ ~ ~ ~ ~ ADMINISTRATION MENU ~ ~ ~ ~ ~ ~ */
    JMenu jmAdmin;
    JMenuItem jmiAdmin_Bulk;
    jmAdmin = new JMenu("Admin");
    jmAdmin.setMnemonic(KeyEvent.VK_A);
    jmiAdmin_Bulk = new JMenuItem("Bulk Import");
    jmiAdmin_Bulk.setMnemonic(KeyEvent.VK_B);
    jmiAdmin_Bulk.addActionListener(new Client_BulkUpload());
    jmAdmin.add(jmiAdmin_Bulk);
    jmbMenu.add(jmAdmin);
/* ~ ~ ~ ~ ~ ~ CONFIGURATION MENU ~ ~ ~ ~ ~ ~ */
    JMenu jmConfig;
    JMenu jmConfig_LNF;
    JMenu jmConfig_Log;
    JMenuItem jmiConfig_SetLogDir;
    jmConfig = new JMenu("Configuration");
    jmConfig.setMnemonic (KeyEvent.VK_C);
    jmiConfig_SetLogDir = new JMenuItem("Set Log Directory");
    jmiConfig_SetLogDir.setMnemonic (KeyEvent.VK_V);
    jmiConfig_SetLogDir.addActionListener(new Client_LogDir(this));
    jmConfig_LNF = new JMenu("Look & Feel");
    jmConfig_LNF.setMnemonic (KeyEvent.VK_F);
    // Adding the Look and Feel section
    // Get the installed LookAndFeel (LnF) installed on the system
    UIManager.LookAndFeelInfo[] lafThisSystem = UIManager.getInstalledLookAndFeels();
    // Create a ButtonGroup to ensure that the RadioButtons work correctly
    ButtonGroup bgLnF = new ButtonGroup();
    // Iterate through the LnF array
    for (int i = 0; i < lafThisSystem.length; i++)
    {
      // Add a radio button with the LnF name to the menu
      JRadioButtonMenuItem jrbMI = new JRadioButtonMenuItem(lafThisSystem[i].getName());
      /* Attach an ActionListener to the button that knows:
       * a) The LnF class name
       * b) The parent GUI to apply the LnF to
       * c) The ResourceBundle to use for any issues that need to be echoed to the user
       */
      jrbMI.addActionListener(new Client_LnF(lafThisSystem[i].getClassName(), this));
      // Check the current LnF being iterated over to see if it matches the one saved in the preferences
      if (lafThisSystem[i].getClassName().equals(pGui.get("odin.lnf", "javax.swing.plaf.metal.MetalLookAndFeel")))
      {
        // If it matches then set the button as selected
        jrbMI.setSelected(true);
      }
      // Add the button to the ButtonGroup
      bgLnF.add(jrbMI);
      // Add the button to the Menu
      jmConfig_LNF.add(jrbMI);
    }
    // Building the Logging submenu
    jmConfig_Log = new JMenu("Log Level");
    jmConfig_Log.setMnemonic(KeyEvent.VK_L);
    String[] strLog = new String[] {"Severe","Warning","Info","Config","Fine","Finer","Finest","All","Off"};
    ButtonGroup bgLogs = new ButtonGroup();
    for (int i = 0; i < strLog.length; i++)
    {
      JRadioButtonMenuItem jrbMI = new JRadioButtonMenuItem(strLog[i]);
      jrbMI.addActionListener(new Client_Logging(strLog[i]));
      if (pGui.get("odin.logLevel", "All").equals(strLog[i]))
      {
        jrbMI.setSelected(true);
      }
      bgLogs.add(jrbMI);
      jmConfig_Log.add(jrbMI);
    }
    JMenuItem jmiConfig_XSLT = new JMenuItem("Run XSLT transformation");
    jmiConfig_XSLT.addActionListener(new Client_RunXSLT());
    jmConfig.add(jmConfig_LNF);
    jmConfig.add(jmConfig_Log);
    jmConfig.add(jmiConfig_SetLogDir);
    jmConfig.add(jmiConfig_XSLT);
    jmbMenu.add(jmConfig);
/* ~ ~ ~ ~ ~ ~ WINDOW MENU ~ ~ ~ ~ ~ ~ */
    JMenu jmWindow;
    JMenuItem jmiWindow_Tile;
    JMenuItem jmiWindow_MinimiseAll;
    jmWindow = new JMenu("Window");
    jmWindow.setMnemonic (KeyEvent.VK_W);
    jmiWindow_Tile = new JMenuItem("Tile");
    jmiWindow_Tile.setMnemonic(KeyEvent.VK_T);
    jmiWindow_Tile.addActionListener(new Client_TileWindow());
    jmiWindow_MinimiseAll = new JMenuItem("Minimise All");
    jmiWindow_MinimiseAll.setMnemonic(KeyEvent.VK_M);
    jmiWindow_MinimiseAll.addActionListener(new Client_MinimiseAll());
    jmWindow.add(jmiWindow_Tile);
    jmWindow.add(jmiWindow_MinimiseAll);
    jmbMenu.add(jmWindow);
/* ~ ~ ~ ~ ~ ~ HELP MENU ~ ~ ~ ~ ~ ~ */
    JMenu jmHelp;
    JMenuItem jmiHelp_About;
    jmHelp = new JMenu("Help");
    jmHelp.setMnemonic (KeyEvent.VK_H);
    jmiHelp_About = new JMenuItem("About");
    jmiHelp_About.setMnemonic (KeyEvent.VK_O);
    jmiHelp_About.addActionListener(new HelpAbout());
    jmHelp.add(jmiHelp_About);
    jmbMenu.add(jmHelp);
  }

  /**
   * Part of the constructor, it builds and places the core components of the GUI
   * on-screen.
   */
  private void buildGUI()
  {

    // Settting the JMenuBar
    setJMenuBar(jmbMenu);
    // Applying the preferred Look and Feel to the application. If it can't be found then revert to the Metal LnF
    String strLnF = pGui.get("odin.lnf", "javax.swing.plaf.metal.MetalLookAndFeel");
    try
    {
      log.finest("Trying to change Look and Feel for Odin to " + strLnF);
      // Apply the new LnF
      UIManager.setLookAndFeel(strLnF);
      // Refresh all of the components in the JFrame
      SwingUtilities.updateComponentTreeUI(this);
    }
    catch (Exception x)
    {
      // If there's a problem applying the LnF then log the error and notify the user in the status pane
      log.throwing(getClass().getName(), "buildGUI()", x);
      System.err.println("Look and Feel could not be applied to the GUI.");
    }
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("Open Document and Information Network - Odin");
    // Set the dimensions of the window to be the same as the current screen size but with a 10px border around the edges
    Dimension dimScreenSize = new Dimension();
    this.setSize(400, 300);
    // Set the background image of the JDesktopPane
    ImageIcon iiB = new ImageIcon("images.Odin148BackgroundLogo.png");
    JLabel lblBackground = new JLabel(iiB);
    lblBackground.setSize(iiB.getIconWidth() + 20, dimScreenSize.height-iiB.getIconHeight());
    lblBackground.setVerticalAlignment(SwingConstants.BOTTOM);
    jdMain = new JDesktopPane();
    jdMain.setDoubleBuffered(true);
    JScrollPane jspDesktop = new JScrollPane(jdMain);
    getContentPane().add(jspDesktop,BorderLayout.CENTER);
    // Add the customised Window listener
    addWindowListener(new CloseOdin());
    // Set the GUI to visible
    setVisible(true);
  }

  /**
   * First of all the method checks whether the database can be connected to. If
   * it cannot then the user is warned and no windows opened to monitor COM ports.
   * Otherwise it looks through the application preferences for details of COM
   * ports to monitor and then starts threads accordingly.
   * @see #addPort(java.lang.String, int, int, java.lang.String, float)
   */
  private void startApp()
  {
    odin.odin.ws.ServicePublisher publisher = new odin.odin.ws.ServicePublisher();
    //publisher.publishOdin();
    publisher.publishSleipnir();
  }

  /**
   * Used for other classes to obtain a copy of the current Odin MDI parent application window
   * @return The main Odin window that is being displayed.
   */
  public static OdinClient getOdinWindow()
  {
    return odinClient;
  }

  /**
   * Adds the supplied instance of <code>JInternalFrame</code> to the desktop
   * for display to the user.
   * @param jIF An internal frame to be displayed to the user.
   * @see javax.swing.JInternalFrame
   */
  public void addToDesktop(JInternalFrame jIF)
  {
    jdMain.add(jIF);
  }

  /**
   * Returns the instance of <code>JDesktopPane</code> to the receiver.
   * @return The instance of <code>JDesktopPane</code> used by the current class instance.
   * @see javax.swing.JDesktopPane
   */
  public JDesktopPane getDesktopPane()
  {
    return jdMain;
  }
}