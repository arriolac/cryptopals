package main.lib;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class CryptoLib {

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

        final byte[] lhsBytes = Hex.decode(hex1);
        final byte[] rhsBytes = Hex.decode(hex2);
        final byte[] xorBytes = fixedXOR(lhsBytes, rhsBytes);
        return Hex.encode(xorBytes);
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
     * @param hexString the input string, hex encoded
     * @return the cipher
     */
    public static char findSingleByteXorCipher(@NotNull String hexString) {
        final byte[] decodedInput = Hex.decode(hexString);
        return findSingleByteXorCipher(decodedInput);
    }

    public static char findSingleByteXorCipher(@NotNull byte[] input) {
        int currentMax = Integer.MIN_VALUE;
        char match = 0;
        for (char c = 0; c < 256; c++) {
            final byte[] fixedXor = repeatingKeyXor(input, (byte) c);
            final String decoded = new String(fixedXor);
            final int charCount = Strings.countAlpha(decoded);
            if (Strings.countAlpha(decoded) > currentMax) {
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
        return fixedXOR(hexInput, repeatedCipher);
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
        return Hex.encode(xorResult);
    }


    /**
     * Applies the repeating key xor operation to the byte array {@code input} using the cipher {@code cipher}.
     *
     * @param input  the input in bytes
     * @param cipher the cipher
     * @return the result as a byte array
     */
    public static byte[] repeatingKeyXor(byte[] input, byte cipher) {
        final byte[] repeatedCipher = new byte[input.length];
        IntStream.range(0, input.length).forEach(i -> repeatedCipher[i] = cipher);
        return fixedXOR(input, repeatedCipher);
    }
}
