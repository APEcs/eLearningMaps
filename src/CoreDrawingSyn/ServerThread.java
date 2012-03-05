package CoreDrawingSyn;

import elearningmaps.MessageObject;
import elearningmaps.User;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/************************************************************************
 * This is a Server Thread. Create a new ServerSocket Object            *
 * on localhost.
 *
 * (1) The server will hold a list of all connections.
 *
 * (2) Also, the server will hold a list of all Shapes that are drawn on screen
 *     ShapeEntry. This list will form the synchronization management mechanism
 *     that needed. When a Communicate Client thread receives a
 *     MessageObject, the communicate client will analyse it and then the sevrer
 *     will store it in this List as a ShapeEntry object in the shapeManagerList.
 *
 * Note: The server will control the list of Shapes and make sure every local
 *       List in each of the Clients is updated.
 *
 * @author Klitos Christodoulou
 */
public class ServerThread extends Thread {

  /***
   * Declarations
   */

  /*A reference to the timer check lock policy*/
  private Timer lockPolicyTimer;

  /*Hold Server Socket*/
  private ServerSocket serverSocket = null;

  /*Hold incoming Socket*/
  private Socket incomingSocket = null;


  /*List that holds all clients connected*/
  private List clientHandlerList;

  /*List that holds all SHAPES on the Canvas and coordinate local Lists in
   each of the Clients*/
  private List shapeManagerList;


  /*Constructor*/
  public ServerThread(ServerSocket sock) {
    super("ServerThread");

    /*Hold a reference to the Socket*/
    this.serverSocket = sock;

    /*Client Handler List - Synchronized*/
    clientHandlerList = Collections.synchronizedList(new ArrayList());

    /*Hold all SHAPES drawn on Canvas - Synchronized*/
    shapeManagerList  = Collections.synchronizedList(new ArrayList());

    /*Start the timer*/
    wakeUpLockPolicy();
  }//end constructor

  
  /***
   * RUN METHOD
   */
 public void run() {

   try {
    /*Loop and continously listen for new connections*/
    while (true) {
     /*New socket created when connection established with client*/
     incomingSocket = serverSocket.accept();

     /*Inform that a new connection is established*/
     //System.out.println("\n ## NEW SERVER ## \nConnection accepted from: " +
       //                  incomingSocket.getInetAddress().toString());

     /*Create a new ClientHandlerSyn hold the socket*/
     ClientHandlerSyn client_handler_obj = new ClientHandlerSyn(this,incomingSocket);

     /*Store this Client handler object to the list.
     Use the list for broadcasting*/
     if (client_handler_obj != null)
       clientHandlerList.add(client_handler_obj);

    }//end while
   }//try
   catch (Exception exe) {
     System.out.println("ServerThread.java - Exception while loop");
   }
 }//end run


  /*****************************************************************
  * This method is used to remove a dead connection from the
  * client handler List. It gets as an argument the Username of the
  * User bound to a connection and then removes the connection from
  * the ClientHandlerList
  *
  */
 public synchronized void flushList(String user_name){

  for (int i=0; i< this.clientHandlerList.size(); i++) {

   User temp_user
     = ((ClientHandlerSyn) this.clientHandlerList.get(i)).getUser();

   /*If the socket was closed or no user is bound to an open Client Handler
    ,then remove it from the list*/
   if (temp_user.getUserName().equals(user_name) ) {
     this.clientHandlerList.remove(i);
   }//end if
  }//end for

 }//end flush()


 /****
  * Pass from the list of Client Handler and close all live connections:
  *   - also broadcast a message to all clients that this server is
  *     shuting down.
  */
  public synchronized void serverShutDown() {

    /*Inform all users that the server will quit*/
    broadcast(new String("serverquit"));

    /*When down clear the ClientHandlerList*/
    this.clientHandlerList.clear();
  }//end serverShutDown()


