package CoreDrawingSyn;

import elearningmaps.User;
import java.net.Socket;

/**************************************************************************
 * This class will be responisble to hold information for each open       *
 * connection to the server.                                              *
 *                                                                        *
 * This class will hold:                                                  *
 *  (1) the incomingSocket                                                *
 *  (2) the inetAddress (Because I have the incming socket I can get the  *
 *                       address at any time)                             *
 *  (3) a reference to the ServerAcceptThread                             *
 *  (4) a user object                                                     *
 *                                                                        *
 * @author Klitos Christodoulou                                           *
 * @email christk6@cs.man.ac.uk                                           *
 *                                                                        *
 *************************************************************************/

public class ClientHandlerSyn {

  //Properties of ClientHandlerSyn

  private User userObjectForThisClient;                 /*Hold a user object that will bound on to this ClientHandlerSyn Object*/
  private Socket socketForThisClient;                   /*Store the incomingSocket for this client*/
  private ServerThread referToServerThread;             /*Hold a reference to ServerThread Class*/
  private CommunicateClientSyn  communicateWithClient;     /*Point to the particular client and communicate*/


  /***
   * Construct a new ClientHandlerSyn object
   *  (args 1) A reference of the class that call it (ServerAcceptThread)
   *  (args 2) The socket created when a connectionestablished
   *                                         (by the accept() method)
   *
   *  (args 3) 
   */
  public ClientHandlerSyn(ServerThread s,
                       Socket incomingSocket) {
    
    /*At start this ClientHandlerSyn is not bound to any User*/
    this.userObjectForThisClient = null;

    /*Socket for this client*/
    this.socketForThisClient = incomingSocket;

    /*Hold a reference to go back to the ServerAcceptThread*/
    this.referToServerThread = s;

    /***
     * THEN, create a new Thread to communicate with the client,
     * we do not know which user is yet. We sent the following
     * arguments:
     *   (1) a reference to this object (ClientHandlerSyn)
     *   (2) and the incoming Socket object
     */
    /*We start a CommunicateClientSyn for each client*/
     communicateWithClient = new CommunicateClientSyn(this,socketForThisClient);
     communicateWithClient.start();
  }//end 


/******************************************
 * SUPPORTING METHODS FOR CLIENT HANDLER  *
 ******************************************/

 /*Bind a specific user to this ClientHandlerSyn object,
  to speisify that in this connection this user is bound*/
 public void boundUser(User u) {
   userObjectForThisClient = u;
 }

 /*Unbound User from this ClientHandlerSyn*/
 public void unBoundUser() {
   userObjectForThisClient = null;
 }

 /*Get the user for this ClientHandlerSyn*/
 public User getUser() {
   return this.userObjectForThisClient;
 }

 /*Get the socket associated with this ClientHandlerSyn object*/
 public Socket getClientHandlerSocket() {
   return this.socketForThisClient;
 }

 /*Get a reference back to the ServerAcceptThread class*/
 public ServerThread  getReferToServerAcceptThread() {
   return this.referToServerThread;
 }

 /* This is the tunnel that connects to a client. Use this to send a
  * message or broadcast
  *
  * @return
  */
 public CommunicateClientSyn getTunnel() {
   return this.communicateWithClient;
 }
}//end class
