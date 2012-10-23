/* Class name: HelpAbout
 * File name:  HelpAbout.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    11-Jan-2009 17:47:57
 * Modified:   19-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  19-Jul-2010 Adapted for Odin from JavaNews
 * 1.000  17-Jun-2009 Code finalised and released.
 * 0.001  11-Jan-2009 Initial build
 */

package odin.sleipnir;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Responsible for providing information about the application and the
 * environment in which it runs.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class HelpAbout extends JInternalFrame
{
  /**
   * This initialises the class and its associated logger instance. It displays the contents of the
   * about.html file located in the HTML folder of the application directory.
   */
  public HelpAbout(ResourceBundle rb)
  {
    super(rb.getString("gui.help.about.name"), true, true, true, true);
    super.setLayer(1);
    JScrollPane jspJEP = new JScrollPane();
    try
    {
      JEditorPane jepHelpText = new JEditorPane();
      jepHelpText.setEditable(false);
      jepHelpText.setPage(this.getClass().getResource(rb.getString("gui.help.about.file")));
      jspJEP = new JScrollPane(jepHelpText);
    }
    catch (IOException ioX)
    {
      System.err.println(rb.getString("general.error"));
      System.err.print(ioX);
    }
    ArrayList alColumns = new ArrayList();
    alColumns.add(rb.getString("gui.help.about.table.key"));
    alColumns.add(rb.getString("gui.help.about.table.value"));
    ArrayList alRows = new ArrayList();
    alRows.add(new Object[]{rb.getString("gui.help.about.table.javaVendor"),System.getProperty("java.vendor")});
    alRows.add(new Object[]{rb.getString("gui.help.about.table.javaVersion"),System.getProperty("java.version")});
    alRows.add(new Object[]{rb.getString("gui.help.about.table.platform"),System.getProperty("os.name")});
    alRows.add(new Object[]{rb.getString("gui.help.about.table.platformVersion"),System.getProperty("os.version")});
    alRows.add(new Object[]{rb.getString("gui.help.about.table.architecture"),System.getProperty("os.arch")});
    alRows.add(new Object[]{rb.getString("gui.help.about.table.locale"),System.getProperty("user.language") + "_" + System.getProperty("user.country")});
    GenericTable gT = new GenericTable();
    gT.setData(alColumns, alRows);
    JTable jt = new JTable(gT);
    JScrollPane jspJT = new JScrollPane(jt);
    JSplitPane jsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jspJEP, jspJT);
    jsplit.setContinuousLayout(true);
    jsplit.setDividerLocation(225);
    this.add(jsplit, BorderLayout.CENTER);
    MainWindow parent = MainWindow.getMainWindow();
    parent.addToDesktop(this);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(500, 400);
    this.setVisible(true);
  }
}