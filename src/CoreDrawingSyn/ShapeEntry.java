package CoreDrawingSyn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/****************************************************************************
 * SECTION: ShapeManagerList ENTRY - For Locking Synchronisation Mechanism  *
 *                                                                          *
 * This class is used to create a different entry of each shape             *
 * drawn on screen and store it in ServerThread -> shapeManagerList         *
 *                                                                          *
 * ShapeEntry : - shapeID       : identify each shape                       *
 *              - shapeCreator  : who created the shape                     *
 *              - lock          : lock for each shape                       *
 *              - queueList     : Hold a list of usernames waiting to       *
 *                                gain access to that object.               *
 *                                                                          *
 * @author Klitos Christodoulou                                             *
 * email : christk6@cs.man.ac.uk                                            *
 *                                                                          *
 * Date : 20/07/2010 - Time: 11:02                                          *
 *                                                                          *
 ****************************************************************************/

public class ShapeEntry {

 /*ShapeEntry Properties*/
 private String shapeID       = "";
 private String shapeCreator  = "";
 private Date dateLock        = null;

 /*Hold the username that has the object now*/
 private String whoHasTheLock = "";

 /*Declare a Volatile variable to represent the lock for this Shape*/
 //private volatile String lock = "";
 private  String lock = "";
 private  List queueList       = null;


 /*ShapeEntry - Constructor*/
 public ShapeEntry(String id, String creator) {

  /*Properties*/
   shapeID      = id;
   shapeCreator = creator;

  /*Lock for this Shape*/
   lock = "unlocked";

  /*QueueList*/
  queueList  = Collections.synchronizedList(new ArrayList());
 }//end constructor


 /*************************************
  *         Getter Methods            *
  *************************************/
 public String getLockStatus() {
  return this.lock;
 }//end

 public List getQueueList() {
   return this.queueList;
 }//end

 public String getShapeID() {
   return this.shapeID;
 }//end

 public String getShapeCreator() {
   return this.shapeCreator;
 }//end

 public String getWhoHasTheLock() {
   return this.whoHasTheLock;
 }//end

 /*Get the time this object was locked,
  if null it means this is unlocked*/
 public Date getLockDate() {
   return this.dateLock;
 }//end

 

 /*************************************
  *         Setter Methods            *
  *************************************/
 public void setLockStatus(String status) {
  /*Set this lock status*/
  this.lock = status;

  /*If status = locked, record the time*/
  if (status.equals("locked"))
    this.dateLock = recordDate();
  else
    this.dateLock = null;
 }//end method

 public void setWhoHasTheLock(String whl) {
  this.whoHasTheLock = whl;
 }//end

 public void setShapeID(String sid) {
   this.shapeID = sid;
 }//end

 public void setShapeCreator(String sc) {
   this.shapeCreator = sc;
 }//end
 
  /*************************************
  *        Supporting Methods          *
  *************************************/
 public void addToQueue(String username) {
   if (! isEntry(username))
     queueList.add(new String(username));
 }//end

 public boolean isEntry(String username) {
   boolean result = false;

   for (int i=0; i<queueList.size(); i++){
     if (queueList.get(i).equals(username)) {
       result = true;
       break;
     }//end if
   }//end for
   return result;
 }//end

 /***
  * Method that returns the current Date object. 
  * 
  * @return
  */
 public synchronized Date recordDate() {
   Calendar cal = Calendar.getInstance();
  return cal.getTime();
 }//end method


 /**
  * Create a toString() method to print this object
  */
  @Override
 public String toString() {
    return "ShapeEntry > shapeID: " + shapeID + " ,creator: " + shapeCreator +
       " ,lockStatus: " + lock +" | QUEUE LIST :" + queueList.toString();
 }//end
}//end class