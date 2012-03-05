package CoreDrawing.CoreShapes;

import CoreDrawingSyn.DrawCanvasSyn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;

/*************************************************************
 * This class represents a "Concept" of a concept map. In    *
 * low level, this class creates a :                         *
 *   (1) A Rectangle that represents the concept             *
 *   (2) A JTextField that will hold the text descriping     *
 *   this concept.                                           *
 *   (3) This shape also contains a list of components       *
 *   holding the Components that this JLabel is connected to *
 *                                                           *
 * @author Klitos Christodoulou                              *
 * email: christk6@cs.man.ac.uk                              *
 *                                                           *
 ************************************************************/

public class ShapeConcept extends JLabel implements ShapesInterface {

 /*Propeties declarations*/
 private String conceptDescription = "";
 private String oldOriginalText = "";
 private String creatorUserName = "";
 private String shapeUniqueID = "";
 private String [] image_details = null;
 private Calendar registerCalendar = null;
 private boolean isShapeConceptIcon = false;

 /*Those are for the Popup menu appears to edit text*/
 private JTextField inputText = null;
 private JPopupMenu popupMenu;

 /*Flag to indicate if this is drawing from Asynchronous or Synchronous
  *
  *   0 =  Asynchronous
  *   1 =  Synchronous
  */
 private int FLAG = 0;

 /*Private */
 private ArrayList<Component> connectedComponents;


 
 /***
  * Constructor for Image ShapeConcepts
  * 
  */
public ShapeConcept(Rectangle rect, String aUser, 
                            String iconName, String id, String [] array, int mode) {

 /*Call super constructor and construct a jLabel with an icon*/
 super();

 /*this is a concept with icon*/
 isShapeConceptIcon = true;

 /*Store image details [link,size]*/
 image_details = array;

 /*Set Icon for this JLabel*/
 this.setIcon(new javax.swing.ImageIcon("src/webimages/" + iconName));

 //System.out.println("" + getClass().getResource(id));

 /*Set and reshape JLabel to the specific Rectangle (size and position)*/
 this.setBounds(rect);


 /*Set Size to the preferredSize*/
 this.setSize(this.getPreferredSize().width  + 10,
              this.getPreferredSize().height + 10);

 /*Set back to white*/
 this.setOpaque(true);
 this.setBackground(Color.white);

 /*Default is asynchronous drawing, set FLAG to 0*/
 FLAG = mode;

 /*Set the border to the new Round Border class*/
 this.setBorder(new RoundBorder(Color.BLACK, 1));

 /*Construct the list of connected components*/
 connectedComponents = new ArrayList<Component>();

 /*Properties for the History Tracker*/
 creatorUserName = aUser;  /*Username of creatpt*/

 /*Register calendar to keep creation time*/
 registerCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00"));

  /*Store the id - used to identify the object by the server & localBufferList*/
 shapeUniqueID = id;

}//end constructor 2



 /**********************************************
  * ShapeConcept Constructor - NO WEB LINKS
  * 
  * @param rect
  * @param descr
  */
 public ShapeConcept(Rectangle rect, String descr,
                   String aUser, String id) {

   /*Call superconstructor and build a JLabel*/
   super(descr);

   /*Default is asynchronous drawing, set FLAG to 0*/
   FLAG = 0;

   /*Is shape concept with icon*/
   isShapeConceptIcon = false;

    /*Set and reshape JLabel to the specific Rectangle*/
   this.setBounds(rect);

   /*Set the text of this JLabel in the CENTER*/
   this.setHorizontalAlignment(JLabel.CENTER);

   /*Set the border to the new Round Border class*/
   this.setBorder(new RoundBorder(Color.BLACK, 1));

   /*Set Foreground*/
   this.setOpaque(true);
   this.setBackground(Color.white);

   /*Construct the list of connected components*/
   connectedComponents = new ArrayList<Component>();

   /*Store the id - used to identify the object by the server & localBufferList*/
   shapeUniqueID = id;

   /*Properties for the History Tracker*/
   creatorUserName = aUser;  /*Username of creatpt*/
   /*Register calendar to keep creation time*/
   registerCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00"));

   /***
    * Register an ActionListener over the textfield,
    * the user edits the text and then hit enter, the
    * text of this JLabel is changed
    */
   inputText = new JTextField();
   inputText.setBorder(null);
   inputText.addActionListener(new ActionListener()
    {
     public void actionPerformed(ActionEvent event)
     {
      applyTextFromPopUp();
     }
    });

    /***
     * At the JTextField over the JPopupmenu
     */
    popupMenu = new JPopupMenu();
    popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLUE));
    popupMenu.add(inputText);

   /*Store the Description text for this concept*/
   this.conceptDescription = descr;
   oldOriginalText = this.getText();
 }//end constructor: Default


