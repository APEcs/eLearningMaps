package elearningmaps;

import CoreServer.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*******************************************************************
 * This is the main class that containts the main() method. This class
 * will be called first. The class will just call the creation of the
 * SERVER GUI.
 *
 * @author Klitos Christodoulou
 * Created on 02-Jun-2010, 12:30:12
 * @version 1
 */
public class ServerComponent {

/**
 * @param args the command line arguments
 */
 public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
       public void run() {
        try {

          /*Let the OS choose the Look and Feel*/
          UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

          new ServerConsoleUI().setVisible(true);

        } catch (ClassNotFoundException ex) {
          Logger.getLogger(ServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
          Logger.getLogger(ServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(ServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
          Logger.getLogger(ServerComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
         }
     });
  }//end main method
}//end ServerComponent Class
