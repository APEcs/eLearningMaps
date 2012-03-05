package elearningmaps;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

/************************************************************************
 *                                                                      *
 * This is a common custom class for both client and server. The class  *
 * will be serialized and send through a socket to the client. It       *
 * represent each one user of eLearningMaps. The server will keep a     *
 * list of users that are register to the service.                      *
 *                                                                      *
 * A user of eLearning Maps the following properties:                   *
 *     fullname                                                         *
 *     username                                                         *
 *     password                                                         *
 *     email                                                            *
 *     cookie                                                           *
 *     List of contacts                                                 *
 *                                                                      *
 * @author Klitos Christodoulou                                         *
 * Date 03/06/2010, Time 20:12                                          *
 *                                                                      *
 ************************************************************************/
public class User implements Serializable {

 private String fullName;
 private String userName;
 private String passWord;
 private String email;
 private ArrayList contactList = null;
 private String active;
 /*Hold inetAddress of this client*/
 private InetAddress inetAddress;

 /*****************************************************
  * Constructor - Construct a new user.
  */
 public User (String fullname, String userName, String pass, String email
              , String active) {

   this.fullName        = fullname;
   this.userName        = userName;
   this.passWord        = pass;
   this.email           = email;
   this.active          = active;

   /*Initialize the contact list*/
   contactList = new ArrayList();

 }//end constructor


 /*******************************************************
  * GETTER METHODS
  ******************************************************/

 /*Get inetAddress fot this User*/
 public InetAddress getInetAddress() {
  return this.inetAddress;
 }//end method

 /*Get full name of this user*/
 public String getFullName() {
   return this.fullName;
 }//end method

 /*Get username of this user*/
 public String getUserName() {
   return this.userName;
 }//end method

 /*Get password of this user*/
 public String getPassWord() {
  return this.passWord;
}//end method

 /*Get email address of this user*/
 public String getEmail() {
  return this.email;
}//end method

 /*Is this user online (active) or not*/
 public String isActive() {

   String result="";

   if (this.active.equals("true"))
     result = "Active";
   else
     result = "Inactive";

   return result;
 }//end method

 /*********************************************************
  *               CONTACT LIST                            *
  *                                                       *
  * This method will return the contact list of this user *
  * Contact list or friends list                          *
  *********************************************************/

 public ArrayList getContactList() {
   return contactList;
 }

 /*Count the number of conctacts this user has*/
  public int getNumberOfcontacts() {
    if (contactList == null) {
       return 0;
     }//end if
    else {
      return this.contactList.size();
    }
  }//end numberOfCOntacts

 
  /********************************************************
   * SETTER METHODS                                       *
   *******************************************************/

 /*Set inetAddress fot this User*/
 public void setInetAddress(InetAddress in) {
  this.inetAddress = in;
 }//end method

 /*Change full name of this user*/
 public void setFullName(String name) {
  this.fullName = name;
 }//end method

 /*Change username of this user*/
 public void setUserName(String usern) {
   this.userName = usern;
 }//end method

 /*Change password of this user*/
 public void setPassWord(String pass) {
   this.passWord = pass;
}//end method

 /*Change email address of this user*/
 public void setEmail(String e_mail) {
   this.email = e_mail;
}//end method


 /*Change the status of this user (active) or not*/
 public void setActive(String act) {
  this.active = act;
 }//end method

 /*********************************************************
  *               CONTACT LIST                            *
  *                                                       *
  *  ADD a user as a contact of this user                 *
  *********************************************************/

 /*Add a user to the contact list*/
 public boolean addContact(User member) {

   if (! isEntry(member)) {
    this.contactList.add(member);
    return true;
   }
   else
     return false;
 }//end method


 /*This method will check if a contact is already a contact*/
 public boolean isEntry(User aUser) {

   boolean exists = false;


    //Loop through the List that holds the Clients
    for (int i=0; i< this.contactList.size(); i++) {

      User temp_user
              = (User) this.contactList.get(i);

      if (temp_user.getUserName().equals(aUser.getUserName())) {
        exists = true;
        break;
      }//end inner if
    }//end for

  return exists;

 }//end isEntry



 /*Remove user from contact list*/
 public void remContact(int index) {

   this.contactList.remove(index);
 }//end method


  /*******************************************************
  *     SUPPORTING METHODS                                *
  * Return a String XML representation of this Arc object *
  * used for XML.                                         *
  *********************************************************/
  public String toClientXML()
  {
   return    "afullname=\"" + this.fullName +"\" "
           + "busername=\"" + this.userName +"\" "
		   + "cpassword=\"" + this.passWord +"\" "
		   + "demail=\""  + this.email +"\""
		   + ">";
  }//end

 public String toMemberXML()
  {
   return " <member "
           + "afullname=\"" + this.fullName +"\" "
           + "busername=\"" + this.userName +"\" "
		   + "cpassword=\"" + this.passWord +"\" "
		   + "demail=\""  + this.email +"\""
		   + "/>";
  }//end

 /*This will return the complete XML representation with contacts*/
 public String toStringContactsXML(){
  
   String xml = "";

   xml += "\n  <contacts> ";

   for (int i=0; i<contactList.size(); i++) {
      xml += "\n   " + ((User) contactList.get(i)).toMemberXML();
   }//end for

   xml += "\n  </contacts> ";

   return xml;
  }//end method

  /*This is the complete representation of a user in XML*/
  public String toStringXML()
   {
    String xml = "";
  
     xml += " <user ";
     xml += this.toClientXML();
     xml += toStringContactsXML();
     xml += "\n </user> ";

    return xml;
   }//end 

}//end class