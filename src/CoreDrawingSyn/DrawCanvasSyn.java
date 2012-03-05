package CoreDrawingSyn;


import CoreDrawing.CoreShapes.ShapeComment;
import CoreDrawing.CoreShapes.ShapeConcept;
import CoreDrawing.CoreShapes.ShapeLinking;
import CoreDrawing.CoreShapes.ShapeURL;
import CoreDrawing.CoreShapes.ShapesInterface;
import elearningmaps.MessageObject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import java.net.URL;
import javax.imageio.ImageIO;

/**************************************************************************
 * DrawCanvas.java                                                        *
 * Created on 15 June 2010, 16:14:23                                      *
 *                                                                        *
 * This class represents the drawing canvas where users can do their      *
 * drawings. This class extends JPanel and it will be used in the         *
 * construction of the main draw window given by the DrawConsoleUI class  *
 *                                                                        *
 * It is also the CLIENT or viewport with in a JScrollPane                *
 *                                                                        *
 * @author Klitos Christodoulou                                           *
 * christk6@cs.man.ac.uk                                                  *
 **************************************************************************/

public class DrawCanvasSyn extends JPanel
                           implements Scrollable, MouseMotionListener {

 /***
  * Declaration of Properties/Variables
  */
 
 /*Lists*/
 private List labelLocalBuffer;       /*Hold a list of JLabels*/

 /*Reference to the DrawConsoleUI class*/
 private DrawConsoleUISyn drawConsoleUI = null;

 /*Hold a reference to the selected shape*/
 ShapesInterface selectedShape = null;

 /*Temp hold the selecedShape*/
 ShapeConcept firstConcept = null;
 ShapeLinking firstLinking = null;
 ShapeConcept secondConcept = null;
 ShapeLinking secondLinking = null;
 
 /*Determine if the mouse is dragged*/
 private boolean mouseIsDragged = false;
 private boolean haveConnect = false;

 /*Variable used on drag shape*/
 private Point preTranslationPoint = null;
 int offsetX,offsetY;

 /*Rectanlge to by send when joining two shapes*/
 private Rectangle encloseRectJoin = null;

 /*Filename of Image to be inserted*/
 private String [] imageDetails = new String[2];

 /*Variabel used by findShapeWithID(), to save shape id*/
 private int saveIndex = 0;

 /************************************/

 
 /**
  * Constructor
  */
 public DrawCanvasSyn(DrawConsoleUISyn drawConsole) {
  /*Call the super constructor and set the background colour*/
  super();
  this.setBackground(Color.white);

  /*Set the size of this component*/
  this.setPreferredSize(new Dimension(970,800));

  /*Enable autoscroll for this panel, when there is a need to
  update component's view*/
  this.setAutoscrolls(true);

  /*Construct a list that will hold the shapes*/
  //shapesBuffer = Collections.synchronizedList(new ArrayList());
  labelLocalBuffer  = Collections.synchronizedList(new ArrayList());

  /*Hold a reference to the DrawConsoleUI class*/
  drawConsoleUI = drawConsole;

  /*AutoScroll*/
  this.setAutoscrolls(true);

  /***
   * Register Listeners to the canvas
   */

  /*Mouse move*/
  this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
   public void mouseMoved(java.awt.event.MouseEvent evt) {
     //panelMouseMoved(evt);
  }//end mouse motion moved

  public void mouseDragged(java.awt.event.MouseEvent evt) {
    try {
      panelMouseDragged(evt);
    } catch (Exception e) {e.getMessage(); }
   }//end mouse motion Dragged
  });

  /*Mouse Clicked*/
  this.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
     try {
      panelMouseClicked(evt);
    } catch (Exception e) {e.getMessage(); }
   }//end

  /*Mouse Pressed*/
  public void mousePressed(java.awt.event.MouseEvent evt) {
    try {
      panelMousePressed(evt);
   } catch (Exception e) {e.getMessage(); }
  }//end

  /*Mouse Released*/
  public void mouseReleased(java.awt.event.MouseEvent evt) {
    try {
      panelMouseReleased(evt);
    } catch (Exception e) {e.getMessage(); }
   }//end
  });
 }//end constructor


/****************************************************************
 * Method: PAINTCOMPONENT
 *
 * Description:
 *
 * @param g
 */
 protected synchronized void paintComponent(Graphics g) {
  /*Call the paintComponent of the super class*/
  super.paintComponent(g);

  /*Cast the object to a Graphics2D object*/
  Graphics2D g2 = (Graphics2D) g;

  /*Enable rendering Antialiasing and other rendering optimizations*/
  g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);

  g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


  /***
   * Loop through the List that holds all the shapes.
   * shapesBuffer list and Draw them on screen
   */
  for (int i=0; i<labelLocalBuffer.size(); i++)
   {
     /*Get the Concept for each index i*/
     ShapesInterface currentConcept = (ShapesInterface) labelLocalBuffer.get(i);

 
     /*Pass from the connected List*/
    if (currentConcept instanceof ShapeConcept || currentConcept instanceof ShapeLinking) {
     for (int k=0; k<currentConcept.getConnComponenetsList().size(); k++) {
       /*Get the center point of this Concept*/
       Point p1 = currentConcept.getCenter();
       Point p2 = ((ShapesInterface) currentConcept.getConnComponenetsList().get(k)).getCenter();
       
       //Point p2 = ((ShapeConcept) currentConcept.getConnComponenetsList().get(k)).getCenter();

       g2.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
     }//end for
    }//end if check
   }//end for

}//end paintComponent


