package test.lib;

import main.lib.Bytes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BytesTests {

    @Test
    public void testHammingDistanceSameStringLength() {
        assertEquals(37, Bytes.hammingDistance("this is a test", "wokka wokka!!!"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHammingDistanceDifferentStringLength() {
        assertEquals(0, Bytes.hammingDistance("same", "not same"));
    }

    @Test
    public void testHammingDistanceSameString() {
        assertEquals(0, Bytes.hammingDistance("same", "same"));
    }
}
