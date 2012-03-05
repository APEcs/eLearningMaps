package CoreServer;

import elearningmaps.User;
import java.net.Socket;

/**********************************************************************
 * This class will be responisble to hold information for each open
 * connection to the server.
 *
 * This class will hold:
 *  (1) the incomingSocket
 *  (2) the inetAddress (Because I have the incming socket I can get the address at any time)
 *  (3) a reference to the ServerAcceptThread
 *  (4) a user object
 *
 * @author Klitos
 */
public class ClientHandler {

  //Properties of ClientHandler

  private User userObjectForThisClient;                 /*Hold a user object that will bound on to this ClientHandler Object*/
  private Socket socketForThisClient;                   /*Store the incomingSocket for this client*/
  private ServerAcceptThread referToServerAcceptThread; /*Hold a reference to ServerAcceptThread*/
  private CommunicateClient  communicateWithClient;     /*Point to the particular client and communicate*/


  /***
   * Construct a new ClientHandler object
   *  (args 1) A reference of the class that call it (ServerAcceptThread)
   *  (args 2) The socket created when a connectionestablished
   *                                         (by the accept() method)
   *
   *  (args 3) 
   */
  public ClientHandler(ServerAcceptThread s,
                       Socket incomingSocket) {
    
    /*At start this ClientHandler is not bound to any User*/
    this.userObjectForThisClient = null;

    /*Socket for this client*/
    this.socketForThisClient = incomingSocket;

    /*Hold a reference to go back to the ServerAcceptThread*/
    this.referToServerAcceptThread = s;

    /***
     * THEN, create a new Thread to communicate with the client,
     * we do not know which user is yet. We sent the following
     * arguments:
     *   (1) a reference to this object (ClientHandler)
     *   (2) and the incoming Socket object
     */
    /*We start a CommunicateClient for each client*/
     communicateWithClient = new CommunicateClient(this,socketForThisClient);
     communicateWithClient.start();
  }//end 


/******************************************
 * SUPPORTING METHODS FOR CLIENT HANDLER  *
 ******************************************/

 /*Bind a specific user to this ClientHandler object,
  to speisify that in this connection this user is bound*/
 public void boundUser(User u) {
   userObjectForThisClient = u;
 }

 /*Unbound User from this ClientHandler*/
 public void unBoundUser() {
   userObjectForThisClient = null;
 }

 /*Get the user for this ClientHandler*/
 public User getUser() {
   return this.userObjectForThisClient;
 }

 /*Get the socket associated with this ClientHandler object*/
 public Socket getClientHandlerSocket() {
   return this.socketForThisClient;
 }

 /*Get a reference back to the ServerAcceptThread class*/
 public ServerAcceptThread  getReferToServerAcceptThread() {
   return this.referToServerAcceptThread;
 }

 /* This is the tunnel that connects to a client. Use this to send a
  * message or broadcast
  *
  * @return
  */
 public CommunicateClient getTunnel() {
   return this.communicateWithClient;
 }
}//end class
