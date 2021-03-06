/* Class name: Attribute
 * File name:  Attribute.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    17-Apr-2011 15:03:05
 * Modified:   17-Apr-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  17-Apr-2011 Initial build
 */

package odin.sleipnir;

import java.util.ResourceBundle;
import javax.swing.*;

/**
 * Provides a subclass of <code>JInternalFrame</code> which can be used to insert
 * specialised attributes when either uploading a document to the Odin repository, or,
 * searching for documents already uploaded.
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class Attribute extends javax.swing.JInternalFrame
{
  private Upload uploadParent;
  private Search searchParent;
  private ResourceBundle rbUser;

    /**
     * Creates a new Attribute window for use with an <code>Upload</code> window.
     * @param parent The <code>Upload</code> window where the new attribute will be included.
     * @param rb The <code>ResourceBundle</code> to be used for localisation
     * @see odin.sleipnir.Upload
     * @see java.util.ResourceBundle
     */
    public Attribute(Upload parent, ResourceBundle rb)
    {
      super(rb.getString("gui.upload.newAttribute"),true,true,true,true);
      super.setLayer(8);
      uploadParent = parent;
      rbUser = rb;
      initComponents();
      MainWindow mw = MainWindow.getMainWindow();
      mw.addToDesktop(this);
      this.setVisible(true);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Creates a new Attribute window for use with an <code>Search</code> window.
     * @param parent The <code>Search</code> window where the new attribute will be included.
     * @param rb The <code>ResourceBundle</code> to be used for localisation
     * @see odin.sleipnir.Search
     * @see java.util.ResourceBundle
     */
    public Attribute(Search parent, ResourceBundle rb)
    {
      super(rb.getString("gui.upload.newAttribute"),true,true,true,true);
      super.setLayer(8);
      searchParent = parent;
      rbUser = rb;
      initComponents();
      MainWindow mw = MainWindow.getMainWindow();
      mw.addToDesktop(this);
      this.setVisible(true);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jtfName = new javax.swing.JTextField();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jtfValue = new javax.swing.JTextField();
    jbOk = new javax.swing.JButton();

    setTitle(rbUser.getString("gui.attributes.title")); // NOI18N

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    jLabel1.setText(rbUser.getString("gui.attributes.name")); // NOI18N

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    jLabel2.setText(rbUser.getString("gui.attributes.value")); // NOI18N

    jbOk.setText(rbUser.getString("gui.attributes.ok")); // NOI18N
    jbOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbOkActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jbOk, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jtfValue, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
              .addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jbOk)
        .addContainerGap(23, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void jbOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOkActionPerformed
      if (jtfName.getText().equals("") || jtfValue.getText().equals(""))
      {
        JOptionPane.showMessageDialog(null, rbUser.getString("gui.attribute.error"));
      }
      else
      {
        if (uploadParent != null)
        {
          uploadParent.addAttribute(jtfName.getText(), jtfValue.getText());
        }
        else if (searchParent != null)
        {
          searchParent.addAttribute(jtfName.getText(), jtfValue.getText());
        }
        this.setVisible(false);
      }
    }//GEN-LAST:event_jbOkActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JButton jbOk;
  private javax.swing.JTextField jtfName;
  private javax.swing.JTextField jtfValue;
  // End of variables declaration//GEN-END:variables

}
