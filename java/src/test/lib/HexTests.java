package test.lib;

import main.lib.Hex;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class HexTests {

    // Set 1 - Problem # 1
    @Test
    public void testConvertHexToBase64() {
        final String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        final String result = Hex.convertToBase64(input);
        assertEquals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeInvalidLength() {
        Hex.decode("0");
    }

    @Test
    public void testDecodeValidLength() {
        final byte[] expectedResult = {0x00};
        final byte[] result = Hex.decode("00");
        for (int i = 0; i < expectedResult.length; i++) {
            assertEquals(expectedResult[i], result[i]);
        }
    }

    @Test
    public void testEncode() {
        final byte[] input = {(byte) 0xf1};
        assertEquals("f1", Hex.encode(input));
    }
}
