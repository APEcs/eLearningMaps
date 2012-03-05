package elearningmaps;

import elearningmaps.User;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/***
 * This is the medium of communication between server and client and
 * vice versa. It is a message composed with meaningful information to
 * the DrawingServer. There are 4 kinds of MessageObject :
 *
 * The idea is that each of the 4 MessageObjects should hold all the
 * information necessary to recreate the request locally at each clients
 * machine
 * 
 * (1) "Create a new Shape" (ShapeConcept or ShapeLinking)
 * (2)
 * (3)
 * (4)
 *
 *
 * @author Klitos Christodoulou
 */
public class MessageObject implements Serializable {

  /*Properties*/
  /*"Create a new Shape"*/
  private String    msgPurpose       = null;
  private String    shapeType        = null;
  private Rectangle positionRect     = null;
  private String    description      = null;
  private String    creatorUserName  = null;
  private String    shapeID          = null;
  private User      userToBound      = null;
  private String    creatorEmail     = null;
  /*Plus extra case ShapeURL*/
  private String    webLink          = null;
  /*Extra case : shapeconcept with image*/
  private String [] imageDetails     = null;

  /*"Conference session variables"*/
  private String sessionParticipants = null;
  private String hostClient          = null;
  private int    hostPort;
  private String hostIP              = null;

  /*"Chat message system"*/
  private String chatMessage         = null;
  private String chatSenderUserName  = null;
  private Color  chatSenderColor     = null;

  /*"Aquire lock request"*/
  private String lockShapeID         = null;
  private String userWantToLock      = null;
  private String lockStatus          = null;

  /*"Change description"*/
  private String editShapeID         = null;
  private String editNewText         = null;
  private String editEditor          = null;

  /*"Translate a Shape"*/
  private int newX1                  = 0;
  private int newY1                  = 0;
  private String translateShapeID    = null;
  private String whoTranslate        = null;

  /*"Join two shapes"*/
  private String [] idArray          = null;
  private String whoJoin             = null;

  /*"Update wait list"*/
  private ArrayList<String> arrayOfParticipants = null;
  private String recipient           = null;


  /***
   * (1) Construct a message object for : "Create a new Shape"
   *  Usage: NEW SERVER
   *
   *  Used for: - ShapeConcept
   *            - ShapeComment add extra : email.
   *
   */
public MessageObject(String msgP, String shapetype, Rectangle rect,
                     String descr, String creator, String email){

  this.msgPurpose       = msgP;
  this.shapeType        = shapetype; /*Type of this Shape*/
  this.positionRect     = rect;
  this.description      = descr;   /*The text inside the Concept*/
  this.creatorUserName  = creator;
  this.creatorEmail     = email;
 
  /*Special case, this id will be unique to each shape and it will
   used when we want to find it*/
  this.shapeID          = creator +"+"+ randomUsername();     /*username+randomstring*/
}//end costructor


/***
 * (2) Construct a new message object for : "Sending the User object to bound"
 * Usage : NEWSERVER
 */

public MessageObject(String msgP, User u) {
 this.msgPurpose  = msgP;
 this.userToBound = u;
}//end constructor

/***
 * (3) Construct a new MessageObject for : "chat"
 */
public MessageObject(String msgP, String chatMes,
                     String chatS, Color clr) {

 this.msgPurpose         = msgP;
 this.chatMessage        = chatMes;
 this.chatSenderUserName = chatS;
 this.chatSenderColor    = clr;
}//end constructor

/***
 * (4) Used to send a MessageObject to the OLDSERVER. The message
 * too initiate the "conference call". It contains :
 *   - purpose = "conference"
 *   - hostclient = the name of the client that host the session
 *   - hostport   = the port of the client that host the session
 *   - hostip     = the ip of the hostclient which will be used as the
 *                  NEW SERVER.
 */
public MessageObject(String msgP, String sessionP, String host_client,
                     int host_port, String host_ip) {

 this.msgPurpose           = msgP;
 this.sessionParticipants  = sessionP;
 this.hostClient           = host_client;
 this.hostPort             = host_port;
 this.hostIP               = host_ip;
}//end constructor


/***
 * (5) Construct a MessageObject for requesting the permission from
 * the server to lock a particular object.
 *
 * Message Purpose is : "lockrequest"
 * Also used for :       "releaseshape" , (shapeid, usern)
 *                       "populatelist"
 *                       "deleteshape"
 * 
 * From : Client to Server
 */
public MessageObject(String msgP, String shapeID, String usern) {
   this.msgPurpose           = msgP;
   this.lockShapeID          = shapeID;
   this.userWantToLock       = usern;
}//end constructor

/***
 * (6) Construct a MessageObject for requesting the permission from
 * the server to lock a particular object.
 *
 * Message Purpose is : "lockrequest"
 *                    
 *
 * From : Server to Client
 */
public MessageObject(String shapeID, String lockS) {
   this.msgPurpose           = "lockrequest";
   this.lockShapeID          = shapeID;
   this.lockStatus           = lockS;
}//end constructor

/***
 * (7) Construct a MessageObject sending the new text to a
 * ShapeConcept or a ShapeComment
 *
 * Message Purpose is : "descriptionchange"
 *
 * 
 */
public MessageObject(String msgP, String shapeID, String newDescri, String editor) {
  this.msgPurpose           = msgP;
  this.editShapeID          = shapeID;
  this.editNewText          = newDescri;
  this.editEditor           = editor;
}

/***
 * (8) Construct a MessageObject sending new top-left corner
 * coordinates (x,y). This point will be used to translate a
 * shape to a new location.
 *
 * Message Purpose is : "translateshape"
 */
public MessageObject(int x1, int y1, String id, String who) {
  this.msgPurpose           = "translateshape";
  this.newX1                = x1;
  this.newY1                = y1;
  translateShapeID          = id;
  this.whoTranslate         = who;
}

/***
 * (9) Construct a MessageObject for broadcasting to other
 * clients that two shapes where join by a user
 *
 * Message Purpose is : "joinshapes"
 */
public MessageObject(String [] array, Rectangle rect, String who) {
  this.msgPurpose           = "joinshapes";
  this.idArray              = array;
  this.positionRect         = rect;
  this.shapeID              = who +"+"+ randomUsername();
  this.whoJoin              = who;
}

