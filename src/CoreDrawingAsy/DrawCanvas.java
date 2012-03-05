package CoreDrawingAsy;

import CoreDrawing.CoreShapes.ShapeComment;
import CoreDrawing.CoreShapes.ShapeConcept;
import CoreDrawing.CoreShapes.ShapeLinking;
import CoreDrawing.CoreShapes.ShapeURL;
import CoreDrawing.CoreShapes.ShapesInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

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

public class DrawCanvas extends JPanel
                           implements Scrollable, MouseMotionListener
 {

 /***
  * Declaration of Properties/Variables
  */
 
 /*Lists*/
 private List labelBuffer;       /*Hold a list of JLabels*/

 /*Reference to the DrawConsoleUI class*/
 private DrawConsoleUI drawConsoleUI = null;

 /*Filename of Image to be inserted*/
  private String imageToInsert = "";

 /*Hold a reference to the selected shape*/
 ShapesInterface selectedShape = null;

 /*Hold a reference to a sticky note*/
 ShapeComment stickyNoteSelected = null;

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

 /*Listen to key events*/
 int selectedShapeIndex = -1; /*get the index of the selected shape
                                 .Use it when delete*/

 /************************************/

 
 /**
  * Constructor
  */
 public DrawCanvas(DrawConsoleUI drawConsole) {
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
  labelBuffer  = Collections.synchronizedList(new ArrayList());

  /*Hold a reference to the DrawConsoleUI class*/
  drawConsoleUI = drawConsole;

  
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
  for (int i=0; i<labelBuffer.size(); i++)
   {
     /*Get the Concept for each index i*/
     ShapesInterface currentConcept = (ShapesInterface) labelBuffer.get(i);

 
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


/*******************************************************************
 * Method: panelMouseClicked                                       *
 */
 private void panelMouseClicked(java.awt.event.MouseEvent evt) throws Exception
 {
   /*Monitor which mouse button was clicked.*/
   /*LEFT*/
   if (evt.getButton() == MouseEvent.BUTTON1)
    {

     Point pointClicked = evt.getPoint();

     /*Read and save the object on that Point*/
     ShapesInterface currentShape = getLabelInPoint(pointClicked);

      /*Chek if something ws clicked and open URL*/
      if (currentShape != null) {
         if  (currentShape instanceof ShapeURL) {
           /*If shit button is pressed then fire URL*/
           if (evt.isShiftDown())
           ((ShapeURL) currentShape).openURL();
         }//end if
      }//end if


    }//end LEFT mouse btn
   /*MIDDLE*/
   else if (evt.getButton() == MouseEvent.BUTTON2) {}
   /*RIGHT*/
   else if (evt.getButton() == MouseEvent.BUTTON3) {
    /***
     * Draw a concept by clicking the right mouse button
     */
     Point pointClicked = evt.getPoint();

     /*Read and save the object on that Point*/
     ShapesInterface currentShape = getLabelInPoint(pointClicked);

     /*CASE: WEBLINK*/
      /*Chek if something ws clicked and open URL*/
      if (currentShape != null) {
         if  (currentShape instanceof ShapeURL) {
           ((ShapeURL) currentShape).editConceptShapeText();
         }
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

       
       int defaultWidth  = 140;
       int defaultHeight = 40;
       
       /*Create a bound Rectangle to enclose this JLabel*/
        Rectangle encloseRect = new Rectangle(pointClicked.x - (int) defaultWidth/2,
                                              pointClicked.y - (int) defaultHeight/2,
                                              defaultWidth,
                                              defaultHeight);


        /*Add the JLabel in the array of JLabels*/
        /*Edw na valw na pianei to username that created that concept*/
        ShapeConcept sc = new ShapeConcept(encloseRect,"This is a concept",
                                                        "UserTest", "id");

       /*Add this ShapeConcept in the bufferList*/
       this.labelBuffer.add(sc);

       /*Draw the ShapeConcept object on the JPanel*/
       this.add(sc);

       //System.out.println("X: " + evt.getX() + " - Y: " + evt.getY());
       
       repaint();
      }//end if point is contained
      else {
        if (currentShape instanceof ShapeConcept)
         /*Call the rename method of this ShapeConcept*/
         currentShape.editConceptShapeText();
        else if (currentShape instanceof ShapeLinking)
         /*Call the rename method of this ShapeLinking*/
         currentShape.editConceptShapeText();
      }//inner else
     }//end if

     /****
      * Else if : the tool selected is Notes
      */
     else if (drawConsoleUI.getSelectedToolID() == 3) {
 
      /*Check whether the Point clicked is not within another
       ShapeConcept*/
      if (currentShape == null) {

       /*Create a bound Rectangle to enclose this JLabel*/
       Rectangle commentRect = new Rectangle(pointClicked.x - 10,
                                             pointClicked.y - 10,
                                             20,
                                             20);
       
       /*Create a new Note Shape (ShapeComment)*/
       //Dame na ala3w ton user
       ShapeComment note = new ShapeComment(commentRect,"UserTest",
                                                        "email@email",
                                                       "Add a note",
                                                               "id");

       /*Add this ShapeConcept in the bufferList*/
       this.labelBuffer.add(note);

       /*Draw the ShapeConcept object on the JPanel*/
       this.add(note);
       
       repaint();

      }//end if
      else {
       if (currentShape instanceof ShapeComment)
         //call edit ShapeCommentPanel
         currentShape.editConceptShapeText();
      }//end else
     }//end if
     else if (drawConsoleUI.getSelectedToolID() == 4) {
      if (currentShape == null) {

        Rectangle iconRect = new Rectangle(pointClicked.x - 10,
                                         pointClicked.y - 10,
                                             20,20);


       /*Create a new ShapeConcept with icon*/
       ShapeConcept addImage = new ShapeConcept(iconRect,"UserTest",
                                                 this.imageToInsert,
                                                              "id",
                                                              null,0);

       /*Add this ShapeConcept in the bufferList*/
       this.labelBuffer.add(addImage);

       /*Draw the ShapeConcept object on the JPanel*/
       this.add(addImage);       
      }
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
   }//end ShapeURL
  }//end if

 /**When mouse is released, finish dragging just reset values**/
 releasePoint = null;
 mouseIsDragged = false;
 //selectedShape = null;
 repaint();
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

 if (this.selectedShape != null)
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

   /*Read and save the object on that Point*/
   selectedShape = getLabelInPoint(pointClicked);

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

        /***************
         * START CASES *
         ***************/

        /*CASE-1: first=Concept & sec=Concept*/
        if (firstConcept !=null && secondConcept !=null && firstLinking == null && secondLinking == null) {

         /*Find the middle between the two points*/
         Point p1 = firstConcept.getCenter();
         Point p2 = secondConcept.getCenter();

         /*Find the mid point of the line*/
         Point midPoint = new Point((p1.x + p2.x)/2,(p1.y + p2.y)/2);

         /*Find the rectangle at the center*/
         Rectangle encloseRect = new Rectangle(midPoint.x - (int) 140/2,
                                               midPoint.y - (int) 40/2,
                                               140,
                                               40);

         /*Create a new LinkingPhaze*/
         ShapeLinking linkPhraze = new ShapeLinking(encloseRect,"This is a link",
                                                               "UserTest", "id");

         /*Save Link phraze it in the shape buffer List*/
         this.labelBuffer.add(linkPhraze);

         /*Draw the ShapeConcept object on the JPanel*/
         this.add(linkPhraze);

         /**/
         if (! firstConcept.isEntry(linkPhraze)){
          linkPhraze.addComponent(firstConcept);
         }

         if (! linkPhraze.isEntry(secondConcept)){
          secondConcept.addComponent(linkPhraze);
         }

       }//END CASE 1
       /*CASE-2: first=Concept & sec=Link*/
       else if (firstConcept != null && secondConcept == null && firstLinking == null && secondLinking != null) {
       
        if (! firstConcept.isEntry(secondLinking))
           secondLinking.addComponent(firstConcept);
         else {
          /*Detect two way*/

          //tempConcept.addComponent(getLabelInPoint(pointClicked));
          //System.out.println("Detect two way");
         }//else
       }//END CASE 2
       /*CASE-3: first=Link & sec=Concept*/
       else if (firstConcept == null && secondConcept != null && firstLinking != null && secondLinking == null) {
       
        if (! firstLinking.isEntry(secondConcept))
           secondConcept.addComponent(firstLinking);
         else {
          /*Detect two way*/

          //tempConcept.addComponent(getLabelInPoint(pointClicked));
          //System.out.println("Detect two way");
         }
       }//END CASE 3
       /*CASE-4: first=Link & sec=Link*/
       else if (firstConcept == null && secondConcept == null && firstLinking != null && secondLinking != null) {
      }//END CASE 4

       /*Testing*/
       //System.out.println("firstConcept  : " + firstConcept);
       //System.out.println("firstLinking  : " + firstLinking);
       //System.out.println("secondConcept : " + secondConcept);
       //System.out.println("secondLinking : " + secondLinking);


       /*Clear variables for Cases*/
       firstConcept  = null;
       firstLinking  = null;
       secondConcept = null;
       secondLinking = null;

       /*When done switch back to the default SELECTION TOOL*/
       drawConsoleUI.setSelectedToolID(1);
       repaint();
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
public void createWebLink(String description, String weblink) throws MalformedURLException{

  /*Create a bound Rectangle to enclose this JLabel*/
  Rectangle encloseRect = new Rectangle(304 - (int) 140/2,
                                        136 - (int) 40/2, 140, 40);


  /*Add the JLabel in the array of JLabels*/
  ShapeURL webLink = new ShapeURL(encloseRect,"UserTest",
                                  weblink,description,"id",0);

 
  /*Add this ShapeConcept in the bufferList*/
  this.labelBuffer.add(webLink);

  /*Draw the ShapeConcept object on the JPanel*/
  this.add(webLink);

  /*Refresh screen*/
  repaint();
}//end method


/***
 * This method is called by the PanelImageAsy to set the
 * name of the image to use when adding image to panel.
 * 
 * @param name
 */
public void setImageName(String name) {
  imageToInsert = name;
}

/*******************************************************************
 * Method: getLabelInPoint
 *
 * Description: This method return the JLabel Object that contains
 * the point supplied as an argument. Basically it loops through
 * the List of all JLabels and then it finds the index and return
 * the JLabel object in that position.
 */
public ShapesInterface getLabelInPoint(Point mousePoint) {

  ShapesInterface label = null;
  boolean isFound = false;
  
  /*Reset selectedShapeIndex to = -1*/
  selectedShapeIndex = -1;

  /*Loop the array*/
  for ( int i = this.labelBuffer.size() - 1; i >= 0; i-- )
   {
    /*CASE: ShapeConcept*/
    if (labelBuffer.get(i) instanceof ShapeConcept) {

    /*Highlight this ShapeConcept*/
    ((ShapeConcept) labelBuffer.get(i)).hightlight(false);

	Rectangle rect = ((ShapeConcept) labelBuffer.get(i)).getBounds();

    if (rect.contains((mousePoint)) && !isFound) {
      label = (ShapeConcept)labelBuffer.get(i);
      ((ShapeConcept) labelBuffer.get(i)).hightlight(true);

      /*Only one object is marked as selected*/
      isFound = true;

      /*Store the index of this shape*/
      selectedShapeIndex = i;

      /*Set too to draw concept*/
      if (drawConsoleUI.getSelectedToolID() != 2)
         drawConsoleUI.setSelectedToolID(1);

     }//end if
    }/*END CASE ShapeConcept*/
    else if (labelBuffer.get(i) instanceof ShapeLinking) {

	 Rectangle rect = ((ShapeLinking) labelBuffer.get(i)).getBounds();

     if (rect.contains((mousePoint)) && !isFound) {
      label = (ShapeLinking) labelBuffer.get(i);
 
      /*Only one object is marked as selected*/
      isFound = true;

      /*Store the index of this shape*/
      selectedShapeIndex = i;
      break;
     }//end if
    }/*END CASE ShapeLinking*/
    else if (labelBuffer.get(i) instanceof ShapeComment) {

    /*Highlight this ShapeConcept*/
    ((ShapeComment) labelBuffer.get(i)).hightlight(false);

	Rectangle rect = ((ShapeComment) labelBuffer.get(i)).getBounds();

    if (rect.contains((mousePoint)) && !isFound) {
      label = (ShapeComment)labelBuffer.get(i);
      ((ShapeComment) label).hightlight(true);

      /*Only one object is marked as selected*/
      isFound = true;

      /*Store the index of this shape*/
      selectedShapeIndex = i;

      /*Set tool to draw concept*/
      if (drawConsoleUI.getSelectedToolID() != 2)
         drawConsoleUI.setSelectedToolID(3);
     
     }//end if
    }/*END CASE ShapeConcept*/
    else if (labelBuffer.get(i) instanceof ShapeURL) {

    /*Highlight this ShapeURL*/

	Rectangle rect = ((ShapeURL) labelBuffer.get(i)).getBounds();

    if (rect.contains((mousePoint)) && !isFound) {
      label = (ShapeURL)labelBuffer.get(i);

      /*Only one object is marked as selected*/
      isFound = true;

      /*Store the index of this shape*/
      selectedShapeIndex = i;

      //break;
     }//end if
    }/*END CASE ShapeURL*/
   }//end for

  return label;
}//end method


/***
 * Get the selected index, used for delete
 */
public int getSelectedShapeIndex(){
  return this.selectedShapeIndex;
}

public void setSelectedShapeIndex(int s){
  this.selectedShapeIndex = s;
}

public List getLabelBuffer() {
    return this.labelBuffer;
}

public void removeJLabel(java.awt.Component comp) {
  this.remove(comp);
  /*Refresh window*/
  repaint();
}



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
         ImageIO.write(exportImage, "PNG", fileToSave);
    } catch(Exception exe){
      System.out.println("DrawCanvas.java - Exception");
    }//catch
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

  public void keyTyped(KeyEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void keyPressed(KeyEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void keyReleased(KeyEvent e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }


}//end class draw canvas
