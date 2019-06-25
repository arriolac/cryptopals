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

    // Problem # 3
    @Test
    public void testSingleByteXorCipher() {
        final String input = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        final char result = CryptoLib.findSingleByteXorCipher(input);
        assertEquals('X', result);
    }

    // Problem # 5
    @Test
    public void testRepeatedXor() {
       final String input = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
       final String key = "ICE";
       assertEquals(
               "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f",
               CryptoLib.repeatingKeyXor(input, key)
       );
    }
}
