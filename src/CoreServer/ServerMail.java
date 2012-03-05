package CoreServer;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**************************************************************************
 * Class : Represents Mail Service for the Server.                        *
 * A simple Mail server that will do get all details from GUI             *
 * setup service and then sent an email to the client that                *
 * request the forgotten password.                                        *
 *                                                                        *
 * External Libraries : This class is using external libraries            *
 * which can be found in the "mail" folder.                               *
 *                                                                        *
 * @author Klitos Christodoulou                                           *
 * @email christk6@cs.man.ac.uk                                           *
 *                                                                        *
 **************************************************************************/

public class ServerMail {

  /*Variable Declarations*/
  private String client_username = "";
  private String host = "";
  private int port;
  private String mail_username = "";
  private String password = "";
  private String recoverPass = "";
  private Properties props = null;
  private Session mailSession = null;


  /*Constructor*/
  public ServerMail(String clientu, String h, int p,
                    String mailusern, String passw, String recoverP) {
    
    /*Store mail parameters*/
    client_username = clientu;
    host = h;
    port = p;
    mail_username = mailusern;
    password = passw;
    recoverPass = recoverP;

    props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    mailSession = Session.getInstance(props);
  }//end constructor

  
  /***
   * This methos id responsible for sending the mail to the
   * client.
   */
  public void sendMail(String fromMail, String toMail) {

   try {
     /*Setup a new message*/
     Message message = new MimeMessage(mailSession);
	 message.setFrom(new InternetAddress(fromMail));

     /*Recipient mail*/
	 message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(toMail));

     /*Message body and subject*/
	 message.setSubject("eLearningMaps - Pass Recovery");
	 
     message.setText("Dear " + client_username + ",\n\n Your password for the service" +
       " is : " + recoverPass + "\n\n Please do not reply to this email." );

     /*Transport */
	 Transport trans = mailSession.getTransport("smtp");

     /*Mean that port is not necessary*/
     if (port == -1) {
	   trans.connect(host, mail_username, password);
     }//end if
     else {
       trans.connect(host, port, mail_username, password);
     }
     trans.sendMessage(message,
     message.getRecipients(Message.RecipientType.TO));

     /*Close connection*/
     trans.close();

   	} catch (Exception e) {
      System.out.println("Exception - ServerMail class");
      e.getStackTrace();
    }//catch
  }//end method

}//end ServeMail
