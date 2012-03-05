package CoreServer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/***
 * This class is responsible to encrypt user password for
 * email authentication. The encoder version is stored, then
 * is decoded to be used when sending an email.
 * Algorithm is DES
 *
 *
 * @author Klitos Christodouloy
 * @email christk6@cs.man.ac.uk
 */
public class ServerEncryption {

 private Cipher cipher = null;
 private SecretKeyFactory keyFactory = null;
 private SecretKey key = null;
 private DESKeySpec keySpec = null;
 private String secretKey = "eLearningMapsABCDEFGHIJKLMNO987654321&^%$£";
 private sun.misc.BASE64Encoder base64encoder = null;
 private sun.misc.BASE64Decoder base64decoder = null;


 public ServerEncryption() {
  try {
   keySpec = new DESKeySpec(secretKey.getBytes("UTF8"));
   keyFactory = SecretKeyFactory.getInstance("DES");
   key = keyFactory.generateSecret(keySpec);
   base64encoder = new BASE64Encoder();
   base64decoder = new BASE64Decoder();
  } catch (Exception e) {
    System.out.println("ServerEncryption - Exception");
  }
 }

 /***
  * This method will do the encryption 
  * 
  * @param s
  * @return
  */
 public String doEncryption(String s) {

  String encrypedPwd = "";

  try {
   byte[] cleartext = s.getBytes("UTF8");

   Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
   cipher.init(Cipher.ENCRYPT_MODE, key);
   encrypedPwd = base64encoder.encode(cipher.doFinal(cleartext));

   } catch (Exception e) {
     System.out.println("ServerEncryption - Exception " + e);
   }

  return encrypedPwd;
 }//end method

 /***
  * This method will decrypt the encrypted string
  *
  * @param s
  * @return
  */
 public String doDecrypt(String s) {

  byte[] plainTextPwdBytes = null;

  try {

   byte[] encrypedPwdBytes = base64decoder.decodeBuffer(s);

   cipher = Cipher.getInstance("DES");// cipher is not thread safe
   cipher.init(Cipher.DECRYPT_MODE, key);
   plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));

   } catch (Exception e) {
     System.out.println("ServerEncryption - Exception " + e);
   }

  return new String(plainTextPwdBytes);
 }//end method


}//end class