  /***
   * (10) Construct a message object for : "Create a new Shape"
   *  Usage: NEW SERVER
   *
   *  Used for: - ShapeURL only
   *
   */
public MessageObject(String msgP, String shapetype, Rectangle rect,
                     String descr, String link ,String creator, String email){

  this.msgPurpose       = msgP;
  this.shapeType        = shapetype; /*Type of this Shape*/
  this.positionRect     = rect;
  this.description      = descr;     /*The text inside the Concept*/
  this.webLink          = link;      /*ShapeURL link*/
  this.creatorUserName  = creator;
  this.creatorEmail     = email;

  /*Special case, this id will be unique to each shape and it will
   used when we want to find it*/
  this.shapeID          = creator +"+"+ randomUsername();     /*username+randomstring*/
}//end costructor


  /***
   * (11) Construct a message object for : "Create a new Shape"
   *  Usage: NEW SERVER
   *
   *  Used for: - ShapeConcept with Image only
   *
   */
public MessageObject(String msgP, String shapetype, Rectangle rect,
                     String [] array, String creator, String email){

  this.msgPurpose       = msgP;
  this.shapeType        = shapetype; /*Type of this Shape*/
  this.positionRect     = rect;
  this.imageDetails     = array;     /*[link,size] of each image*/
  this.creatorUserName  = creator;
  this.creatorEmail     = email;

  /*Special case, this id will be unique to each shape and it will
   used when we want to find it*/
  this.shapeID          = creator +"+"+ randomUsername();     /*username+randomstring*/
}//end costructor

/***
 * (12) - MessageObject to update wait list for a session
 */
public MessageObject(String to, ArrayList<String> array, double i) {
    this.msgPurpose       = "updatewaitlist";
    this.recipient        = to;
    arrayOfParticipants   = array;
}//end constructor

/***************************************
 * Getter methods for all variables    *
 ***************************************/

 //(1)
 public String getMsgPurpose() {
  return this.msgPurpose;
 }//end method

 public String getShapeType() {
  return this.shapeType;
 }//end method

 public Rectangle getPositionRect() {
  return this.positionRect;
 }//end method

 public String getDescription() {
  return this.description;
 }//end method

 public String getCreatorUserName() {
  return this.creatorUserName;
 }//end method

 public String getCreatorEmail() {
  return this.creatorEmail;
 }//end method

 public String getShapeID() {
  return this.shapeID;
 }//end method

 //(2)
 public User getUser(){
  return this.userToBound;
 }//end method

 //(3)
 public String getChatMsg() {
  return this.chatMessage;
 }//end method

 public String getChatSenderUsrname() {
  return this.chatSenderUserName;
 }//end method

 public Color getChatSenderColor() {
  return this.chatSenderColor;
 }//end method

 //(4)
 public String getSessionParticipants(){
  return this.sessionParticipants;
 }//end method

 public String getHostClient(){
  return this.hostClient;
 }//end method

