package CoreClient;

import elearningmaps.User;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/*****************************************************************************
 * ClientAddContactUI.java                                                   *
 *                                                                           *
 * This class is responsible to generate a UI where the user can select a    *
 * contact and add it to his/her contact list. A list of all user will be    *
 * presented to the user to choose from.                                     *
 *                                                                           *
 * @author Klitos Christodoulou                                              *
 * email: christk6@cs.man.ac.uk                                              *
 *                                                                           *
 * Created on 14-Jun-2010, 11:38:54                                          *
 *                                                                           *
 *****************************************************************************/

public class ClientAddContactUI extends javax.swing.JFrame {

 /*Variable declarations*/
 private ClientScreenUI client_scr = null;

 /*Vector will hold the entries for the JList*/
 private Vector fillContactsList = new Vector();
 private List allContacts = null;

 /*ClientAddContactUI constructor*/
 public ClientAddContactUI(ClientScreenUI cs, List l) {
  /*Hold a reference of the ClientScreenUI*/
  client_scr = cs;

  /*Store a reference to the List (ExistingUsers)*/
  allContacts = l;

  /*Create the JList and then build the UI*/
  createList(allContacts);

  /*Build the UI*/
  initComponents();

  /*Place JFrame in the middle of the screen*/
  this.setLocationRelativeTo(null);
 }//end constructor

  /*Initialize the form*/
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    contactsList = new javax.swing.JList(fillContactsList);
    backButton = new javax.swing.JButton();
    addButton = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("eLearningMaps - Add Contact");
    setLocationByPlatform(true);
    setResizable(false);

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/contact_gui.png"))); // NOI18N
    jLabel1.setText(" Add Contact");

    contactsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    contactsList.setCellRenderer(new AddContactsCellRenderer());
    jScrollPane1.setViewportView(contactsList);

    backButton.setText("Close");
    backButton.setToolTipText("Go Back");
    backButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        backButtonActionPerformed(evt);
      }
    });

    addButton.setText("Add Contact");
    addButton.setToolTipText("Add Contact");
    addButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addContainerGap(210, Short.MAX_VALUE)
        .addComponent(addButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(backButton)
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(backButton)
          .addComponent(addButton))
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

 /***
  * Back button just hide this UI. User return back to the ClientScreenUI
  * 
  * @param evt
  */
  private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
   this.setVisible(false);
  }//GEN-LAST:event_backButtonActionPerformed

  /***
   * The user wants to add this user to his contact list.
   * 
   * @param evt
   */
  private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
   int [] selected = contactsList.getSelectedIndices();
    //System.out.println("Selected Elements:  " + selected.length);

    for (int i = 0; i < selected.length; i++) {
      ContactEntry element = (ContactEntry) contactsList.getModel()
            .getElementAt(selected[i]);
        //System.out.println("You want to add:  " + element.getUserName());
        client_scr.addContact(element.getContact());
      }
  }//GEN-LAST:event_addButtonActionPerformed

 /***
  * This method will read the list of Existing users from the 
  *
  */
 public void createList(List aList) {

  for (int i=0; i<aList.size(); i++) {

    /*Get the user for that index*/
    User contact_user = (User) aList.get(i);

  /***
   * It will add :
   *  (1) a User object
   *  (2) full name
   *  (3) username
   */
  /*Add to the list except current user*/
  if (! contact_user.getUserName().equals(client_scr.getUser().getUserName())) {
   this.fillContactsList.add(new ContactEntry(contact_user,
                               contact_user.getFullName(),
                               contact_user.getUserName(),
                               contact_user.getEmail()));
   }//end if
  }//end for
 }//end method


/*****************
 * UPDATE THE LIST:
 * This method will make this UI visible again but first
 * it will read and update the List
 *
 * @param aList
 */
 public void updateList(List aList) {
  /*Clear the vector*/
  this.fillContactsList.clear();

  /*create the list of contacts*/
  this.createList(aList);

  /*update the list of contacts*/
  contactsList.setListData(fillContactsList);
 }//end

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton addButton;
  private javax.swing.JButton backButton;
  private javax.swing.JList contactsList;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  // End of variables declaration//GEN-END:variables

/***********************************************************************
 * This inner class is used to create a custom Listmodel for the JList
 * that is responsibel to show the contacts of each client.
 *
 */
class AddContactsCellRenderer extends JLabel implements ListCellRenderer {
 private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

 /*Constructor*/
 public AddContactsCellRenderer() {
  setOpaque(true);
  setIconTextGap(12);
 }//end

 public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

   ContactEntry entry = (ContactEntry) value;
   setText("Name : " + entry.getFullName()
            + " | Username : " + entry.getUserName()
            + " | Email : " + entry.getEmail());

   setBackground(isSelected ? HIGHLIGHT_COLOR : Color.WHITE);
   setForeground(isSelected ? Color.WHITE : Color.BLACK);

   return this;
  }//end method
 }//end inner class
}//end class
