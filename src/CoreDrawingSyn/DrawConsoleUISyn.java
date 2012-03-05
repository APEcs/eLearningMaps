package CoreDrawingSyn;

import CoreClient.ClientScreenUI;
import CoreClient.ListUserEntry;
import CoreDrawing.CoreShapes.ShapesInterface;
import elearningmaps.MessageObject;
import elearningmaps.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/******************************************************************
 * DrawConsoleUISyn.java
 *
 * This class will represent the drawing Canvas of the application.
 * A user will choose a set of other users to draw with. This UI
 * is representing a common canvas where users can draw in a
 * collaborative manner.
 *
 * NOTE: Identical class but used for Synchronous drawing.
 *
 * @author  Klitos Christodoulou
 * christk6@cs.man.ac.uk
 *
 * Created on 16-Jun-2010, 10:59:07
 */


public class DrawConsoleUISyn extends javax.swing.JFrame {

  /***
   * Variable Declarations
   */
  /*For the JList component*/
  private Vector entriesVector = new Vector();

  /*Select the number of the selected tool*/
  private int SELECTEDTOOL          = 0;    /*Default tool now*/
  private final int CONCEPTTOOL     = 1;    /*Draw Shape Concepts*/
  private final int PROPOSITIONTOOL = 2;    /*Proposition Tool*/
  private final int NOTESTOOL       = 3;    /*Insert Sticky note Tool*/
  private final int IMAGETOOL       = 4;    /*Add Image from web Tool*/

  private ServerSocket socketServer = null;
  private MessageObject conferenceMsgObj = null;

  /*This Object will be used to communicate with the NEW server*/
  private  CommunicateServerSyn  communicateNewServer = null;
  
  /*Two kinds of mode for this Window (CLIENT / SERVER)*/
  private String MODE = "";
  private ClientScreenUI client_serv_gui= null;

  /*Variable to hold User object*/
  private String userName = null;
  private String userEmail = null;

  /*Hold a random colour for each window, use it for the Chat system*/
  private Color chatColor = null;

  private String stringOfParticipants = "";


  /****
   * Constructor 1
   *
   * Purpose : The purpose of this constructor is to create a
   * Client Draw console. This will be checked by the variable
   */
  public DrawConsoleUISyn(String modeIs, ClientScreenUI client_serv) {
   initComponents();

   /*Set the mode*/
   MODE = modeIs;

   /*Strore a reference to the previous window (ClientScreenUI.java)*/
   client_serv_gui = client_serv;

   /*Each time a DrawConsoleUISyn is created generate a random colour*/
   chatColor =  new Color ((int)(Math.random() * 255),
                           (int)(Math.random() * 255),
                           (int)(Math.random() * 255));
   
   /*Place JFrame in the middle of the screen*/
   this.setLocationRelativeTo(null);

   /*Replace window listener*/
   this.addWindowListener(new WindowAdapter()
    {
     @Override
     public void windowClosing( WindowEvent event) {
      try {
        quitMethod();
      } catch (InterruptedException ex) {
          System.out.println("DrawConsoleUI - Exception while Quit");
      }
    }//end WindowEvent
   });//end WindowListener

  }//end constructor


