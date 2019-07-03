package main.lib;

import java.util.stream.IntStream;

public class PKCS {
    public static String applyPadding(String input, int blockSize) {
        final int paddingSize = blockSize - (input.length() % blockSize);
        if (paddingSize == blockSize) {
            return input;
        }

        System.out.println("Applying padding size: " + paddingSize);
        final char[] padding = new char[paddingSize];
        IntStream.range(0, paddingSize).forEach(i -> padding[i] = (char) paddingSize);
        return input + new String(padding);
    }
}