/*********************************************************************
 * SECTION: SYNCHRONISATION MANAGER                                  *
 *                                                                   *
 * This section will enclose all the methods necessary to            *
 * implement the synchronisation manager needed for the collaborate  *
 * drawing.                                                          *
 *                                                                   *
 *********************************************************************/


 /****
  * Method is used to reconstruct a Shape
  *
  * @param mo
  */
 public void createShape(MessageObject mo) throws MalformedURLException{
   if (mo.getShapeType().equals("ShapeConcept")){

     /*(1) Create a new ShapeConcept*/
     ShapeConcept sc = new ShapeConcept(mo.getPositionRect(),
                                         mo.getDescription(),
                                     mo.getCreatorUserName(),
                                             mo.getShapeID());

     /*(2) Set the FLAG = 1 of this ShapeConcept to indicate that we are
     in synchronous MODE*/
     sc.setFLAG(1);

     /*(3) Add this ShapeConcept to List: labelLocalBuffer */
     this.labelLocalBuffer.add(sc);

     /*(4) Add it to JPanel */
     this.add(sc);
   }//end case: ShapeConcept
   else if (mo.getShapeType().equals("ShapeConceptImg")) {

    try {

    /*(1) Initially it takes the [link,size]*/
     /*Get the name of this image*/
     String [] image_details = mo.getImageDetails();

      /*Construct original image*/
     Image original_image = ImageIO.read(new URL(image_details[0]));

     /*Split the link and get the last*/
     String [] stingTokens = image_details[0].split("/");

     /*Recover the name of this Image*/
     String imageName = stingTokens[stingTokens.length-1];

     /*Check if the imageName exists*/
     String newFinalName = this.checkFileName(imageName);


     /*Get the extension of the Image and check it*/
     String extension = image_details[0].substring(image_details[0].length()-3,
                                                     image_details[0].length());


     
    /*Now check the size of this Image*/
     if (image_details[1].equals("d")) {
        BufferedImage bi = ImageIO.read( new URL(image_details[0]));
       ImageIO.write(bi,extension,new File("src/webimages/"+ newFinalName));
     }
     else if (image_details[1].equals("s")) {
      processImage(image_details[0],original_image.getWidth(null)/3,
                                   original_image.getHeight(null)/3,
                                             extension,newFinalName);
     }
     else if(image_details[1].equals("m")) {
      processImage(image_details[0],original_image.getWidth(null)/2,
                                   original_image.getHeight(null)/2,
                                             extension,newFinalName);
     }
     else if(image_details[1].equals("l")) {
      processImage(image_details[0],original_image.getWidth(null)*2,
                                   original_image.getHeight(null)*2,
                                             extension,newFinalName);
     }//end if

    /*(1) Create a new ShapeConcept*/
    ShapeConcept addImage = new ShapeConcept(mo.getPositionRect(),
                                          mo.getCreatorUserName(),
                                                     newFinalName,
                                                  mo.getShapeID(),
                                                    image_details,
                                                 1); /*Flag = 1*/

     /*(2) Add this ShapeConcept to List: labelLocalBuffer */
     this.labelLocalBuffer.add(addImage);

     /*(3) Add it to JPanel */
     this.add(addImage);
    } catch (Exception e) {
      System.out.println("Cannot read image.");
    }//end
   }//end case: ShapeConcept with Image
   else if (mo.getShapeType().equals("ShapeComment")){

    /*(1) Create a new ShapeComment*/
     ShapeComment note = new ShapeComment(mo.getPositionRect(),
                                          mo.getCreatorUserName(),
                                          mo.getCreatorEmail(),
                                          mo.getDescription(),
                                          mo.getShapeID());

     /*(2) Set the FLAG = 1 of this ShapeConcept to indicate that we are
     in synchronous MODE*/
     note.setFLAG(1);

     /*(3) Add this ShapeConcept to List: labelLocalBuffer */
     this.labelLocalBuffer.add(note);

     /*(4) Add it to JPanel */
     this.add(note);
   }//end case: ShapeComment
   else if (mo.getShapeType().equals("ShapeURL")){

    /*(1) Create a new ShapeURL*/
    ShapeURL webLink = new ShapeURL(mo.getPositionRect(),
                                mo.getCreatorUserName(),
                                        mo.getWebLink(),
                                    mo.getDescription(),
                                        mo.getShapeID(),
                                                     1); /*Flag = 1*/


    /*(2) Add this ShapeURL to List: labelLocalBuffer */
    this.labelLocalBuffer.add(webLink);

    /*(3) Add it to JPanel */
    this.add(webLink);
   }//end case: ShapeURL

   /*At the end always call repaint(), to refresh graphics*/
   repaint();
 }//end method