  /*Construct the UI*/
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    topPanel = new javax.swing.JPanel();
    tabsPanel = new javax.swing.JTabbedPane();
    toolsTab = new javax.swing.JPanel();
    jToolBar1 = new javax.swing.JToolBar();
    conceptButton = new javax.swing.JToggleButton();
    linkButton = new javax.swing.JToggleButton();
    notesButton = new javax.swing.JToggleButton();
    imageButton = new javax.swing.JToggleButton();
    webLinkButton = new javax.swing.JButton();
    jSeparator1 = new javax.swing.JToolBar.Separator();
    deleteButton = new javax.swing.JButton();
    saveButton = new javax.swing.JButton();
    historyPanel = new javax.swing.JPanel();
    mainPanel = new javax.swing.JPanel();
    canvasScrollPane = new javax.swing.JScrollPane();
    canvasPanel = new DrawCanvasSyn(this);
    chatPanel = new javax.swing.JPanel();
    chatTextField = new javax.swing.JTextField();
    sendButton = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    chatTextArea = new javax.swing.JTextArea();
    propertiesPanel = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    partyList = new javax.swing.JList(entriesVector);
    jLabel2 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle("eLearningMaps - Drawing Console");
    setLocationByPlatform(true);

    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    conceptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/concept.png"))); // NOI18N
    conceptButton.setText("Concept");
    conceptButton.setToolTipText("Add a concept");
    conceptButton.setEnabled(false);
    conceptButton.setFocusable(false);
    conceptButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    conceptButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    conceptButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        conceptButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(conceptButton);

    linkButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/link.png"))); // NOI18N
    linkButton.setText("Join");
    linkButton.setToolTipText("Form Proposition");
    linkButton.setEnabled(false);
    linkButton.setFocusable(false);
    linkButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    linkButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    linkButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        linkButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(linkButton);

    notesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/notes.png"))); // NOI18N
    notesButton.setText("Sticky Note");
    notesButton.setToolTipText("Add a comment");
    notesButton.setEnabled(false);
    notesButton.setFocusable(false);
    notesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    notesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    notesButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        notesButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(notesButton);

    imageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/add_image.png"))); // NOI18N
    imageButton.setText("Add Image");
    imageButton.setToolTipText("Insert Image");
    imageButton.setEnabled(false);
    imageButton.setFocusable(false);
    imageButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    imageButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    imageButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        imageButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(imageButton);

    webLinkButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/add_link.png"))); // NOI18N
    webLinkButton.setText("Add Link");
    webLinkButton.setToolTipText("Insert Web Link");
    webLinkButton.setEnabled(false);
    webLinkButton.setFocusable(false);
    webLinkButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    webLinkButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    webLinkButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        webLinkButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(webLinkButton);
    jToolBar1.add(jSeparator1);

    deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/del_btn.png"))); // NOI18N
    deleteButton.setText("Erase");
    deleteButton.setToolTipText("Delete");
    deleteButton.setEnabled(false);
    deleteButton.setFocusable(false);
    deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    deleteButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        deleteButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(deleteButton);

    saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/export.png"))); // NOI18N
    saveButton.setText("Export");
    saveButton.setToolTipText("Export Canvas");
    saveButton.setEnabled(false);
    saveButton.setFocusable(false);
    saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    saveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveButtonActionPerformed(evt);
      }
    });
    jToolBar1.add(saveButton);

    javax.swing.GroupLayout toolsTabLayout = new javax.swing.GroupLayout(toolsTab);
    toolsTab.setLayout(toolsTabLayout);
    toolsTabLayout.setHorizontalGroup(
      toolsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
    );
    toolsTabLayout.setVerticalGroup(
      toolsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(toolsTabLayout.createSequentialGroup()
        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabsPanel.addTab("Tools", toolsTab);

    javax.swing.GroupLayout historyPanelLayout = new javax.swing.GroupLayout(historyPanel);
    historyPanel.setLayout(historyPanelLayout);
    historyPanelLayout.setHorizontalGroup(
      historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 441, Short.MAX_VALUE)
    );
    historyPanelLayout.setVerticalGroup(
      historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 72, Short.MAX_VALUE)
    );

    tabsPanel.addTab("History", historyPanel);

    javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
    topPanel.setLayout(topPanelLayout);
    topPanelLayout.setHorizontalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(tabsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
    );
    topPanelLayout.setVerticalGroup(
      topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(topPanelLayout.createSequentialGroup()
        .addComponent(tabsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    mainPanel.setBackground(new java.awt.Color(153, 255, 153));

    canvasScrollPane.setAutoscrolls(true);

    javax.swing.GroupLayout canvasPanelLayout = new javax.swing.GroupLayout(canvasPanel);
    canvasPanel.setLayout(canvasPanelLayout);
    canvasPanelLayout.setHorizontalGroup(
      canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 511, Short.MAX_VALUE)
    );
    canvasPanelLayout.setVerticalGroup(
      canvasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 455, Short.MAX_VALUE)
    );

    canvasScrollPane.setViewportView(canvasPanel);

    javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
    mainPanel.setLayout(mainPanelLayout);
    mainPanelLayout.setHorizontalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(canvasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
    );
    mainPanelLayout.setVerticalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(canvasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
    );

    chatTextField.setText("Chat here");

    sendButton.setText("Send ..");
    sendButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sendButtonActionPerformed(evt);
      }
    });

    chatTextArea.setColumns(20);
    chatTextArea.setEditable(false);
    chatTextArea.setLineWrap(true);
    chatTextArea.setRows(5);
    chatTextArea.setToolTipText("Chat Area...");
    jScrollPane1.setViewportView(chatTextArea);

    javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
    chatPanel.setLayout(chatPanelLayout);
    chatPanelLayout.setHorizontalGroup(
      chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(chatPanelLayout.createSequentialGroup()
        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(chatPanelLayout.createSequentialGroup()
            .addComponent(chatTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(sendButton))
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
        .addContainerGap())
    );
    chatPanelLayout.setVerticalGroup(
      chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(chatPanelLayout.createSequentialGroup()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(chatTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(sendButton)))
    );

    partyList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    partyList.setCellRenderer(new WaitCellRenderer());
    jScrollPane2.setViewportView(partyList);

    jLabel2.setText("Session Participants :");

    javax.swing.GroupLayout propertiesPanelLayout = new javax.swing.GroupLayout(propertiesPanel);
    propertiesPanel.setLayout(propertiesPanelLayout);
    propertiesPanelLayout.setHorizontalGroup(
      propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(propertiesPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel2)
        .addContainerGap(242, Short.MAX_VALUE))
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, propertiesPanelLayout.createSequentialGroup()
        .addContainerGap(72, Short.MAX_VALUE)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(80, 80, 80))
    );
    propertiesPanelLayout.setVerticalGroup(
      propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, propertiesPanelLayout.createSequentialGroup()
        .addContainerGap(17, Short.MAX_VALUE)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/logo.png"))); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(propertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(chatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addContainerGap())))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel1)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(chatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(propertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
          .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

 /***
  * Start this GUI as A SERVER.
  *
  */
 public void startUIasServer(User user_obj, String participants,
                                                    int port, String localIP) {
  try {
   if (MODE.equals("SERVER")) {

      /*Set the title to show mode*/
     this.setTitle(this.getTitle()+" | Mode is : " + this.MODE
                  + " , Username : " +  user_obj.getUserName());

     /*Update the string of participants for this session*/
     stringOfParticipants = participants;

     /*Update the username variable*/
     userName = user_obj.getUserName();

     /*Update the email variable*/
     userEmail = user_obj.getEmail();

      /*(1)  Start connection a new thread to connecti with the server,
           Create a new object of CommunicateServerSyn Thread*/
      communicateNewServer = new CommunicateServerSyn(localIP, port,
                                                           user_obj,
                                             user_obj.getUserName(),
                                                               this);

      //(2) Start the Thread
      communicateNewServer.start();

     /*(2) Show the GUI*/
     this.setVisible(true);
   }//end SERVER mode
  } catch (Exception e) {
    this.createWarningDialog("ERR01 - Cannot start session.");
  }//catch
 }//end method


 /***
  * USED BY THE CLIENTS.
  * 
  * Clients can participate only in one session at a time.
  * This method will check whether this client already participate 
  * in a session.
  * 
  *  Return:  true  - if participate
  *           false - if not participate 
  * 
  * @param mo
  * @return
  */
 public void startUIasClient(MessageObject mo, User user_obj) {
  try {
   if (conferenceMsgObj == null && MODE.equals("CLIENT")) {

     /*(1) Set the title to show mode*/
     this.setTitle(this.getTitle()+" | Mode is : " + this.MODE
                  + " , Username : " +  user_obj.getUserName());

     /*Update the string of participants for this session*/
     stringOfParticipants = mo.getSessionParticipants();

     /*Update the username variable*/
     userName = user_obj.getUserName();

      /*Update the email variable*/
     userEmail = user_obj.getEmail();

     /*(2) Set messageobject to that MessageObject*/
     conferenceMsgObj = mo;

     /*(3) Start connection a new thread to connecti with the server*/
     communicateNewServer = new CommunicateServerSyn(mo.getHostIP(),
                                                   mo.getHostPort(),
                                                           user_obj,
                                                 mo.getHostClient(),
                                                               this);

     /*(4) Start Thread & Show the GUI*/
     communicateNewServer.start();
     this.setVisible(true);

   } else {
     System.out.println("Cannot participate in more that one sessions");
   }//else
  } catch (Exception e) {
    this.createWarningDialog("ERR02 - Cannot start session.");
  }//catch
 }//end method

 /****
  * This method returns the MessagObject for this client
  *
  * @return
  */
 public MessageObject getMesObjForThisClient() {
   return conferenceMsgObj;
 }//end method


 /******
  * This is the draw concept tool
  *
  * @param evt
  */
 /******
  * This is the draw LINK (PROPOSITION TOOL)
  *
  * @param evt
  */
  private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed

    ShapesInterface member = this.getDrawCanvas().getSelectedShape();

    if (member != null && member.getWaitColour().equals("g")) {

      /*Use MessageObject of "lockrequest"*/
      MessageObject delObj = new MessageObject("deleteshape",
                                                member.getShapeUniqueID(),
                                                this.getUserName());

      /*Forward message to server*/
      sendMessageToServer(delObj);

    }//end if

    /*Set member back to null*/
    this.getDrawCanvas().clearSelectedShape();
  }//GEN-LAST:event_deleteButtonActionPerformed

  /***
   * This button will enable the notes mode. The user can stick
   * notes into the Canvas JPanel.
   * 
   * @param evt
   */
  /***
   * Method : CHAT SYSTEM
   * 
   * When the user hit the chat button :
   *  - Get the text from the chat box
   *  - Wrap the text in a MessageObject and send it to the server
   *  - Sent it to the users in that session
   * 
   * @param evt
   */
  private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed

   /*Message to send from textField*/
   String messageToSend = chatTextField.getText().trim();

   if (! messageToSend.equals("")) {
      /*Create a new MessageObject each time user wants to chat*/
     MessageObject chatObj = new MessageObject("chat",messageToSend,
                                               this.getUserName(),
                                                this.chatColor);

     /*Clear textbox, for the new message to enter*/
     chatTextField.setText("");

     /*Forward message to server*/
     sendMessageToServer(chatObj);
    }//end if
  }//GEN-LAST:event_sendButtonActionPerformed

   /***
   * This button will enable the notes mode. The user can stick
   * notes into the Canvas JPanel.
   *
   * @param evt
   */
  private void conceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conceptButtonActionPerformed
    updateTools(SELECTEDTOOL);
    /*Set the draw tool from default to CONCEPTTOOL*/
   this.SELECTEDTOOL = CONCEPTTOOL;

   /*If toggle is false then set SelectedTool back to the defauld state*/
   if (!this.conceptButton.isSelected()) {
      this.SELECTEDTOOL = 0;
   }//end if
  }//GEN-LAST:event_conceptButtonActionPerformed


 /******
  * This is the draw LINK (PROPOSITION TOOL)
  *
  * @param evt
  */
  private void linkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkButtonActionPerformed
   updateTools(SELECTEDTOOL);
   this.SELECTEDTOOL = PROPOSITIONTOOL;

   /*If toggle is false then set SelectedTool back to the defauld state*/
   if (!this.linkButton.isSelected()) {
      this.SELECTEDTOOL = 0;
   }//end if
  }//GEN-LAST:event_linkButtonActionPerformed

  
 /***
  * Set draw tool to notes tool
  * @param evt
  */
  private void notesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notesButtonActionPerformed
   updateTools(SELECTEDTOOL);
   this.SELECTEDTOOL = NOTESTOOL;

   /*If toggle is false then set SelectedTool back to the defauld state*/
   if (!this.notesButton.isSelected()) {
      this.SELECTEDTOOL = 0;
   }//end if
  }//GEN-LAST:event_notesButtonActionPerformed

  
 /***
  * Supporting method to return the canvas
  *
  */
