package main.sets;

import main.lib.AES;
import main.lib.Hex;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

public class Set2 {

    public static void runProblem10() {
        try {
            // Read lines from file
            final Path path = Path.of("src/main/sets/test_files/set2_problem10_input.txt");
            final Optional<String> encryptedText = Files.readAllLines(path).stream().reduce((s1, s2) -> s1 + s2);
            if (encryptedText.isPresent()) {
                final String key = "YELLOW SUBMARINE";
                final String iv = "\u0000".repeat(key.length());
                final byte[] blob = Base64.getDecoder().decode(encryptedText.get());
                final String decryptedText = AES.cbcDecrypt(blob, key, iv);
                System.out.println(decryptedText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runProblem11() {
        try {
            final String plaintextString = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
            final String encrypted = AES.encryptionOracle(plaintextString);
            final boolean isECB = AES.isEcbModeEncrypted(Hex.encode(encrypted.getBytes()));
            System.out.println("Detected mode: " + (isECB ? "ECB" : "CBC"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