/***
 * This method is called when a user clicks on a Shape and wants to
 * grant permission on that Shape.
 *
 * @param shapeID
 */
 public void requestLockShapeFromServer(String shapeID) {

   /*(1) Prepare a new MessageObject to acquire locking that shape from
    server*/
   MessageObject mo_lockShape = new MessageObject("lockrequest",
                                                        shapeID,
                                    drawConsoleUI.getUserName());

   /*(2) Send MessageObject to the Server*/
   drawConsoleUI.sendMessageToServer(mo_lockShape);
 }//end method

 /***
  * This method will be called when user left clicks and request to
  * change the description of a :
  *   - ShapeConcept
  *   - ShapeLinking
  *
  * @param shapeID
  * @param creatorName
  * @param newDescription
  */
  public void requestChangeDescription(String shapeID,
                                       String newDescription,
                                       int mode) {

   /*(1) Mode flag is : 0 - Change description of a concept
    * 
    * Prepare a new MessageObject to request a new description from
    * server.
    * 
    * Note use the MessageObject for : "lockrequest" , so use that methods.
    */
   MessageObject mo_changeDescription = null;
   if (mode == 0) {
     mo_changeDescription = new MessageObject("descriptionchange",
                                                          shapeID,
                                                   newDescription,
                                                               "");
   }
   else if (mode == 1) {
     /*Mode flag is : 1  - Change Sticky note text, send :
     - shapeUniqueID
     - editor
     - new note text*/  
     mo_changeDescription = new MessageObject("descriptionchange",
                                                          shapeID,
                                                   newDescription,
                                      drawConsoleUI.getUserName());
   }//end else


   /*(2) Send MessageObject to the Server, to broadcast change */
   drawConsoleUI.sendMessageToServer(mo_changeDescription);

   /*(3) Send Message to Server to release shape*/
   drawConsoleUI.sendMessageToServer(new MessageObject("releaseshape",
                                                              shapeID,
                                          drawConsoleUI.getUserName()));
  }//end method


 /***
  * METHODS THAT DO THE ACTUAL CHANGE
  *
  * This method is responsible to set the colour for the
  * request to lock shape.
  *   "Red"   = Cannot modify shape
  *   "Green" = Full access to shape
  * 
  * @param mo
  */
 public void grandPrivilage(MessageObject mo) {

  /*Loop the list and find the Shape*/
  for ( int i = this.labelLocalBuffer.size() - 1; i >= 0; i-- )
   {
    /*CASE: ShapeConcept*/
    if (labelLocalBuffer.get(i) instanceof ShapeConcept) {

     ShapeConcept member = (ShapeConcept)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getLockShapeID())) {

      /*If response is "ok" - then make color "GREEN" (grant access)*/
      if (mo.getLockStatus().equals("ok")) {
        member.setWaitColour("g");
       }
      else if (mo.getLockStatus().equals("wait")) { 
        member.setWaitColour("r");
      }//else
      else {
         member.setWaitColour("b");
      }//else
       break; 
     }//end if
    }/*END CASE ShapeConcept*/
    else if (labelLocalBuffer.get(i) instanceof ShapeLinking) {
     ShapeLinking member = (ShapeLinking)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getLockShapeID())) {

      /*If response is "ok" - then make color "GREEN" (grant access)*/
      if (mo.getLockStatus().equals("ok")) {
        member.setWaitColour("g");
       }
      else if (mo.getLockStatus().equals("wait")) {
        member.setWaitColour("r");
      }//else
      else {
        member.setWaitColour("b");
      }//else
       break; 
     }//end if
    }/*END CASE ShapeLinking*/
    else if (labelLocalBuffer.get(i) instanceof ShapeComment) {

     ShapeComment member = (ShapeComment)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getLockShapeID())) {

      /*If response is "ok" - then make color "GREEN" (grant access)*/
      if (mo.getLockStatus().equals("ok")) {
        member.setWaitColour("g");
       }
      else if (mo.getLockStatus().equals("wait")) {
        member.setWaitColour("r");
      }//else
      else {
        member.setWaitColour("b");
      }//else
       break; 
     }//end if
    }/*END CASE ShapeComment*/
    else if (labelLocalBuffer.get(i) instanceof ShapeURL) {
     ShapeURL member = (ShapeURL) labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getLockShapeID())) {

      /*If response is "ok" - then make color "GREEN" (grant access)*/
      if (mo.getLockStatus().equals("ok")) {
        member.setWaitColour("g");
       }
      else if (mo.getLockStatus().equals("wait")) {
        member.setWaitColour("r");
      }//else
      else {
        member.setWaitColour("b");
      }//else
       break;
     }//end if
    }/*END CASE ShapeURL*/
   }//end for

  /*Call repaint to refresh colours*/
  this.repaint();
 }//end method


 /****
  * METHOD THAT DOES AN ACTUAL CHANGE
  *
  * Change the description of a ShapeConcept
  * na kamw to idio kai gia ta alla shapes, na valw tis nees methods
  * pou ekana sto ShapeConcept
  *
  * @param mo
  */
 public void changeDescription(MessageObject mo) {

  /*Loop the list and find the Shape*/
  for ( int i = this.labelLocalBuffer.size() - 1; i >= 0; i-- )
   {
    /*CASE: ShapeConcept*/
    if (labelLocalBuffer.get(i) instanceof ShapeConcept) {

     ShapeConcept member = (ShapeConcept)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getEditShapeID())) {

       /*Call a method to change the text of this shape*/
       
       member.applyTextFromOutside(mo.getEditNewText());

      break;  
     }//end if
    }/*END CASE ShapeConcept*/
    else if (labelLocalBuffer.get(i) instanceof ShapeLinking) {
     ShapeLinking member = (ShapeLinking)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getEditShapeID())) {

       /*Call a method to change the text of this shape*/

       member.applyTextFromOutside(mo.getEditNewText());

      break;
     }//end if
    }/*END CASE ShapeLinking*/
    else if (labelLocalBuffer.get(i) instanceof ShapeURL) {
     ShapeURL member = (ShapeURL)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getEditShapeID())) {

       /*Call a method to change the text of this shape*/

       member.applyTextFromOutside(mo.getEditNewText());

      break;
     }//end if
    }/*END CASE ShapeURL*/
    else if (labelLocalBuffer.get(i) instanceof ShapeComment) {
     ShapeComment member = (ShapeComment)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getEditShapeID())) {

       /*Call a method to change the text of this shape*/

       member.getCommentPanel().applyChanges(mo.getEditEditorName(),
                                             mo.getEditNewText());

      break;
     }//end if
    }/*END CASE ShapeComment*/
   }//end for
 }//end method



 /****
  * METHOD THAT DOES AN ACTUAL CHANGE
  *
  * Change the description of a ShapeConcept
  * na kamw to idio kai gia ta alla shapes, na valw tis nees methods
  * pou ekana sto ShapeConcept
  *
  * @param mo
  */
 public void translateShape(MessageObject mo) {

  /*Loop the list and find the Shape*/
  for ( int i = this.labelLocalBuffer.size() - 1; i >= 0; i-- )
   {
    /*CASE: ShapeConcept*/
    if (labelLocalBuffer.get(i) instanceof ShapeConcept) {

     ShapeConcept member = (ShapeConcept)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getTranslateID())) {

       /*Call a method to translate shape to the new Point*/

       member.setLocation(mo.getNewX1(), mo.getNewY1());

      break;  
     }//end if
    }/*END CASE ShapeConcept*/
    else if (labelLocalBuffer.get(i) instanceof ShapeLinking) {
     ShapeLinking member = (ShapeLinking)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getTranslateID())) {

       /*Call a method to translate shape to the new Point*/

       member.setLocation(mo.getNewX1(), mo.getNewY1());

      break;
     }//end if
    }/*END CASE ShapeLinking*/
    else if (labelLocalBuffer.get(i) instanceof ShapeComment) {
     ShapeComment member = (ShapeComment)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getTranslateID())) {

       /*Call a method to translate shape to the new Point*/

       member.setLocation(mo.getNewX1(), mo.getNewY1());

      break;
     }//end if
    }/*END CASE ShapeComment*/
    else if (labelLocalBuffer.get(i) instanceof ShapeURL) {
     ShapeURL member = (ShapeURL)labelLocalBuffer.get(i);

     /*Try to match ids*/
     if (member.getShapeUniqueID().equals(mo.getTranslateID())) {

       /*Call a method to translate shape to the new Point*/

       member.setLocation(mo.getNewX1(), mo.getNewY1());

      break;
     }//end if
    }/*END CASE ShapeURL*/
   }//end for

  /*Update the screen with a repaint - used for the lines*/
  this.repaint();
 
 }//end method


 /****
  * METHOD THAT DOES AN ACTUAL CHANGE
  *
  * This method is responsible to reconstruct a join between
  * two Shapes, create a link between them
  *
  * @param mo
  */
 public void joinShapes(MessageObject mo) {

   ShapeConcept first_Concept = null;
   ShapeLinking first_Linking = null;
   ShapeConcept second_Concept = null;
   ShapeLinking second_Linking = null;

   /*Get the String array of IDS*/
   String [] ids = mo.getArrayOfIDs();

   /*FIRST SHAPE : Check the type of shape from first ID*/
   ShapesInterface first_shape_id = findShapeWithID(ids[0]);

   if  (first_shape_id instanceof ShapeConcept)
     first_Concept = (ShapeConcept) first_shape_id;
   else if  (first_shape_id instanceof ShapeLinking)
     first_Linking = (ShapeLinking) first_shape_id;

   /*SECOND SHAPE : Check the type of shape from second ID*/
   ShapesInterface second_shape_id = findShapeWithID(ids[1]);
   
   if  (second_shape_id instanceof ShapeConcept)
     second_Concept = (ShapeConcept) second_shape_id;
   else if  (second_shape_id instanceof ShapeLinking)
     second_Linking = (ShapeLinking) second_shape_id;

   /***************
    * START CASES *
    ***************/

    /*CASE-1: first=Concept & sec=Concept*/
    if (first_Concept !=null && second_Concept !=null
                          && first_Linking == null && second_Linking == null) {

     /*Create a new LinkingPhaze*/
     ShapeLinking linkPhraze = new ShapeLinking(mo.getPositionRect(),
                                                    "This is a link",
                                               mo.getWhoJoinShapes(),
                                                     mo.getShapeID());

     /*(2) Set the FLAG = 1 of this ShapeConcept to indicate that we are
     in synchronous MODE*/
     linkPhraze.setFLAG(1);

     /*Save Link phraze it in the shape buffer List*/
     this.labelLocalBuffer.add(linkPhraze);

     /*Draw the ShapeConcept object on the JPanel*/
     this.add(linkPhraze);

     /*Join*/
     if (! first_Concept.isEntry(linkPhraze)){
       linkPhraze.addComponent(first_Concept);
     }//end if

     if (! linkPhraze.isEntry(second_Concept)){
       second_Concept.addComponent(linkPhraze);
     }//end if
   }//END CASE 1
   else if (first_Concept != null && second_Concept == null
                          && first_Linking == null && second_Linking != null) {
     if (! first_Concept.isEntry(second_Linking))
       second_Linking.addComponent(first_Concept);
     else {
        /*Detect two way*/

        //tempConcept.addComponent(getLabelInPoint(pointClicked));
      }//else
   }//END CASE 2
   /*CASE-3: first=Link & sec=Concept*/
   else if (first_Concept == null && second_Concept != null
                         && first_Linking != null && second_Linking == null) {
     if (! first_Linking.isEntry(second_Concept))
       second_Concept.addComponent(first_Linking);
     else {
       /*Detect two way*/

       //tempConcept.addComponent(getLabelInPoint(pointClicked));
      }
   }//END CASE 3
   /*CASE-4: first=Link & sec=Link*/
   else if (first_Concept == null && second_Concept == null
                           && first_Linking != null && second_Linking != null) {
   }//END CASE 4

   /*Refresh the screen, call repaint*/
   repaint();
 }//end method

 
  /****
  * METHOD THAT DOES AN ACTUAL CHANGE
  *
  * This method is responsible for deleting a shape from;
  *   - labels local buffer
  *   - JPanel component
  *
  * @param mo
  */
 public void deleteShapeID(MessageObject mo) {

   /*Clear saveIndex*/
   saveIndex = 0;

   /*Find member with this deleteID*/
   ShapesInterface delMember = findShapeWithID(mo.getLockShapeID());


       /*(1) Remove it from Canvas*/
      if (delMember instanceof ShapeConcept)
       this.remove((ShapeConcept) delMember);
      else if (delMember instanceof ShapeLinking)
       this.remove((ShapeLinking) delMember);
      else if (delMember instanceof ShapeComment)
       this.remove((ShapeComment) delMember);
      else if (delMember instanceof ShapeURL)
       this.remove((ShapeURL) delMember);

      /*(2) Remove it from the Buffer*/
      this.labelLocalBuffer.remove(saveIndex);

      /*(3) Refresh JPanel*/
      this.repaint();

 }//end method


 /***
  * This is a supporting method. The method accept a shapeID and
  * returns a ShapesInterface object that mathches that ID.
  *
  * @param id
  * @return
  */
 public ShapesInterface findShapeWithID(String id) {

   ShapesInterface outcome = null;

   for ( int i = this.labelLocalBuffer.size() - 1; i >= 0; i-- )
   {
     String memberid = ((ShapesInterface) labelLocalBuffer.get(i)).getShapeUniqueID();

     if ( memberid.equals(id)) {
      outcome = (ShapesInterface) labelLocalBuffer.get(i);
      saveIndex = i;
      break;
     }//end if
   }//end for

   return outcome;
 }//end method



 /***
  * Return the SelectedShape
  * @return
  */
 public ShapesInterface getSelectedShape() {
   return this.selectedShape;
 }

 /***
  * Set SelectedShape to null
  * @return
  */
 public void clearSelectedShape() {
   this.selectedShape = null;
 }

