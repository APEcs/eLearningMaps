package CoreDrawingAsy;

import java.net.MalformedURLException;


/******************************************************
 * PanelLinkAsy.java
 *
 * Created on 29-Jul-2010, 11:13:16
 *
 *
 *
 * @author Klitos Christodoulou
 */
public class PanelLinkAsy extends javax.swing.JPanel {

  /*Variable declarations*/
  private DrawCanvas canvasPanel = null;

    /** Creates new form PanelLinkAsy */
    public PanelLinkAsy(DrawCanvas canvas) {
     initComponents();

     /*Save a reference to the Cnvas*/
     canvasPanel = canvas;
    }

    /*Construct the GUI*/
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    addLinkBtn = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();
    linkTxt = new javax.swing.JTextField();
    descTxt = new javax.swing.JTextField();

    jLabel1.setText("Description :");

    addLinkBtn.setText("Insert Link");
    addLinkBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addLinkBtnActionPerformed(evt);
      }
    });

    jLabel2.setText("URL:");

    linkTxt.setText("http://");

    descTxt.setText("description");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel2)
              .addComponent(jLabel1))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(linkTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
              .addComponent(descTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)))
          .addComponent(addLinkBtn, javax.swing.GroupLayout.Alignment.TRAILING))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(descTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(linkTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(7, 7, 7)
        .addComponent(addLinkBtn)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

    private void addLinkBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLinkBtnActionPerformed
      /***
       * Get the details from screen and construct a new Link Shape
       */

     String description = this.descTxt.getText();
     String link        = this.linkTxt.getText();

     try {

      if (!description.equals("") || !link.equals("")) {

        canvasPanel.createWebLink(description, link);

        /*Set visibility to false*/
        this.getParent().setVisible(false);
      }//end if
      else {
        descTxt.setText("Description");
        linkTxt.setText("http://");
      }//end else
     }//try
     catch (MalformedURLException ex) {
      System.out.println("PanelLinkAsy");
     }//catch
    }//GEN-LAST:event_addLinkBtnActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton addLinkBtn;
  private javax.swing.JTextField descTxt;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JTextField linkTxt;
  // End of variables declaration//GEN-END:variables

}//end class