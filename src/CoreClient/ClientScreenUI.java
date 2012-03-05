package CoreClient;

import CoreClient.CommunicateServer.communicateServerThread;
import CoreDrawingSyn.DrawConsoleUISyn;
import CoreDrawingSyn.ServerThread;
import elearningmaps.MessageObject;
import elearningmaps.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

/*****************************************************************************
 * ClientScreenUI.java                                                         *
 *                                                                           *
 * This class is responsible to present the user with a UI after a successful*
 * login to the service. The UI will allow the customer to see his/her       *
 * contact list and add more friends. From the contact list the user can     *
 * choose a subset or all of its contacts and start drawing with them. This  *
 * is the case where the client component will become a Server component.    *
 * This UI will pop up the drawing console of the application.               *
 *                                                                           *
 * @author Klitos Christodoulou                                              *
 * email: christk6@cs.man.ac.uk                                              *
 *                                                                           *
 * Created on 07-Jun-2010, 17:27:58                                          *
 *                                                                           *
 *****************************************************************************/

public class ClientScreenUI extends javax.swing.JFrame {

  /*Vector will hold the entries for the JList*/
  private Vector entriesVector = new Vector();
  private List   allContacts = null;

  /*Properties, declarations*/
  private User user_obj = null; /*hold a reference to a user object*/
  private CommunicateServer communicateSrv = null;
  private ClientConsoleUI client_console = null;

  /*Idle Thread*/
  private communicateServerThread idle = null;

  /*Declarations for New Server Thread*/
  private ServerSocket serverSocket = null;
  private ServerThread serverThread = null;

  /*Hold the window for the server*/
  DrawConsoleUISyn isServer = null;


  /*Constructor - Creates new form ClientScreenUI */
  public ClientScreenUI(User usr, CommunicateServer cs, ClientConsoleUI clientCon) {
    /*Store a reference ot the previous window ClinetConole*/
    client_console = clientCon;

    /*store the User object, This will be used to get
    the contact list of this client and present it
    to the user*/
    user_obj = usr;
    
    /*Update the CONTACTS JList Vector*/
    createListEntries(user_obj);

   /*Initialize the UI*/
   initComponents();

   /*Store an object of CommunicateServer - this will be used when
    communicating with the server*/
   communicateSrv = cs;
   
   /*Place JFrame in the middle of the screen*/
   this.setLocationRelativeTo(null);

   /*Change the name of the user*/
   fullNameLbl.setText("Fullname  : " + usr.getFullName());
   whoIsUserLbl.setText("Username: " + usr.getUserName());
   statusLbl.setText("Status: " + usr.isActive());

   /*Replace window listener*/
   this.addWindowListener(new WindowAdapter()
    {
     @Override
     public void windowClosing( WindowEvent event) {
      try {
        quitMethod();
      } catch (InterruptedException ex) {
         System.out.println("ClientScreen - Exception while Quit");
      }
    }//end WindowEvent
   });//end WindowListener

   /*Create an instance of the enclosing class in Communicate server.
    This is a thread so that the client can continusly listen to
    any changes/ or messages sent from the server*/
    idle = communicateSrv.new communicateServerThread(this);
    idle.start();
  }//constructor

 /** This method is called from within the constructor to
  * initialize the form.*/
 @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    clientLogoPNG = new javax.swing.JLabel();
    whoIsUserLbl = new javax.swing.JLabel();
    statusLbl = new javax.swing.JLabel();
    fullNameLbl = new javax.swing.JLabel();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    tabContactsPanel = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    contactList = new JList(entriesVector);
    addButton = new javax.swing.JButton();
    remButton = new javax.swing.JButton();
    drawCallButton = new javax.swing.JToggleButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle("eLearningMaps - Client Screen");
    setLocationByPlatform(true);
    setResizable(false);

    clientLogoPNG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/logo.png"))); // NOI18N

    whoIsUserLbl.setText("Username: ");

    statusLbl.setText("Status:");

