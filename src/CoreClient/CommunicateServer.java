package CoreClient;

import elearningmaps.MessageObject;
import elearningmaps.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

/*****************************************************************************
 * This class is repsonsible to send requests to the server. This thread     *
 * is called by both:                                                        *
 *  (1) ClientRegisterUI when the user wants to register                     *
 *  (2) ClientConsole when the user needs to login                           *
 *                                                                           *
 * @author Klitos Christodoulou                                              *
 * @email christk6@cs.man.ac.uk                                              *
 ****************************************************************************/

public class CommunicateServer {

 /*Propeties declarations*/
 private String serverIP;
 private int    serverPort;

 /*Socket and buffers*/
 private Socket clientSocket;
 private ObjectOutputStream out;
 private ObjectInputStream  in;

 /*Constructor*/
 public CommunicateServer(String ip, int servport) {

  /*Get server ip and port*/
  this.serverIP = ip;
  this.serverPort = servport;

  try {
    /*Create a new socket to the server*/
    clientSocket = new Socket(serverIP, serverPort);

    /*Keep the socket alive*/
    clientSocket.setKeepAlive(true);

    /*Get input and output streams to the socket*/
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    out.flush();
    in = new ObjectInputStream(clientSocket.getInputStream());

   }//try
  catch (Exception e) {
   System.out.println("Communicate Server: Exception");
   System.out.println("" + e.getMessage());
  }//catch
 }//end constructor


  /*************************************************************************
  * ClientRegister Class : Will call this method when the user fills the
  * form and wants to register to the server. It will return true if
  * register successful and false if not.
  *
  *  Send String of the form:
  *    /register?full_name?user_name?pass?email@com?false
  *
  * @param fn
  * @param usern
  * @param pass
  * @param email
  * @return
  */

 public boolean doRegister(String fn, String usern, String pass, String email) {
  /*Set result to false*/
  boolean result = false;

  try {
   /*Send a message to the server to register this user*/
   sendMessage(new String("/register?" + fn +"?" + usern + "?"
                           + pass + "?" + email + "?false"));

   /*Read the response that server sent*/
   //System.out.println("" + in.readObject()); //test delete this AFTO ITAN TO LATHOS
   String serverResponse = (String) in.readObject();

   /*Read the first part of the string from server*/
   StringTokenizer requestTokens = new StringTokenizer(serverResponse, "?");
   String firstToken = requestTokens.nextToken("?");

   /*If username is not used the user is register and bound to the open connection*/
   if (firstToken.equals("SUC01")) {
    /*Show the message from the server*/
     result = true;
    }//end if
   else if (firstToken.equals("ERR01")) {
   /*If username exist prompt the user to change it*/
     result = false;
   }//end else

   /*Do not want to close connection yet*/
   //sendMessage(new String("/quit"));
  }//try
  catch (Exception e)
  {
    System.out.println("CommunicateServer: Exception");
    System.out.println("" + e.getMessage());
  }//catch

  return result;
 }//doRegister


/**************************************************************************
  * This method is called when the user requires to login and start using
  * the service. This method returns the USER object that matches with the
  * login infomration provided from the server.
  *
  * Send String of the form:
  *    /login?username?password
  *
  * @param user
  * @param pass
  * @return
  */
  public User doLogin(String user, String pass) {

   User u = null;

   try {

    /*Request login from the server*/
    sendMessage(new String("/login?" + user + "?" + pass));

    /*Get the Server response*/
    Object serverResponse = (Object) in.readObject();

    if (serverResponse instanceof User) {
      u = (User) serverResponse;
    }//end if
    else if (serverResponse instanceof String) {
     /*Read the first part of the string from server*/
     StringTokenizer requestTokens = new StringTokenizer((String) serverResponse, "?");
     String firstToken = requestTokens.nextToken("?");

      if (firstToken.equals("ERR02")) {
        /*Show the message from the server*/
        //System.out.println("Client already logged in.");
        u = null;
      }//end if
     else if (firstToken.equals("ERR03")) {
        /*Show the message from the server*/
        //System.out.println("No user was found.");
        u = null;
      }//end if

    }//else
   }catch (Exception e) {
    System.out.println("CommunicateServer: Exception");
    System.out.println("" + e.getMessage());
   }//catch

   return u;
  }//doLogin


  /***
   * This method will communicate with the server and request a
   * password recovery. The pass will be sent to user's email
   * address. For security reasons.
   *
   * @param user
   * @return
   */
  public String doPassRecover(String user) {

   String result = "";

   try
    {
     /*Request login from the server*/
     sendMessage(new String("/passrecover?" + user));

     /*Get the Server response*/
     Object serverResponse = (Object) in.readObject();

     if (serverResponse instanceof String) {
      /*Read the first part of the string from server*/
      StringTokenizer requestTokens = new StringTokenizer((String) serverResponse, "?");
      String firstToken = requestTokens.nextToken("?");

      if (firstToken.equals("SUC02")) {
       result = "User found, check your inbox.";
      }
      else if (firstToken.equals("ERR04")) {
       result = "Invalid username, cannot recover password.";
      }
     }//String
    } catch (Exception e) {
     System.out.println("CommunicateServer: Exception");
     System.out.println("" + e.getMessage());
    }//catch

  return result;
 }//end method


