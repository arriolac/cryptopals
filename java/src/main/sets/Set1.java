package main.sets;

import main.lib.Bytes;
import main.lib.CryptoLib;
import main.lib.Hex;
import main.lib.Strings;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
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
        final List<KeySize> guessedSizes = guessKeySize(inputBytes);
        for (final KeySize keySize : guessedSizes) {

            final int guessedSize = keySize.keySize;

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
            final String key = sb.toString();

            // Decrypt text using key
            if (guessedSize == 29) { // 29 because I looked at the output and key size == 29 is in english
                System.out.println(String.format("Key is: '%s'", key));
                System.out.println("\nDecrypting...\n");
                final Optional<String> combinedLines = lines.stream().reduce((s, s2) -> s + s2);
                combinedLines.ifPresent(s -> {
                    final byte[] decodedLine = Base64.getDecoder().decode(s);
                    final String lineDefaultCharSet = new String(decodedLine);
                    final String hex = CryptoLib.repeatingKeyXor(lineDefaultCharSet, key);
                    System.out.println(new String(Hex.decode(hex)));
                });
            }
        }
    }

    public static void runProblem7() {
        try {
            // Initialize Cipher object
            final SecretKey key = new SecretKeySpec("YELLOW SUBMARINE".getBytes(), "AES");
            final Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Read lines from file
            final Path path = Path.of("src/main/sets/test_files/set1_problem7_input.txt");
            final Optional<String> encryptedMessage = Files.readAllLines(path).stream().reduce((s1, s2)-> s1 + s2);
            if (encryptedMessage.isPresent()) {
                final byte[] target = Base64.getDecoder().decode(encryptedMessage.get());
                final byte[] decrypted = cipher.doFinal(target);
                System.out.println(new String(decrypted));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<KeySize> guessKeySize(byte[] input) {
        List<KeySize> keySizes = new ArrayList<>();
        for (int keySize = 2; keySize <= 40; keySize++) {
            byte[] part1 = Bytes.toPrimitive(IntStream.range(0, keySize)
                    .mapToObj(i -> input[i])
                    .toArray(Byte[]::new));
            byte[] part2 = Bytes.toPrimitive(IntStream.range(keySize, 2 * keySize)
                    .mapToObj(i -> input[i])
                    .toArray(Byte[]::new));
            byte[] part3 = Bytes.toPrimitive(IntStream.range(2 * keySize, 3 * keySize)
                    .mapToObj(i -> input[i])
                    .toArray(Byte[]::new));
            byte[] part4 = Bytes.toPrimitive(IntStream.range(3 * keySize, 4 * keySize)
                    .mapToObj(i -> input[i])
                    .toArray(Byte[]::new));

            final int normalizedDistance1 = Bytes.hammingDistance(part1, part2) / keySize;
            final int normalizedDistance2 = Bytes.hammingDistance(part3, part4) / keySize;
            final int normalizedDistance = (normalizedDistance1 + normalizedDistance2) / 2;
            keySizes.add(new KeySize(normalizedDistance, keySize));
        }
        Collections.sort(keySizes);
        keySizes.forEach(keySize -> System.out.println(keySize.toString()));
        return keySizes;
    }

    private static class KeySize implements Comparable<KeySize> {
        private final int distance;
        private final int keySize;

        private KeySize(int distance, int keySize) {
            this.distance = distance;
            this.keySize = keySize;
        }

        @Override
        public int compareTo(@NotNull Set1.KeySize other) {
            return distance - other.distance;
        }

        @Override
        public String toString() {
            return String.format("Size: %d, Distance: %d", keySize, distance);
        }
    }
}
