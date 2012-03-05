package CoreDrawing.CoreShapes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.border.LineBorder;

/******************************************************************
 *                                                                *
 * RoundBorder.java                                               *
 *                                                                *
 * This class construct a custom border with Rounded Corners that *
 * is used to draw the Rectangle that enclose the JLabel.         *
 *                                                                *
 *                                                                *
 * @author Klitos Christodoulou                                   *
 * email: christk6@cs.man.ac.uk                                   *
 *                                                                *
 * Created on 17 June 2010, 21:32                                 *
 ******************************************************************/

public class RoundBorder extends LineBorder
 {
  private int arcW = 15;
  private int arcH = 15;
  private Color colour = null;
  private String color = "b";

/*Constructor*/
public RoundBorder(Color clr, int thickness) {
 
  super(clr, thickness);
  colour = clr;
}//end constructor


/*Get the Colour of this border*/
public String getBorderColor() {
  return this.color;
}//end method

/*Set a string that will represent the colour*/
public void setBorderColor(String t) {
  this.color = t;
}//end method

@Override
/*****
 * Method: paintBorder()
 *
 * Override the existing method from the superclass in order to
 * draw the custome border.
 *
 */
public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
 {
  Graphics2D g2 = (Graphics2D) g;

  g2.setColor(colour);
  g2.drawRoundRect(x, y, width - 1, height - 1, arcW, arcH);
 }//end
}//end class
