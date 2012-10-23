/* Class name: SplashScreen
 * File name:  SplashScreen.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    16-Jul-2010 18:33:30
 * Modified:   16-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.000  16-Jul-2010 Initial build
 */

package odin.sleipnir;

import java.awt.*;
import javax.swing.*;

/**
 * Used to provide information to the user while the main components of the
 * application are initialised.
 * @version 1.000
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class SplashScreen extends JWindow
{
  /** The width of the splash screen */
  private static final int WIDTH = 450;
  /** The height of the splash screen */
  private static final int HEIGHT = 180;
  /** The colour of the progress bar contents */
  private static final Color ProgressBar_COLOUR = Color.BLACK;
  /** The colour for text displayed within the splash screen */
  private static final Color Text_COLOUR = Color.BLACK;
  /** The status text displayed above the progress bar */
  private static JLabel textVal;
  /** The progress bar displayed on the splash screen */
  private static JProgressBar progress;

  /**
   * Initialises the class and sets the maximum value of steps taken to initialise
   * the application. Once the application has passed through these stages, and
   * called <code>increaseProgress()</code> accordingly the splash-screen is
   * updated and eventually closed to allow for interaction with the main program.
   * @param maximumValue The total number of stages of initialisation for the program.
   */
  public SplashScreen(int maximumValue)
  {
    JPanel content = (JPanel) getContentPane();
    content.setBackground(Color.WHITE);

    // Set the window's bounds, centering the window.
    Dimension screen = Toolkit.getDefaultToolkit( ).getScreenSize( );
    int x = (screen.width-WIDTH)/2;
    int y = (screen.height-HEIGHT)/2;
    setBounds(x,y,WIDTH,HEIGHT);

    // Build the splash screen.
    JLabel label = new JLabel(new ImageIcon(this.getClass().getResource("images/SleipnirLogo.png")));
    JPanel jPBottom = new JPanel(new GridLayout(2,1));
    textVal = new JLabel("Loading...", JLabel.CENTER);
    textVal.setFont(new Font("Sans-Serif", Font.BOLD, 12));
    textVal.setForeground(Text_COLOUR);
    progress = new JProgressBar();
    progress.setMaximum(maximumValue);
    progress.setForeground(ProgressBar_COLOUR);
    jPBottom.add(textVal);
    jPBottom.add(progress);
    content.add(label, BorderLayout.CENTER);
    content.add(jPBottom, BorderLayout.SOUTH);

    // Display it.
    setVisible(true);
  }

  /**
   * Increases the value of the progress bar without changing the displayed text.
   * The value of the progress bar is incremented by one. If the new value of the
   * progress bar is greater than the value the splash-screen was initialised with
   * then it is closed.
   */
  public void increaseProgress()
  {
    int newVal = progress.getValue() + 1;
    if (newVal <= progress.getMaximum())
    {
      progress.setValue(newVal);
    }
    else
    {
      this.setVisible(false);
      this.dispose();
    }
  }

  /**
   * Increases the value of the progress bar and updates the displayed text to
   * that of the supplied argument. The <code>increaseProgress()</code> method
   * is called to update the value and so the same conditions apply.
   * @param newText The new text to be displayed above the progress bar.
   * @see #increaseProgress()
   */
  public void increaseProgress(String newText)
  {
    textVal.setText(newText);
    increaseProgress();
  }
}