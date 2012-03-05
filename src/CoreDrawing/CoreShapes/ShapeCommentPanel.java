/**********************************************************************
 * ShapeCommentPanel.java                                             *
 *                                                                    *
 * Created on 24-Jun-2010, 14:19:00                                   *
 *                                                                    *
 * This class will construct a jPanel that is going to be used to     *
 * represent the Comment object.                                      *
 *
 * It holds a :
 *  (1) JTextArea that holds the comments
 *  (2) Creator details
 *  (3) Date created
 *
 * @author Klitos Christodoulou
 * email : christk6@cs.man.ac.uk
 */

package CoreDrawing.CoreShapes;

import java.awt.Component;
import java.awt.PopupMenu;
import java.util.Date;
import javax.swing.JPopupMenu;

/*Properties declaration*/

public class ShapeCommentPanel extends javax.swing.JPanel {

/*Hold properties*/
private String creatorUserName = "";
private String editedByList    = "";
private Date   creationDate = null;
private String email;
private String comment;
private ShapeComment parent = null;

 /** Creates new form ShapeCommentPanel */
 public ShapeCommentPanel(ShapeComment sc, String info, String aUser, String mail, Date aDate) {
  initComponents();

  /*Hold the information*/
  comment = info;
  creatorUserName = aUser;
  email = mail;
  creationDate = aDate;
  parent = sc;

  /*Add the information to the appropriate Components*/
  this.usernameTxt.setText(creatorUserName);
  this.editedTxt.setText(editedByList);
  this.emailTxt.setText(email);
  this.dateTxt.setText(creationDate.toString());
  this.commentArea.setText(comment);

 }//end constructor

 /** This method is called from within the constructor to
  * construct the GUI
 */
 @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane2 = new javax.swing.JScrollPane();
    jTextArea1 = new javax.swing.JTextArea();
    jLabel2 = new javax.swing.JLabel();
    usernameLbl = new javax.swing.JLabel();
    emailLbl = new javax.swing.JLabel();
    dateLbl = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    commentArea = new javax.swing.JTextArea();
    okBtn = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    usernameTxt = new javax.swing.JTextField();
    emailTxt = new javax.swing.JTextField();
    dateTxt = new javax.swing.JTextField();
    editeByLbl = new javax.swing.JLabel();
    editedTxt = new javax.swing.JTextField();

    jTextArea1.setColumns(20);
    jTextArea1.setRows(5);
    jScrollPane2.setViewportView(jTextArea1);

    jLabel2.setText("jLabel2");

    setBackground(new java.awt.Color(255, 255, 153));
    setBorder(javax.swing.BorderFactory.createCompoundBorder());
    setToolTipText("Enter a Comment");

    usernameLbl.setText("Sticky Creator :");

    emailLbl.setText("Creator Email  :");

    dateLbl.setText("Creation Date :");

    commentArea.setColumns(20);
    commentArea.setLineWrap(true);
    commentArea.setRows(5);
    jScrollPane1.setViewportView(commentArea);

    okBtn.setText("Ok");
    okBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okBtnActionPerformed(evt);
      }
    });

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/comment.png"))); // NOI18N

    usernameTxt.setEditable(false);
    usernameTxt.setBorder(null);

    emailTxt.setEditable(false);
    emailTxt.setBorder(null);

    dateTxt.setEditable(false);
    dateTxt.setBorder(null);

    editeByLbl.setText("Edited By :");

    editedTxt.setEditable(false);
    editedTxt.setBorder(null);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(usernameLbl)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(usernameTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
          .addComponent(okBtn, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel1)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(emailLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(dateLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(editeByLbl)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(editedTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(usernameLbl)
          .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(editeByLbl)
          .addComponent(editedTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(emailLbl)
          .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(dateLbl)
          .addComponent(dateTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(okBtn)
            .addContainerGap())
          .addComponent(jLabel1)))
    );
  }// </editor-fold>//GEN-END:initComponents

/***
 * Add an editor for this note
 *
 * @param editor
 */
public void addEditor(String editor) {
  editedByList += editor + ", ";
  this.editedTxt.setText(editedByList);
}

/***
 * Get a String representing a list of users that edit 
 * this note
 * 
 * @return
 */
public String getEditors() {
  return editedByList;
}


/***
 * Change comment text
 *
 * @param str
 */
public void setComment(String str) {
 comment = str;
 this.commentArea.setText(str);
}//end method


/***
 * Get the comment text
 *
 * @return
 */
public String getComment() {
  return this.comment;
}//end

/***
 * Get this comment's creator username
 *
 * @return
 */
public String getCreatorName() {
  return this.creatorUserName;
}//end

/***
 * Get creators email address
 *
 * @param evt
 */
public String getCreatorEmail() {
  return this.email;
}//end

/***
 * Get the Date this comment was created
 *
 * @return
 */
public Date getCreationDate() {
  return this.creationDate;
}//end

 private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed

   /*Change the text to whatsever is on the jTextArea*/
   this.setComment(this.commentArea.getText());

   /*Call ShapeComment method to apply changes*/
   parent.applyChangesFromCommentPanel(this.getComment());

 }//GEN-LAST:event_okBtnActionPerformed


 /***
  * This method will apply changes from outside
  */
 public void applyChanges(String editor, String newNoteText) {
   this.addEditor(editor);
   this.setComment(newNoteText);
 }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextArea commentArea;
  private javax.swing.JLabel dateLbl;
  private javax.swing.JTextField dateTxt;
  private javax.swing.JLabel editeByLbl;
  private javax.swing.JTextField editedTxt;
  private javax.swing.JLabel emailLbl;
  private javax.swing.JTextField emailTxt;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JTextArea jTextArea1;
  private javax.swing.JButton okBtn;
  private javax.swing.JLabel usernameLbl;
  private javax.swing.JTextField usernameTxt;
  // End of variables declaration//GEN-END:variables

}//end class
