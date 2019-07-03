package main.lib;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class AES {

    /**
     * Generates a random 16 byte key.
     *
     * @return the generated key
     */
    public static String generateKey() {
        final int keySize = 16;
        return IntStream.range(0, keySize)
                .mapToDouble(i -> Math.random())
                .mapToInt(i -> (int) (i * 127))
                .mapToObj(i -> Character.toString((char) i))
                .reduce("", String::concat);
    }

    /**
     * This function will encrypt the provided input using either AES in ECB mode or in CBC mode (randomly determined).
     *
     * @param input the input text to encrypt
     * @return the encrypted text
     */
    public static String encryptionOracle(String input) throws
            IllegalBlockSizeException,
            InvalidKeyException,
            BadPaddingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException {
        // Append random bytes before and after
        final int paddingByteSize = (int) ((Math.random() * 5) + 5);
        final String padding = "C".repeat(paddingByteSize);
        final String paddedInput = PKCS.applyPadding(padding + input + padding, 16);
        System.out.println(String.format("Input: '%s'", paddedInput));

        // Encrypt either ECB or CBC
        final int randomInt = new Random().nextInt(2);
        final byte[] plainText = paddedInput.getBytes();
        final String key = generateKey();
        if (randomInt == 1) {
            System.out.println("ECB Encrypting...");
            return new String(ecb(plainText, key, Cipher.ENCRYPT_MODE));
        } else {
            System.out.println("CBC Encrypting...");
            return cbc(plainText, key, generateKey(), Cipher.ENCRYPT_MODE);
        }
    }

    /**
     * Performs AES decryption using the Electronic Codebook (ECB) mode.
     *
     * @param cipherText the text to decipher in base 64
     * @param key        the AES key
     * @return the decrypted text
     */
    public static String ecbDecrypt(String cipherText, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final byte[] target = Base64.getDecoder().decode(cipherText);
        return new String(ecb(target, key, Cipher.DECRYPT_MODE));
    }

    private static byte[] ecb(byte[] blob, String key, int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        final Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
        cipher.init(cipherMode, secretKey);
        return cipher.doFinal(blob);
    }

    /**
     * Performs AES decryption using the Cipher Block Chaining (CBC) mode.
     *
     * @param cipherBlob           the blob to decipher
     * @param key                  the AES key
     * @param initializationVector the initialization vector (IV) for the first block
     * @return the decrypted text
     */
    public static String cbcDecrypt(byte[] cipherBlob, String key, String initializationVector) throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {
        return cbc(cipherBlob, key, initializationVector, Cipher.DECRYPT_MODE);
    }

    private static String cbc(byte[] blob, String key, String initializationVector, int mode) throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {
        final int blockSize = key.length();
        byte[] previousCipherBlock = initializationVector.getBytes();
        final StringBuilder decipheredText = new StringBuilder();
        final boolean isEncrypting = (mode == Cipher.ENCRYPT_MODE);
        for (int i = 0; i < blob.length; i += blockSize) {

            // Get current block
            final int startIndex = i;
            final byte[] currentCipherBlock = Bytes.toPrimitive(
                    IntStream.range(0, blockSize)
                            .mapToObj(j -> blob[startIndex + j])
                            .toArray(Byte[]::new)
            );

            // Encrypt or decrypt
            if (isEncrypting) {
                final byte[] xoredBlock = CryptoLib.fixedXOR(currentCipherBlock, previousCipherBlock);
                final byte[] encryptedBlock = ecb(xoredBlock, key, mode);
                decipheredText.append(new String(encryptedBlock));
                previousCipherBlock = encryptedBlock;
            } else {
                final byte[] currentDecipheredBlock = ecb(currentCipherBlock, key, mode);
                final byte[] xorDecryptedBlock = CryptoLib.fixedXOR(currentDecipheredBlock, previousCipherBlock);
                decipheredText.append(new String(xorDecryptedBlock));
                previousCipherBlock = currentCipherBlock;
            }
        }

        return decipheredText.toString();
    }

    /**
     * Returns whether or not the provided hex string is AES ECB mode encrypted. This method uses the fact that the
     * same 16 byte plain text will result in the same 16 byte ciphertext using AES in ECB mode.
     *
     * @param hexString the hex string
     * @return true if the provided hex string is AES ECB mode encrypted, false, otherwise
     */
    public static boolean isEcbModeEncrypted(String hexString) {
        final Set<String> seenStrings = new HashSet<>();
        final int blockSize = 16;
        for (int i = 0; i <= hexString.length() - blockSize; i += blockSize) {
            final int endIndex = (i + blockSize <= hexString.length()) ? i + blockSize : hexString.length();
            final String currentString = hexString.substring(i, endIndex);
            if (seenStrings.contains(currentString)) {
                return true;
            }
            seenStrings.add(currentString);
        }
        return false;
    }
}
