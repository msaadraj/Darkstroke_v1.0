package client;
 
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

    // Example static AES key (store securely, not in code in production!)
    private static final String SECRET = "1234567890123456"; // 16 bytes for AES-128
    private static final SecretKey SECRET_KEY = new SecretKeySpec(SECRET.getBytes(), "AES");

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return plainText; // fallback
        }
    }
}
