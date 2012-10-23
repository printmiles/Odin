/* Class name: GenericFrame
 * File name:  GenericFrame.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    18-Mar-2009 19:13:20
 * Modified:   28-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  28-Jul-2010 Code adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.gui.internalframe;
import odin.odin.gui.OdinClient;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.*;
import odin.odin.gui.*;
import odin.odin.object.logging.LoggerFactory;
import odin.odin.object.table.GenericTable;

/**
 * Used for displaying the results of queries from the database. It provides
 * a simple means for displaying the results of the query in a table that can be
 * sorted by the user, by clicking on the column headers.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class GenericFrame extends JInternalFrame
{
  /** The logger to be used for writing messages to the application log */
  private Logger log;
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
   * @see org.kcl.odin.odin.object.table.GenericTable
   */
  public GenericFrame(String title, GenericTable gT)
  {
    super(title, true, true, true, true);
    super.setLayer(1);
    // Obtain an instance of Logger for the class
    log = LoggerFactory.getLogger(getClass().getName());
    JTable jT = new JTable(gT);
    jT.setRowSorter(new TableRowSorter(gT));
    JScrollPane jspTable = new JScrollPane(jT);
    this.add(jspTable);
    table = gT;
    SpringLayout slGenericFrame = new SpringLayout();
    this.setLayout(slGenericFrame);
    slGenericFrame.putConstraint(SpringLayout.NORTH, jspTable, 2, SpringLayout.NORTH, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.EAST, jspTable, -2, SpringLayout.EAST, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.SOUTH, jspTable, -2, SpringLayout.SOUTH, this.getContentPane());
    slGenericFrame.putConstraint(SpringLayout.WEST, jspTable, 2, SpringLayout.WEST, this.getContentPane());
    OdinClient.getOdinWindow().addToDesktop(this);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(500,350);
    this.setVisible(true);
    log.finest("Displaying new generic frame for: " + title);
  }

  /**
   * Returns the instance of <code>GenericTable</code> used to contain data
   * displayed within the window.
   * @return The data model displayed in the window.
   * @see javanews.object.table.GenericTable
   */
  public GenericTable getGenericTable()
  {
    return table;
  }
}