/***
 * This method will return if this ShapeConcept is the one
 * having an icon.
 */
 public boolean isShapeConceptIcon(){
   return isShapeConceptIcon;
 }

/***
 * Return the description of this ShapeConcept
 */
public String getDescription(){
  return this.conceptDescription;
}

/***
 * Return the image details array
 */
public String[] getImageArray(){
  return this.image_details;
}

/***
 * This method returns the Top-Left upper corner point
 *
 * @return
 */
 public Point getTopLeftPoint()
 {
   return this.getBounds().getLocation();
 }//end if

/*****
 * This method is called to whenever the shape is ranslated
 *
 * @param x1
 * @param y1
 */
 public void translateShape(int x1, int y1)
 {
  this.setLocation(x1, y1);
 }//end method

  /*****
   * This method is called by the ActionListener of the
   * JTextField, responsible to update the text of the
   * JLabel.
   */
  private void applyTextFromPopUp()
  {
   /*Read the text*/
   String labelText = inputText.getText();
   oldOriginalText = labelText;
   
   /*Check if text needs multiple lines*/
   if (labelText.length() > 22) {
     labelText = multipleLines(labelText);
   }

   this.setText(labelText);
   /*Set the size of this JLanel add a small margin*/
   this.setSize(this.getPreferredSize().width  + 15 ,
                this.getPreferredSize().height + 15);

   /***
    * Synchronisation change
    *   - For asynchronous = update text
    *   - For synchronous  = also call a method to send a message to server
    */
   if (this.getParent() != null) {
     this.getParent().repaint();

     /*Check the FLAG if synchronous*/
     if (this.FLAG == 1) {
      ((DrawCanvasSyn)  this.getParent()).requestChangeDescription
                           (this.shapeUniqueID,oldOriginalText,0);
     }//end synchronous shape
   }//end if

   /*Make PopUp disappear*/
   popupMenu.setVisible(false);
  }//end method


  /****************************************************
   * Synchronisation methods.                         *
   ****************************************************/
  public int getFLAG() {
    return this.FLAG;
  }

  public void setFLAG(int f) {
    this.FLAG = f;
  }

 /*Method called by DrawCanvas.forwardChangeDesctoCanvas() */
 public void applyTextFromOutside(String inputText)
  {
   /*Read the text*/
   String labelText = inputText;
   oldOriginalText = labelText;

   /*Check if text needs multiple lines*/
   if (labelText.length() > 22) {
     labelText = multipleLines(labelText);
   }

   this.setText(labelText);
   /*Set the size of this JLabel add a small margin*/
   this.setSize(this.getPreferredSize().width  + 15 ,
                this.getPreferredSize().height + 15);

   /*Just update the canvas*/
   if (this.getParent() != null) { this.getParent().repaint(); }//end if

  }//end method

  /****
   * This method is used to create JLabel text with
   * multiple lines. 
   *
   * @param originalText
   * @return
   */
 public String multipleLines(String originalText) {

  String newText="<HTML>";
  String temp="";
  int newLength=0;

  while(originalText.length() > temp.length())
   {
    temp = originalText.substring(0, 22);
    newText += temp + "<BR>";

    originalText = originalText.replaceFirst(temp, "");
    newLength =  originalText.length();
   }//end while

   return new String(newText + originalText + "</HTML>");
 }//end


  /****
   * This method is called by the mouseListener on this
   * JLabel when a right click is detected. Prompt the
   * user to edit the text.
   */
  public void editConceptShapeText()
  {

    Rectangle bounds = this.getBounds();

    inputText.setText(oldOriginalText);
    popupMenu.setPreferredSize(
        new Dimension(this.getPreferredSize().width *2,  28));
    popupMenu.show(this, bounds.width/2, bounds.height/2);


    inputText.requestFocusInWindow();
    inputText.selectAll();
    
  }//end method


 /***
  * This method just return the enclosed JLabel
  *
  * @return
  */
 public JLabel getEnclosedLabel() {
   return this;
 }//end



/***
 * This method will Highlight the selected ShapeConcept
 * to indicate that is selected by changing the
 */
public void hightlight(boolean isPressed) {

 if (isPressed) {
   this.setBorder(new SoftBevelBorder(BevelBorder.RAISED,
       Color.BLUE, Color.BLUE.darker(), Color.BLUE, Color.blue.brighter()));
  }
 else if (! isPressed) {
    this.setBorder(new RoundBorder(Color.BLACK, 1));
 }

 //repaint();
}//end highlight


/***
 * Three colour will be used : like traffic lights
 *   - Orange = wait for an object status
 *   - Green  = means that object is unlocked
 *   - Red    = means that object is locked
 */
