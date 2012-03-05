package CoreServer;

import elearningmaps.User;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xml.sax.SAXException;


/**********************************************************************
 *                                                                    *
 * This class is responsible for reading an XML file which is the     *
 * database of all customers register to the service. The class reads *
 * the XML file, and constructs an array of existingClients. This     *
 * ArrayList will be used for:                                        *
 *    (1) in ServerAcceptThread - isEntry() method. To check wether   *
 *        a username is already register or not.                      *
 *                                                                    *
 *                                                                    *
 *  Created on 07 Jun 2010, 01:59                                     *
 *  Revised on 08 Jun 2010, 10:20                                     *
 *                                                                    *
 *  @author Klitos Christodoulou                                      *
 *  @version 1.0 eLearningMaps                                        *
 *  christk6@cs.man.ac.uk                                             *
 *********************************************************************/

public class ServerFileReader {

  /*Declare properties*/
  private File sourceFile = null;
  private List existingUsers;
  private int index;

  /*Constructor*/
  public ServerFileReader(File f)
   {
     /*Input file*/
     this.sourceFile = f;
   }//end constructor


 /***
  * Method responsible for reading the XML file by calling a recursive
  * method.
  *
  */
  public void readClientsXML() throws ParserConfigurationException, SAXException, IOException {

    existingUsers = Collections.synchronizedList(new ArrayList());
    index = -1;

      Document doc =
              (Document) DocumentBuilderFactory.newInstance().
                                newDocumentBuilder().parse(sourceFile);

   //normalize text representation
  doc.getDocumentElement().normalize();

   //Get the root element
   Element rootElement = doc.getDocumentElement();

   /**
    * Get all elements <user/>
    */
    NodeList initialList = rootElement.getChildNodes();

    //Call the recursive method to loop throught all the nodes
    passFromNodes(initialList);

    /*Call the method to print the array [testing]*/
    //printArray();
  }//end method

 /***
  * This is a recursive method to read all the nodes and subnodes of
  * an xml file. The method aims each time to reconstruct an arraylist
  * of all users and their contacts from reading an XML file.
  *
  * @param tmpList
  */
 public void passFromNodes(NodeList tmpList) {


 //This is the current node
 Node initialNode = null;

 //Loop through the initial list
 for (int i=0; i<tmpList.getLength(); i++)
  {

   //set the (i) node as the initial node
   initialNode = tmpList.item(i);


   /**
    * If the node is <user> then find is details and conctuct a new user,
    * add this user to the arrayList
    */
   if (initialNode.hasAttributes() && initialNode.getNodeName().equals("user")) {

    NamedNodeMap nodeMap = initialNode.getAttributes();

    /*User object details, will be collected from the file*/
    String fullname=null;
    String userName=null;
    String passWord=null;
    String email=null;
    String active="false";

    //Loop through the nodeMap to get the attributes of that node
    for (int j=0; j<nodeMap.getLength(); j++)
     {
      Node aNode = nodeMap.item(j);

      /*Read the details fro this user node*/
       if (aNode.getNodeName().equals("afullname")) {
         fullname = aNode.getNodeValue();
         //System.out.println("fullname: " + fullname);
        }//end if
       if (aNode.getNodeName().equals("busername")) {
         userName = aNode.getNodeValue();
         //System.out.println("username: " + userName);
       }
       if (aNode.getNodeName().equals("cpassword")) {
         passWord = aNode.getNodeValue();
         //System.out.println("pass: " + passWord);
       }
       if (aNode.getNodeName().equals("demail")) {
         email = aNode.getNodeValue();
         //System.out.println("email: " + email);
       }
      }//end for

      /*Now create the user object, use the details above*/
      User u = new User(fullname,
                        userName,
                        passWord,
                        email,
                        active);

      /*Add this user to the existing users array list*/
     if (!isEnrty(u)) {
      existingUsers.add(u);
      index ++;
     }
      //System.out.println("Counter" + index);

     }//end if has attributes

   /***
    * Now find the <contacts> members of this <user>
    */

   if (initialNode.hasAttributes() &&  initialNode.getNodeName().equals("member")) {

    NamedNodeMap nodeMap = initialNode.getAttributes();

    /*User object details, will be collected from the file*/
    String fullname=null;
    String userName=null;
    String passWord=null;
    String email=null;
    String active="false";

    //Loop through the nodeMap to get the attributes of that node
    for (int j=0; j<nodeMap.getLength(); j++)
     {
      Node aNode = nodeMap.item(j);

      /*Read the details fro this user node*/
       if (aNode.getNodeName().equals("afullname")) {
         fullname = aNode.getNodeValue();
         //System.out.println("fullname: " + fullname);
        }//end if
       if (aNode.getNodeName().equals("busername")) {
         userName = aNode.getNodeValue();
        //System.out.println("username: " + userName);
       }
       if (aNode.getNodeName().equals("cpassword")) {
         passWord = aNode.getNodeValue();
        //System.out.println("pass: " + passWord);
       }
       if (aNode.getNodeName().equals("demail")) {
         email = aNode.getNodeValue();
        //System.out.println("email: " + email);
       }
      }//end for

      /*Now create the user object, use the details above*/
      User contact = new User(fullname,
                        userName,
                        passWord,
                        email,
                        active);

      /*Add this user to the existing users array list*/
      ((User) existingUsers.get(index)).addContact(contact);

     }//end if has attributes
   else {
    // index--;
    }//else


   /*Case: check if current node has child nodes*/
   if (initialNode.hasChildNodes()) {

    //Find the subchilder of the current node, create new list
    NodeList newList = initialNode.getChildNodes();

    //Do recursive to find the sub childer. Call the method again
    passFromNodes(newList);
  }//end if
 }//end for

 }//end method

public boolean isEnrty(User u) {

  boolean exists = false;

  if (u !=null) {
    //Loop through the List that holds the Clients
    for (int i=0; i< existingUsers.size(); i++) {

      User temp_user
              = (User) existingUsers.get(i);

      if (temp_user.getUserName().equals(u.getUserName())) {
        exists = true;
       break;
      }
      else
        exists = false;
    }//end for
   }//end if

  return exists;

 }//end isEntry

 /******************************************************************
  *
  * Supporting methods
  * 
  * This method returns an ArrayList of existing users. The list is
  * recostructed from an XML file.
  *
  * @return ArrayList
  */
 public synchronized List getExistingClientsList() {
  return this.existingUsers;
 }//end method

/*This is a test method to print the arraylist*/
public void printArray() {

 for (int i=0; i<existingUsers.size(); i++) {
   System.out.println(((User)existingUsers.get(i)).toStringXML());

  }//end for
 }//end printArray

 /****
   * Method to change the file
   *
   */
  public void setFile(File f) {
    this.sourceFile = f;
  } //end
 
}//end class