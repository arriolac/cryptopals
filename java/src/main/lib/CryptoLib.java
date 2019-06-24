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
            throw new IllegalArgumentException("Lengths do not match");
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
    public static char singleByteXorCipher(@NotNull String input) {
        int currentMax = Integer.MIN_VALUE;
        char match = 0;
        for (int i = 0; i < 256; i++) {
            final String hexValue = Integer.toHexString(i);
            final String candidate = hexValue.repeat(input.length() / hexValue.length());
            final String fixedXOR = CryptoLib.fixedXOR(input, candidate);
            final byte[] resultBytes = CryptoLib.decodeHexString(fixedXOR);
            final String decoded = new String(resultBytes);
            final int charCount = countAlpha(decoded);
            if (countAlpha(decoded) > currentMax) {
                match = (char) i;
                currentMax = charCount;
            }
        }
        return match;
    }

    private static int countAlpha(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            final char currentChar = input.charAt(i);
            count += Character.isAlphabetic(currentChar) ? 1 : 0;
        }
        return count;
    }

    private static byte[] decodeHexString(String hexString) {
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
