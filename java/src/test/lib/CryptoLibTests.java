package test.lib;

import main.lib.CryptoLib;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CryptoLibTests {

    // Set #1

    // Problem # 1
    @Test
    public void testConvertHexToBase64() {
        final String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        final String result = CryptoLib.convertHexToBase64(input);
        assertEquals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t", result);
    }

    // Problem # 2
    @Test
    public void testFixedXor() {
        final String input1 = "1c0111001f010100061a024b53535009181c";
        final String input2 = "686974207468652062756c6c277320657965";
        final String result = CryptoLib.fixedXOR(input1, input2);
        assertEquals("746865206b696420646f6e277420706c6179", result);
    }
}