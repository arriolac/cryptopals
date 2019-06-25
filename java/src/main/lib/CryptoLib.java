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
     * Returns a string as a result of performing a bitwise XOR operation between the hex strings {@code lhs} and
     * {@code rhs}.
     *
     * @param hex1 the 1st hex string
     * @param hex2 the 2nd hex string
     * @return the XOR'd string in hex
     */
    public static String fixedXOR(@Nullable String hex1, @Nullable String hex2) {
        if (hex1 == null || hex2 == null) {
            return null;
        }

        final byte[] lhsBytes = decodeHexString(hex1);
        final byte[] rhsBytes = decodeHexString(hex2);
        final byte[] xorBytes = fixedXOR(lhsBytes, rhsBytes);
        return decodeToHexString(xorBytes);
    }

    /**
     * Applies the XOR bitwise operation to the provided 2 byte arrays
     *
     * @return the result of the bitwise operation
     */
    public static byte[] fixedXOR(byte[] lhs, byte[] rhs) {
        if (lhs.length != rhs.length) {
            throw new IllegalArgumentException("Byte array sizes don't match.");
        }
        final byte[] fixedXor = new byte[lhs.length];
        for (int i = 0; i < lhs.length; i++) {
            fixedXor[i] = (byte) (lhs[i] ^ rhs[i]);
        }
        return fixedXor;
    }

    /**
     * Returns the single byte XOR cipher for the given string.
     *
     * @param input the input string
     * @return the cipher
     */
    public static char findSingleByteXorCipher(@NotNull String input) {
        int currentMax = Integer.MIN_VALUE;
        char match = 0;
        for (char c = 0; c < 256; c++) {
            final String fixedXOR = repeatingKeyXor(input, c);
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
     * Applies the repeating key xor operation to the hex string {@code input} using the single byte
     * cipher {@code cipher}.
     *
     * @param hexInput  the input string
     * @param cipher the cipher to apply
     * @return the result, hex encoded
     */
    public static String repeatingKeyXor(@NotNull String hexInput, char cipher) {
        final String cipherInHex = Integer.toHexString(cipher);
        final String repeatedCipher = cipherInHex.repeat(hexInput.length() / cipherInHex.length());
        return CryptoLib.fixedXOR(hexInput, repeatedCipher);
    }

    /**
     * Applies the repeating key xor operation to the input string {@code input} by repeating the string {@code key}
     * such that the length of the repeating key matches that of {@code input}.
     * @param input the input
     * @param key the key to repeat
     * @return the result, hex encoded
     */
    public static String repeatingKeyXor(@NotNull String input, @NotNull String key) {
        final byte[] inputInBytes = input.getBytes();

        String repeatedKey = key.repeat(input.length() / key.length());
        final int lengthDiff = input.length() - repeatedKey.length();
        if (lengthDiff != 0) {
            repeatedKey += key.substring(0, lengthDiff);
        }
        final byte[] repeatedKeyInBytes = repeatedKey.getBytes();

        final byte[] xorResult = fixedXOR(repeatedKeyInBytes, inputInBytes);
        return decodeToHexString(xorResult);
    }

    /**
     * Counts the number of alphabets in {@code input}.
     *
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

    public static String decodeToHexString(byte[] byteArray) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            final byte currByte = byteArray[i];
            final int firstDigit = (currByte & 0xF0) >> 4;
            final int secondDigit = currByte & 0x0F;
            stringBuilder.append(Character.forDigit(firstDigit, 16))
                    .append(Character.forDigit(secondDigit, 16));
        }
        return stringBuilder.toString();
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