public void setWaitColour(String str) {

  /*A new RoundBorder*/
  RoundBorder rb = null;

  /*wait - (ORANGE)*/
  if (str.equals("o")) {
    rb = new RoundBorder(new Color(255,201,14), 1);
    rb.setBorderColor("o");
    this.setBorder(rb);
  }//end
  /*go - (GREEN)*/
  else if (str.equals("g")) {
    rb = new RoundBorder(new Color(181,230,29), 1);
    rb.setBorderColor("g");
    this.setBorder(rb);
  }//end
  /*stop - (RED)*/
  else if (str.equals("r")) {
    rb = new RoundBorder(new Color(237,28,36), 1);
    rb.setBorderColor("r");
    this.setBorder(rb);
  }//end
  else if (str.equals("b")) {
    rb = new RoundBorder(Color.BLACK, 1);
    rb.setBorderColor("b");
    this.setBorder(rb);
  }//end
}//end if

/***
 * Get waitColour = Will determine if an object is unlocked
 * 
 * @param str
 */
public String getWaitColour() {
  return ((RoundBorder) this.getBorder()).getBorderColor();
}

 /*************************************************************
  *                                                           *
  * METHODS: used for keeping a list of connected components  *
  * of this JLabel.                                           *
  *                                                           *
  *************************************************************/

  /***
   * This method returns a list of connected components
   *
   * @return
   */
  public ArrayList<Component> getConnComponenetsList(){
    return connectedComponents;
  }//end method

 /***
  * This method adds a Component to the connected list
  * It connects the component to this JLabel.
  *
  * @param c
  */
 public void addComponent(Component c) {
    /*Check if component is not this*/
    /*Check if the component not already exist*/
  if (c != null && c != this && !isEntry(c)) {
    connectedComponents.add(c);
   }
  else
    System.out.println("Already exists");
 }//end method

 /***
  * This method checks if the component already exist
  *
  * @param c
  * @return
  */
  public boolean isEntry(Component c) {
   boolean isHere = false;

   /*Check if parameter is not null*/
   if (c != null) {

     /*Loop and find component*/
     for (int i=0; i<connectedComponents.size(); i++){
       if (connectedComponents.get(i) == c) {
         isHere = true;
         break;
       }//end if
     }//end for
   }//end if

  return isHere;
 }//end method


 /***
  * Delete a Component from the list of connected Components
  *
  * @param c
  */
 public void delComponent(Component c) {
   
   /*Check if parameter is not null*/
   if (c != null) {

     /*Loop and find component*/
     for (int i=0; i<connectedComponents.size(); i++){
       if (connectedComponents.get(i) == c) {
         connectedComponents.remove(i);
         break;
       }//end if
     }//end for
   }//end if
 }//end method

 /***
  * This method returns a Point representing the center of this
  * JLabel component.
  *
  * @return
  */
 public Point getCenter()
  {
    /*Get the Rectangle that bounds this JLabel*/
    Rectangle rect = this.getBounds();

    return new Point((int) rect.getCenterX(), (int) rect.getCenterY());
  }//end



 /*************************************************************
  *                                                           *
  *       KEEP DETAILS FOR THE HISTORY TRACKER                *
  *                                                           *
  *  (1) Keep the name of the user that created this Object   *
  *  (2) Keep the time and date created (GMT)                 *
  * 
  *************************************************************/

 /***
  * This method returns the date that this object was register
  *
  * @return
  */
 public java.util.Date getCreationDate() {
  return this.registerCalendar.getTime();
 }//end method

  /***
   * This method returns the UserName of the client
   * that created this ShapeConcept
   *
   * @return
   */
  public String getCreatorName() {
   return this.creatorUserName;
  }

  /***
   * This method returns the ShapeUniqueID that will be used to
   * identify this ShapeConcept by the Server & Local list
   *
   * @return
   */
  public String getShapeUniqueID() {
   return this.shapeUniqueID;
  }

  /***
   * This method used to set the ShapeUniqueID that will be used to
   * identify this ShapeConcept by the Server & Local list
   *
   * @return
   */
  public void setShapeUniqueID(String sid) {
    shapeUniqueID = sid;
  }

  /***
   * This method gives an XML string representation of this object, so
   * that it can be reconstructed or stored. It basically stores the
   * (x,y) width and height of the Rectangle object that represents this
   * JLabel object.
   *
   * @return
   */
 public String toStringXML() {
   return " <shape type=\"concept\" " + "x1=\""   + this.getBounds().x +"\" "
		                              + "y1=\""   + this.getBounds().y +"\" "
								      + "/>";
  }//end method

}//end class