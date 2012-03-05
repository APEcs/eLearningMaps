package CoreDrawing.CoreShapes;

import CoreDrawingSyn.DrawCanvasSyn;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.awt.Desktop;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

/******************************************************************
 * This class represents a JLabel that acts as a URL.             *
 *
 * @author Klitos Christodoulou                                   *
 * email: christk6@cs.man.ac.uk                                   *
 ******************************************************************/

public class ShapeURL extends JLabel implements ShapesInterface{

 /*Variable Declarations*/
 private Applet applet =  new Applet();
 private String targetUrl;
 private Color unvisitedURL = new Color(27,147,205);
 private Color visitedURL = new Color(7,120,67);
 private int FLAG;
 private String creatorUserName = "";
 private String shapeUniqueID = "";
 private Calendar registerCalendar = null;

 /*Those are for the Popup menu appears to edit text*/
 private JTextField inputText = null;
 private JPopupMenu popupMenu;
 private String oldOriginalText="";


/****
 * Constructor for building : Dynamic Links
 *
 *  - accept the position of the rectangle
 *  - the creators username of this Link
 *  - the shapeUniqueID
 *  - and a FLAG to indicate that we are in Synchronous mode
 *
 * @param rect
 * @param aUser
 * @param icon
 */
public ShapeURL(Rectangle rect, String aUser, String url,
                       String descr, String id, int flag)
                                              throws MalformedURLException {
  super();

  /*Indicate the mode of drawing*/
  FLAG = flag;

  /*Set and reshape JLabel to the specific Rectangle*/
  this.setBounds(rect);

  /*Set the text of this JLabel in the CENTER*/
  this.setHorizontalAlignment(JLabel.CENTER);

  /*Set Foreground*/
  this.setOpaque(true);
  this.setBackground(Color.white);
  setForeground(unvisitedURL);

  /*Store the url*/
  targetUrl = url;

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
       applyLinkFromPopUp();
     }
    });

    /***
     * At the JTextField over the JPopupmenu
     */
    popupMenu = new JPopupMenu();
    popupMenu.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLUE));
    popupMenu.add(inputText);

    /*Create the Link*/
    this.setText(descr);

    /*Store the Description text for this concept*/
    this.setSize(this.getPreferredSize().width  + 15 ,
                this.getPreferredSize().height + 15);

   oldOriginalText = this.getText();

}//end constructor: Links


 /*Override paint method*/
 public void paint(Graphics g) {
  Rectangle r;
  super.paint(g);
  r = g.getClipBounds();
  g.drawLine(10,
    r.height - this.getFontMetrics(this.getFont()).getDescent(),
    this.getFontMetrics(this.getFont()).stringWidth(this.getText()),
    r.height - this.getFontMetrics(this.getFont()).getDescent());
 }//end paint()
 

 /*Set the color if URL is visited*/
 public void setUnvisitedURLColor(Color c) {
  unvisitedURL = c;
 }

 /*Set the color if URL is not visited yet*/
 public void setVisitedURLColor(Color c) {
  visitedURL = c;
 }

/*Called on mouse click, open default browser*/
public void openURL(){
   /*Change visited color*/
   setForeground(visitedURL);

   java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

   /*Check if OS support the action*/
   if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
      System.out.println( "OS doesn't support the browser action." );

    }//end if

   /*An exception may thrown*/
   try {
     java.net.URI uri = new java.net.URI( this.targetUrl );
     desktop.browse( uri );
    }//try
   catch ( Exception e ) {
     System.out.println("OS doesn't support the browser action.");
    }//catch
}//end method


  /*****
   * This method is called by the ActionListener of the
   * JTextField, responsible to update the text of the
   * JLabel.
   */
  private void applyLinkFromPopUp()
  {
   /*Read the text*/
   String labelText = inputText.getText();
   oldOriginalText = labelText;

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

  
 /*Method called by DrawCanvas.forwardChangeDesctoCanvas() */
 public void applyTextFromOutside(String inputText)
  {
   /*Read the text*/
   String labelText = inputText;
   oldOriginalText = labelText;

   this.setText(labelText);
   /*Set the size of this JLabel add a small margin*/
   this.setSize(this.getPreferredSize().width  + 15 ,
                this.getPreferredSize().height + 15);

   /*Just update the canvas*/
   if (this.getParent() != null) { this.getParent().repaint(); }//end if

  }//end method


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


  public ArrayList<Component> getConnComponenetsList() {
    return null;
  }//end if


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

  /****************************************************
   * Synchronisation methods.                         *
   ****************************************************/
  public int getFLAG() {
    return this.FLAG;
  }

  public void setFLAG(int f) {
    this.FLAG = f;
  }

 /***
  * This method returns a URL representation of this shape.
  * This can be used when writting files to store canvas
  * [FUTURE WORK]
  *
  * @return
  */
 public String toStringXML() {
   return " <shape type=\"webURL\" "  + "x1=\""   + this.getBounds().x +"\" "
		                              + "y1=\""   + this.getBounds().y +"\" "
								      + "/>";
  }//end method

}//end class
