package main.sets;

import main.lib.Bytes;
import main.lib.CryptoLib;
import main.lib.Hex;
import main.lib.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

public class Set1 {
    public static void runProblem4() {
        final Path path = Path.of("src/main/sets/test_files/set1_problem4_input.txt");
        try {
            final List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                final String line = lines.get(i);
                final char cipher = CryptoLib.findSingleByteXorCipher(line);
                final String resultXor = CryptoLib.repeatingKeyXor(line, cipher);
                final byte[] resultBytes = Hex.decode(resultXor);
                final String result = new String(resultBytes);
                final int charCount = Strings.countAlpha(result);
                if (charCount > 23) {
                    System.out.println(String.format("Line %d, char count (%d):", i, charCount));
                    System.out.println("    " + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runProblem6() {
        final Path path = Path.of("src/main/sets/test_files/set1_problem6_input.txt");

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Optional<String> result = lines.stream().reduce((s1, s2) -> s1 + s2);
        if (!result.isPresent()) {
            return;
        }

        // Convert base 64 input into raw bytes
        final String fullString = result.get();
        final byte[] inputBytes = Base64.getDecoder().decode(fullString);
        System.out.println("Input bytes size " + inputBytes.length);

        // Guess key length
        int guessedSize = guessKeySize(inputBytes);
        System.out.println("Guessed size " + guessedSize);

        // Create blocks
        final List<List<Byte>> blocks = new ArrayList<>();
        IntStream.range(0, guessedSize).forEach(i -> blocks.add(new ArrayList<>()));
        for (int i = 0; i < inputBytes.length / guessedSize; i++) {
            for (int j = 0; j < guessedSize; j++) {
                final List<Byte> current = blocks.get(j);
                current.add(inputBytes[(i * guessedSize) + j]);
            }
        }

        // Solve single character XOR
        final StringBuilder sb = new StringBuilder();
        for (final List<Byte> currentBytes : blocks) {
            final byte[] current = Bytes.toPrimitive(currentBytes.toArray(Byte[]::new));
            final char key = CryptoLib.findSingleByteXorCipher(current);
            sb.append(key);
        }
        System.out.println(String.format("Key is: '%s'", sb.toString()));
    }

    private static int guessKeySize(byte[] input) {
        int minLength = Integer.MAX_VALUE;
        int guessedSize = 0;
        for (int keySize = 2; keySize <= 40; keySize++) {
            byte[] part1 = Bytes.toPrimitive(IntStream.range(0, keySize)
                    .mapToObj(i -> input[i])
                    .toArray(Byte[]::new));
            byte[] part2 = Bytes.toPrimitive(IntStream.range(keySize, keySize+keySize)
                    .mapToObj(i -> input[i])
                    .toArray(Byte[]::new));

            final int normalizedDistance = Bytes.hammingDistance(part1, part2) / keySize;
            if (normalizedDistance < minLength) {
                minLength = normalizedDistance;
                guessedSize = keySize;
            }
        }
        return guessedSize;
    }

}
