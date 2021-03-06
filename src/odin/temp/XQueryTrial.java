/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * XQueryTrial.java
 *
 * Created on 18-Apr-2011, 21:32:05
 */

package odin.temp;

import javax.xml.xquery.*;
import net.sf.saxon.xqj.SaxonXQDataSource;
import org.w3c.dom.*;

/**
 * Used to test XQuery statements for syntax and results prior to inclusion in the main application
 * @author Alex
 */
public class XQueryTrial extends javax.swing.JFrame {

    /** Creates new form XQueryTrial */
    public XQueryTrial() {
        initComponents();
        this.setTitle("XQuery Test:");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jta = new javax.swing.JTextArea();
    jlStatus = new javax.swing.JLabel();
    jbGo = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jta.setColumns(20);
    jta.setRows(5);
    jScrollPane1.setViewportView(jta);

    jlStatus.setText("Ready");

    jbGo.setText("Execute Query");
    jbGo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbGoActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 400, Short.MAX_VALUE)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jbGo)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 300, Short.MAX_VALUE)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jbGo)
          .addComponent(jlStatus))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void jbGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGoActionPerformed
      jlStatus.setText("Working...");
      int count = 0;
      try
      {
        SaxonXQDataSource xqds = new SaxonXQDataSource();
        XQConnection con = xqds.getConnection();
        XQPreparedExpression expr = con.prepareExpression(jta.getText());
        XQResultSequence result = expr.executeQuery();
        while (result.next()) {
          Node root = result.getNode();
          exploreNode(root,0);
          count++;
        }
      }
      catch (XQException xqX)
      {
        xqX.printStackTrace();
      }
      finally
      {
        jlStatus.setText("Found " + count + " node(s)");
      }
}//GEN-LAST:event_jbGoActionPerformed

    /**
     * Print the contents of the <code>Node</code> and any sub-nodes contained within it.
     * @param root Node to be investigated
     * @param depth Depth of the node within the DOM tree
     * @see org.w3c.dom.Node
     */
    public void exploreNode(Node root, int depth)
    {
      System.out.println(root.getParentNode().getNodeName() + "\t" + root.getNodeName() + "\t" + depth + "\t"+ root.getTextContent());
      NodeList nl = root.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
      {
        Node n = nl.item(i);
        if (n.hasChildNodes()) { exploreNode(n, depth+1); }
      }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new XQueryTrial().setVisible(true);
            }
        });
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JButton jbGo;
  private javax.swing.JLabel jlStatus;
  private javax.swing.JTextArea jta;
  // End of variables declaration//GEN-END:variables

}
