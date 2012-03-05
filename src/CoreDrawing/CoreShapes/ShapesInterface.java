package CoreDrawing.CoreShapes;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


/***************************************************************
 * Force all eLearningMaps shapes to implement those methods   *
 *                                                             *
 *                                                             *
 * @author Klitos Christodoulou                                *
 * email : christk6@cs.man.ac.uk                               *
 ***************************************************************/

public interface ShapesInterface 
{
 /***
  * Classes that implement this interface must give an
  * implementation of this method
  *
  * @param g
  */

 public ArrayList<Component> getConnComponenetsList();

 public Point getCenter();

 public void editConceptShapeText();

 public String getShapeUniqueID();

 public void translateShape(int x1, int y1);

 public void setWaitColour(String str);

 public String getWaitColour();

 public Point getTopLeftPoint();

     
  /*Provide a custom implementation to the toString(). The method
   will generate an XML representation of eLearningMaps shape*/
  public String toStringXML();
}//end Interface