 public int getHostPort(){
  return this.hostPort;
 }//end method

 public String getHostIP(){
  return this.hostIP;
 }//end method

 //(5) , (6)
 public String getLockShapeID(){
  return this.lockShapeID;
 }//end method

 public String getUserWantToLock(){
  return this.userWantToLock;
 }//end method

 public String getLockStatus(){
  return this.lockStatus;
 }//end method

 //7
 public String getEditShapeID(){
  return this.editShapeID;
 }//end method
 
 public String getEditNewText(){
  return this.editNewText;
 }//end method
  
 public String getEditEditorName(){
  return this.editEditor;
 }//end method

//8
 public int getNewX1(){
  return this.newX1;
 }//end method

 public int getNewY1(){
  return this.newY1;
 }//end method

 public String getTranslateID(){
  return this.translateShapeID;
 }//end method

 public String getWhoTranslate(){
  return this.whoTranslate;
 }//end method

 //9
 public String [] getArrayOfIDs(){
  return this.idArray;
 }//end method

 public String getWhoJoinShapes(){
  return this.whoJoin;
 }//end method

 //10
 public String getWebLink(){
  return this.webLink;
 }//end method

 //11
 public String [] getImageDetails(){
   return this.imageDetails;
 }//end method

 //12
 public ArrayList<String> getArrayOfParticipants(){
   return this.arrayOfParticipants;
 }//end method

public String getRecipient(){
   return this.recipient;
 }//end method

/*************************************
 * Setter methods for all variables  *
 *************************************/

/* (1)
 * 
 */
 public void setMsgPurpose(String p) {
  this.msgPurpose = p;
 }//end method

 public void setShapeType(String t) {
  this.shapeType = t;
 }//end method

 public void setPositionRect(Rectangle r) {
  this.positionRect = r;
 }//end method

 public void setDescription(String d) {
  this.description = d;
 }//end method

 public void setCreatorUserName(String c) {
  this.creatorUserName = c;
 }//end method

 public void setCreatorEmail(String e) {
  this.creatorEmail = e;
 }//end method

 public void setShapeID(String id) {
  this.shapeID = id;
 }//end method


/* (2)
 *
 */
 public void setUserToBound(User u) {
  this.userToBound = u;
 }//end method

 //(3)
 public void setChatMsg(String cmsg) {
  this.chatMessage = cmsg;
 }//end method

 public void setChatSenderUsrname(String csn) {
  this.chatSenderUserName = csn;
 }//end method

 public void setChatSenderColor(Color clr) {
  this.chatSenderColor = clr;
 }//end method

 //(4)
 public void setSessionParticipants(String sessionp){
  this.sessionParticipants = sessionp;
 }//end method

 public void setHostClient(String hc) {
  this.hostClient = hc;
 }//end method

 public void setHostPort(int p){
  this.hostPort = p;
 }//end method

 public void setHostIP(String ip){
  this.hostIP = ip;
 }//end method

  //(5), (6)
 public void setLockShapeID(String shID){
  this.lockShapeID = shID;
 }//end method

 public void setUserWantToLock(String usr){
  this.userWantToLock = usr;
 }//end method

 public void setLockStatus(String locks){
  this.lockStatus = locks;
 }//end method

 //7
 public void setEditShapeID(String editid){
  this.editShapeID = editid;
 }//end method

 public void setEditNewText(String editnew){
  this.editNewText = editnew;
 }//end method

 public void setEditEditorName(String aEditor){
  this.editEditor = aEditor;
 }//end method

//8
 public void setNewX1(int x){
   this.newX1 = x;
 }//end method

 public void setNewY1(int y){
  this.newY1 = y;
 }//end method

 public void setTranslateID(String id){
  this.translateShapeID = id;
 }//end method

 public void setWhoTranslate(String who){
  this.whoTranslate = who;
 }//end method

  //9
 public void setArrayOfIDs(String [] array){
  this.idArray = array;
 }//end method

 public void setWhoJoinShapes(String wj){
  this.whoJoin = wj;
 }//end method

 //10
 public void setWebLink(String l){
   this.webLink = l;
 }//end method

 //11
 public void setImageDetails(String [] arr){
   this.imageDetails = arr;
 }//end method

 /********************************************
  * Othe Supporting Methods                  *
  ********************************************/

  /***
  * This method will produce random string that will be uesed to
  * create an identification of a Shape so that we can distinguish
  * it from othe objects.
  *
  * Format is as follows : username+thisrandomstring
  *
  * @return
  */
  public String randomUsername() {

   Random r = new Random();

   String token = Long.toString(Math.abs(r.nextLong()), 36);

   return token;
 }//end method

}//end class