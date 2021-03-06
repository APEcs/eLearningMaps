package CoreServer;

import elearningmaps.MessageObject;
import elearningmaps.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

/*************************************************************************
 *                                                                       *
 * This class communicate with a Client.                                 *
 *                                                                       *
 * @author Klitos Christodoulou                                          *
 * Date 05/06/2010, Time 15:54                                           *
 *************************************************************************/

public class CommunicateClient extends Thread {

  //Read and write objects to an underlying stream
  ObjectOutputStream out;
  ObjectInputStream in;


  //Properies of CommunicateClient
  ClientHandler clientHandlerObj;
  Socket        socketfromClientHandler;

  /*Constructor*/
  public CommunicateClient(ClientHandler h, Socket s) {
    super("CommunicateClient");

    clientHandlerObj = h;
    socketfromClientHandler = s;
  }//end constructor


  /***
   * Thread run() method
   */
  public void run() {
   try {
     
     /***
      * The following lines establish the socket connection between the client
      * and the server and open a PrintWriter and a BufferedReader. Client and
      * Server can now exchange messages
      */

     /*send data to client*/
      //out = new PrintWriter(socketfromClientHandler.getOutputStream(), true);
      out = new ObjectOutputStream(socketfromClientHandler.getOutputStream());
      out.flush();

	 /*receive data from client*/
      //in = new BufferedReader(new InputStreamReader(socketfromClientHandler.getInputStream()));
	  in = new ObjectInputStream(socketfromClientHandler.getInputStream());

     /****
      * Now, read the request from the client to determine what to do next.
      * Two choices:
      *  (1) User needs to register. If is not register then register
      *  (2) User is already register so :
      *      - check login details
      *      - if ok bound the user to the ClientHandler object so
      *        they can talk.
      */

      Object request;

      //Continuously wait for request strings until receive "quit".
      while ((request = (Object) in.readObject()) != null) {

      /*Requests are just simple strings*/
      if (request instanceof String) {
       /***
        * Request from client is in this format:
        *    /register?fullname?username?pass?email
        *
        *    cookie will be generated by the server and sent to the client
        *    next time 
        * Use StringTokenizer to split the string
        */
       StringTokenizer requestTokens = new StringTokenizer((String) request, "?");
       String firstToken = requestTokens.nextToken("?");

       /*Determine which command is send, get the first token*/
       if (firstToken.equals("/register")) {

         /*Create a new user object*/
         User u = new User(requestTokens.nextToken(),
                           requestTokens.nextToken(),
                           requestTokens.nextToken(),
                           requestTokens.nextToken(),
                           requestTokens.nextToken());

         /*Set the inetAddress of User u*/
         //u.setInetAddress(socketfromClientHandler.getInetAddress());

        /**
         * Check if username is already register, if not then bound
         * the user to the ClientHandler. Else send a message back to
         * the client.
         * To types of messages are send back to the Client
         *   (1) ERRXX?message - denote an error
         *   (2) SUCXX?message - denote a success of a request
         */

        if ( !this.clientHandlerObj.getReferToServerAcceptThread().isEntry(u)) {
         /*Bound this USER to this ClientHandler*/
          clientHandlerObj.boundUser(u);

          /*ALLAGH: Add the user in the existing users array*/
          clientHandlerObj.getReferToServerAcceptThread().getExistingClientList().add(u);

          sendMessage("SUC01?User register successful.");
        }//if
        else {
          /*Send message back to the client*/
          sendMessage("ERR01?Username already in used.");
        }//else

       }//end command: register
       else if (firstToken.equals("/passrecover")) {
         
         /*Call method to detect User that request pass recovery*/
         User u = clientHandlerObj.getReferToServerAcceptThread()
                                  .doPassRecover(requestTokens.nextToken());

         /*If User found :
          *
          * - Retrieve password.
          * - Retrieve email address.
          * - Sent email to user.
          */
         if (u != null) {
           sendMessage("SUC02?User found, check your inbox.");
          }
         else {
           sendMessage("ERR04?Invalid username, cannot recover password.");
         }//else
       }//end command: passrecover
       /*Client request a Login, to access contact list */
       else if (firstToken.equals("/login")) {
      
         /*Find the user who wants to login*/
         User u = clientHandlerObj.getReferToServerAcceptThread()
                                  .doLogin(requestTokens.nextToken(),
                                           requestTokens.nextToken());

         /*Success login details = User found*/
         if (u != null) {

           /*Set the user as active*/
            u.setActive("true");
          
           /*if this connection is unbound*/
           if (clientHandlerObj.getUser() == null || clientHandlerObj.getUser() != u) {
         
             /*Check if the user is bound to some other connection*/
             if (! clientHandlerObj.getReferToServerAcceptThread().isConnected(u)) {
               /*Bound this connection to this user*/
               clientHandlerObj.boundUser(u);

               /*Set user's inetAddress*/
               //u.setInetAddress(socketfromClientHandler.getInetAddress());


               /*Before sendind user object update the contacts*/
               for (int i=0; i<u.getContactList().size(); i++) {
                 User cont = (User) u.getContactList().get(i);

                 if (clientHandlerObj.getReferToServerAcceptThread().isOnline(cont)) {
                  cont.setActive("true");
                  }//if
                 else {
                  cont.setActive("false");
                 }//else
                }//end for

               /*Send this user object to the client*/
               sendMessage((User) u);
             }//inner if
             else {
               sendMessage("ERR02?Client already logged in.");
             }//else
           }//inner if
           else {

              /*Before sendind user object update the contacts*/
              for (int i=0; i<u.getContactList().size(); i++) {
               User cont = (User) u.getContactList().get(i);

               if (clientHandlerObj.getReferToServerAcceptThread().isOnline(cont)) {
                 cont.setActive("true");
                }//if
               else {
                 cont.setActive("false");
                }//else
               }//end for

            /*if this connection is bound to user "u" then just send this
            user object to the client*/
            sendMessage((User) u);


           }//else

            /*TEST = Inform other clients that this user is online*/
            clientHandlerObj.getReferToServerAcceptThread()
                                  .updateContact(u.getUserName(), u.isActive());

         }//end if
        else
         {
          /*no user was found with that username and pass
           send a message to the client*/
           sendMessage("ERR03?No user was found.");
        }//end else
       }//end command: login
       else if (false) {
         /*wants to add a friend*/
       }
       else if (request.equals("/test")) {
         //this is a test 
         sendMessage("test");
       }
       else if (request.equals("/allcontacts")) {
         /*Send the list of all contacts to the client*/
         sendMessage((List) clientHandlerObj.getReferToServerAcceptThread().getExistingClientList());
       }
       else if (request.equals("/shutdown")) {
        /**
         * This request will be sent prior the client sents any
         * "quit" message. This is to stop the clients thread in
         * CommunicateServer class from listening to more requests
         * from the server. That way it will allow the client to
         * quit without any errors closing all connections and buffers
         * releasing memory.
         */
        sendMessage("shutdown");
       }
       else if (false) {
         /*wants to get the contact list*/
       }
       /*Means that the client has quit, terminate the connection with
        the client*/
       else if (request.equals("/quit")) {

        /*FIX bug : remove user only when user is bound on a connection*/
        if (clientHandlerObj.getUser() != null) {

         /*Set client status to inactive*/
         clientHandlerObj.getUser().setActive("false");

         /*Inform all the users that this user is not online anymore*/
         clientHandlerObj.getReferToServerAcceptThread()
                       .updateContact(clientHandlerObj.getUser().getUserName(),
                                        clientHandlerObj.getUser().isActive());
        
         }//end if

         /*Send message to inticate that the client will quit*/
         sendMessage("SUCQUIT");
        
         /*ALLAGH: Firstly unbound the client*/
         clientHandlerObj.unBoundUser();

         /*close buffers*/
         in.close();
    	 out.close();
         
         /*close socket*/
		 socketfromClientHandler.close();

         /*Flush the list from drop connections*/
         clientHandlerObj.getReferToServerAcceptThread().flushList();
         break;
       }//end
      }/*STRING REQUESTS*/
      else if (request instanceof User) {
       /*To add or remove a user from the contact list*/

        User tu = (User) request;

        /*Replace the user of this connection*/
        clientHandlerObj.boundUser(tu);

        /*Replace user in existing list*/
        clientHandlerObj.getReferToServerAcceptThread().replaceEntry(tu);

        /*Before sendind user object update the contacts*/
        for (int i=0; i<tu.getContactList().size(); i++) {

         User cont = (User) tu.getContactList().get(i);

         if (clientHandlerObj.getReferToServerAcceptThread().isOnline(cont)) {
           cont.setActive("true");
         }//if
         else {
           cont.setActive("false");
         }//else
        }//end for

        /*if this connection is bound to user "u" then just send this
        user object to the client*/
        sendMessage((User) tu);

      }/*USER REQUESTS*/
     else if (request instanceof MessageObject) {
       /*Message object*/
       MessageObject mo = (MessageObject) request;
      
       /*Check the purpose of this message object*/
       if (mo.getMsgPurpose().equals("/conference")) {

         /*(1) Initially save who hosted the session*/
         //String host_user = mo.getHostClient();

         //System.out.println("Hostage: " + host_user);
         
         /*(2) Create a string token with all session participants*/
         StringTokenizer participantsToken
                  = new StringTokenizer(mo.getSessionParticipants(), "?");

         /*(3) Loop each username and broadcast the details necessary
          to connect the specific username to the NEW SERVER*/
         while (participantsToken.hasMoreElements())
          {
           String tokenUserName = participantsToken.nextToken();

           /*(4) Now call the broadcast() - ServerAcceptThread - to
            send this MessageObject to that username*/
            clientHandlerObj.getReferToServerAcceptThread().broadcast(tokenUserName, request);
          }//end while
       }//end if
      }/*MESSAGE OBJECT REQUESTS*/
     }//end while
    }//end try
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
 }//end run()


/*****
 * Method to send messages to the Client
 * I change it to object to sent messages in general
 */
 public void sendMessage(Object msg)
  {
	try {
     /*This call is important to reset the state of object
      if keep sending the same object with just a change to
      its properties*/
     out.reset();

	 out.writeObject(msg);
	 out.flush();
     }//try
    catch(Exception e){
       System.out.print("Exception: CommunicateClient, when writting message");
       System.out.print(e.getMessage());
	 }//catch
  }//end sendMessage()

}//end class