public DrawCanvasSyn getDrawCanvas() {
 return  (DrawCanvasSyn) this.canvasPanel;
}//end if


 /****
  * WEB LINK BUTTON
  *
  * @param evt
  */
  private void webLinkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webLinkButtonActionPerformed
    /*Update the tool bar*/
    updateTools(SELECTEDTOOL);
    this.SELECTEDTOOL = 0;

    /*Display a JPopup menu with the PanelLink attach to*/
    JPopupMenu popupMenu = new JPopupMenu();
    popupMenu.add(new PanelLinkSyn((DrawCanvasSyn) this.canvasPanel));

    /*Set Popmenu in the exact position just under the button*/
    popupMenu.show(this,this.webLinkButton.getX()+13,
                        this.webLinkButton.getY()+113);
  }//GEN-LAST:event_webLinkButtonActionPerformed

  
  /***
  * Action for Add image button.
  *
  * @param evt
  */
  private void imageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageButtonActionPerformed
  updateTools(SELECTEDTOOL);
   this.SELECTEDTOOL = IMAGETOOL;

 if (this.imageButton.isSelected()) {
   /*Display a JPopup menu with the PanelLink attach to*/
   JPopupMenu popupMenu = new JPopupMenu();
   popupMenu.add(new PanelImageSyn((DrawCanvasSyn) this.canvasPanel));

   /*Set Popmenu in the exact position just under the button*/
   popupMenu.show(this,this.imageButton.getX()+13,
                       this.imageButton.getY()+113);


   /*If toggle is false then set SelectedTool back to the default state*/
  } else if (!this.imageButton.isSelected()) {
      this.SELECTEDTOOL = 0;
   }//end if
  }//GEN-LAST:event_imageButtonActionPerformed

  
  /***
   *  
   * Main top menu -> Export
   * 
   * @param evt
   */
  private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
 JFileChooser fileChooser = new JFileChooser();

 /*Add custom filter for files*/
 fileChooser.addChoosableFileFilter(new PNGFilter());

 /*Show save dialog*/
 int returnVal = fileChooser.showSaveDialog(this);

 /*If save button is clicked*/
 if(returnVal == JFileChooser.APPROVE_OPTION) {
  try
  {
   /**
    * Call mehtod to store file into specific location
    */
   String filePath = "";

   /**
    * Check if the filename has .png appended at the end
    **/
    if (fileChooser.getSelectedFile().getName().matches(".*\\.png"))
     {
      filePath = fileChooser.getSelectedFile().getPath();
     }//end if
    else /*if .png is not appened.Then add it at the end*/
     {
      filePath = fileChooser.getSelectedFile().getPath()+".png";
     }//end else

    /*Create the new file*/
    File fileToSave = new File(filePath);

    /*Check if the file already exists*/
    if (fileToSave.exists())
    {
     int confirm = JOptionPane.showConfirmDialog
	 (null,"The filename '"+filePath+"' already exists." +
             "\nDo you want to replace it?",
                        "eLearningMaps - Option Confirm",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
	 //if yes button clicked exit
	 if (confirm == JOptionPane.YES_OPTION)
	  {

       /***
        * This means that the user wants to override the selected
        * file. Firstly delete the file
        ***/
        this.getDrawCanvas().takeSnapShot(fileToSave);
	  }//end if
     }//end if file to save exists
    else
     {
      this.getDrawCanvas().takeSnapShot(fileToSave);
     }//end if file does not exist
   }//end try
  catch (Exception exe) {
   JOptionPane.showMessageDialog(this,"eLearningMaps output stream error.");
  }//end catch
 }//end if
  }//GEN-LAST:event_saveButtonActionPerformed


    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
        try {
          UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
          Logger.getLogger(DrawConsoleUISyn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
          Logger.getLogger(DrawConsoleUISyn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(DrawConsoleUISyn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
          Logger.getLogger(DrawConsoleUISyn.class.getName()).log(Level.SEVERE, null, ex);
        }
            new DrawConsoleUISyn("SERVER",null).setVisible(true);
            }
        });
    }

