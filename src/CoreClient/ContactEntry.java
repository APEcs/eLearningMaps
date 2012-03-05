package CoreClient;

import elearningmaps.User;

/*****************************************************************************
 * ContactEntry.java                                                         *
 *                                                                           *
 * This class represents an entry of the Custom JList from the Add Contact   *
 * window class. Each member of this class stores:                           *
 *                                                                           *
 *    (1) the username of the contact that will presented on the             *
 *        contact list.                                                      *
 *                                                                           *
 *    (2) the full name of the contact                                       *
 *                                                                           *
 * @author Klitos Christodoulou                                              *
 * email: christk6@cs.man.ac.uk                                              *
 *                                                                           *
 * Created on 14-Jun-2010, 11:29:38                                          *
 *                                                                           *
 *****************************************************************************/

public class ContactEntry {

 /*Declaration of properties*/
 private final String userName;
 private final String fullName;
 private final String email;

 private User contact;

 /*Constructor*/
 public ContactEntry(User u ,String fulln, String usern, String e) {
  this.contact = u;
  this.fullName = fulln; 
  this.userName = usern;
  this.email = e;
 }//end constructor

  /****
  * Get user object for this contact.
  *
  * @return
  */
 public User getContact() {
  return this.contact;
 }//end

 /****
  * Get the username. The username will be shown in the contact list
  *
  * @return
  */
 public String getUserName() {
  return this.userName;
 }//end

 /****
  * Get the Fullname of this contact.
  *
  * @return
  */
 public String getFullName() {
  return this.fullName;
 }//end

  /****
  * Get the Email of this contact.
  *
  * @return
  */
 public String getEmail() {
  return this.email;
 }//end

 /***
  * Override toString()
  *
  * @return
  */
  @Override
  public String toString() {
    return this.userName;
  }//end
}//end class