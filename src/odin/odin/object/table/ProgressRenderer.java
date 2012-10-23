/* Class name: ProgressRenderer
 * File name:  ProgressRenderer.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    06-Jun-2011 21:25:40
 * Modified:   06-Jun-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  06-Jun-2011 Initial build
 */
package odin.odin.object.table;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Used in conjunction with the Bulk Upload function. This class is responsible for
 * rendering cell values within a <code>GenericTable</code> as progress bars
 * using the <code>javax.swing.JProgressBar</code> class.
 * <p>Adapts code from: http://download.oracle.com/javase/tutorial/uiswing/components/table.html
 * and http://download.oracle.com/javase/tutorial/uiswing/examples/components/TableDialogEditDemoProject/src/components/ColorRenderer.java [Accessed 10th May 2011]
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 * @see odin.odin.object.table.GenericTable
 * @see odin.odin.gui.internalframe.BulkUploadFrame
 * @see javax.swing.JProgressBar
 */

public class ProgressRenderer extends DefaultTableCellRenderer
{ 
  private static final int MAX_PROGRESS_VALUE = 6;
  
  /**
   * Initialise settings within the class
   */
  public ProgressRenderer()
  {   
    setOpaque(true);
  }
  
  /**
   * Provides a subclass instance of <code>Component</code> which has been updated
   * to reflect the changing input value (progressValue). In this case the Component
   * returned is an instance of <code>JProgressBar</code> updated to reflect the
   * new value.
   * @param table The table we're updating
   * @param progressValue The new value to be displayed
   * @param isSelected Whether the cell is selected or not
   * @param hasFocus Whether the cell has the user's focus
   * @param row The row number
   * @param column The column number
   * @return The updated <code>JProgressBar</code>
   * @see java.awt.Component
   * @see javax.swing.JProgressBar
   */
  public Component getTableCellRendererComponent(JTable table, Object progressValue, boolean isSelected, boolean hasFocus, int row, int column)
  { 
    int progress = Integer.parseInt(progressValue.toString());
    if (progress <= MAX_PROGRESS_VALUE) // Upload progress
    {
      JProgressBar jpb = new JProgressBar();
      jpb.setMinimum(0);
      jpb.setMaximum(MAX_PROGRESS_VALUE);
      jpb.setValue(progress);
      return jpb;
    }
    else if (progress == 201) // Upload successful
    {
      JLabel jl = new JLabel("Done");
      return jl;
    }
    else // Any other codes are going to be errors
    {
      JLabel jl = new JLabel("Failed");
      return jl;
    }
  }
}