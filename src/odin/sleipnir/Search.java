/* Class name: Search
 * File name:  Search.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    24-Jul-2010 22:12:05
 * Modified:   29-Apr-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.002  29-Apr-2011 Changed structure and added internationalisation
 * 0.001  24-Jul-2010 Initial build
 */

package odin.sleipnir;

import java.util.*;
import javax.xml.datatype.*;
import odin.shared.xml.*;

/**
 * JavaDoc to follow.
 * @version 0.002
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */

public class Search extends javax.swing.JInternalFrame
{
  ResourceBundle rbUser;
  TreeSet tsMime;
  TreeSet tsExt;
  TreeMap<String,String> tmLanguages;
  TreeMap<String,String> tmCountries;

  /** Creates new form Search */
  public Search(ResourceBundle rb)
  {
    super("Search",true,true,true,true);
    super.setLayer(1);
    rbUser = rb;
    initComponents();
    populateLocale();
    updateTabNames();
    GenericTable gT = new GenericTable();
    ArrayList rows = new ArrayList();
    ArrayList columns = new ArrayList();
    columns.add(rb.getString("gui.upload.table.key"));
    columns.add(rb.getString("gui.upload.table.value"));
    rows.add(new Object[]{"",""});
    gT.setData(columns, rows);
    jtAttributes.setModel(gT);
    jcbOnMonth.removeAllItems();
    jcbOnMonth.setSelectedItem("");
    jcbOnMonth.addItem(rb.getString("gui.month.1"));
    jcbOnMonth.addItem(rb.getString("gui.month.2"));
    jcbOnMonth.addItem(rb.getString("gui.month.3"));
    jcbOnMonth.addItem(rb.getString("gui.month.4"));
    jcbOnMonth.addItem(rb.getString("gui.month.5"));
    jcbOnMonth.addItem(rb.getString("gui.month.6"));
    jcbOnMonth.addItem(rb.getString("gui.month.7"));
    jcbOnMonth.addItem(rb.getString("gui.month.8"));
    jcbOnMonth.addItem(rb.getString("gui.month.9"));
    jcbOnMonth.addItem(rb.getString("gui.month.10"));
    jcbOnMonth.addItem(rb.getString("gui.month.11"));
    jcbOnMonth.addItem(rb.getString("gui.month.12"));
    MainWindow mw = MainWindow.getMainWindow();
    mw.addToDesktop(this);
    this.setVisible(true);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }
  
  public void updateTabNames()
  {
    jtpBothTypes.setTitleAt(0, rbUser.getString("gui.search.basic"));
    jtpBothTypes.setTitleAt(1, rbUser.getString("gui.search.advanced"));
    jtpAdvancedSearch.setTitleAt(0, rbUser.getString("gui.search.document"));
    jtpAdvancedSearch.setTitleAt(1, rbUser.getString("gui.search.author"));
    jtpAdvancedSearch.setTitleAt(2, rbUser.getString("gui.upload.locale"));
    jtpAdvancedSearch.setTitleAt(3, rbUser.getString("gui.search.attr"));
    jtpAdvancedSearch.setTitleAt(4, rbUser.getString("gui.search.added"));
  }

  public void populateLocale()
  {
    tmLanguages = new TreeMap<String,String>();
    tmCountries = new TreeMap<String,String>();
    Locale lHome = rbUser.getLocale();
    // Get the complete list of languages supported within Locale.
    String[] listLangs = Locale.getISOLanguages();
    // Iterate through the list and add them to the TreeMaps
    // Start with languages
    for (String lang : listLangs)
    {
      String name = (new Locale(lang)).getDisplayLanguage(lHome);
      tmLanguages.put(name,lang);
    }
    // Now countries
    String[] listCountries = Locale.getISOCountries();
    for (String country : listCountries)
    {
      String name = (new Locale(lHome.getLanguage(), country)).getDisplayCountry(lHome);
      tmCountries.put(name,country);
    }
    // Now add both items to the GUI combo-boxes
    String empty = rbUser.getString("gui.blank");
    jcbLang.removeAllItems();
    jcbLang.addItem(empty);
    Iterator iLang = tmLanguages.keySet().iterator();
    while (iLang.hasNext())
    {
      jcbLang.addItem(iLang.next().toString());
    }
    jcbCountry.removeAllItems();
    jcbCountry.addItem(empty);
    Iterator iCountry = tmCountries.keySet().iterator();
    while (iCountry.hasNext())
    {
      jcbCountry.addItem(iCountry.next().toString());
    }
    if (lHome.getDisplayLanguage(lHome).equals(""))
    {
      Locale lTemp = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
      jcbLang.setSelectedItem(lTemp.getDisplayLanguage(lTemp));
      jcbCountry.setSelectedItem(lTemp.getDisplayCountry(lTemp));
    }
    else
    {
      String lng = lHome.getDisplayLanguage(lHome);
      String ctry = lHome.getDisplayCountry(lHome);
      jcbLang.setSelectedItem(lng);
      jcbCountry.setSelectedItem(ctry);
    }
  }

