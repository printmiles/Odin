/* Class name: ModernSplash
 * File name:  ModernSplash.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    13-Oct-2008 13:47:01
 * Modified:   18-Jul-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.002  18-Jul-2011 SplashScreen modernised
 * 1.001  12-Jul-2010 Code adapted for Odin
 * 1.000  17-Jun-2009 Code finalised and released.
 * 0.001  13-Oct-2008 Initial build
 */

package odin.odin.gui;
import java.awt.*;
import javax.swing.*;

/**
 * Used to provide information to the user while the main components of the
 * application are initialised.
 * @version 1.002
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class ModernSplash extends JWindow
{
  private JLayeredPane jlpParent;
  private JLabel jlAgents;
  private JLabel jlBackground;
  private JLabel jlGui;
  private JLabel jlLogs;
  private JLabel jlMime;
  private JLabel jlReady;
  private JLabel jlReports;
  private int WIDTH = 500;
  private int HEIGHT = 200;

  /**
   * <p>Initialises the class for the predetermined stages for this application.
   * Once the application has completed each of the start-up stages, it must call
   * the <code>stageComplete()</code> method (where <code>stage</code> is the
   * corresponding start-up process) to update the GUI.</p>
   * <p>Once completed loading the splash-screen should then be closed to signify
   * that the application is ready for user input.</p>
   */
  public ModernSplash()
  {
    // Set the window's bounds, centering the window.
    Dimension screen = Toolkit.getDefaultToolkit( ).getScreenSize();
    int x = (screen.width-WIDTH)/2;
    int y = (screen.height-HEIGHT)/2;
    setBounds(x,y,WIDTH,HEIGHT);

    // Build the splash screen.
    initComponents();

    // Display it.
    setVisible(true);
  }
  
  /**
   * Hides the SplashScreen
   */
  public void closeSplashScreen()
  {
    try
    {
      Thread.sleep(500);
    }
    catch (Exception x)
    {
      // The thread has been interrupted but not seriously
    }
    finally
    {
      setVisible(false);
    }
  }
  
  /**
   * Used to show the log initialisation stage is complete by swapping the grey-scale
   * icon for the coloured version.
   */
  public void logsComplete()
  {
    jlLogs.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/logs.png")));
  }
  
  /**
   * Used to show the mime database initialisation stage is complete by swapping the grey-scale
   * icon for the coloured version.
   */
  public void mimeComplete()
  {
    jlMime.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/mime.png")));
  }
  
  /**
   * Used to show the report initialisation stage is complete by swapping the grey-scale
   * icon for the coloured version.
   */
  public void reportsComplete()
  {
    jlReports.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/reports.png")));
  }
  
  /**
   * Used to show the GUI initialisation stage is complete by swapping the grey-scale
   * icon for the coloured version.
   */
  public void guiComplete()
  {
    jlGui.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/gui.png")));
  }
  
  /**
   * Used to show the agent initialisation stage is complete by swapping the grey-scale
   * icon for the coloured version.
   */
  public void agentsComplete()
  {
    jlAgents.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/agents.png")));
  }
  
  /**
   * Used to show the application is complete by swapping the grey-scale
   * icon for the coloured version.
   */
  public void readyComplete()
  {
    jlReady.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/ready.png")));
  }
  
  /**
   * This method has been adapted from a class built using NetBeans GUI builder.
   */
  private void initComponents()
  {
    jlpParent = new JLayeredPane();
    jlBackground = new JLabel();
    jlReady = new JLabel();
    jlLogs = new JLabel();
    jlMime = new JLabel();
    jlReports = new JLabel();
    jlGui = new JLabel();
    jlAgents = new JLabel();

    setMinimumSize(new Dimension(500, 200));

    jlBackground.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/background.png")));
    jlBackground.setBounds(0, 0, 500, 200);
    jlpParent.add(jlBackground, JLayeredPane.DEFAULT_LAYER);

    jlLogs.setHorizontalAlignment(SwingConstants.CENTER);
    jlLogs.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/g_logs.png")));
    jlLogs.setBounds(58, 136, 64, 64);
    jlpParent.add(jlLogs, JLayeredPane.PALETTE_LAYER);

    jlMime.setHorizontalAlignment(SwingConstants.CENTER);
    jlMime.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/g_mime.png")));
    jlMime.setBounds(122, 136, 64, 64);
    jlpParent.add(jlMime, JLayeredPane.PALETTE_LAYER);

    jlReports.setHorizontalAlignment(SwingConstants.CENTER);
    jlReports.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/g_reports.png")));
    jlReports.setBounds(186, 136, 64, 64);
    jlpParent.add(jlReports, JLayeredPane.PALETTE_LAYER);

    jlGui.setHorizontalAlignment(SwingConstants.CENTER);
    jlGui.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/g_gui.png")));
    jlGui.setBounds(250, 136, 64, 64);
    jlpParent.add(jlGui, JLayeredPane.PALETTE_LAYER);

    jlAgents.setHorizontalAlignment(SwingConstants.CENTER);
    jlAgents.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/g_agents.png")));
    jlAgents.setBounds(314, 136, 64, 64);
    jlpParent.add(jlAgents, JLayeredPane.PALETTE_LAYER);
    
    jlReady.setHorizontalAlignment(SwingConstants.CENTER);
    jlReady.setIcon(new ImageIcon(getClass().getResource("/odin/odin/images/g_ready.png")));
    jlReady.setBounds(378, 136, 64, 64);
    jlpParent.add(jlReady, JLayeredPane.PALETTE_LAYER);

    getContentPane().add(jlpParent);

    pack();
  }
}