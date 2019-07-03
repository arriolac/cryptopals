package test.lib;

import main.lib.AES;
import main.lib.Hex;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AESTests {

    @Test
    public void testGenerateKey() {
        assertEquals(16, AES.generateKey().getBytes().length);
    }

    // Problem # 8
    @Test
    public void testIsAesEcbEncrypted() {
        final String plaintextString = "yellow submarine some other str yellow submarine";
        final SecretKey key = new SecretKeySpec("MY NAME IS CHRIS".getBytes(), "AES");
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final byte[] encryptedBytes = cipher.doFinal(plaintextString.getBytes());
            final String encryptedHexString = Hex.encode(encryptedBytes);
            assertTrue(AES.isEcbModeEncrypted(encryptedHexString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
