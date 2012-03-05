package CoreDrawingSyn;

import elearningmaps.MessageObject;
import elearningmaps.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * COMMUNICATE WITH SERVER THREAD
 * 
 * This thread allow a client to communicate with the Server.
 *  
 *
 *
 * @author Klitos
 */
public class CommunicateServerSyn extends Thread {

 /*Propeties declarations*/
 private String serverIP,host_client;
 private int    serverPort;
 private User   user;
 private DrawConsoleUISyn draw_console_UI = null;

 /*Socket and buffers*/
 private Socket clientSocket;
 private ObjectOutputStream out;
 private ObjectInputStream  in;

 /*Constructor*/
 public CommunicateServerSyn(String ip, int servport, User u,
                                      String hostclient, DrawConsoleUISyn dc) {

  super("CommunicateServerSyn");
  /*Get server ip and port*/
  this.serverIP = ip;
  this.serverPort = servport;
  this.user = u;
  this.host_client = hostclient;

  /*Store a reference to the GUI so it can be accessed*/
  draw_console_UI = dc;
 }//end constructor


 /***
  * Listen for responses from the server
  */
public void run() {
  try {
    /*Create a new socket to the server*/
    clientSocket = new Socket(serverIP, serverPort);

    //System.out.println("clientSocket is " + clientSocket.isConnected());

    /*Keep the socket alive*/
    clientSocket.setKeepAlive(true);

    /*Get input and output streams to the socket*/
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    out.flush();
    in = new ObjectInputStream(clientSocket.getInputStream());

    /*Loop*/
    Object fromServer;

    /*Server listen for objects*/
    while ((fromServer = (Object) in.readObject()) != null) {

      /*If those objects are strings*/
      if (fromServer instanceof MessageObject) {
       /*Read the first line of this object to determine what to do*/
       /*Determine the purpose of this object*/
       String purpose = ((MessageObject) fromServer).getMsgPurpose();

       /*Check the purpose of this MessageObject*/
       if (purpose.equals("createshape")) {
         draw_console_UI.forwardMOtoCanvas((MessageObject) fromServer);
        }//end command : CREATE SHAPE
       else if (purpose.equals("deleteshape")) {
         draw_console_UI.forwardDELCanvas((MessageObject) fromServer);
        }//end command: DELETE SHAPE
       else if (purpose.equals("lockrequest")){
         draw_console_UI.forwardLocktoCanvas((MessageObject) fromServer);
        }//end command : REQUEST LOCK OBJECT
       else if (purpose.equals("translateshape")) {
         draw_console_UI.forwardTranslateToCanvas((MessageObject) fromServer);
       }//end command: TRANSLATE SHAPE
       else if (purpose.equals("joinshapes")) {
         draw_console_UI.forwardJoinToCanvas((MessageObject) fromServer);
       }//end command: JOIN SHAPES
       else if (purpose.equals("descriptionchange")){
         draw_console_UI.forwardChangeDescToCanvas((MessageObject) fromServer);
        }//end command: CHANGE DESCRIPTION TEXT
       else if (purpose.equals("chat")) {
         draw_console_UI.constructChatWindow((MessageObject) fromServer);
        }//end command: CHAT SYS
       else if (purpose.equals("populatelist")) {
         draw_console_UI.serverUpdateWait((MessageObject) fromServer);
        }//end command: POPPULATE LIST
       else if (purpose.equals("updatewaitlist")) {
         draw_console_UI.clientUpdateWait((MessageObject) fromServer);
        }//end command: UPDATE WAIT LIST
       }/*MessageObject*/
      else if (fromServer instanceof String) {
        if (fromServer.equals("whoareyou")){
         /*Wrap User object to a MessageObject instance and send it to the
         server. The reason to sent this object is for the server to
          bound an open connection to this specific client*/
         MessageObject mo = new MessageObject("bounduser",this.user);

         /*forward the mo*/
         this.sendMessage(mo);

         /*Send a Message to the host_client to populate his list*/
         this.sendMessage(new MessageObject("populatelist",
                                   this.user.getUserName(),
                                              host_client));
        }
        /*FIX THIS*/
        else if (fromServer.equals("serverquit")) {


          /*Setup a message and send it*/
          sendMessage(new MessageObject("quitall",this.user));

          /*close buffers*/
          in.close();
    	  out.close();

          /*close socket*/
		  this.clientSocket.close();

         /*Break the loop*/
         break;
        }//end
        else if (fromServer.equals("stopuserloop")) {

          /*close buffers*/
          in.close();
    	  out.close();

          /*close socket*/
		  this.clientSocket.close();

         /*Break the loop*/
         break;
        }//end command : QUIT USER
      }/*String*/
     }//end while

    /*When while stops , it means that the connection dropped so popup a message
     to indicate that.*/
     this.draw_console_UI.showQuitMessage();
  
   }//try
  catch (Exception e) {
   System.out.println("CommunicateServerSyn: Exception");
   System.out.println("" + e.getMessage());
  }//catch
  finally {
   //Closing connection
	try {
	  in.close();
	  out.close();
	  clientSocket.close();
	 }//try
	catch(IOException ioException){
	  System.out.println("CommunicateServerSyn: Exception (Finally)");
   	 }//catch
	}//finally
}//end run



   /************************************************************************
   * This method is used for HouseKeepping. Is called whenever is         *
   * required to close the socket and in/out buffers.
   *
   * Two cases:
    *    - if user is server
    *    - if user is client
   ************************************************************************/
  public void doQuit(String mode){

   MessageObject mo_quit = null;

   /*Two cases: if user is plain client, and one if user is server*/
   if (mode.equals("CLIENT")) {
    /*Create a quit message for this user*/
    mo_quit = new MessageObject("quituser",this.user);
   }

   /*Send the message*/
   if (mo_quit !=null && clientSocket.isConnected()) {
        sendMessage(mo_quit);
   }//end if

  }//end method


 /************************************************************************
 * Method used to send requests to the SERVER.                           *
 *************************************************************************/
 public void sendMessage(Object msg)
  {
	try {

     /*This call is important to reset the state of object
     if keep sending the same object with just a change to
     its properties*/
     out.reset();

	 out.writeObject(msg);
     //out.reset();
	 out.flush();
     }//try
    catch(Exception e){
       System.out.println("Exception: CommunicateServerSyn, when writting message.");
       System.out.println(e.getMessage());
       e.printStackTrace();
	 }//catch

  }//end sendMessage()

}//end class