    fullNameLbl.setText("Fullname  :");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(clientLogoPNG)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(statusLbl))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(whoIsUserLbl))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(fullNameLbl)))
        .addContainerGap(115, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addComponent(clientLogoPNG)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(fullNameLbl)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(whoIsUserLbl)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(statusLbl)
        .addContainerGap())
    );

    contactList.setCellRenderer(new ContactsCellRenderer());
    jScrollPane1.setViewportView(contactList);

    javax.swing.GroupLayout tabContactsPanelLayout = new javax.swing.GroupLayout(tabContactsPanel);
    tabContactsPanel.setLayout(tabContactsPanelLayout);
    tabContactsPanelLayout.setHorizontalGroup(
      tabContactsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabContactsPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
        .addContainerGap())
    );
    tabContactsPanelLayout.setVerticalGroup(
      tabContactsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabContactsPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        .addContainerGap())
    );

    jTabbedPane1.addTab("Contacts", tabContactsPanel);

    addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/add_contact.png"))); // NOI18N
    addButton.setToolTipText("Add Contact");
    addButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addButtonActionPerformed(evt);
      }
    });

    remButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/rem_contact.png"))); // NOI18N
    remButton.setToolTipText("Remove Contact");
    remButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        remButtonActionPerformed(evt);
      }
    });

    drawCallButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/draw_btn.png"))); // NOI18N
    drawCallButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/draw_btn2.png")));
    drawCallButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        drawCallButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
        .addContainerGap())
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(drawCallButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(remButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(19, 19, 19))
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(drawCallButton, 0, 0, Short.MAX_VALUE)
          .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
          .addComponent(remButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, Short.MAX_VALUE))
        .addGap(22, 22, 22))
    );

    jTabbedPane1.getAccessibleContext().setAccessibleName("Contacts");

    pack();
  }// </editor-fold>//GEN-END:initComponents

 /************************************************************************
  * This method will trigger the contruction of another UI window for
  * the user to select a contact to add to his contact list. The contact
  * list will be retrieved from the server at the time the user request
  * it, so as to get the most updated list of contacts.
  *
  * @param evt
  */
 private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
  /*Request the list from the server*/
  communicateSrv.doContacts();
 }//GEN-LAST:event_addButtonActionPerformed

 /****
  * User select users and remove them from the contact list
  * 
  * @param evt
  */
 private void remButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remButtonActionPerformed
  int [] selected = contactList.getSelectedIndices();
  //System.out.println("Selected Elements:  " + selected.length);

  for (int i = 0; i < selected.length; i++) {
    ListUserEntry element = (ListUserEntry) contactList.getModel()
         .getElementAt(selected[i]);
    //System.out.println("You want to remove : " + element.getUserName());
    this.remContact(element.getContact());
   }//end for
 }//GEN-LAST:event_remButtonActionPerformed

 private void drawCallButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawCallButtonActionPerformed

 /*Check if at least one contact is online*/
 boolean atLeastOne = false;
 
 int [] selected = contactList.getSelectedIndices();

 /*If button is pressed*/
 if (this.drawCallButton.isSelected()) {


  /* > Call the method to start the new Server and transform client to both
   server and client. The current */
   if (selected.length > 0) {

     /*Check if at lease one of the selectec contacts is alive*/
     for (int j = 0; j < selected.length; j++) {
       ListUserEntry testElmnt = (ListUserEntry) contactList.getModel()
         .getElementAt(selected[j]);
       if (testElmnt.getContact().isActive().equals("Active")) {

         atLeastOne = true;

         break;
       }//end if at lease one contact is alive
     }//end for
   }//end if some contact is selected


   /*2 Cases*/
   if (selected.length > 0 && atLeastOne) {
     /*If true start the server*/
     transformClient(true,selected);    
   }//end if
   else {
     this.drawCallButton.setSelected(false);
   
     /*If thread is alive stop it*/
     if (serverThread != null) {
      transformClient(false,selected);
    }//end if
   }//else
 }//end button is selected
 else {
   /*If thread is alive (mean is not null) stop it*/
    if (serverThread != null) {

     /*Before closing the server, sent a message to all clients,
      that the server is closed*/
      serverThread.serverShutDown();
     
      transformClient(false,selected);

     }//end if
  }
 }//GEN-LAST:event_drawCallButtonActionPerformed

 
 /************************************************************************
  * This method will be called by the drawButtonActionPerformed() method. The
  * method is responsible to transform a a client to both Server and Client
  * mode. In simple words this method will start the new Thread.
  * 
  * @param s
  */