/********************************************************************/


/*******************************************************************
 * Method: panelMouseClicked                                       *
 */
 private void panelMouseClicked(java.awt.event.MouseEvent evt) throws Exception
 {

   /*Monitor which mouse button was clicked.*/
   /*LEFT*/
   if (evt.getButton() == MouseEvent.BUTTON1)
    {

    }//end LEFT mouse btn
   /*MIDDLE*/
   else if (evt.getButton() == MouseEvent.BUTTON2) {}
   /*RIGHT*/
   else if (evt.getButton() == MouseEvent.BUTTON3) {
    /***
     * Draw a concept by clicking the right mouse button
     */
     
      /*Initially read the point clicked*/
      Point pointClicked = evt.getPoint();

      /*Read and save the object on that Point*/
      ShapesInterface currentShape = getLabelInPoint(pointClicked);


      /*CASE: WEBLINK*/
      /*Chek if something is clicked and open URL*/
      if (currentShape != null) {
         if  (currentShape.getWaitColour().equals("g") 
                             && (currentShape instanceof ShapeURL)) {
           ((ShapeURL) currentShape).editConceptShapeText();
         }//end if
      }//end if



      /***
      * If the DrawConcept tool was pressed:
      *  (1) that point becomes the middle of the ShapeConceptOLD shape
      *  (2) draw that shape on screen
      */
     if (drawConsoleUI.getSelectedToolID() == 1) {

      /*Check whether the Point clicked is not within another
       ShapeConcept*/
      if (currentShape == null) {


       /***
        * COLLABORATE: "create a new ShapeConcept"
        *  (1) Get the details needed to create this ShapeConcept
        *  (2) Setup a MessageObject to inform the server
        *  (3) On server response : - add this ShapeConcept on labelLocalBuffer
        *                           - add this ShapeConcept(JLabel) on the JPanel
        */

       int defaultWidth  = 140;
       int defaultHeight = 40;
       
       /*(1) Create a bound Rectangle to enclose this JLabel*/
        Rectangle encloseRect = new Rectangle(pointClicked.x - (int) defaultWidth/2,
                                              pointClicked.y - (int) defaultHeight/2,
                                              defaultWidth,
                                              defaultHeight);

       /*(2) Prepare a new MessageObject to send to the Server */
       MessageObject mo_shapeConcept = new MessageObject("createshape",
                                                         "ShapeConcept",
                                                            encloseRect,
                                                    "This is a concept",
                                              drawConsoleUI.getUserName(),
                                              drawConsoleUI.getUserEmailAdd());

       /*(3) Send MessageObject to the Server*/
       drawConsoleUI.sendMessageToServer(mo_shapeConcept);

      }//end if point is contained
      else {
         /*Check if user has priviledges for that ShapeConcept*/
         if (currentShape.getWaitColour().equals("g") &&
                                      (currentShape instanceof ShapeConcept)) {

          /*Call the rename method of this ShapeConcept*/
             currentShape.editConceptShapeText();
         }//end if
         /*Check if user has priviledges for that ShapeConcept*/
         else if (currentShape.getWaitColour().equals("g") &&
                                      (currentShape instanceof ShapeLinking)) {

          /*Call the rename method of this ShapeConcept*/
             currentShape.editConceptShapeText();
         }//end if
      }//inner else
     }//end if

     /****
      * Else if : the tool selected is Sticky Notes
      */
     else if (drawConsoleUI.getSelectedToolID() == 3) {
       

      /*Check whether the Point clicked is not within another
       ShapeComment*/
      if (currentShape == null) {

       /*(1) Create a bound Rectangle to enclose this JLabel*/
       Rectangle commentRect = new Rectangle(pointClicked.x - 10,
                                             pointClicked.y - 10,
                                             20,
                                             20);

       /*(2) Prepare a new MessageObject to send to the Server */
       MessageObject mo_shapeComment = new MessageObject("createshape",
                                                        "ShapeComment",
                                                           commentRect,
                                                          "Add a note",
                                           drawConsoleUI.getUserName(),
                                       drawConsoleUI.getUserEmailAdd());


       /*(3) Send MessageObject to the Server*/
       drawConsoleUI.sendMessageToServer(mo_shapeComment);

      }//end if
      else {
        /*Check if user has priviledges for that ShapeComment*/
        if (currentShape.getWaitColour().equals("g") &&
                                      (currentShape instanceof ShapeComment)) {

          /*Call edit ShapeCommentPanel*/
          currentShape.editConceptShapeText();
         }//end if
      }//end else
     }//end if
   else if (drawConsoleUI.getSelectedToolID() == 4) {
      
     /*Check whether the Point clicked is not within another
       ShapeConcept with Image*/
     if (currentShape == null) {

       /*(1) Create a bound Rectangle to enclose this JLabel*/
        Rectangle iconRect = new Rectangle(pointClicked.x - 10,
                                           pointClicked.y - 10,
                                                        20,20);

        /*(2) Initially check if there is an extension*/
        String extension = imageDetails[0].substring(imageDetails[0].length()-4,
                                                     imageDetails[0].length());

      if (extension.equals(".gif") || extension.equals(".png")
           || extension.equals(".jpg") || extension.equals(".jpeg") ) {

       /*(3) Prepare a new MessageObject to send to the Server
       Note : descr = imageToInsert*/
       MessageObject mo_conceptImg = new MessageObject("createshape",
                                                   "ShapeConceptImg",
                                                            iconRect,
                                                        imageDetails,
                                         drawConsoleUI.getUserName(),
                                     drawConsoleUI.getUserEmailAdd());


        /*(4) Send MessageObject to the Server*/
        drawConsoleUI.sendMessageToServer(mo_conceptImg);
       }//end if there is an extension
      }//end if
    }//end if
   }//end RIGHT mouse btn
 }//end mouseClick



