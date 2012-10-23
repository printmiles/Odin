/* Class name: ResultsFrame
 * File name:  ResultsFrame.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    18-Mar-2009 19:13:20
 * Modified:   10-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  10-May-2011 Code adapted for use in Odin
 * 1.000  17-Jun-2009 Code finalised and released.
 * 0.003  11-Jun-2009 Fixed problem with exporting data due to a null variable.
 * 0.002  27-Mar-2009 Added Save As CSV button under the Export Data jMenu.
 * 0.001  18-Mar-2009 Initial build
 */

package odin.sleipnir;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Used for displaying the results of SQL queries from the database. It provides
 * a simple means for displaying the results of the query in a table that can be
 * sorted by the user, by clicking on the column headers. It also provides a
 * means for the data to be exported to another program for further analysis or
 * presentation through the open CSV (comma separated value) format.
 * @version 1.001
 * @author Alex Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class ResultsFrame extends JInternalFrame
{
  /** The table holding the data to be displayed */
  private GenericTable table;

  /**
   * Initialises the class and internal logger as well as displaying the data
   * contained within the supplied instance of <code>GenericTable</code>.
   * <p>The instance of <code>GenericTable</code> passed as an argument will
   * have already had information about its structure defined through its own
   * initialisation. This allows for the this class to simply wrap it in an
   * instance of <code>JScrollPane</code> and present it to the user with a
   * basic menu allowing for the data contents to be exported.
   * <p>The class is primarily used to show the results from the tree of PSEs,
   * links, dates and times shown on the left of the monitoring screen.
   * @param title The title of the window.
   * @param gT The data to be displayed in the window.
   * @see GenericTable
   */
  public ResultsFrame(String title, GenericTable rT, ResourceBundle rb)
  {
    super(title, true, true, true, true);
    super.setLayer(1);
    JTable jT = new JTable(rT);
    jT.setRowSorter(new TableRowSorter(rT));
    jT.getColumnModel().getColumn(3).setCellRenderer(new ResultsRenderer(rb.getLocale()));
    // Remove the first
    jT.removeColumn(jT.getColumnModel().getColumn(0));
    JScrollPane jspTable = new JScrollPane(jT);
    this.add(jspTable);
    table = rT;
    SpringLayout slGenericFrame = new SpringLayout();
    this.setLayout(slGenericFrame);
    slGenericFrame.putConstraint(SpringLayout.NORTH, jspTable, 2, SpringLayout.NORTH, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.EAST, jspTable, -2, SpringLayout.EAST, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.SOUTH, jspTable, -2, SpringLayout.SOUTH, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.WEST, jspTable, 2, SpringLayout.WEST, this.getContentPane());
    JMenuBar jmb = new JMenuBar();
    JMenu jmExport = new JMenu(rb.getString("gui.results.export"));
    JMenuItem jmiCSV = new JMenuItem(rb.getString("gui.results.export.csv"));
    jmiCSV.setMnemonic(KeyEvent.VK_C);
    // FIXME Fix export event
    //jmiCSV.addActionListener(new GenericFrame_Export(this));
    jmExport.add(jmiCSV);
    jmb.add(jmExport);
    this.setJMenuBar(jmb);
    MainWindow.getMainWindow().addToDesktop(this);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(500,350);
    this.setVisible(true);
  }

  /**
   * Returns the instance of <code>GenericTable</code> used to contain data
   * displayed within the window.
   * @return The data model displayed in the window.
   * @see GenericTable
   */
  public GenericTable getGenericTable()
  {
    return table;
  }
}