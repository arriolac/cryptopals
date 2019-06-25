package main.lib;

import org.jetbrains.annotations.Nullable;

public class Strings {

    /**
     * Counts the number of alphabets in {@code input}.
     *
     * @param input the input string
     * @return the number of alphabets
     */
    public static int countAlpha(@Nullable String input) {
        if (input == null) {
            return 0;
        }

        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            final char currentChar = input.charAt(i);
            count += Character.isAlphabetic(currentChar) ? 1 : 0;
        }
        return count;
    }
}