  /***
   * This method will be used to broadcast a message to ALL connected
   * users on this service. The mesthod accepts the object to be send
   * and loops through the ClientHandler List broadcasting the msg to
   * everyone.
   *
   * @param msg
   */
  public synchronized void broadcast(Object msg) {

    for (int i=0; i< this.clientHandlerList.size(); i++) {
      /*Send the message if connection is open and is bounded to a user*/
      ClientHandlerSyn member = (ClientHandlerSyn) clientHandlerList.get(i);

      /*Find all alive connections*/
      if ( ! member.getClientHandlerSocket().isClosed()
                                         && member.getUser() != null) {

        /*Communicate with the client. Tunnel is the object of class
         CommunicateClient*/
        member.getTunnel().sendMessage(msg);
      }//end if
    }//end for
  }//end method

  
  /***
   * Broadcast a message to a SPECIFIC user.
   * ClientHandler list has the users online at the moment.
   *
   * @param username
   * @param msg
   */
  public synchronized void broadcast(String username, Object msg) {

    for (int i=0; i< this.clientHandlerList.size(); i++) {
      /*Send the message if connection is open and is bounded to a user*/
      ClientHandlerSyn member = (ClientHandlerSyn) clientHandlerList.get(i);

      /*Find all alive connections*/
      if ( ! member.getClientHandlerSocket().isClosed()
                                         && member.getUser() != null) {

        /*Find the specific User*/
        if (member.getUser().getUserName().equals(username)) {
         /*Communicate with the specific client. Tunnel is the
          object of class CommunicateClient*/
          member.getTunnel().sendMessage(msg);
          break;
      }//inner if
     }//end if
    }//end for
  }//end method


/*********************************************************************
 * SECTION: SYNCHRONISATION MANAGER                                  *
 *                                                                   *
 * This section will enclose all the methods necessary to            *
 * implement the synchronisation manager needed for the collaborate  *
 * drawing.                                                          *
 *                                                                   *
 *********************************************************************/

  /***
   * This method is used to add an entry to the shapeManagerList. Each
   * entry contains information regarding the Lock and the queueList as
   * well as the creator id and username.
   *
   * @param id
   * @param creator
   */
  public synchronized void addShapeEntry(String id, String creator) {
    this.shapeManagerList.add(new ShapeEntry(id, creator));
  }//end method


 /***
  * This method is used to remove an entry from the shapeManagerList.
  *
  * @param id
  * @param whodelete
  */
 public synchronized void delShapeEntry(String id, String whodelete) {

    /*Find the Shape with ID and remove it*/
    for (int i=0; i< this.shapeManagerList.size(); i++) {
      ShapeEntry member = (ShapeEntry) shapeManagerList.get(i);

      /*(1) Find the entry that matches the ShapeID*/
      if (member.getShapeID().equals(id)) {
        shapeManagerList.remove(i);
       break; 
      }//end if
    }//end for
 }//end method

/***
 * This method is responsible to grant access to an object to a
 * specific user, that request to lock the object.
 *
 *   (1) Initially it loops the array that hold the ShapeEntries to
 *        find the particulat "shapeID"
 *
 * @param shapeUniqueid
 * @param userWantToLock
 */
  public synchronized void lockShape(String shapeUniqueid,
                                     String userWantToLock) {

   for (int j=0; j< this.shapeManagerList.size(); j++) {
     ShapeEntry member = (ShapeEntry) shapeManagerList.get(j);
   }//end for

   /*Loop the list of ShapeEntries to find the ID to lock*/
   for (int i=0; i< this.shapeManagerList.size(); i++) {
      ShapeEntry member = (ShapeEntry) shapeManagerList.get(i);

      /*(1) Find the entry that matches the ShapeID*/
      if (member.getShapeID().equals(shapeUniqueid)) {
        /*(2)Check if the shape is already lock or not*/
        if (member.getLockStatus().equals("unlocked")) {

            /*If shape is free then: -lock the object*/
            member.setLockStatus("locked");

            /*Set this user as the one that has the lock now*/
            member.setWhoHasTheLock(userWantToLock);

            /*and - broadcast a message to the "userWantToLock" that is ok*/
            this.broadcast(userWantToLock, new MessageObject(shapeUniqueid,"ok"));        
        }//end case: "unlocked"
        else if (member.getLockStatus().equals("locked")) {
           /*If shape is locked, place username in the queue to wait*/
           member.addToQueue(userWantToLock);

           /*Call method again to update Time [TEST]*/
           member.setLockStatus("locked");

           /*and - broadcast a message to the "userWantToLock" that it must wait*/
           this.broadcast(userWantToLock, new MessageObject(shapeUniqueid,"wait"));
        }//end case: "locked"

        break;
      }//end if
   }//end for
  }//end method