/********************************************************************
 * Method: panelMouseReleased                                       *
 *                                                                  *
 * (1) The method will check if the Shape is release outside the    *
 *  Canvas, if yes then resize the Canvas.                          *
 *                                                                  *
 * 
 * @param evt
 * @throws Exception
 ********************************************************************/
private void panelMouseReleased(java.awt.event.MouseEvent evt)
                                                     throws Exception {

  /*This is the release point*/
  Point releasePoint = null;

  /*Check if something was selected and if the mouse was dragged*/
  if ((selectedShape != null)&& (mouseIsDragged)) {
   
   /*Get the current release Point*/
   releasePoint = evt.getPoint();

   /*Check if Shapes are within boundaries*/
   if (this.selectedShape instanceof ShapeConcept) {

    /*Check if the ShapeConcept is inside the canvas area*/
    offScreenShapeResize( ((ShapeConcept) selectedShape).getBounds());
   }//end ShapeConcept
   else if (this.selectedShape instanceof ShapeLinking) {

     /*Check if the ShapeConcept is inside the canvas area*/
    offScreenShapeResize( ((ShapeLinking) selectedShape).getBounds());
   }//end LinkingPhraze
   else if (this.selectedShape instanceof ShapeComment) {

     /*Check if the ShapeConcept is inside the canvas area*/
    offScreenShapeResize( ((ShapeComment) selectedShape).getBounds());
   }//end ShapeComment
   else if (this.selectedShape instanceof ShapeURL) {

     /*Check if the ShapeConcept is inside the canvas area*/
    offScreenShapeResize( ((ShapeURL) selectedShape).getBounds());
   }//end ShapeComment
  }//end if

 /**When mouse is released, finish dragging just reset values**/
 
 mouseIsDragged = false;
 
 /*When stop dragging release shape*/
 if (!mouseIsDragged && releasePoint != null) {

   
   /*If the shape dragged was green = access, BROADCAST the change to
    other clients as well*/
   if (selectedShape.getWaitColour().equals("g")) {

    /*(1) Setup a new MessagObject to inform other clients*/
    MessageObject mo_translate =
                         new MessageObject(selectedShape.getTopLeftPoint().x,
                                           selectedShape.getTopLeftPoint().y,
                                           selectedShape.getShapeUniqueID(),
                                           drawConsoleUI.getUserName());


    /*(2) Send MessageObject to the Server*/
    drawConsoleUI.sendMessageToServer(mo_translate);

    /*(3) Send Message to Server to release shape*/
    drawConsoleUI.sendMessageToServer(new MessageObject("releaseshape",
                                      selectedShape.getShapeUniqueID(),
                                         drawConsoleUI.getUserName()));
   }//end if

   releasePoint = null;
   selectedShape = null;
 }//end if
 //repaint();  //--> gia na dokimase to rename popup
}//end panelMouseReleased


