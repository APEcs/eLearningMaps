package CoreClient;

import elearningmaps.User;
import java.io.Serializable;
import javax.swing.ImageIcon;

/*****************************************************************************
 * ListUserEntry.java                                                            *
 *                                                                           *
 * This class represents an entry of the Custom JList from the ClientScreen  *
 * class. Each member of this class stores:                                  *
 *                                                                           *
 *    (1) the username of the contact that will presented on the             *
 *        contact list.                                                      *
 *                                                                           *
 *    (2) the icon of this user (active / inactive)                          *
 *                                                                           *
 * @author Klitos Christodoulou                                              *
 * email: christk6@cs.man.ac.uk                                              *
 *                                                                           *
 * Created on 12-Jun-2010, 12:25:28                                          *
 *                                                                           *
 *****************************************************************************/

public class ListUserEntry {

 /*Declaration of properties*/
 private final String userName;

 private String iconPath;

 private User contact;

 private ImageIcon image;

 /*Constructor*/
 public ListUserEntry(User u ,String contactname, String img) {
    this.userName = contactname;
    this.iconPath = img;

    this.contact = u;
 }//end constructor


  /*Constructor*/
 public ListUserEntry(String contactname, String img) {
    this.userName = contactname;
    this.iconPath = img;
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

 /***
  * Get an image icon for this entry
  *
  * @return
  */
 public ImageIcon getImage() {
  if (image == null) {
    image = new ImageIcon((getClass().getResource(iconPath)));
   }
   return image;
 }

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