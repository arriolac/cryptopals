package main.sets;

import main.lib.CryptoLib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Set1 {
    public static void runProblem4() {
        final Path path = Path.of("src/main/sets/test_files/set1_problem4_input.txt");
        try {
            final List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                final String line = lines.get(i);
                final char cipher = CryptoLib.findSingleByteXorCipher(line);
                final String resultXor = CryptoLib.repeatingKeyXor(line, cipher);
                final byte[] resultBytes = CryptoLib.decodeHexString(resultXor);
                final String result = new String(resultBytes);
                final int charCount = CryptoLib.countAlpha(result);
                if (charCount > 23) {
                    System.out.println(String.format("Line %d, char count (%d):", i, charCount));
                    System.out.println("    " + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
