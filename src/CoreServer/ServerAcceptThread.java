package CoreServer;

import elearningmaps.User;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/***************************************************************************
 * This class is responsible to add the listening to multiple requests
 * capability of the server. The server will loop forever and be ready
 * to listen to requests. This class was constructed as a different thread
 * to release the GUI from the while(true) loop.
 *
 * This is the class that will call the MultiServerThread
 *
 * @author Klitos
 */
public class ServerAcceptThread extends Thread {

  /*Thread properties*/
  private ServerSocket servSocket = null;
  private Socket incomingSocket = null; /*This is the new socket created
                                              by the accept() method*/

  /***
   * This is a List that will hold all the users (clients) connected to the
   * server.
   */
  private List clientHandlerList;

  private javax.swing.JTextArea logMsg;
  private ServerConsoleUI server_console;   /*Reference to Server Console*/
  private ServerMail serverMailMechanism = null; /*Mail mechanism*/

  /******************************************************
   * Constructor method will accept:
   * (1) the ServerSocket, from which it will create the new socket
   * (2) An object of the JTextArea used to 
   */

  public ServerAcceptThread (ServerSocket sock,
                             javax.swing.JTextArea log,
                             ServerConsoleUI server_cons) {
    
   super("ServerAcceptThread");

   this.servSocket = sock;
   logMsg = log;

   /*Client Handler List - Synchronized*/
   clientHandlerList = Collections.synchronizedList(new ArrayList());

   /*ALLAGH: Store a reference of server console*/
   server_console = server_cons;
  }//end constructor


  /*Thread run method*/
  public void run() {
	try {

      /*Loop and continously listen to requests*/
      while (true) {
        /*New socket created when connection established with client*/
        incomingSocket = servSocket.accept();

        /*Update server console with a message*/
        logMsg.append("\n Accepted from " +
                          incomingSocket.getInetAddress().toString());


        /*(A-1) Create a new ClientHandler*/
        ClientHandler client_handler_obj = new ClientHandler(this,incomingSocket);

        /*(A-2) Store this Client handler object to the list.
         Use the list for broadcasting*/
        if (client_handler_obj != null)
         clientHandlerList.add(client_handler_obj);


        /*Count number of open connections*/
        logMsg.append("\n Open connections: " +
                          this.clientHandlerList.size());

        /*Autoscroll bar*/
        logMsg.setCaretPosition(logMsg.getText().length());

       }//end
	 } catch (IOException e) {
	   logMsg.append("\n Listening to requests: Socket failure");
	 }//end catch
  }//end try method

  
  /***
   * This method is triggered when a client logs in/out in the service. The
   * responsibility of this method is to inform all the clients that
   * have this client in their contact lists and update their status
   * to "false" / "true".
   * 
   * @param user
   * @param status
   */
  public synchronized void updateContact(String user, String status) {

    for (int i=0; i< this.clientHandlerList.size(); i++) {
      /*Send the message if connection is open and is bounded to a user*/
      ClientHandler member = (ClientHandler) clientHandlerList.get(i);

      /*Find all alive connections*/
      if ( ! member.getClientHandlerSocket().isClosed()
                                         && member.getUser() != null) {

        /*Find which member has this username as contact*/
        for (int j=0; j<member.getUser().getContactList().size(); j++) {
          User contact = (User) member.getUser().getContactList().get(j);

          /*Find if this member has this contact*/
          if (contact.getUserName().equals(user)) {

            if (status.equals("Active")) {
              /*make contact online*/
              contact.setActive("true");

              /*broad cast to the member a message*/
              member.getTunnel().sendMessage(member.getUser());
            }
            else
            {
              /*make contact online*/
              contact.setActive("false");

              /*broad cast to the member a message*/
              member.getTunnel().sendMessage(member.getUser());
            }//end inner else
          }//end if
        }//end inner for
      }//inner if
     }//end for
  }//end method


/*******************************************************************
 * This is the same method as isConnected() except that it does not
 * flush the list from dead connections. It just finds out if a
 * user is online or not. This method support the updateContact
 * method().
 *
 * @param aUser
 * @return
 */
  
 public synchronized boolean isOnline(User aUser) {

  boolean exists = false;

  if (aUser != null) {

    //Loop through the List that holds the Client Handler
    for (int i=0; i< this.clientHandlerList.size(); i++) {

      User temp_user
              = ((ClientHandler) this.clientHandlerList.get(i)).getUser();

      if ((temp_user != null) && temp_user.getUserName().equals(aUser.getUserName())) {
        exists = true;
        break;
      }//end inner if
    }//end for
   }//end if

  return exists;

 }//end isEntry


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
      ClientHandler member = (ClientHandler) clientHandlerList.get(i);

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
      ClientHandler member = (ClientHandler) clientHandlerList.get(i);