public void transformClient(boolean s, int [] sel) {
  /*String will hold all usernames that are active and will
   participate to the conference call*/
   String usersToCall = "";

 try {
  if (s) {
    
       //System.out.println("Transform Server Started");
       //System.out.println("On port: " + client_console.getNewServerPort());

       /*Create a new Server Socket - on port 3081*/
       serverSocket = new ServerSocket(client_console.getNewServerPort());

       /*(1) Create and start server thread*/
       serverThread = new ServerThread(serverSocket);

       /*(2) Start Server */
       serverThread.start();

       /*(3) After starting the server. Pass through the list of
        selected users. Those that are alive send details to connect
        to the new server*/
       for (int i = 0; i < sel.length; i++) {
         ListUserEntry element = (ListUserEntry) contactList.getModel()
                                                    .getElementAt(sel[i]);
        if (element.getContact().isActive().equals("Active")) {
          /*Create a string with all usernames to participate*/
          usersToCall += element.getContact().getUserName() + "?";
        }//end if
      }//end for

       /*Now that program know users to draw with send them to the OLDSERVER
        * 
        *   Note: for the IP send the global IP using method to find it
        *         or send the localip address
        */

       /*(1) Determine which IP address to use
        *      - Always use the Global IP address to connect
        *      - Otherwise use 0.0.0.0
        */
       String readIP = getPublicIPAddress();

       /*If error change readIP to this users localhost*/
       if (readIP.equals("ERROR") || (client_console.getLocalNetworkCheck())) {
         readIP =  serverSocket.getInetAddress().getHostAddress();
         
       }//end if

       //System.out.println("NEW SERVER IP IS: " + readIP);

       /*(2) Call method to do the conference*/
       this.communicateSrv.doConference(usersToCall,user_obj.getUserName(),
                                        client_console.getNewServerPort(),readIP);

       /*(3) Start the GUI for collaborative drawing*/
      isServer = new DrawConsoleUISyn("SERVER",this);
      /*Send : - send the User object for this client
               - the port
               - the localIP*/
      isServer.startUIasServer(user_obj,
                               usersToCall,
                               client_console.getNewServerPort(),
                               serverSocket.getInetAddress().getHostAddress());

     }//end if
    else {
       /*Stop thread*/
       serverThread.stop();

       /*Close server socket*/
       serverSocket.close();

       /*Set GUI to false*/
       isServer.setVisible(false);

       /*Clear the thread*/
       serverThread = null;

       /*Make sure you clear server socket*/
       serverSocket = null;

       /*Set Gui variable to null*/
       isServer = null;

     }//end else
    }//try
   catch (Exception exe) {
      //exe.printStackTrace();
    }//end
}//end method



 /********************************************************************
  * This method will get the contact list for this User. It will then
  * pass through the contact list and find out which users are active
  * and which are not. Finally it will create a ListUserEntry object
  * for each contact and add it to the Vector. The vector will be used
  * to generate the JList.
  */
 public void createListEntries(User aUser) {

  /*String to hold the icon path*/
  String path = "";

  /*Get the contact list from this User object*/
   ArrayList contacts = aUser.getContactList();

  /*For each element in the contact list, create a JList entry*/
   for (int i=0; i<contacts.size(); i++) {

    /*Get the user for that index*/
    User contact_user = (User) contacts.get(i);

    /*Check is user is active or inactive to determine the icon to use*/
    if (contact_user.isActive().equals("Active")) {
      path = "/elearningmaps/ico/active.png";
     }
    else if (contact_user.isActive().equals("Inactive")) {
      path = "/elearningmaps/ico/inactive.png";
     }

    /**
     * Create an entry for this Contact in the JList.
     * Parameters are :
     *   (1) contact_user = used to retrive extra information about the user
     *       like ip address, username e.t.c
     *   (2) the username = shown in the JList
     *   (3) icon path = for the icon
     */
     entriesVector.add(new ListUserEntry(contact_user, contact_user.getUserName(), path));
   }//end for

 }//end

 /*Method used to update the list*/
 public void updateList(User u) {
  /*Update the client object with the new one*/
   this.user_obj = u;

  /*Clear the vector*/
  this.entriesVector.clear();

  /*create the list of contacts*/
  this.createListEntries(u);

  /*update the list of contacts*/
  contactList.setListData(entriesVector);
 }//end

 /****
  * This method just return the current user object
  * 
  * @return
  */
 public User getUser() {
   return this.user_obj;
 }//end method


 /******************************************************************
  * Add a contact to this user
  */
 public void addContact(User addU) {
  
   if (user_obj.addContact(addU)) {
    createWarningDialog("User added.");
   }
   else
    createWarningDialog("User already a contact.");

   /*Send change to the Server and update the user object*/
   this.communicateSrv.addContact(user_obj);
 }//end if

 /******************************************************************
  * Remove contact from this user
  */
 public void remContact(User remU) {
  /*Get the contact list from this User object*/
  ArrayList contacts = user_obj.getContactList();

  for (int i=0; i<contacts.size(); i++) {

    /*Get the user for that index*/
    User contact_user = (User) contacts.get(i);

    if (contact_user.getUserName().equals(remU.getUserName()))
    {
      /*remove user from the list*/
      contacts.remove(i);
      break;
    }//end if
  }//end for

   /*Send change to the Server and update the user object*/
   this.communicateSrv.addContact(user_obj);
 }//end method

 /*******************************************************************
  * This method will hold a reference to the list of all contacts
  * as sent by the Server
  */
public void showList(List l) {
  allContacts = l;

  /*call the ClientAddContactUI*/
  if (this.allContacts != null) {
   ClientAddContactUI addContactUI = new ClientAddContactUI(this, allContacts);
   addContactUI.setVisible(true);
  }//end if
}//end method

