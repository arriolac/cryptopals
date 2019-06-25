package main.lib;

import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public class Hex {

    /**
     * Converts a string of hex values into a base64 string.
     * <p>
     * Problem #1
     *
     * @param hexString the hex string
     * @return the base64 string
     */
    public static String convertToBase64(@Nullable String hexString) {
        if (hexString == null) {
            return null;
        }
        final byte[] bytes = Hex.decode(hexString);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encode(byte[] byteArray) {
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

    public static byte[] decode(String hexString) {
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
