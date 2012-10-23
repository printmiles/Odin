/* Class name: ResultsRenderer
 * File name:  ResultsRenderer.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    10-May-2011 16:29:54
 * Modified:   10-May-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  10-May-2011 Initial build
 */
package odin.sleipnir;

import java.awt.Component;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import java.util.Locale;
import java.util.TreeMap;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * JavaDoc to follow.
 * <p>Adapts code from: http://download.oracle.com/javase/tutorial/uiswing/components/table.html
 * and http://download.oracle.com/javase/tutorial/uiswing/examples/components/TableDialogEditDemoProject/src/components/ColorRenderer.java [Accessed 10th May 2011]
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class ResultsRenderer extends DefaultTableCellRenderer
{
  private JLabel jlFlag;
  private Locale lHome;
  private TreeMap<String,String> tmCountries;
  
  public ResultsRenderer(Locale myLocale)
  {   
    setOpaque(true);
    lHome = myLocale;
    String[] listCountries = Locale.getISOCountries();
    tmCountries = new TreeMap<String,String>();
    for (String country : listCountries)
    {
      String name = (new Locale(lHome.getLanguage(), country)).getDisplayCountry(lHome);
      tmCountries.put(country, name);
    }
  }
  
  public Component getTableCellRendererComponent(JTable table, Object countryName, boolean isSelected, boolean hasFocus, int row, int column)
  { 
    String strISO = (String) countryName;
    jlFlag = new JLabel();
    URL iconURL = this.getClass().getResource("images/flags/"+ strISO.toLowerCase() +".png");
    ImageIcon iiFlag;
    if (iconURL != null)
    {
      iiFlag = new ImageIcon(this.getClass().getResource("images/flags/"+ strISO.toLowerCase() +".png"));
    }
    else
    {
      iiFlag = new ImageIcon(this.getClass().getResource("images/flags/zz.png"));
    }
    Image i = iiFlag.getImage();
    Image j = i.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
    iiFlag.setImage(j);
    jlFlag.setIcon(iiFlag);
    String toolTip = tmCountries.get(strISO);
    jlFlag.setToolTipText(toolTip);
    return jlFlag;
  }
}