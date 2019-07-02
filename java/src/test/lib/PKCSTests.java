package test.lib;

import main.lib.PKCS;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PKCSTests {

    @Test
    public void testApplyHasPadding() {
        final String input = "YELLOW SUBMARINE";
        final String result = PKCS.applyPadding(input, 20);
        assertEquals("YELLOW SUBMARINE\u0004\u0004\u0004\u0004", result);
    }

    @Test
    public void testApplyNoPadding() {
        final String input = "YELLOW SUBMARINE";
        final String result = PKCS.applyPadding(input, 16);
        assertEquals("YELLOW SUBMARINE", result);
    }

    @Test
    public void testApplyHasPaddingMultipleBlocks() {
        final String input = "YELLOW SUBMARINE";
        final String result = PKCS.applyPadding(input, 3);
        assertEquals("YELLOW SUBMARINE\u0002\u0002", result);
    }
}