      /*Find all alive connections*/
      if ( ! member.getClientHandlerSocket().isClosed()
                                         && member.getUser() != null) {

        /*Communicate with the client. Tunnel is the object of class
         CommunicateClient*/
        member.getTunnel().sendMessage(msg);
      }//end if
    }//end for
  }//end method


  /****
   * ALLAGH: Method to return the existing clients list from Server console
   *  
   * @return
   */
  public synchronized List getExistingClientList() {
   return this.server_console.getExistingClientsList();
  }//end

  /****************************************************************
   * A method synchronized to return the list to everyone that
   * needs it
   *
   * @return
   */
 public synchronized List getClientHandlerList() {

   if (this.clientHandlerList != null) {
    /*Flush the list first, remove open unused connections*/
    flushList();
   }//end if
   
   /*return the list*/
   return this.clientHandlerList;
   
 }//end method

 /*****************************************************************
  * This method is called to flush the ClientHandlerList for
  * connections that are open but not bound to any user. Such
  * connections must be removed from the List
  */
 public synchronized void flushList(){
  for (int i=0; i< this.clientHandlerList.size(); i++) {

   User temp_user
     = ((ClientHandler) this.clientHandlerList.get(i)).getUser();

   Socket temp_socket = ((ClientHandler) this.clientHandlerList.get(i)).getClientHandlerSocket();

   /*If the socket was closed or no user is bound to an open Client Handler
    ,then remove it from the list*/
   if (temp_socket.isClosed() && temp_user == null ) {
     this.clientHandlerList.remove(i);
   }//end if
  }//end for


 }//end flush()

 /*****************************************************************
  * Method to determine if a username already exists. The method
  * will check:
  *
  *   (1) all members of existingClients array. Mean client
  *       is offline but exists.
  */
 public synchronized boolean isEntry(User aUser) {

  //an doule4ei na valw kai edw to flush

  boolean exists = false;


  List existingCliList = getExistingClientList();

  if (aUser != null) {

    //Loop through the List that holds the Clients
    for (int i=0; i< existingCliList.size(); i++) {

      User temp_user
              = (User) existingCliList.get(i);

      if ((temp_user != null) && temp_user.getUserName().equals(aUser.getUserName())) {
        exists = true;
        break;
      }//end inner if
    }//end for
   }//end if

  return exists;

 }//end isEntry

 /****************************************************************
  * Pass through the existingClients List :
  *  (1) Find the user
  *  (2) Replace user with the new one
  *
  * This is called when a user update its contacts and needs to be
  * updated in this list so that it can be saved correctly in the
  * xml file.
  *
  * @param aUser
  */
public synchronized void replaceEntry(User newUser) {


  /*Get the list of existing users so far*/
  List existingCliList = getExistingClientList();

  if (newUser != null && existingCliList != null) {
    
   //Loop through the List that holds the Clients
   for (int i=0; i< existingCliList.size(); i++) {

    User oldUser
         = (User) existingCliList.get(i);

    /*Find the old user and replace with the new user object*/
    if (oldUser.getUserName().equals(newUser.getUserName())) {
     /*Now replace it*/
     existingCliList.set(i, newUser);
     break;
    }//end inner if
   }//end for
  }//end check
}//end replaceEntry




 /*****************************************************************
  * Method LOGIN the User. The method at first will check if
  * the client exist and then it will LOGIN the it returns:
  *   - null
  *   - or the User object 
  */
public synchronized User doLogin(String username, String pass) {

  User usr = null;

  /*Get the list of existing users so far*/
  List existingCliList = getExistingClientList();

  //Loop through the List that holds the Clients
  for (int i=0; i< existingCliList.size(); i++) {

   User temp_user
         = (User) existingCliList.get(i);
   
   if (username.equals(temp_user.getUserName()) 
                      && pass.equals(temp_user.getPassWord())  ) {
    usr = temp_user;
    break;
   }//end inner if
  }//end for

  return usr;
}//end doLogin


 /*****************************************************************
  * Method to do PASS RECOVER for a User.
  *  - Search for the username
  *  - If User is found
  */
public synchronized User doPassRecover(String username) {

  User usr = null;

  /*Get the list of existing users so far*/
  List existingCliList = getExistingClientList();

  //Loop through the List that holds the Clients
  for (int i=0; i< existingCliList.size(); i++) {

   User temp_user
         = (User) existingCliList.get(i);

   if (username.equals(temp_user.getUserName())) {
    usr = temp_user;
    break;
   }//end inner if
  }//end for


  /*If user is not null*/
   if (usr != null) {
    /*Get mail service details*/
    StringTokenizer requestTokens = new StringTokenizer(server_console.getMailDetails(), "?");

    //System.out.println("Details:" + server_console.getMailDetails());

    int port = Integer.parseInt(requestTokens.nextToken());
    String hos = requestTokens.nextToken();
    String mailFrom = requestTokens.nextToken();
    String user_nm  = requestTokens.nextToken();
    String pass_w  = requestTokens.nextToken();


    serverMailMechanism = new ServerMail(usr.getUserName(),
                                                  hos,port,
                                                  user_nm,
                                                  pass_w,
                                                  usr.getPassWord());

    /*Send email to user*/
    serverMailMechanism.sendMail(mailFrom, usr.getEmail());
   }//end if

  return usr;
}//end doPassRecover


/*****************************************************************
 * This method checks if the client is already active and bound
 * to a specific connection, if he is then it will not be allocated
 * a new connection until close the connection and removed from
 * the ClientHandle List
 *
 * Method to determine if a username already exists. The method
 * will check:
 *   (1) Loop all members of CLIENTHANDLER list. Mean that the
 *       client is connected at the moment.
 *
 *  If true means that the user is already login and online,
 *  do not bound him again to a new connection.
 */
 public synchronized boolean isConnected(User aUser) {

  boolean exists = false;

  //Flush the ClientHandler array from dead connections
  this.flushList();
  
  if (aUser != null) {

    //Loop through the List that holds the Client Handler
    for (int i=0; i< this.clientHandlerList.size(); i++) {

      User temp_user
              = ((ClientHandler) this.clientHandlerList.get(i)).getUser();

      if ((temp_user != null) && temp_user.getUserName().equals(aUser.getUserName())) {
        exists = true;
        break;
      }//end inner if
    }//end for
   }//end if

  return exists;

 }//end isEntry



  /****************************************************************
   * Supporting getter methods
   */
  //This method returns the socket returned by the accept method.
  public Socket getNewSocket() {

    return this.incomingSocket;

  }//end getNewSocket

}//end class