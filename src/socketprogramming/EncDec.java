/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming;

/**
 *
 * @author user
 */
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

class EncDec {

    private static final String ALGO = "AES";
    private static final byte[] keyValue =
           new byte[]{'S', 'a', '2', 'n', 'Y', '_', '0',
                'u', '=', '!', '@', '%', 't', 'K', '5', ';'};
    private static final String UNICODE_FORMAT  = "UTF8";

    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes(UNICODE_FORMAT));
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

}
