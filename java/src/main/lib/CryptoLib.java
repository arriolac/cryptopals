package main.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public class CryptoLib {

    /**
     * Converts a string of hex values into a base64 string.
     * <p>
     * Problem #1
     *
     * @param hexString the hex string
     * @return the base64 string
     */
    public static String convertHexToBase64(@Nullable String hexString) {
        if (hexString == null) {
            return null;
        }
        final byte[] bytes = decodeHexString(hexString);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Returns a string as a result of performing a bitwise XOR operation between {@code lhs} and {@code rhs}.
     *
     * @param lhs the 1st string
     * @param rhs the 2nd string
     * @return the XOR'd string
     */
    public static String fixedXOR(@Nullable String lhs, @Nullable String rhs) {
        if (lhs == null || rhs == null) {
            return null;
        }

        if (lhs.length() != rhs.length()) {
            throw new IllegalArgumentException("Lengths do not match " + lhs.length() + ", " + rhs.length());
        }

        final byte[] lhsBytes = decodeHexString(lhs);
        final byte[] rhsBytes = decodeHexString(rhs);
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < lhsBytes.length; i++) {
            final byte xoredByte = (byte) (lhsBytes[i] ^ rhsBytes[i]);
            final int firstDigit = (xoredByte & 0xF0) >> 4;
            final int secondDigit = xoredByte & 0x0F;
            stringBuilder.append(Character.forDigit(firstDigit, 16))
                    .append(Character.forDigit(secondDigit, 16));
        }
        return stringBuilder.toString();
    }

    /**
     * Returns the single byte XOR cipher for the given string.
     * @param input the input string
     * @return the cipher
     */
    public static char findSingleByteXorCipher(@NotNull String input) {
        int currentMax = Integer.MIN_VALUE;
        char match = 0;
        for (char c = 0; c < 256; c++) {
            final String fixedXOR = applySingleByteXorCipher(input, c);
            final byte[] resultBytes = CryptoLib.decodeHexString(fixedXOR);
            final String decoded = new String(resultBytes);
            final int charCount = countAlpha(decoded);
            if (countAlpha(decoded) > currentMax) {
                match = c;
                currentMax = charCount;
            }
        }
        return match;
    }

    /**
     * Applies the xor operation to {@code input} using the single byte cipher {@code cipher}.
     * @param input the input string
     * @param cipher the cipher to apply
     * @return the result, hex encoded
     */
    public static String applySingleByteXorCipher(@NotNull String input, char cipher) {
        final String cipherInHex = Integer.toHexString(cipher);
        final String repeatedCipher = cipherInHex.repeat(input.length() / cipherInHex.length());
        return CryptoLib.fixedXOR(input, repeatedCipher);
    }

    /**
     * Counts the number of alphabets in {@code input}.
     * @param input the input string
     * @return the number of alphabets
     */
    public static int countAlpha(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            final char currentChar = input.charAt(i);
            count += Character.isAlphabetic(currentChar) ? 1 : 0;
        }
        return count;
    }

    public static byte[] decodeHexString(String hexString) {
        final int hexStringLength = hexString.length();
        if (hexStringLength % 2 != 0) {
            throw new IllegalArgumentException("Hex string not of even length.");
        }

        byte[] decodedHexString = new byte[hexStringLength / 2];
        for (int i = 0; i < hexStringLength; i += 2) {
            final int firstCharValue = Character.digit(hexString.charAt(i), 16);
            final int secondCharValue = Character.digit(hexString.charAt(i + 1), 16);
            final byte currentByte = (byte) ((firstCharValue << 4) + secondCharValue);
            decodedHexString[i / 2] = currentByte;
        }
        return decodedHexString;
    }
}
