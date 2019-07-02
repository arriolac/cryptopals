package main.lib;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.stream.IntStream;

public class AES {

    /**
     * Performs AES decryption using the Cipher Block Chaining (CBC) mode.
     *
     * @param cipherText           the text to decipher in base 64
     * @param key                  the AES key
     * @param initializationVector the initialization vector (IV) for the first block
     * @return the decrypted text
     */
    public static String cbcDecrypt(String cipherText, String key, String initializationVector) throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {
        final byte[] cipherBlob = Base64.getDecoder().decode(cipherText);

        // Initialize Cipher object
        final SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        final Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Run CBC Mode Decryption
        final int blockSize = key.length();
        byte[] previousCipherBlock = initializationVector.getBytes();
        final StringBuilder decipheredText = new StringBuilder();
        for (int i = 0; i < cipherBlob.length; i += blockSize) {
            final int startIndex = i;
            final byte[] currentCipherBlock = Bytes.toPrimitive(
                    IntStream.range(0, blockSize)
                            .mapToObj(j -> cipherBlob[startIndex + j])
                            .toArray(Byte[]::new)
            );
            final byte[] currentDecipheredBlock = cipher.doFinal(currentCipherBlock);
            final byte[] xorDecryptedBlock = CryptoLib.fixedXOR(currentDecipheredBlock, previousCipherBlock);
            decipheredText.append(new String(xorDecryptedBlock));
            previousCipherBlock = currentCipherBlock;
        }

        return decipheredText.toString();
    }
}
