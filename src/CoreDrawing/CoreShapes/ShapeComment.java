package CoreDrawing.CoreShapes;

import CoreDrawingSyn.DrawCanvasSyn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;

/****************************************************************
 * This class represent a comment on the Concept Map. A comment *
 * is represented by an icon. A Jpanel is attached to a comment *
 * The JPanel will hold information about the comment and       *
 * author, date and email of the author.                        *
 *                                                              *
 *                                                              *
 * @author Klitos Christodoulou                                 *
 * email : christk6@cs.man.ac.uk                                *
 ***************************************************************/

public class ShapeComment extends JLabel implements ShapesInterface {

/*Properties and Variables Declaration */
private String creatorUserName = "";
private Calendar registerCalendar = null;
private String email;
private String comment;
private String shapeUniqueID = "";
private ShapeCommentPanel commnentPanel = null;
private JPopupMenu popupMenu;
private ArrayList<Component> connectedComponents; /*Hold connections*/

 /*Flag to indicate if this is drawing from Asynchronous or Synchronous
  *
  *   0 =  Asynchronous
  *   1 =  Synchronous
  */
 private int FLAG = 0;

public ShapeComment(Rectangle rect, String aUser,
                    String aMail, String info, String id) {

 /*Call super constructor and construct a jLabel with an
  icon*/
 super();

 /*Set Icon for this JLabel*/
 this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elearningmaps/ico/notes.png")));

 /*Set and reshape JLabel to the specific Rectangle (size and position)*/
 this.setBounds(rect);

 /*Set Size to the preferredSize*/
 this.setSize(this.getPreferredSize().width  + 12,
              this.getPreferredSize().height + 12);

 /*Construct the list of connected components*/
 connectedComponents = new ArrayList<Component>();

 /*Store the id - used to identify the object by the server & localBufferList*/
 shapeUniqueID = id;

 /***
  * Hold the following information
  */
 
 /*Hold the username of the Creator*/
 creatorUserName = aUser;

 /*Hold the email*/
 email = aMail;

 /*Hold a comment*/
 comment = info;

 /*Register calendar to keep creation time*/
 registerCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00"));

 /*Hold a reference to a ShapeCommentPanel*/
 commnentPanel = new ShapeCommentPanel(this, comment,creatorUserName,
                                       email,registerCalendar.getTime());

 popupMenu = new JPopupMenu();
 popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLUE));
 popupMenu.add(commnentPanel);

}//end constructor


/***
 * This method returns the Top-Left upper corner point
 *
 * @return
 */
 public Point getTopLeftPoint()
 {
   return this.getBounds().getLocation();
 }//end if


/****
 * This method is called to whenever the shape is translated
 *
 * @param x1
 * @param y1
 */
 public void translateShape(int x1, int y1)
 {
  this.setLocation(x1, y1);
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


/****
 * Get access to the commentPanel that holds all the information
 *   (1) comment
 *   (2) creator username
 *   (3) date created
 *   (4) email address
 *
 * @param g
 */
public ShapeCommentPanel getCommentPanel() {
  return this.commnentPanel;
}

/****
 * This method is responsible to popup the new JPanel to
 * the this JLabel as a parrent
 * 
 */
 public void editConceptShapeText() {

  /*Get the bounds of this JLabel*/
  Rectangle bounds = this.getBounds();

  /*Show popup with JPanel*/
  popupMenu.show(this, bounds.width/2, bounds.height/2);

  /*Request focus on that JPanel*/
  this.commnentPanel.requestFocusInWindow();

 }//end method


 /***
  * This method is called when the "Ok" button in the ShapeCommentPanel
  * is called.
  */
 public void applyChangesFromCommentPanel(String text)
  {
   
   /***
    * Synchronisation change
    *   - For asynchronous = update text
    *   - For synchronous  = also call a method to send a message to server
    */
   if (this.getParent() != null) {
     /*Check the FLAG if synchronous*/
     if (this.FLAG == 1) {
      ((DrawCanvasSyn)  this.getParent()).requestChangeDescription
                           (this.shapeUniqueID,text,1);
     }//end synchronous shape
   }//end if

   /*Make the popup that holds this ShapeCommentPanel to disappear*/
   popupMenu.setVisible(false);
  }//end method

 /***
  * Highligh ShapeComment
  * @param isPressed
  */
 public void hightlight(boolean isPressed) {

 if (isPressed) {
 this.setBorder(new SoftBevelBorder(BevelBorder.RAISED,
        Color.BLUE, Color.BLUE.darker(), Color.BLUE, Color.blue.brighter()));
 }
 else if (! isPressed) {
    this.setBorder(null);
 }

 repaint();
}//end method


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

    this.setBorder(null); 
  }//end
}//end if

/***
 * Get waitColour = Will determine if an object is unlocked
 *
 * @param str
 */
public String getWaitColour() {
  if (this.getBorder() == null) {
    return "b";
  }
  else {
   return ((RoundBorder) this.getBorder()).getBorderColor();
  }
}

  /****************************************************
   * Synchronisation methods.                         *
   ****************************************************/
  public int getFLAG() {
    return this.FLAG;
  }

  public void setFLAG(int f) {
    this.FLAG = f;
  }

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
   return " <shape type=\"comment\" " + "x1=\""   + this.getBounds().x +"\" "
		                              + "y1=\""   + this.getBounds().y +"\" "
								      + "/>";
  }//end method


  public ArrayList<Component> getConnComponenetsList() {
   return connectedComponents;
  }
}//end class