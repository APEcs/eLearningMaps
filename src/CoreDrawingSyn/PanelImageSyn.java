package CoreDrawingSyn;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

/************************************************************
 *
 * Created on 29-Jul-2010, 15:26:05
 *
 * @author Klitos Christodoulou
 * email: christk6@cs.mac.ac.uk
 *
 */
public class PanelImageSyn extends javax.swing.JPanel {

  /*Variable Declarations*/
  private ButtonGroup choice = null;
  private DrawCanvasSyn canvasPanel = null;
  private Image imageFromWeb = null;
  private String sizeChoice  = "";

    /** Creates new form PanelImageSyn */
    public PanelImageSyn(DrawCanvasSyn canvas) {
     choice = new ButtonGroup();

     canvasPanel = canvas;
      
     initComponents();
    }//end constructor

    /*Constructor*/
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    okButton = new javax.swing.JButton();
    linkTxt = new javax.swing.JTextField();
    urlLbl = new javax.swing.JLabel();
    radioDefault = new javax.swing.JRadioButton();
    radioSmall = new javax.swing.JRadioButton();
    radioMed = new javax.swing.JRadioButton();
    radioLarge = new javax.swing.JRadioButton();

    okButton.setText("Ok");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okButtonActionPerformed(evt);
      }
    });

    linkTxt.setText("http://");

    urlLbl.setText("URL:");

    choice.add(radioDefault);
    radioDefault.setSelected(true);
    radioDefault.setText("Default");

    choice.add(radioSmall);
    radioSmall.setText("Small");

    choice.add(radioMed);
    radioMed.setText("Medium");

    choice.add(radioLarge);
    radioLarge.setText("Large");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(urlLbl)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(linkTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(radioDefault)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(radioSmall)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioMed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioLarge))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(okButton)))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(urlLbl)
          .addComponent(linkTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(radioDefault)
          .addComponent(radioSmall)
          .addComponent(radioMed)
          .addComponent(radioLarge))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 4, Short.MAX_VALUE)
        .addComponent(okButton)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
      /***
      * Details to be used when inserting a picture.
      */
    try {
     String link  = this.linkTxt.getText();
     /*Get the extension of the Image and check it*/
     String extension = link.substring(link.length()-4, link.length());

      if (!link.equals("")) {

        /*Now check the extension*/
        if (extension.equals(".gif") || extension.equals(".png")
           || extension.equals(".jpg") || extension.equals(".jpeg") ) {

         extension = extension.substring(1,extension.length());

         /*Default size*/
         if (radioDefault.isSelected()) {
            /*Set the size choice*/
            sizeChoice = "d";

            /*Update the variables in DrawCanvas*/
            canvasPanel.setImageDetails(link,sizeChoice);
          }
         else if (radioSmall.isSelected()) {
            /*Set the size choice*/
            sizeChoice = "s";

            /*Update the variables in DrawCanvas*/
            canvasPanel.setImageDetails(link,sizeChoice);
          }
         else if (radioMed.isSelected()) {
            /*Set the size choice*/
            sizeChoice = "m";
            
            /*Update the variables in DrawCanvas*/
            canvasPanel.setImageDetails(link,sizeChoice);
          }
         else if (radioLarge.isSelected()) {
            /*Set the size choice*/
            sizeChoice = "l";

            /*Update the variables in DrawCanvas*/
            canvasPanel.setImageDetails(link,sizeChoice);
         }//end else
       

         /*Set visibility to false*/
         this.getParent().setVisible(false);
        }//end inner if
        else {
          this.getParent().setVisible(false);

          /*Display message to user*/
          JOptionPane.showMessageDialog(this, "Please enter a valid image extension." +
            "\n [.png, .jpg, .gif]");
        }//inner else
      }//end if
      else {
        linkTxt.setText("http://");
      }//end else
     }//try
     catch (Exception ex) {
      System.out.println("PanelImageSyn");
     }//catch
    }//GEN-LAST:event_okButtonActionPerformed



  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField linkTxt;
  private javax.swing.JButton okButton;
  private javax.swing.JRadioButton radioDefault;
  private javax.swing.JRadioButton radioLarge;
  private javax.swing.JRadioButton radioMed;
  private javax.swing.JRadioButton radioSmall;
  private javax.swing.JLabel urlLbl;
  // End of variables declaration//GEN-END:variables

}//enc class
