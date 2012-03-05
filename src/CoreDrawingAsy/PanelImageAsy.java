package CoreDrawingAsy;

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
 *                                                          *
 * Created on 29-Jul-2010, 15:26:05                         *
 *                                                          *
 * @author Klitos Christodoulou                             *
 * email: christk6@cs.mac.ac.uk                             *
 *                                                          *
 ************************************************************/

public class PanelImageAsy extends javax.swing.JPanel {

  /*Variable Declarations*/
  private ButtonGroup choice = null;
  private DrawCanvas canvasPanel = null;
  private Image imageFromWeb = null;

    /** Creates new form PanelImageAsy */
    public PanelImageAsy(DrawCanvas canvas) {
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
     Image original_image = ImageIO.read(new URL(link));

     /*Get the name of this image*/
     String [] stingTokens = link.split("/");
     String imageName = stingTokens[stingTokens.length-1];


     /*Get the extension of the Image and check it*/
     String extension = link.substring(link.length()-4, link.length());

     

      if (!link.equals("")) {

        /*Now check the extension*/
        if (extension.equals(".gif") || extension.equals(".png")
           || extension.equals(".jpg") || extension.equals(".jpeg") ) {

         extension = extension.substring(1,extension.length());
         //System.out.println("new extension : " +extension);

         /*Default size*/
         if (radioDefault.isSelected()) {
            BufferedImage bi = ImageIO.read( new URL(link));
            ImageIO.write(bi,extension,new File("src/webimages/"+ imageName));
            /*Update the variables in DrawCanvas*/
            canvasPanel.setImageName(imageName);
          }
         else if (radioSmall.isSelected()) {
            processImage(link,original_image.getWidth(null)/3,
                             original_image.getHeight(null)/3,
                                          extension,imageName);
          }
         else if (radioMed.isSelected()) {
            processImage(link,original_image.getWidth(null)/2,
                             original_image.getHeight(null)/2,
                                          extension,imageName);
          }
         else if (radioLarge.isSelected()) {
            processImage(link,original_image.getWidth(null)*2,
                             original_image.getHeight(null)*2,
                                          extension,imageName);
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
      System.out.println("PanelImageAsy");
     }//catch
    }//GEN-LAST:event_okButtonActionPerformed


/***
 * (1) This method will process and download the Image from the web.
 * (2) It will also adjust the size.
 */
public void processImage(String urlLink, int width, int height,
                         String ext, String image_name) {
 try {

   /*Buffer image from the url*/
   BufferedImage bsrc = ImageIO.read(new URL(urlLink));

   BufferedImage bdest =
      new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   Graphics2D g2 = bdest.createGraphics();
   AffineTransform at =
      AffineTransform.getScaleInstance((double)width/bsrc.getWidth(),
          (double)height/bsrc.getHeight());
   g2.drawRenderedImage(bsrc,at);

   /*Write image to a file*/
   ImageIO.write(bdest,ext,new File("src/webimages/"+ image_name));

   /*Update the variables in DrawCanvas*/
   canvasPanel.setImageName(image_name);

  }//try
 catch (Exception e)
  {
    this.getParent().setVisible(false);
    JOptionPane.showMessageDialog(this, "Can not download image.");
  }//end catch
}//end method


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