  public void addAttribute(String name, String value)
  {
    GenericTable gt = (GenericTable) jtAttributes.getModel();
    if (((String) gt.getValueAt(0, 0)).equals("")) { gt.clear(); }
    gt.addRow(new Object[]{name,value});
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbSearch = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jtpBothTypes = new javax.swing.JTabbedPane();
        jpBasicSearch = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtaBasicSearchWords = new javax.swing.JTextArea();
        jtpAdvancedSearch = new javax.swing.JTabbedPane();
        jpDocument = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jtfTitle = new javax.swing.JTextField();
        jtfGuid = new javax.swing.JTextField();
        jpAuthor = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jtfOrg = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jtfFirstName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jtfWebsite = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jtfSurname = new javax.swing.JTextField();
        jcbPrefix = new javax.swing.JComboBox();
        jpLocale = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jcbLang = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jcbCountry = new javax.swing.JComboBox();
        jpAttributes = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtAttributes = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaKeywords = new javax.swing.JTextArea();
        jbAddAttribute = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jpAdded = new javax.swing.JPanel();
        jtfBy = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtfFrom = new javax.swing.JTextField();
        jcbOnMonth = new javax.swing.JComboBox();
        jtfOnYear = new javax.swing.JTextField();
        jtfOnDay = new javax.swing.JTextField();

        setTitle(rbUser.getString("gui.search.name")); // NOI18N
        setMaximumSize(new java.awt.Dimension(562, 495));
        setMinimumSize(new java.awt.Dimension(562, 495));
        setPreferredSize(new java.awt.Dimension(562, 495));

        jbSearch.setText(rbUser.getString("gui.search.search")); // NOI18N
        jbSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSearchActionPerformed(evt);
            }
        });

        jLabel1.setText(rbUser.getString("gui.search.instructions")); // NOI18N

        jLabel4.setText(rbUser.getString("gui.search.basic.instructions")); // NOI18N

        jtaBasicSearchWords.setColumns(20);
        jtaBasicSearchWords.setRows(5);
        jScrollPane3.setViewportView(jtaBasicSearchWords);

        javax.swing.GroupLayout jpBasicSearchLayout = new javax.swing.GroupLayout(jpBasicSearch);
        jpBasicSearch.setLayout(jpBasicSearchLayout);
        jpBasicSearchLayout.setHorizontalGroup(
            jpBasicSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpBasicSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpBasicSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpBasicSearchLayout.setVerticalGroup(
            jpBasicSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBasicSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        jtpBothTypes.addTab("tab2", jpBasicSearch);

        jpDocument.setBorder(javax.swing.BorderFactory.createTitledBorder(rbUser.getString("gui.search.document"))); // NOI18N

        jLabel16.setText(rbUser.getString("gui.search.guid")); // NOI18N

        jLabel17.setText(rbUser.getString("gui.upload.title")); // NOI18N

        javax.swing.GroupLayout jpDocumentLayout = new javax.swing.GroupLayout(jpDocument);
        jpDocument.setLayout(jpDocumentLayout);
        jpDocumentLayout.setHorizontalGroup(
            jpDocumentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDocumentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpDocumentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfGuid, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jtfTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpDocumentLayout.setVerticalGroup(
            jpDocumentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDocumentLayout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfGuid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(201, Short.MAX_VALUE))
        );

        jtpAdvancedSearch.addTab("tab1", jpDocument);

        jpAuthor.setBorder(javax.swing.BorderFactory.createTitledBorder(rbUser.getString("gui.search.author"))); // NOI18N

        jLabel10.setText(rbUser.getString("gui.upload.prefix")); // NOI18N

        jLabel11.setText(rbUser.getString("gui.upload.organisation")); // NOI18N

        jLabel12.setText(rbUser.getString("gui.upload.firstName")); // NOI18N

        jLabel13.setText(rbUser.getString("gui.upload.email")); // NOI18N

        jLabel14.setText(rbUser.getString("gui.upload.surname")); // NOI18N

        jLabel15.setText(rbUser.getString("gui.upload.website")); // NOI18N

        jcbPrefix.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-None-", "Mr", "Mrs", "Ms", "Miss", "Dr", "Prof", "Sir", "Rev", "Lady" }));

        javax.swing.GroupLayout jpAuthorLayout = new javax.swing.GroupLayout(jpAuthor);
        jpAuthor.setLayout(jpAuthorLayout);
        jpAuthorLayout.setHorizontalGroup(
            jpAuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpAuthorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jtfWebsite, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jtfEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jtfOrg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jtfSurname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jtfFirstName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jcbPrefix, javax.swing.GroupLayout.Alignment.LEADING, 0, 484, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpAuthorLayout.setVerticalGroup(
            jpAuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAuthorLayout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfOrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jtpAdvancedSearch.addTab("tab2", jpAuthor);

        jpLocale.setBorder(javax.swing.BorderFactory.createTitledBorder(rbUser.getString("gui.upload.locale"))); // NOI18N

        jLabel2.setText(rbUser.getString("gui.upload.language")); // NOI18N

        jcbLang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Blank -" }));

        jLabel3.setText(rbUser.getString("gui.upload.country")); // NOI18N

        jcbCountry.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "- Blank -" }));

        javax.swing.GroupLayout jpLocaleLayout = new javax.swing.GroupLayout(jpLocale);
        jpLocale.setLayout(jpLocaleLayout);
        jpLocaleLayout.setHorizontalGroup(
            jpLocaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpLocaleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpLocaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jcbCountry, javax.swing.GroupLayout.Alignment.LEADING, 0, 484, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jcbLang, 0, 484, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpLocaleLayout.setVerticalGroup(
            jpLocaleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLocaleLayout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(201, Short.MAX_VALUE))
        );

        jtpAdvancedSearch.addTab("tab4", jpLocale);

        jpAttributes.setBorder(javax.swing.BorderFactory.createTitledBorder(rbUser.getString("gui.search.attr"))); // NOI18N

        jtAttributes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jtAttributes);

        jtaKeywords.setColumns(20);
        jtaKeywords.setRows(5);
        jScrollPane2.setViewportView(jtaKeywords);

        jbAddAttribute.setText(rbUser.getString("gui.upload.newAttribute")); // NOI18N
        jbAddAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAddAttributeActionPerformed(evt);
            }
        });

        jLabel6.setText(rbUser.getString("gui.upload.keywords")); // NOI18N

        jLabel18.setText(rbUser.getString("gui.upload.specialised")); // NOI18N

        javax.swing.GroupLayout jpAttributesLayout = new javax.swing.GroupLayout(jpAttributes);
        jpAttributes.setLayout(jpAttributesLayout);
        jpAttributesLayout.setHorizontalGroup(
            jpAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jbAddAttribute, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jpAttributesLayout.setVerticalGroup(
            jpAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAttributesLayout.createSequentialGroup()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbAddAttribute)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtpAdvancedSearch.addTab("tab5", jpAttributes);

        jpAdded.setBorder(javax.swing.BorderFactory.createTitledBorder(rbUser.getString("gui.search.added"))); // NOI18N

        jLabel7.setText(rbUser.getString("gui.search.by")); // NOI18N

        jLabel8.setText(rbUser.getString("gui.search.from")); // NOI18N

        jLabel9.setText(rbUser.getString("gui.search.on")); // NOI18N

        jcbOnMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jpAddedLayout = new javax.swing.GroupLayout(jpAdded);
        jpAdded.setLayout(jpAddedLayout);
        jpAddedLayout.setHorizontalGroup(
            jpAddedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAddedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAddedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jtfBy, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addGroup(jpAddedLayout.createSequentialGroup()
                        .addComponent(jtfOnDay, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbOnMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfOnYear, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpAddedLayout.setVerticalGroup(
            jpAddedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAddedLayout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpAddedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbOnMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfOnYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfOnDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jtpAdvancedSearch.addTab("tab6", jpAdded);

        jtpBothTypes.addTab("tab2", jtpAdvancedSearch);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jtpBothTypes)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtpBothTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jbSearch)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSearchActionPerformed
      ProgressForm pf = new ProgressForm(rbUser);
      pf.setProgress(5);
      pf.setStatus(rbUser.getString("gui.progress.search.collect"));
      pf.increaseProgress();

      if (jtpBothTypes.getSelectedIndex() == 0)
      {
        performBasicSearch(pf);
      }
      else if (jtpBothTypes.getSelectedIndex() == 1)
      {
        performAdvancedSearch(pf);
      }
      else
      {
        // Something's gone wrong if we end up here.
        // Close the progress bar and return the user to the state before clicking on find documents
        pf.setVisible(false);
        pf=null;
      }
      
    }//GEN-LAST:event_jbSearchActionPerformed

    public void searchForDoc(String guid)
    {
      jtfGuid.setText(guid);
      ProgressForm pf = new ProgressForm(rbUser);
      pf.setProgress(5);
      pf.setStatus(rbUser.getString("gui.progress.search.collect"));
      pf.increaseProgress();
      performAdvancedSearch(pf);
    }
    
    private void performBasicSearch(ProgressForm pf)
    {
      ObjectFactory sharedFactory = new ObjectFactory();
      odin.shared.xml.Basic.Document bdDoc = sharedFactory.createBasicDocument();
      odin.shared.xml.Basic.Document.Words bdWords = sharedFactory.createBasicDocumentWords();
      StringTokenizer st = new StringTokenizer(jtaBasicSearchWords.getText());
      while (st.hasMoreTokens())
      {
        bdWords.getWord().add((String) st.nextToken());
      }
      bdDoc.setWords(bdWords);
      BasicSearchWorker bsw = new BasicSearchWorker(pf, bdDoc, rbUser);
      bsw.execute();
    }

    private void performAdvancedSearch(ProgressForm pf)
    {
      ObjectFactory sharedFactory = new ObjectFactory();
      odin.shared.xml.Search.Document searchDoc = sharedFactory.createSearchDocument();
      // Check for a guid or title
      if (jtfGuid.getDocument() != null || !jtfGuid.getText().equals("")) { searchDoc.setGuid(jtfGuid.getText()); }
      if (jtfTitle.getDocument() != null || !jtfTitle.getText().equals("")) { searchDoc.setTitle(jtfTitle.getText()); }
      // Create an instance of Author and process the details
      odin.shared.xml.Search.Document.Author sdAuthor = sharedFactory.createSearchDocumentAuthor();
      if (!jcbPrefix.getSelectedItem().toString().equals("-None-"))
      { sdAuthor.setPrefix(jcbPrefix.getSelectedItem().toString()); }
      else { sdAuthor.setPrefix(""); }
      if (jtfFirstName.getDocument() != null || !jtfFirstName.getText().equals("")) { sdAuthor.setFirstName(jtfFirstName.getText()); }
      if (jtfSurname.getDocument() != null || !jtfSurname.getText().equals("")) { sdAuthor.setSurname(jtfSurname.getText()); }
      if (jtfOrg.getDocument() != null || !jtfOrg.getText().equals("")) { sdAuthor.setCompany(jtfOrg.getText()); }
      if (jtfEmail.getDocument() != null || !jtfEmail.getText().equals("")) { sdAuthor.setEmail(jtfEmail.getText()); }
      if (jtfWebsite.getDocument() != null || !jtfWebsite.getText().equals("")) { sdAuthor.setWebsite(jtfWebsite.getText()); }
      // Create an instance of Locale and process the details
      odin.shared.xml.Search.Document.Locale sdLocale = sharedFactory.createSearchDocumentLocale();
      sdLocale.setLanguage(tmLanguages.get((String) jcbLang.getSelectedItem()));
      sdLocale.setCountry(tmCountries.get((String) jcbCountry.getSelectedItem()));
      // Create an instance of Attributes and process the details
      odin.shared.xml.Search.Document.Attributes sdAttributes = sharedFactory.createSearchDocumentAttributes();
      GenericTable gt = (GenericTable) jtAttributes.getModel();
      for (int i = 0; i < gt.getRowCount(); i++)
      {
        Object[] temp = gt.getRow(i);
        odin.shared.xml.Search.Document.Attributes.Attribute a = sharedFactory.createSearchDocumentAttributesAttribute();
        a.setKey((String)temp[0]);
        a.setValue((String)temp[1]);
        sdAttributes.getAttribute().add(a);
      }
      // Create an instance of Keywords and process the details
      odin.shared.xml.Search.Document.Keywords sdKeywords = sharedFactory.createSearchDocumentKeywords();
      if (!(jtaKeywords.getText().trim()).equals(""))
      {
        StringTokenizer st = new StringTokenizer(jtaKeywords.getText(),";");
        while (st.hasMoreTokens())
        {
          sdKeywords.getKeyword().add((String) st.nextToken());
        }
      }
      // Create an instance of Added and process the details
      odin.shared.xml.Search.Document.Added sdAdded = sharedFactory.createSearchDocumentAdded();
      if (jtfBy.getDocument() != null || !jtfBy.getText().equals("")) { sdAdded.setBy(jtfBy.getText()); }
      if (jtfFrom.getDocument() != null || !jtfFrom.getText().equals("")) { sdAdded.setFrom(jtfFrom.getText()); }
      String day = jtfOnDay.getText();
      String year = jtfOnYear.getText();
      if ((!day.equals("")) && (!year.equals("")))
      {
        int month = jcbOnMonth.getSelectedIndex();
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.set(Integer.parseInt(year), month, Integer.parseInt(day));
        try
        {
          DatatypeFactory dataFactory = DatatypeFactory.newInstance();
          XMLGregorianCalendar xgc = dataFactory.newXMLGregorianCalendar(gCal);
          sdAdded.setAt(xgc);
        }
        catch (Exception x)
        {
          x.printStackTrace();
        }
      }
      // Assemble the search document and send to a worker thread
      searchDoc.setAuthor(sdAuthor);
      searchDoc.setLocale(sdLocale);
      searchDoc.setAttributes(sdAttributes);
      searchDoc.setKeywords(sdKeywords);
      searchDoc.setAdded(sdAdded);
      SearchWorker sw = new SearchWorker(pf, searchDoc, rbUser);
      sw.execute();
    }

    private void jbAddAttributeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAddAttributeActionPerformed
      Attribute a = new Attribute(this, rbUser);
    }//GEN-LAST:event_jbAddAttributeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbAddAttribute;
    private javax.swing.JButton jbSearch;
    private javax.swing.JComboBox jcbCountry;
    private javax.swing.JComboBox jcbLang;
    private javax.swing.JComboBox jcbOnMonth;
    private javax.swing.JComboBox jcbPrefix;
    private javax.swing.JPanel jpAdded;
    private javax.swing.JPanel jpAttributes;
    private javax.swing.JPanel jpAuthor;
    private javax.swing.JPanel jpBasicSearch;
    private javax.swing.JPanel jpDocument;
    private javax.swing.JPanel jpLocale;
    private javax.swing.JTable jtAttributes;
    private javax.swing.JTextArea jtaBasicSearchWords;
    private javax.swing.JTextArea jtaKeywords;
    private javax.swing.JTextField jtfBy;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfFirstName;
    private javax.swing.JTextField jtfFrom;
    private javax.swing.JTextField jtfGuid;
    private javax.swing.JTextField jtfOnDay;
    private javax.swing.JTextField jtfOnYear;
    private javax.swing.JTextField jtfOrg;
    private javax.swing.JTextField jtfSurname;
    private javax.swing.JTextField jtfTitle;
    private javax.swing.JTextField jtfWebsite;
    private javax.swing.JTabbedPane jtpAdvancedSearch;
    private javax.swing.JTabbedPane jtpBothTypes;
    // End of variables declaration//GEN-END:variables

}
