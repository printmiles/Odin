/* Class name: MainWindow
 * File name:  MainWindow.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    17-Nov-2008 22:25:48
 * Modified:   19-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  19-Jul-2010 Code adapted from JavaNews (a previous project)
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.sleipnir;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * This class is responsible for the general working environment of the JavaNews application.
 * Processing of messages from the PSEs is not performed here but in the COMPortClient class.
 * This class is used to manage instances of the COMPortClient class and handle the threads
 * contained within them.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class MainWindow extends JFrame
{
  /** The desktop pane containing all of the child windows */
  private JDesktopPane jdMain;
  /** The menubar for the window */
  private JMenuBar jmbMenu;
  /** The centralised instance of the class to be returned once instantiated */
  private static MainWindow slClient;
  /** The ResourceBundle containing all of the localised strings */
  private ResourceBundle rbSleipnir;
  /** The Locale of the current user */
  private Locale lUser;
  /** Details of the Odin server we're connecting to */
  private static OdinServer odin;
  
  /**
   * Initiates the application and internal logger. Requires the current splash
   * screen to be passed as an argument so that updates can be made.
   * @param ssSplash The splash screen being used to provide feedback to the user during the application load
   * @param rb The ResourceBundle to be used for obtaining localised strings
   * @param l The Locale to be used as defined either by the administrator or the local machine
   * @param homeOdinServer The DNS name or IP address of the Odin server to which this client should connect
   */
  public MainWindow(SplashScreen ssSplash, ResourceBundle rb, Locale l, String homeOdinServer)
  {
    // For more information see the constructor above
    rbSleipnir = rb;
    slClient = this;
    lUser = l;
    odin = new OdinServer();
    odin.setIpAddress(homeOdinServer);
    buildMenu();
    buildGUI();
    ssSplash.increaseProgress(rbSleipnir.getString("load.splash.2"));
    startApp();
    ssSplash.increaseProgress(rbSleipnir.getString("load.splash.3"));
    ssSplash.increaseProgress();
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
    JMenuItem jmiFile_Upload;
    JMenuItem jmiFile_Search;
    JMenuItem jmiFile_Exit;
    jmFile = new JMenu(rbSleipnir.getString("gui.main.menu.File"));
    jmFile.setMnemonic(rbSleipnir.getString("gui.main.menu.acc.File").charAt(0));
    jmiFile_Upload = new JMenuItem(rbSleipnir.getString("gui.main.menu.Upload"));
    jmiFile_Upload.setMnemonic(rbSleipnir.getString("gui.main.menu.acc.Upload").charAt(0));
    jmiFile_Upload.addActionListener(new ShowUpload());
    jmiFile_Search = new JMenuItem(rbSleipnir.getString("gui.main.menu.Search"));
    jmiFile_Search.setMnemonic(rbSleipnir.getString("gui.main.menu.acc.Search").charAt(0));
    jmiFile_Search.addActionListener(new ShowSearch());
    jmiFile_Exit = new JMenuItem(rbSleipnir.getString("gui.main.menu.Exit"));
    jmiFile_Exit.setMnemonic(rbSleipnir.getString("gui.main.menu.acc.Exit").charAt(0));
    jmiFile_Exit.addActionListener(new CloseSleipnir());
    jmFile.add(jmiFile_Upload);
    jmFile.add(jmiFile_Search);
    jmFile.addSeparator();
    jmFile.add(jmiFile_Exit);
    jmbMenu.add(jmFile);
/* ~ ~ ~ ~ ~ ~ HELP MENU ~ ~ ~ ~ ~ ~ */
    JMenu jmHelp;
    JMenuItem jmiHelp_Server;
    JMenuItem jmiHelp_About;
    jmHelp = new JMenu(rbSleipnir.getString("gui.main.menu.Help"));
    jmHelp.setMnemonic (rbSleipnir.getString("gui.main.menu.acc.Help").charAt(0));
    jmiHelp_Server = new JMenuItem(rbSleipnir.getString("gui.main.menu.help.server"));
    jmiHelp_Server.setMnemonic(rbSleipnir.getString("gui.main.menu.acc.server").charAt(0));
    jmiHelp_Server.addActionListener(new ShowServerDetails());
    jmiHelp_About = new JMenuItem(rbSleipnir.getString("gui.main.menu.About"));
    jmiHelp_About.setMnemonic(rbSleipnir.getString("gui.main.menu.acc.About").charAt(0));
    jmiHelp_About.addActionListener(new ShowAbout());
    jmHelp.add(jmiHelp_Server);
    jmHelp.addSeparator();
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
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("Sleipnir");
    // Set the dimensions of the window to be the same as the current screen size but with a 10px border around the edges
    Dimension dimScreenSize = new Dimension();
    dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(200, 200, dimScreenSize.width - 400,dimScreenSize.height - 400);
    // Set the background image of the JDesktopPane
    ImageIcon iiB = new ImageIcon(this.getClass().getResource("images/Sleipnir148BackgroundLogo.png"));
    JLabel lblBackground = new JLabel(iiB);
    lblBackground.setSize(iiB.getIconWidth() + 20, dimScreenSize.height-iiB.getIconHeight());
    lblBackground.setVerticalAlignment(SwingConstants.BOTTOM);
    jdMain = new JDesktopPane();
    jdMain.setDoubleBuffered(true);
    JScrollPane jspDesktop = new JScrollPane(jdMain);
    getContentPane().add(jspDesktop,BorderLayout.CENTER);
    // Add the customised Window listener
    addWindowListener(new CloseSleipnir());
    // Set the GUI to visible
    setVisible(true);
  }

  public OdinServer getServer()
  {
    return odin;
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
    
  }

  /**
   * Used for other classes to obtain a copy of the current JavaNews MDI parent application window
   * @return The main JavaNews window that is being displayed.
   */
  public static MainWindow getMainWindow()
  {
    return slClient;
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
  
  public void findDoc(String guid)
  {
    Search s = new Search(rbSleipnir);
    s.searchForDoc(guid);
  }

  public class CloseSleipnir extends WindowAdapter implements ActionListener
  {
    /**
     * This method is called when the application window is requested to shut.
     * The program then closes the server connection gracefully.
     * @param e The triggered WindowEvent
     */
    public void windowClosing(WindowEvent e)
    {
      closeApp();
    }

    /**
     * This method is called when the user clicks on File > Exit from the menu
     * bar. The program then closes the server connection gracefully.
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
      System.gc();
      System.out.println("Closing application at: " + java.util.Calendar.getInstance().getTime().toString());
      System.exit(1);
    } 
  }

  public class ShowUpload implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      Upload u = new Upload(rbSleipnir, lUser);
    }
  }

  public class ShowSearch implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      Search s = new Search(rbSleipnir);
    }
  }

  public class ShowAbout implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      HelpAbout ha = new HelpAbout(rbSleipnir);
    }
  }

  public class ShowServerDetails implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      ServerDetails sd = new ServerDetails(rbSleipnir);
    }
  }
}