/********************************************************************
* Method: panelMouseDragged                                         *
*
*
* @param evt
* @throws java.lang.Exception
*********************************************************************/
private void panelMouseDragged(java.awt.event.MouseEvent evt)
                                                     throws Exception{
 
 /*Store the point where the mouse is dragged*/
 Point dragPoint = null;

 /*Make sure client has access to the shape*/
 if (this.selectedShape != null && selectedShape.getWaitColour().equals("g"))
  {
    /*Change mouse was dragged to true*/
    mouseIsDragged = true;

    /*Store the dragged point*/
    dragPoint = evt.getPoint();

    /*Now translate the Shape by calling the method*/
    /*Both ShapeConcept and ShapeLinking use the same method*/
    selectedShape.translateShape(dragPoint.x - offsetX, dragPoint.y - offsetY);
    repaint();
  }//end if
}//end mouse Dragged


/*******************************************************************
 * Method :
 *
 * Purpose :
 *   (1) To detect the selected shape. It saves the point where
 *       the mouse was pressed and return
 *
 *
 * @param evt
 * @throws Exception
 *******************************************************************/

private void panelMousePressed(java.awt.event.MouseEvent evt)
                                                        throws Exception {

  if (evt.getButton() == MouseEvent.BUTTON1) {
   /*Get the point where the mouse was clicked*/
   Point pointClicked = evt.getPoint();

   /* (1) Read and save the object on that Point*/
    ShapesInterface tempShape = getLabelInPoint(pointClicked);

   /*Extra case: Chek if something ws clicked and open URL*/
   if (tempShape != null) {
     if  (tempShape instanceof ShapeURL) {
     /*If shit button is pressed then fire URL*/
       if (evt.isShiftDown())
        ((ShapeURL) tempShape).openURL();
      }//end if
   }//end if

   /* (2) Call method to aqcuire the lock, if not null. Do not request access
    if selected tool is Join.*/

    if (tempShape != null && tempShape.getWaitColour().equals("b")
                                   && drawConsoleUI.getSelectedToolID() != 2) {
      /*Start cases*/
      if (tempShape instanceof ShapeConcept) {
        /*Set colour to the wait colour*/
        ((ShapeConcept) tempShape).setWaitColour("o");
        
        /*call method to send message to acquire the lock*/
        requestLockShapeFromServer(((ShapeConcept) tempShape).getShapeUniqueID());
      }//end case: ShapeConcept
      else if (tempShape instanceof ShapeLinking) {
        /*Set colour to the wait colour*/
        ((ShapeLinking) tempShape).setWaitColour("o");

        /*call method to send message to acquire the lock*/
        requestLockShapeFromServer(((ShapeLinking) tempShape).getShapeUniqueID());
      }//end case: ShapeLinking
      else if (tempShape instanceof ShapeComment) {
        /*Set colour to the wait colour*/
        ((ShapeComment) tempShape).setWaitColour("o");

        /*call method to send message to acquire the lock*/
        requestLockShapeFromServer(((ShapeComment) tempShape).getShapeUniqueID());
      }//end case: ShapeComment
      else if (tempShape instanceof ShapeURL) {
        /*Set colour to the wait colour*/
        ((ShapeURL) tempShape).setWaitColour("o");

        /*call method to send message to acquire the lock*/
        requestLockShapeFromServer(((ShapeURL) tempShape).getShapeUniqueID());
      }//end case: ShapeURL
    }//end if not null

   /*(3) If shape is green then selected it to translate*/
   if (tempShape != null )
            selectedShape = tempShape;


   /*Save this point, to be used during Transformations*/
   preTranslationPoint = pointClicked;

   /*Check which tool is selected*/
   if (drawConsoleUI.getSelectedToolID() == 2) {

      /*Check if ShapeConcept are connected*/
      if (! haveConnect) {

        /*Check if the first Shape selected is either a Concept or a Link*/
        if  (selectedShape instanceof ShapeConcept) 
           firstConcept = (ShapeConcept) selectedShape;
        else if  (selectedShape instanceof ShapeLinking) 
           firstLinking = (ShapeLinking) selectedShape;
         
       }//end inner if
      else
       {
        /*Get the second componenet at point*/

        ShapesInterface shape = getLabelInPoint(pointClicked);

        /*Check whether the second Shape is Concept or either Linking*/
        if  (shape instanceof ShapeConcept) 
          secondConcept = (ShapeConcept) selectedShape;
        else if  (shape instanceof ShapeLinking) 
          secondLinking = (ShapeLinking) selectedShape;
   
        /* CASE-1: first=Concept & sec=Concept. Consider only one case when
         * sending, other cases when receiving and reconstructing the join
         * [SPECIAL CASE FOR SENDING MESSAGE OBJECT]*/
        if (firstConcept !=null && secondConcept !=null && firstLinking == null && secondLinking == null) {

         /*Find the middle between the two points*/
         Point p1 = firstConcept.getCenter();
         Point p2 = secondConcept.getCenter();

         /*Find the mid point of the line*/
         Point midPoint = new Point((p1.x + p2.x)/2,(p1.y + p2.y)/2);

         /*Calculate a small offset*/
         int offset = this.addOffset(20, 70);
  
         /*Find the rectangle at the center*/
         encloseRectJoin = new Rectangle(midPoint.x + offset - (int) 140/2,
                                          midPoint.y  - (int) 40/2,
                                                              140,
                                                               40);
        }//end CASE 1
        else {
         /*Clear rectangle*/
         encloseRectJoin = null;
        }//else

       /*Synchronisation: Before clear send broadcast them to clients*/

       /*(1) Store shapeIds in a String [] array*/
       String [] idArray = new String[2];

       if (firstConcept != null)
         idArray[0] = firstConcept.getShapeUniqueID();
       else if (firstLinking != null)
         idArray[0] = firstLinking.getShapeUniqueID();

       if (secondConcept != null)
         idArray[1] = secondConcept.getShapeUniqueID();
       else if (secondLinking != null)
         idArray[1] = secondLinking.getShapeUniqueID();

       /*(2) Prepare a new MessageObject to broadcast the two shapes
        to join ("joinshapes")*/
       MessageObject mo_join = new MessageObject(idArray,
                                         encloseRectJoin,
                              drawConsoleUI.getUserName());

       /*(3) Send MessageObject to the Server*/
       drawConsoleUI.sendMessageToServer(mo_join);

       /*Clear variables for Cases*/
       firstConcept  = null;
       firstLinking  = null;
       secondConcept = null;
       secondLinking = null;


       /*When done switch back to the default SELECTION TOOL*/
       drawConsoleUI.setSelectedToolID(1);
       //repaint(); //not necessary here
     }//end inner else

     haveConnect = !haveConnect;
   }//end if tool selected
   /*ELSE: Select shape to drag*/
   else {

    /*CASE : ShapeConcept*/
    if (selectedShape instanceof ShapeConcept) {

     Rectangle rect = ((ShapeConcept) selectedShape).getBounds();

     offsetX = preTranslationPoint.x - rect.x;
     offsetY = preTranslationPoint.y - rect.y;

    }//end inner if
    /*CASE : ShapeLinking*/
    else if (selectedShape instanceof ShapeLinking) {

     Rectangle rect = ((ShapeLinking) selectedShape).getBounds();

     offsetX = preTranslationPoint.x - rect.x;
     offsetY = preTranslationPoint.y - rect.y;

    }//end inner if
    else if (selectedShape instanceof ShapeComment) {

     Rectangle rect = ((ShapeComment) selectedShape).getBounds();

     offsetX = preTranslationPoint.x - rect.x;
     offsetY = preTranslationPoint.y - rect.y;

    }//end inner if
    else if (selectedShape instanceof ShapeURL) {

     Rectangle rect = ((ShapeURL) selectedShape).getBounds();

     offsetX = preTranslationPoint.x - rect.x;
     offsetY = preTranslationPoint.y - rect.y;

    }//end inner if
  }//end else
 }//BUTTON 1
}//end mouse Pressed


