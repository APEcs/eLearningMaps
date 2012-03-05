package CoreDrawingAsy;

import CoreDrawing.CoreShapes.ShapeComment;
import CoreDrawing.CoreShapes.ShapeConcept;
import CoreDrawing.CoreShapes.ShapeLinking;
import CoreDrawing.CoreShapes.ShapeURL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.ServerSocket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/***********************************************************************
 * DrawConsoleUI.java                                                  *
 *                                                                     *
 * This class will represent the drawing Canvas of the application.    *
 * A user will choose a set of other users to draw with. This UI       *
 * is representing a common canvas where users can draw in a           *
 * collaborative manner.                                               *
 *                                                                     *
 * @author  Klitos Christodoulou                                       *
 * christk6@cs.man.ac.uk                                               *
 *                                                                     *
 * Created on 16-Jun-2010, 10:59:07                                    *
 *                                                                     *
 ***********************************************************************/


public class DrawConsoleUI extends javax.swing.JFrame {

  /***
   * Variable Declarations
   */

  /*Select the number of the selected tool*/
  private int SELECTEDTOOL          = 0;    /*Default tool now*/
  private final int CONCEPTTOOL     = 1;    /*Draw Shape Concepts*/
  private final int PROPOSITIONTOOL = 2;    /*Proposition Tool*/
  private final int NOTESTOOL       = 3;    /*Add comments Tool*/
  private final int IMAGETOOL       = 4;    /*Add Image from web Tool*/
  
  private ServerSocket socketServer = null;

  /****
   * Constructor 1
   *
   * Purpose : The purpose of this constructor is to create a
   * Client Draw console. This will be checked by the variable
   */
  public DrawConsoleUI() {
   initComponents();

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
    canvasPanel = new CoreDrawingAsy.DrawCanvas(this);
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle("eLearningMaps - Drawing Console");
    setLocationByPlatform(true);

    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    conceptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/concept.png"))); // NOI18N
    conceptButton.setText("Concept");
    conceptButton.setToolTipText("Add a concept");
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
      .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
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
      .addGap(0, 508, Short.MAX_VALUE)
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
      .addComponent(tabsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
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
      .addGap(0, 787, Short.MAX_VALUE)
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
      .addComponent(canvasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
    );
    mainPanelLayout.setVerticalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(canvasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
    );

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/logo.png"))); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(84, 84, 84)
        .addComponent(jLabel1)
        .addContainerGap())
      .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents


 /**
  * Implementation of Delete function.
  * When a del key is pressed, get the index of the selected
  * shape and delete it. Then update graphics.
  *
  * @param evt
  **/
  private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed

    /*Get the list of shapes from the Canvas*/
    List labeBuffer = getLabelBuffer();
    int index = getSelectedShapeIndex();

    /*Check if something was selected*/
    if ( index != -1 && !labeBuffer.isEmpty()) {

      /*(1) Remove it from Canvas*/
      if (labeBuffer.get(index) instanceof ShapeConcept)
       removeFromPanel((ShapeConcept) labeBuffer.get(index));
      else if (labeBuffer.get(index) instanceof ShapeLinking)
       removeFromPanel((ShapeLinking) labeBuffer.get(index));
      else if (labeBuffer.get(index) instanceof ShapeComment)
       removeFromPanel((ShapeComment) labeBuffer.get(index));
      else if (labeBuffer.get(index) instanceof ShapeURL)
       removeFromPanel((ShapeURL) labeBuffer.get(index));

      /*(2) Remove it from the Buffer*/
      labeBuffer.remove(index);
     }//end if

     /*Reset selected index*/
      index = -1;
    ((DrawCanvas) this.canvasPanel).setSelectedShapeIndex(-1);
  }//GEN-LAST:event_deleteButtonActionPerformed

/***
 * Method to get the selected index from Canvas
 * @return
 */
public int getSelectedShapeIndex() {
  return ((DrawCanvas) this.canvasPanel).getSelectedShapeIndex();
}//end method

/***
 * Method to get the List of JLabels drawn in the Panel
 * @return
 */
public List getLabelBuffer() {
  return ((DrawCanvas) this.canvasPanel).getLabelBuffer();
}//end method

/***
 * Call this method to remove a JLabel from the JPanel
 * @return
 */
public void removeFromPanel(java.awt.Component comp) {
  ((DrawCanvas) this.canvasPanel).removeJLabel(comp);
}//end method

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

   /*If toggle is false then set SelectedTool back to the default state*/
   if (!this.conceptButton.isSelected()) {
      this.SELECTEDTOOL = 0;
   }//end if
  }//GEN-LAST:event_conceptButtonActionPerformed

 /***
  * Set draw tool to notes tool
  * @param evt
  */
  private void notesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notesButtonActionPerformed
   updateTools(SELECTEDTOOL);
   this.SELECTEDTOOL = NOTESTOOL;

   /*If toggle is false then set SelectedTool back to the default state*/
   if (!this.notesButton.isSelected()) {
      this.SELECTEDTOOL = 0;
   }//end if
  }//GEN-LAST:event_notesButtonActionPerformed

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
  * Supporting method to return the canvas
  *
  */
public DrawCanvas getDrawCanvas() {
 return  (DrawCanvas) this.canvasPanel;
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
    popupMenu.add(new PanelLinkAsy((DrawCanvas) this.canvasPanel));

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
   popupMenu.add(new PanelImageAsy((DrawCanvas) this.canvasPanel));

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
          Logger.getLogger(DrawConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
          Logger.getLogger(DrawConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(DrawConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
          Logger.getLogger(DrawConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
            new DrawConsoleUI().setVisible(true);
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


 /***
  * Set previously selected tool back to the normal icon.
  *
  * @param previous
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
     //Exit application close all windows
     System.exit(0);
    }//end if
  }//end quit method


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel canvasPanel;
  private javax.swing.JScrollPane canvasScrollPane;
  private javax.swing.JToggleButton conceptButton;
  private javax.swing.JButton deleteButton;
  private javax.swing.JPanel historyPanel;
  private javax.swing.JToggleButton imageButton;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JToolBar.Separator jSeparator1;
  private javax.swing.JToolBar jToolBar1;
  private javax.swing.JToggleButton linkButton;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JToggleButton notesButton;
  private javax.swing.JButton saveButton;
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
}//end class