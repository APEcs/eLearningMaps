
package elearningmaps;

import CoreClient.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*****************************************************************
 * This class is responsible for calling the Login Scree GUI     *
 * provided by the ClientConsoleUI class.                        *
 *                                                               *
 * @author Klitos Christodoulou                                  *
 * Created on 04-Jun-2010, 10:30:13                              *
 * @version 1                                                    *
 *****************************************************************/

public class ClientComponent {
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
        try {
          /*Let the OS determine Look and Feel*/
          UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
          
          new ClientConsoleUI().setVisible(true);
          
        } catch (ClassNotFoundException ex) {
          Logger.getLogger(ClientComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
          Logger.getLogger(ClientComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(ClientComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
          Logger.getLogger(ClientComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
        });
    }//end main
}//end clientComponent class