/*********************************************************************
 * 
 */
public void showDrawConsoleUI(MessageObject m) {

  /*If this user has not created any New Server then allow it to connect*/
  if (serverSocket == null) {
  /*Popup a message asking this user if wants to participate the session*/
   String popUpMsg = "User: " + m.getHostClient() + " wants to draw with you.\n\n" +
     "Accept the invitation?" ;

   int confirm = JOptionPane.showConfirmDialog
  	(null,popUpMsg,"eLearningMaps - Session invitation.",JOptionPane.YES_NO_OPTION);

   //if yes button then the user accepts
   if (confirm == JOptionPane.YES_OPTION)
    {
      /*If invitation is accepted, disable "draw call" button*/
      drawCallButton.setEnabled(false);

      /*Call method and show GUI*/
      DrawConsoleUISyn isClient = new DrawConsoleUISyn("CLIENT",this);

      /*Start a new */
      isClient.startUIasClient(m,this.user_obj);

     
    }//end if
  }//end check if has already create a New Server
}//end method

 /***
  * Method used by when checkbox "non-local IP" is selected. The method
  * is responsible of finding the local IP address of
  *
  * @return
  */
 public String getPublicIPAddress(){
  try {
   URL findIPUrl = new URL("http://www.whatismyip.com/automation/n09230945.asp");
   BufferedReader read = new BufferedReader( new InputStreamReader(findIPUrl.openStream()));

   /*Read IP Address*/
   String readIPAddress = (read.readLine()).trim();

   return readIPAddress;

  } catch (Exception exe) {
    System.out.println("ServerConsole - Cannot retreive IP address from URL.");
   return "ERROR";
  }//catch
 }//end method


 /*******************************************************************
  * This method will be called when the user wants to quit the
  * application
  */
 private void quitMethod() throws InterruptedException
  {
   //Setup msg appear to the dialog window
   String popUpMsg = "Quit eLearningMaps Client?" ;

   int confirm = JOptionPane.showConfirmDialog
  	(null,popUpMsg,"eLearningMaps - Warning",JOptionPane.YES_NO_OPTION);

   //if yes button clicked exit
   if (confirm == JOptionPane.YES_OPTION)
    {
     //Release the thread
     idle = null;

     /*Check if a communication with the server is made*/
     if (communicateSrv != null ) {
      
       /*Also shutdown all connections to the NEW SERVER*/
       if (serverThread != null) {
         /*shutdown server*/
         serverThread.serverShutDown();
       }

       /*Shutdown the THREAD*/
       communicateSrv.doShutDown();

       /*Add a delay*/
       Thread.sleep(450);
       
       /*Finally quit the application*/
       communicateSrv.doQuit();
     }//end if

     //Exit application close all windows
     System.exit(0);
    }//end if
  }//end quit method


  /********************************************************************
  * This method will be used to create a popUp menu each time an error
  * occurs.
  */
 private void createWarningDialog(String warn)
 {
   JOptionPane.showMessageDialog(this,"eLearningMaps - " + warn);
 }//end

 /*********************************************************************
  * Method to set the drawCallButton enable/disable
  */
public void setDrawCallBtn(boolean b) {
  this.drawCallButton.setEnabled(b);
}

/***
 * Supporting method to return the Server thread
 * 
 * @return
 */
public ServerThread getServerThread() {
  return this.serverThread;
}


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton addButton;
  private javax.swing.JLabel clientLogoPNG;
  private javax.swing.JList contactList;
  private javax.swing.JToggleButton drawCallButton;
  private javax.swing.JLabel fullNameLbl;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JButton remButton;
  private javax.swing.JLabel statusLbl;
  private javax.swing.JPanel tabContactsPanel;
  private javax.swing.JLabel whoIsUserLbl;
  // End of variables declaration//GEN-END:variables

/*************************************************************************
 *                       INNER CLASS                                     *
 *  Custom JList Renderer                                                *
 *                                                                       *
 * This inner class is used to create a custom Listmodel for the JList   *
 * that is responsibel to show the contacts of each client.              *
 *                                                                       *
 *************************************************************************/
class ContactsCellRenderer extends JLabel implements ListCellRenderer {
 private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

 /*Constructor*/
 public ContactsCellRenderer() {
  setOpaque(true);
  setIconTextGap(12);
 }//end

 public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

  ListUserEntry entry = (ListUserEntry) value;
  setText(entry.getUserName());
  setIcon(entry.getImage());

  if (isSelected) {
   setBackground(HIGHLIGHT_COLOR);
   setForeground(Color.white);
   }
  else {
   setBackground(Color.white);
   setForeground(Color.black);
   }//else

   return this;
  }//end method
 }//end inner class
}//end class