package main.lib;

import org.jetbrains.annotations.Nullable;

import javax.sound.midi.Sequence;
import java.util.Arrays;
import java.util.Base64;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

public class CryptoLib {

    /**
     * Converts a string of hex values into a base64 string.
     *
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