/***
 * This method will be called by PanelLink, in order to create
 * a new Link Shape Concept
 *
 */
public void createWebLink(String description, String weblink)
                                              throws MalformedURLException{

  /*Create a bound Rectangle to enclose this JLabel*/
  int offset = addOffset(50,100);

  Rectangle encloseRect = new Rectangle(304 + offset - (int) 140/2,
                                        136 + offset - (int) 40/2, 140, 40);


  /*(2) Prepare a new MessageObject to send to the Server */
  MessageObject mo_shapeURL = new MessageObject("createshape",
                                                   "ShapeURL",
                                                  encloseRect,
                                                  description,
                                                      weblink,
                                  drawConsoleUI.getUserName(),
                               drawConsoleUI.getUserEmailAdd());


  /*(3) Send MessageObject to the Server*/
  drawConsoleUI.sendMessageToServer(mo_shapeURL);

}//end method


/***
 * This method is called by the PanelImageAsy to set the
 * name of the image to use when adding image to panel.
 *
 * @param name
 */
public void setImageDetails(String link, String size) {
  imageDetails[0] = link;  /*name of image*/
  imageDetails[1] = size; /*size choice of image*/
}


/*******************************************************************
 * Method: getLabelInPoint
 *
 * This method is used to find the object that is selected by the
 * user.
 *
 */