  /******************************************************************
   * This method will be called when the client needs to add a user
   * as its contact.
   *
   * @param u
   * @return
   */
  public void addContact(User u) {
    sendMessage((User) u);
  }

 /*************************************************************************
  * This is an important method that must be called prior QUITING the 
  * application and closing the buffers IN/OUT and the connection. 
  * Basically this method MUST be called before the doQuit() method.
  */
 public void doShutDown() {
  sendMessage(new String("/shutdown"));
 }


  /************************************************************************
   * This method is used for HouseKeepping. Is called whenever is         *
   * required to close the socket and in/out buffers.                     *
   ************************************************************************/
  public void doQuit(){
   try {
   /*Initially check if socket is connected*/
    sendMessage(new String("/quit"));

     /*Get the Server response*/
     String serverResponse = (String) in.readObject();

     if (serverResponse != null && serverResponse.equals("SUCQUIT")) {
      in.close();
      out.close();
      clientSocket.close();
     }//end inner if
   } catch (Exception e) {
     System.out.println("CommunicateServer - Error while clear.");
   }//catch
   finally {
     //Force closing connection
	 try {
	  in.close();
	  out.close();
	  clientSocket.close();
     }
	 catch(IOException ioException){
	  System.out.println("CommunicateServer - Exception.");
	 }
   }//finally
  }//end method

/****
 * This method will return the Socket object of this communication to
 * the server
 */
public Socket getSocket() {
  return this.clientSocket;
}

/********
 * This method will be called when the user request a list of all
 * existing users to choose 
 *
 */
 public void doContacts(){
  sendMessage(new String("/allcontacts"));
 }//end doContacts


 /****
  * This method setup a MessageObject with all details needed for
  * the conference call
  */
 public void doConference(String sessionP, String host_client,
                          int host_port, String host_ip) {

   /*Create message object for conference*/
  MessageObject mo = new MessageObject("/conference", sessionP,
                                        host_client, host_port, host_ip);

   /*Send message object*/
  sendMessage((MessageObject) mo);
 }//end doConference


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
	 out.flush();
     }//try
    catch(Exception e){
       System.out.println("Exception: CommunicateServer, when writting message.");
       System.out.println(e.getMessage());
	 }//catch
    
  }//end sendMessage()

 
/**************************************************************************
 *               THREAD TO KEEP INTOUCH WITH THE SERVER                   *
 *                                                                        *
 * This is an important inner class. This class will create a thread      *
 * that will keep running and continously LISTENING for any messages from *
 * the server. This Thread acts as a listener, if a change occurs or if   *
 * a server broadcast a message the client will pick it up.               *
 *                                                                        *
 **************************************************************************/
class communicateServerThread extends Thread {

 private ClientScreenUI client_scr = null;

 /*constructor*/
 public communicateServerThread(ClientScreenUI c) {
   super("communicateServerThread");

   /*Hold a reference of a ClientScreenUI object*/
   client_scr = c;

 }//end

 /***
  * Continously LISTEN for any messages from the server
  */
 public void run() {
  try {
    Object fromServer;

    /*Server listen for objects*/
    while ((fromServer = (Object) in.readObject()) != null) {

      /*If those objects are strings*/
      if (fromServer instanceof String) {

        /*If message from server is pure string*/
        //StringTokenizer requestTokens = new StringTokenizer((String) fromServer, "?");
        //String firstToken = requestTokens.nextToken("?");
        
        if (fromServer.equals("shutdown")) {
          /*Break the loop - kill the thread*/
          break;
        }//end
      }/*String*/
      else if (fromServer instanceof User) {
        /*update the user and contact list of this user*/
        client_scr.updateList((User) fromServer);
      }//end case: User
      else if (fromServer instanceof List) {
        /*update the user and contact list of this user*/
        client_scr.showList((List) fromServer);
      }//end case: List
      else if (fromServer instanceof MessageObject) {
        /*Get a Message object*/
        MessageObject mo = (MessageObject) fromServer;

        /*Check the purpose of this message object*/
        if (mo.getMsgPurpose().equals("/conference")) {
          client_scr.showDrawConsoleUI(mo);
          //System.out.println("Participants are: " + mo.getSessionParticipants());
        }//end if "conference"
      }//end case: MessageObject
    }//end while
  } catch (Exception e) {
    System.out.println("CommunicateServer.java - Thread class - Exception: ");
    System.out.println(e.getMessage());
  }//catch
 }//end run method
}//end inner class
}//end class