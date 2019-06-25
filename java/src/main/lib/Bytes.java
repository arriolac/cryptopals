package main.lib;

public class Bytes {

    /**
     * Converts the hamming distance, or the number of differing bits between two numbers.
     * @param input1 the first input
     * @param input2 the second input
     * @return the number of differing bits in the two strings
     */
    public static int hammingDistance(String input1, String input2) {
        if (input1.length() != input2.length()) {
            throw new IllegalArgumentException("Lengths not the same");
        }

        final byte[] input1Bytes = input1.getBytes();
        final byte[] input2Bytes = input2.getBytes();
        return hammingDistance(input1Bytes, input2Bytes);
    }

    public static byte[] toPrimitive(Byte[] bytes) {
        final byte[] result = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++){
            result[i] = bytes[i];
        }
        return result;
    }

    public static int hammingDistance(byte[] input1, byte[] input2) {
        int distance = 0;
        for (int i = 0; i < input1.length; i++) {
            distance += hammingDistance(input1[i], input2[i]);
        }
        return distance;
    }

    public static int hammingDistance(byte input1, byte input2) {
        byte xor = (byte) (input1 ^ input2);
        int distance = 0;
        for (int i = 0; i < 8; i++) {
            final int result = xor & (1 << i);
            distance += (result == 0) ? 0 : 1;
        }
        return distance;
    }
}
