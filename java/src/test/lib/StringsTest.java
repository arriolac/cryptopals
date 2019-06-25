package test.lib;

import main.lib.Strings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringsTest {
    @Test
    public void testCountAlpha() {
        assertEquals(12, Strings.countAlpha("how many words"));
    }

    @Test
    public void testCountAlphaMixedCase() {
        assertEquals(8, Strings.countAlpha("This Word"));
    }

    @Test
    public void testCountNoAlpha() {
        assertEquals(0, Strings.countAlpha("235234 ;'."));
    }

    @Test
    public void testCountEmpty() {
        assertEquals(0, Strings.countAlpha(""));
    }

    @Test
    public void testCountNull() {
        assertEquals(0, Strings.countAlpha(null));
    }
}