public ShapesInterface getLabelInPoint(Point mousePoint) {

  ShapesInterface label = null;
  boolean isFound = false;

  /*Loop the array*/
  for ( int i = this.labelLocalBuffer.size() - 1; i >= 0; i-- )
   {
    /*CASE: ShapeConcept*/
    if (labelLocalBuffer.get(i) instanceof ShapeConcept) {

    /*Get the bounds of the specific shape in i*/
	Rectangle rect = ((ShapeConcept) labelLocalBuffer.get(i)).getBounds();

    /*Check bound rectangle is contains point*/
    if (rect.contains((mousePoint)) && !isFound) {

      /*Save shape to the value to be returned*/
      label = (ShapeConcept)labelLocalBuffer.get(i);

      /*Only one object is marked as selected*/
      isFound = true;

      /*Set SELECTEDTOOL = CONCEPT*/
      if (drawConsoleUI.getSelectedToolID() != 2)
         drawConsoleUI.setSelectedToolID(1);

      break;
     }//end if
    }/*END CASE ShapeConcept*/
    else if (labelLocalBuffer.get(i) instanceof ShapeLinking) {

     /*Get the bounds of the specific shape in i*/
	 Rectangle rect = ((ShapeLinking) labelLocalBuffer.get(i)).getBounds();

     if (rect.contains((mousePoint)) && !isFound) {

      /*Save shape to the value to be returned*/
      label = (ShapeLinking) labelLocalBuffer.get(i);

      /*Only one object is marked as selected*/
      isFound = true;
      break;
     }//end if
    }/*END CASE ShapeLinking*/
    else if (labelLocalBuffer.get(i) instanceof ShapeComment) {

     /*Get the bounds of the specific shape in i*/
	 Rectangle rect = ((ShapeComment) labelLocalBuffer.get(i)).getBounds();

     /*Check bound rectangle is contains point*/
     if (rect.contains((mousePoint)) && !isFound) {

      /*Save shape to the value to be returned*/
      label = (ShapeComment)labelLocalBuffer.get(i);
  
      /*Only one object is marked as selected*/
      isFound = true;

      /*Set SELECTEDTOOL = CONCEPT*/
      if (drawConsoleUI.getSelectedToolID() != 2)
         drawConsoleUI.setSelectedToolID(3);
      break;
     }//end if
    }/*END CASE ShapeComment*/
    else if (labelLocalBuffer.get(i) instanceof ShapeURL) {

     /*Get the bounds of the specific shape in i*/
	 Rectangle rect = ((ShapeURL) labelLocalBuffer.get(i)).getBounds();

     /*Check bound rectangle is contains point*/
     if (rect.contains((mousePoint)) && !isFound) {

      /*Save shape to the value to be returned*/
      label = (ShapeURL)labelLocalBuffer.get(i);

      /*Only one object is marked as selected*/
      isFound = true;

      /*Set SELECTEDTOOL = CONCEPT*/
      //if (drawConsoleUI.getSelectedToolID() != 2)
         //drawConsoleUI.setSelectedToolID(3);
      break;
     }//end if
    }/*END CASE ShapeURL*/
   }//end for

  return label;
}//end method


/*******************************************************************
 * Method: addOffset                                               *
 *                                                                 *
 * This method is used to add a small offset when a LinkingShape   *
 * is created in order to avoid any two shapes to overlap          *
 *******************************************************************/
public int addOffset(int min, int max) {

 Random rand = new Random();

return rand.nextInt(max - min + 1) + min;
}//end method



/*******************************************************************
 * Method: offScreenShapeResize                                    *
 *                                                                 *
 * This method gets as an attribute the bound shape of a shape and *
 * checks whether the suer drag it outside the DrawCanvas. If yes  *
 * then the canvas resize it self in order to enclose the shape.   *
 *                                                                 *
 * Used with: resizeCanvas() method
 *                                                                 *
 * @param shape                                                    *
 * @return                                                         *
 *******************************************************************/
public boolean offScreenShapeResize(Rectangle shape)
{
  boolean result=false;

  if ( shape.getCenterX() >= getSize().width
    || shape.getCenterY() >= getSize().height ||
      shape.getCenterX() + shape.getWidth() < 0 ||
         shape.getCenterY() + shape.getHeight()/2 < 0 )
         {  
          //When the shape is offscreen then resize canvas
          resizeCanvas(this.getWidth() + (int) shape.getWidth()*2,
                       this.getHeight() + (int) shape.getHeight()*2);

          result = true; /*shape is off screen*/
         }//end if

     return result;
}//end method


/******************************************************************
 * This method will be called whenever the Canvas needs to resize *
 * it self.                                                       *
 *                                                                *
 * @param w                                                       *
 * @param h                                                       *
 ******************************************************************/

public void resizeCanvas(int w, int h) {
  this.setPreferredSize(new Dimension(w,h));
  revalidate();
}//end method

/***************************************************************
 *
 * SECTION : Export JPanel to a JPG
 */
void takeSnapShot(File fileToSave) {
  try {
        /*Construct a new BufferedImage*/
        BufferedImage exportImage = new BufferedImage(this.getSize().width,
                                                  this.getSize().height,
                                                  BufferedImage.TYPE_INT_RGB);

        /*Get the graphics from JPanel, use paint()*/
        this.paint(exportImage.createGraphics());

         fileToSave.createNewFile();
         ImageIO.write(exportImage, "png", fileToSave);
    } catch(Exception exe){
      System.out.println("DrawCanvas.java - Exception");
    }//catch
}//end method

   
/****************************************************************
 * SECTION : Image processing                                   *
 *
 */
public void processImage(String urlLink, int width, int height,
                         String ext, String image_name) {
  try {

   /*Buffer image from the url*/
   BufferedImage bsrc = ImageIO.read(new URL(urlLink));

   BufferedImage bdest =
      new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   Graphics2D g2 = bdest.createGraphics();
   AffineTransform at =
      AffineTransform.getScaleInstance((double)width/bsrc.getWidth(),
          (double)height/bsrc.getHeight());
   g2.drawRenderedImage(bsrc,at);

   /*Write image to a file*/
   ImageIO.write(bdest,ext,new File("src/webimages/"+ image_name));

  }//try
 catch (Exception e)
  {
    this.getParent().setVisible(false);
    JOptionPane.showMessageDialog(this, "Can not download image.");
  }//end catch
}//end method


/***
 * Method to check if the finame exists and returns the new filename
 * by adding a random string.
 */
public String checkFileName(String name) {
  File fileToSave = new File("src/webimages/"+ name);

  if (fileToSave.exists())
   return new String(addOffset(1,100) + "_" + name);
  else
   return name;
}//end method


 /***************************************************************
 * SECTION : Override Methods                                   *
 *                                                              *
 *          Implementation of override methods from Scrollable  *
 *           and MouseMotionListener                            *
 *                                                              *
 ***************************************************************/
 
 public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
  {
   return (orientation == SwingConstants.VERTICAL)
                      ? visibleRect.height/10 : visibleRect.width/10;
  }//end method

 public boolean getScrollableTracksViewportHeight()
  {
   return false;
  }//end method

 public boolean getScrollableTracksViewportWidth()
  {
   return false;
  }//end method

 public int getScrollableUnitIncrement(Rectangle visibleRect,
     	                                   int orientation, int direction)
  {
	return (orientation == SwingConstants.VERTICAL)
                        ? visibleRect.height/10 : visibleRect.width/10;
  }//end method

 public Dimension getPreferredScrollableViewportSize()
  {
	return this.getPreferredSize();
  }//end Dimension

  public void mouseDragged(MouseEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void mouseMoved(MouseEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}//end class draw canvas