  /****
   * 
   * @param shapeUniqueid
   */
  public synchronized void releaseShape(String shapeUniqueid,
                                        String userWantToRelease) {

   /*Loop the list of ShapeEntries to find the ID to release the lock*/
   for (int i=0; i< this.shapeManagerList.size(); i++) {
      ShapeEntry member = (ShapeEntry) shapeManagerList.get(i);

      /*(1) Find the entry that matches the ShapeID*/
      if (member.getShapeID().equals(shapeUniqueid)) {

        /*(2) Look at the queuelist of this ShapeEntry, if is empty*/
        if (member.getQueueList().isEmpty()) {
          /*"unlocked" the shape*/
          member.setLockStatus("unlocked");

          /*Update the user taht had the lock now to null*/
          member.setWhoHasTheLock("");

          /*send a message to the user that request a release that the
           shape was released to make shape (black=free)*/
           this.broadcast(userWantToRelease, new MessageObject(shapeUniqueid,"released"));
         }
        else {
         /*(2) if queuelist is not empty*/

          /*send a message to the user that request a release that the
          shape was released to make shape (black=free)*/
          this.broadcast(userWantToRelease, new MessageObject(shapeUniqueid,"released"));

          /*Get the first "username" that waits in the queueList*/
          String userNameFromQueue = (String) member.getQueueList().get(0);
          
          /*Set this user as the one that has the lock now*/
          member.setWhoHasTheLock(userNameFromQueue);

          /*Remove memeber from queue*/
          member.getQueueList().remove(0);

          /*Send a message to the removed username to grand access to the shape*/
          this.broadcast(userNameFromQueue, new MessageObject(shapeUniqueid,"ok"));

        }//end case: queueList is not empty
       break;
      }//end if
   }//end for

  }//end if


/***
 * Wake up fair lock policy
 */
public void wakeUpLockPolicy() {
   lockPolicyTimer = new Timer();
   lockPolicyTimer.scheduleAtFixedRate(new lockPolicyThread(),2000,8000);
}


/***
 * This method will return if all shapes are released
 */
public synchronized boolean allRelease() {

   boolean all = true; 
  
   /*Loop the list of ShapeEntries to find the ID to release the lock*/
   for (int i=0; i< this.shapeManagerList.size(); i++) {
      ShapeEntry member = (ShapeEntry) shapeManagerList.get(i);

      /*(1) Find the entry that matches the ShapeID*/
      if (member.getLockStatus().equals("locked")) {
        all = false;
      }
   }//end for

   return all;
 }//end method

/*************************************************************************
 * INNER CLASS -  This inner class weaksup a Thread every 8 seconds      *
 * to check which shapes are still locked by clients. When it detects    *
 * an unfair lock which violates the lock fainess policy it takes action *
 *                                                                       *
 *************************************************************************/
class lockPolicyThread extends TimerTask {

  public void run() {


  /*(1) Record the time when the Thread started*/
  Date theadTime = timeNow();

  /*(2) Pass through the ListShapes array*/
  for (int i=0; i< shapeManagerList.size(); i++) {
      ShapeEntry member = (ShapeEntry) shapeManagerList.get(i);

      /*(3) Check if an entry is locked*/
      if (member.getLockStatus().equals("locked")) {
        /*(4) Check for how long is being locked*/
        long howLong = timeDiff(theadTime,member.getLockDate());

        if (howLong > 8) {
          /*(5) call method to release the shape*/
          releaseShape(member.getShapeID(),member.getWhoHasTheLock());
        }//end if >6

      }//end if
   }//end for
  }//end run()

 /***
  * Method returns the difference btw the two times.
  *
  * @param date1
  * @param date2
  * @return
  */
 public long timeDiff(Date date1, Date date2) {

   Calendar c1 = Calendar.getInstance();
   Calendar c2 = Calendar.getInstance();

   c1.setTime(date1);
   c2.setTime(date2);

   return Math.abs((c1.getTimeInMillis() - c2.getTimeInMillis())/1000);
 }//end method


 /***
  * Method that return the current time
  * @return
  */
 public Date timeNow() {
   Calendar cal = Calendar.getInstance();
  return cal.getTime();
 }//end method
}//end inner class

}//end class