/***
 * This method returns the selected tool ID
 *
 * @return
 */
public int getSelectedToolID() {
  return this.SELECTEDTOOL;
}//end method

/***
 * Change the Default TOOL
 *
 * @param id
 * @return
 */
public void setSelectedToolID(int id) {

  updateTools(SELECTEDTOOL);

  /**
   * Disable previously toggled button
   */
   if (id == 1) {
    this.conceptButton.setSelected(true);
   }
  else if (id == 2) {
    this.linkButton.setSelected(true);
   }
  else if (id == 3) {
    this.notesButton.setSelected(true);
   }
  else if (id ==4) {
   this.imageButton.setSelected(true);
  }

  this.SELECTEDTOOL = id;
}//end method


 /**
  * Set previously selected tool back to the normal icon
  */
public void updateTools(int previous){
  

  if (previous == 1) {
    this.conceptButton.setSelected(false);
  }
  else if (previous == 2) {
    this.linkButton.setSelected(false);
  }
  else if (previous == 3) {
    this.notesButton.setSelected(false);
  }
  else if (previous == 4) {
   this.imageButton.setSelected(false);
  }
}//end method


/********************************************************************
 * This method will return the scrollPane of th GUI
 */
public JScrollPane getScrollPane() {
  return this.canvasScrollPane;
}

   /******************************************************************
  * This method will be called when the user wants to quit the       *
  * application                                                      *
  ********************************************************************/
 private void quitMethod() throws InterruptedException
  {
   //Setup msg appear to the dialog window
   String popUpMsg = "Quit eLearningMaps Client?" ;

   int confirm = JOptionPane.showConfirmDialog
  	(null,popUpMsg,"eLearningMaps - Warning",JOptionPane.YES_NO_OPTION);

   //if yes button clicked exit
   if (confirm == JOptionPane.YES_OPTION)
    {
     /*Check if a communication with the server is made*/
     if (communicateNewServer != null) {

       if (MODE.equals("SERVER")) {
        this.client_serv_gui.getServerThread().serverShutDown();
       }
       else if (MODE.equals("CLIENT")) {
        /*Call the doQuit method*/
        communicateNewServer.doQuit(this.MODE);
       }

     }//end if

     //Exit application close all windows
     this.MODE="";
     this.conferenceMsgObj=null;
     this.setVisible(false);
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

 /***
  * When the while loop stops it means that client is disconnected so
  */
public void showQuitMessage() {

  this.createWarningDialog("Connection to server dropped");
  this.setVisible(false);
  this.client_serv_gui.setDrawCallBtn(true);  

}//end method

/***
 * This method will return the username of this user.
 * 
 * @return
 */
public String getUserName() {
 if (userName != null)
  return this.userName;
 else
  return "TestUser";
}//end getUserName

/***
 * This method will return the email address of this user.
 *
 * @return
 */
public String getUserEmailAdd() {
 if (userEmail != null)
  return this.userEmail;
 else
  return "email@domain";
}//end getUserName


/*********************************************************************
 * SECTION: SYNCHRONISATION MANAGER                                  *
 *                                                                   *
 * This section will enclose all the methods necessary to            *
 * implement the synchronisation manager needed for the collaborate  *
 * drawing.                                                          *
 *                                                                   *
 *********************************************************************/


 /***
  * Method used to construct the Chat window
  *
  * @param mo
  */
 public void constructChatWindow(MessageObject mo){

  /*Use this instead, to autoscroll JTextArea*/
  chatTextArea.append(mo.getChatSenderUsrname()
                          + " > : " +mo.getChatMsg() +"\n");

  /*Update the Scrollbar down*/
  chatTextArea.setCaretPosition(chatTextArea.getText().length());

 // chatTextArea.setText( chatTextArea.getText() + mo.getChatSenderUsrname()
   //                      + " > : " +mo.getChatMsg() +"\n" );

  
 }//end method

 /***
  * This method is the way of communicating with the server. The method
  * is responsible of accessing the CommunicateClientSyn method which is
  * responsible of sending messages to the server.
  * 
  * - Also this method will be used by the canvas.
  *
  * @param obj
  */

 public synchronized void sendMessageToServer(Object obj) {
   if (communicateNewServer != null)
     this.communicateNewServer.sendMessage(obj);

   notifyAll();
 }//end method


 /**
  * This method acts as a Linkage meachanism to the canvas. The
  * canvas will be responsible to create the new shape and add 
  * it to the JPanel.
  *
  */
 public void forwardMOtoCanvas(MessageObject mo ) {
    try {
      ((DrawCanvasSyn) this.canvasPanel).createShape(mo);
    } catch (MalformedURLException ex) {
      System.out.println("DrawConsoleUISyn - Exception");
    }//catch
 }//end method

 /*Forward: "lockrequest"*/
 public void forwardLocktoCanvas(MessageObject mo ) {
   ((DrawCanvasSyn) this.canvasPanel).grandPrivilage(mo);
 }

 /*Forward: "descriptionchange"*/
 public void forwardTranslateToCanvas(MessageObject mo ) {
   ((DrawCanvasSyn) this.canvasPanel).translateShape(mo);
 }

 /*Forward: "joinshapes"*/
 public void forwardJoinToCanvas(MessageObject mo ) {
   ((DrawCanvasSyn) this.canvasPanel).joinShapes(mo);
 }

 /*Forward: "descriptionchange"*/
 public void forwardChangeDescToCanvas(MessageObject mo ) {
   ((DrawCanvasSyn) this.canvasPanel).changeDescription(mo);
 }

 /*Forward: "deleteshape"*/
 public void forwardDELCanvas(MessageObject mo ) {
   ((DrawCanvasSyn) this.canvasPanel).deleteShapeID(mo);
 }



 
  /***
  * FOR MODE: SERVER  - Update wait list
  *
  * @param mo
  */
 public void serverUpdateWait(MessageObject mo ) {
   entriesVector.add(new ListUserEntry(mo.getLockShapeID(), "/elearningmaps/ico/active.png"));

   /*update the wait list*/
   partyList.setListData(entriesVector);

   /*A string array that hold the usernames of who is online now*/
   ArrayList<String> array1 = new ArrayList<String>();
   ArrayList<String> array2 = new ArrayList<String>();

   /*Inform other clients who is online by sending the entriesVector list*/
   for (int i=0; i<entriesVector.size(); i++){
     array1.add(((ListUserEntry)entriesVector.get(i)).getUserName());
   }//end for

   /*Send a message to them*/
   this.sendMessageToServer(new MessageObject(mo.getLockShapeID(),
                                                            array1,
                                                             1.0));

   /*Now check if the drawing must start*/
  StringTokenizer session = new StringTokenizer(stringOfParticipants,"?");
     while (session.hasMoreElements()) {
             array2.add(""+session.nextElement());
        }

   /*Compare the arrays 1 & 2*/
   if (array1.containsAll(array2)) {
     /*Now enable the controls to start drawing*/
     conceptButton.setEnabled(true);
     linkButton.setEnabled(true);
     notesButton.setEnabled(true);
     imageButton.setEnabled(true);
     webLinkButton.setEnabled(true);
     deleteButton.setEnabled(true);
     saveButton.setEnabled(true);
   }//end if
 }//end method


 /***
  * FOR MODE: CLIENT  - Update wait list
  *
  * @param mo
  */
 public void clientUpdateWait(MessageObject mo ) {

   ArrayList<String> array1 = mo.getArrayOfParticipants();
   ArrayList<String> array2 = new ArrayList<String>();

   /*Loop the array and fill the vector fot the JList*/
   entriesVector.clear();
   
   for (int i=0; i<array1.size(); i++){
      entriesVector.add(new ListUserEntry(array1.get(i), "/elearningmaps/ico/active.png"));
   }

   /*update the wait list*/
   partyList.setListData(entriesVector);

   /*Now check if the drawing must start*/
   StringTokenizer session = new StringTokenizer(stringOfParticipants,"?");

   while (session.hasMoreElements()) {
             array2.add(""+session.nextElement());
        }//end while

   /*Compare the arrays 1 & 2*/
   if (array1.containsAll(array2)) {
     /*Now enable the controls to start drawing*/
     conceptButton.setEnabled(true);
     linkButton.setEnabled(true);
     notesButton.setEnabled(true);
     imageButton.setEnabled(true);
     webLinkButton.setEnabled(true);
     deleteButton.setEnabled(true);
     saveButton.setEnabled(true);
   }//end if

 }//end method


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel canvasPanel;
  private javax.swing.JScrollPane canvasScrollPane;
  private javax.swing.JPanel chatPanel;
  private javax.swing.JTextArea chatTextArea;
  private javax.swing.JTextField chatTextField;
  private javax.swing.JToggleButton conceptButton;
  private javax.swing.JButton deleteButton;
  private javax.swing.JPanel historyPanel;
  private javax.swing.JToggleButton imageButton;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JToolBar.Separator jSeparator1;
  private javax.swing.JToolBar jToolBar1;
  private javax.swing.JToggleButton linkButton;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JToggleButton notesButton;
  private javax.swing.JList partyList;
  private javax.swing.JPanel propertiesPanel;
  private javax.swing.JButton saveButton;
  private javax.swing.JButton sendButton;
  private javax.swing.JTabbedPane tabsPanel;
  private javax.swing.JPanel toolsTab;
  private javax.swing.JPanel topPanel;
  private javax.swing.JButton webLinkButton;
  // End of variables declaration//GEN-END:variables

 /*******************************************************************
 * Inner Class
 *
 * This is an inner class extending FileFilter for use in
 * JFileChooser (PNG)
 *
 * @author Klitos Christodoulou
 */
class PNGFilter extends javax.swing.filechooser.FileFilter
{
  public boolean accept(File file)
  {
      return file.isDirectory() || file.getName().toLowerCase().endsWith(".png");
  }//end file

  public String getDescription()
  {
      return ".png files";
  }//end inner class PNGfilter

}//end inner class PNGfilter

/*************************************************************************
 *                       INNER CLASS                                     *
 *  Custom JList Renderer                                                *
 *                                                                       *
 * This inner class is used to create a custom Listmodel for the JList   *
 * that is responsibel to show the contacts of each client.              *
 *                                                                       *
 *************************************************************************/
class WaitCellRenderer extends JLabel implements ListCellRenderer {
 private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

 /*Constructor*/
 public WaitCellRenderer() {
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