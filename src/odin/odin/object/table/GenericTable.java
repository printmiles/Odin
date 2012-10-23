/* Class name: GenericTable
 * File name:  GenericTable.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    19-Dec-2008 21:02:04
 * Modified:   28-Jul-2010
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 1.001  28-Jul-2010 Code adapted for Odin.
 * 1.000  17-Jun-2009 Code finalised and released.
 */

package odin.odin.object.table;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.table.AbstractTableModel;
import odin.odin.object.logging.LoggerFactory;

/**
 * This class is used to contain data for tables. All tables used in this application use this class
 * for data storage and retrieval.
 * @version 1.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class GenericTable extends AbstractTableModel
{
  /** The list of column names for the table */
  private ArrayList columnNames = new ArrayList();
  /** The list of row entries for the table */
  private ArrayList data = new ArrayList();
  /** The logger to be used for writing messages to the application log */
  private static Logger log;
  /** The list of user-editable columns within the table */
  private int[] editableCells;

  /**
   * Creates a new instance of the class and initialises the table with some default data values.
   * These default values are in a one column ArrayList with five rows (New Data To Be Displayed Here #n).
   * These can be used if needed for testing but should be overwritten within the application on first use.
   * Therefore the first method called on a new instance of this class must be <code>setData()</code>
   * rather than <code>addRow()</code>.
   * @see java.util.ArrayList
   * @see #addRow(java.lang.Object[])
   * @see #setData(java.util.ArrayList)
   * @see #setData(java.util.ArrayList, java.util.ArrayList)
   */
  public GenericTable()
  {
    log = LoggerFactory.getLogger(getClass().getName());
    log.finer("Building default table");
    columnNames.add("Default Table");
    data.add(new Object[] {"New Data To Be Displayed Here #1"});
    data.add(new Object[] {"New Data To Be Displayed Here #2"});
    data.add(new Object[] {"New Data To Be Displayed Here #3"});
    data.add(new Object[] {"New Data To Be Displayed Here #4"});
    data.add(new Object[] {"New Data To Be Displayed Here #5"});
  }

  /**
   * Returns the number of columns stored within the table.
   * <p><i>Inherited method from the AbstractTableModel class.</i></p>
   * @return The number of columns in the table
   */
  public int getColumnCount()
  {
    return columnNames.size();
  }

  /**
   * Returns the number of rows stored within the table.
   * <p><i>Inherited method from the AbstractTableModel class.</i></p>
   * @return The number of rows in the table
   */
  public int getRowCount()
  {
    return data.size();
  }

  /**
   * Returns the name of a column given its index in the table.
   * <p><i>Inherited method from the AbstractTableModel class.</i></p>
   * @param col The column index
   * @return The column name
   */
  public String getColumnName(int col)
  {
    String name = (String) columnNames.get(col);
    log.finest("Returning value: " + name);
    return name;
  }

  /**
   * Returns the object stored within the table at the position denoted by the
   * row and column index.
   * <p><i>Inherited method from the AbstractTableModel class.</i></p>
   * @param row The row index of the object to be returned
   * @param col The column index of the object to be returned
   * @return The object requested
   */
  public Object getValueAt(int row, int col)
  {
    Object[] temp = (Object[]) data.get(row);
    if (col >= temp.length)
    {
      return null;
    }
    else
    {
      return temp[col];
    }
  }

  /**
   * Returns the class of the column denoted by the aupplied index.
   * <p><i>Inherited method from the AbstractTableModel class.</i></p>
   * @param c The column index
   * @return The class of the column
   */
  public Class getColumnClass(int c)
  {
    return getValueAt(0, c).getClass();
  }

  /**
   * Sets the value of a specific entry in the table given by a row and column index.
   * <p><i>Overrides method from the AbstractTableModel class.</i></p>
   * @param value The new value to store
   * @param row The row index in the table
   * @param col The column index in the table
   */
  public void setValueAt(Object value, int row, int col)
  {
    Object[] temp = (Object[]) data.get(row);
    temp[col] = value;
    fireTableCellUpdated(row, col);
    log.log(Level.FINER, "Value at row " + row + ", column " + col + " has been changed.", value);
  }

  /**
   * Replaces the currently stored values in the table with the new arguments.
   * <p>The columns ArrayList must contain a list of Strings while the rows can
   * contain any object providing that the entries in one column match the type of
   * those above and below it in the table.
   * @param columns A ArrayList contain the names of the columns stored as Strings
   * @param rows A ArrayList containing an array of entries to be stored in the table rows
   * @see java.util.ArrayList
   */
  public void setData(ArrayList columns, ArrayList rows)
  {
    log.finer("Storing new data values and column names");
    columnNames = columns;
    data = rows;
    fireTableStructureChanged();
    if (data == null)
    {
      fireTableStructureChanged();
    }
    else
    {
      fireTableStructureChanged();
    }
  }

  /**
   * Replaces the currently held rows with the contents of the supplied argument.
   * <p>If the table has already been defined then this must have an equal number of
   * columns to the number of column headers though the types may vary from those
   * previously stored in the table.
   * @param rows The new table rows to be stored
   * @see java.util.ArrayList
   */
  public void setData(ArrayList rows)
  {
    log.finer("Storing new data values");
    data = rows;
    fireTableDataChanged();
  }

  /**
   * Adds a new row to the table containing the table rows. The supplied Object array
   * must be in the same order as those already within the table otherwise the entry won't
   * display correctly.
   * @param aNewRow The new row to be added
   */
  public void addRow(Object[] aNewRow)
  {
    log.log(Level.FINER, "Adding new row to the table", aNewRow);
    data.add(aNewRow);
    fireTableRowsInserted(0, data.size()-1);
  }

  /**
   * Used to remove a particular row from the table model.
   * @param x The row number to be removed.
   */
  public void removeRow(int x)
  {
    data.remove(x);
    fireTableRowsDeleted(x, x);
  }

  /**
   * Defines the columns within the table which are editable to those contained within the array.
   * If the table is completely uneditable then this method shouldn't be used or the argument
   * should be set to null or an empty array. The columns whose index is included in the array
   * is then set to editable. This means that if a whole table is set to editable then all indices
   * should be contained within the array from 0 to size-1. The entries may be non-sequential and unordered.
   * @param editableColumns
   */
  public void setEditableColumns(int[] editableColumns)
  {
    editableCells = editableColumns;
  }

  /**
   * Returns a boolean value representing whether the column can be edited or not.
   * @param row This is used internally by Java and not of importance to the functioning of the method.
   * @param col This is the (zero-based) column index that is being queried.
   * @return Whether or not the column contents can be edited.
   */
  public boolean isCellEditable(int row, int col)
  {
    boolean result = false;

    if (editableCells == null)
    {
      return false;
    }

    for (int i = 0; i < editableCells.length; i++)
    {
      if (col == editableCells[i])
      {
        result = true;
      }
    }

    return result;
  }

  /**
   * Used to return the ArrayList containing all of the row data
   * @return The ArrayList containing the Object[] arrays making up the table data
   * @see java.util.ArrayList
   */
  public ArrayList getRows()
  {
    return data;
  }

  /**
   * Used to return the headers of a table
   * @return The ArrayList containing the headers for the table
   * @see java.util.ArrayList
   */
  public ArrayList getColumns()
  {
    return columnNames;
  }

  /**
   * Used to return a particular row within the table
   * @param x The row at position x to be returned
   * @return The row contents as an instance of Object[]
   */
  public Object[] getRow(int x)
  {
    return (Object[]) data.get(x);
  }
}