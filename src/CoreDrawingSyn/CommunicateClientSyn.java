package CoreDrawingSyn;

import elearningmaps.MessageObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This is the class where the server uses to communicate with
 * each client. Each connection has its own reference to this
 * CommunicateClientSyn object. It acts as a tunnel of communication
 * between the Server and each of the Clients.
 *
 *
 * @author Klitos
 */
public class CommunicateClientSyn extends Thread {

 /*Variable Declaration*/

 //Read and write objects to an underlying stream
 ObjectOutputStream out;
 ObjectInputStream in;


  //Properies of CommunicateClientSyn
  ClientHandlerSyn clientHandlerObj;
  Socket        socketfromClientHandler;


 /***
  * Constructor
  */
  public CommunicateClientSyn(ClientHandlerSyn h, Socket s) {
    super("CommunicateClient");

    clientHandlerObj = h;

    socketfromClientHandler = s;

  }//end constructor


  /*Communicate with the client*/
  public void run() {
   try {

     /*(1) Establish the socket connection between Server and Client*/
      /*send data to client*/
      out = new ObjectOutputStream(socketfromClientHandler.getOutputStream());
      out.flush();

	  /*receive data from client*/
	  in = new ObjectInputStream(socketfromClientHandler.getInputStream());

      /*Send a who are you msg to the Client of this Connection*/
      this.sendMessage(new String("whoareyou"));

     /*(2) Listen continously for Client requests*/
    Object request;
    
    while ((request = (Object) in.readObject()) != null) {

      //CASE: MessageObject
      if (request instanceof MessageObject) {

       /*Read the first line of this object to determine what to do*/
       /*Determine the purpose of this object*/
       String purpose = ((MessageObject) request).getMsgPurpose();

       /*Check the purpose of this MessageObject*/
       if (purpose.equals("bounduser")) {

        /*Bound user on this open connection*/
        clientHandlerObj.boundUser(((MessageObject) request).getUser());
        //System.out.println("Bound User : " + ((MessageObject) request).getUser().getUserName());
       }//end command: BOUND USER
       else if (purpose.equals("populatelist")) {
          clientHandlerObj.getReferToServerAcceptThread()
                              .broadcast(((MessageObject) request).getUserWantToLock(),request);
        }//end command: POPPULATE LIST
       else if (purpose.equals("updatewaitlist")) {
          clientHandlerObj.getReferToServerAcceptThread()
                              .broadcast(request);
        }//end command: UPDATE WAIT LIST
       else if (purpose.equals("createshape")) {
         /*(1) Create an entry to the shapeManagerList*/
         clientHandlerObj.getReferToServerAcceptThread()
              .addShapeEntry(((MessageObject) request).getShapeID(),
                             ((MessageObject) request).getCreatorUserName());

         /*(2) Broadcast shape to other Clients*/
         clientHandlerObj.getReferToServerAcceptThread().broadcast(request);
       }//end command: CREATE SHAPE
       else if (purpose.equals("deleteshape")) {
         /*(1) Delete an entry to the shapeManagerList*/
         clientHandlerObj.getReferToServerAcceptThread()
              .delShapeEntry(((MessageObject) request).getLockShapeID(),
                             ((MessageObject) request).getUserWantToLock());

         /*(2) Broadcast delete shape ID to other Clients*/
          clientHandlerObj.getReferToServerAcceptThread()
                              .broadcast(request);
        }//end command: DELETE SHAPE
       else if (purpose.equals("lockrequest")) {
         clientHandlerObj.getReferToServerAcceptThread()
               .lockShape(((MessageObject) request).getLockShapeID(),
                          ((MessageObject) request).getUserWantToLock());
       }//end command: REQUEST LOCK SHAPE
       else if (purpose.equals("translateshape")) {
         clientHandlerObj.getReferToServerAcceptThread().broadcast(request);
       }//end command: TRANSLATE SHAPE
       else if (purpose.equals("joinshapes")) {

         /*If Case 1, add ShapeLinking into shapeManagerList*/
         if (((MessageObject) request).getPositionRect() != null)
          clientHandlerObj.getReferToServerAcceptThread()
              .addShapeEntry(((MessageObject) request).getShapeID(),
                             ((MessageObject) request).getWhoJoinShapes());

         clientHandlerObj.getReferToServerAcceptThread().broadcast(request);
       }//end command: JOIN SHAPES
       else if (purpose.equals("descriptionchange")) {
         clientHandlerObj.getReferToServerAcceptThread().broadcast(request);
       }//end command: CHANGE DESCRIPTION TEXT
       else if (purpose.equals("releaseshape")) {
         /**
          * NOTE : use MessageObject methods for "lockrequest"
          * Here is : (shapeid, userwanttorelease)
          */

         clientHandlerObj.getReferToServerAcceptThread()
               .releaseShape(((MessageObject) request).getLockShapeID(),
                            ((MessageObject) request).getUserWantToLock());
        }//end command: RELEASE CHANGE
       else if (purpose.equals("chat")) {
         /*Broadcast message to all active clients for this session*/
         clientHandlerObj.getReferToServerAcceptThread().broadcast(request);
       }//end command: CHAT SYS
       else if (purpose.equals("quitall")) {
         /*close buffers*/
         in.close();
    	 out.close();

         /*close socket*/
		 socketfromClientHandler.close();

        break;
       }//end command: QUIT ALL USERS
       else if (purpose.equals("quituser")) {


         /*Send message to stop user's loop*/
         sendMessage("stopuserloop");

         /*close buffers*/
         in.close();
    	 out.close();

         /*close socket*/
		 socketfromClientHandler.close();

         /*Flush the list from drop connections*/
         clientHandlerObj.getReferToServerAcceptThread().flushList(((MessageObject) request).getUser().getUserName());
        break;
       }//end command: QUIT USER

       /*Then send the message to the mehtod for analysis*/
       
      }//end if

      //CASE: STRING
      
       /* - If quit receive then, break the loop and exit*/
      else if (request instanceof String) {

       }//end if Srting
     }//end while
    }//try
     catch (Exception e) {
      System.out.print("Exception: CommunicateClient");
      e.printStackTrace();
     }//catch
    finally {
 	  //Terminate connection
      try {
	   in.close();
	   out.close();
	   socketfromClientHandler.close();
	   }//try
      catch(IOException e){
       System.out.print("Exception: CommunicateClient");
       System.out.print(e.getMessage());
	 }//catch
   }//finally
  }//end run method


  /*****
 * Method to send messages to the Client
 * I change it to object to sent messages in general
 */
 public void sendMessage(Object msg)
  {
	try {
     /*This call is important to reset the state of object
      if keep sending the same object with just a change to
      its properties - to reset prepei na ine meta to writeObject*/
     out.reset();

	 out.writeObject(msg);
	 //out.reset();
     
     out.flush();
     }//try
    catch(Exception e){
       System.out.print("Exception: CommunicateClient, when writting message");
       System.out.print(e.getMessage());
	 }//catch
  }//end sendMessage()
}//end class