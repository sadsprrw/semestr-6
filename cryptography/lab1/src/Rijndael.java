import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rijndael {
    public static List<byte[]> encode(String data, String keyString) {
        byte[] key = keyString.getBytes();

        byte[] tBytes = data.getBytes();

        Rijndael encoder = new Rijndael();
        List<byte[]> chunks = chunk(tBytes);
        List<byte[]> encrypted = new ArrayList<>();
        for (byte[] current : chunks) {
            encrypted.add(encoder.encrypt(key, current));
        }
        return encrypted;
    }

    public static String decode(List<byte[]> encodedData, String keyString) {
        byte[] key = keyString.getBytes();

        Rijndael decoder = new Rijndael();
        StringBuilder decryptedTestString = new StringBuilder();
        for (byte[] temporary : encodedData) {
            byte[] decryptedBytes = decoder.decrypt(key, temporary);
            decryptedTestString.append(new String(decryptedBytes, StandardCharsets.UTF_8));
        }
        return decryptedTestString.toString();
    }

    public static boolean compareDecoded(String base, String decoded) {
        try {
            for (int i = 0; i < base.length(); i++) {
                if (base.charAt(i) != decoded.charAt(i)) return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static List<byte[]> chunk(byte[] tBytes) {
        List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < tBytes.length; i += 16) {
            byte[] current = Arrays.copyOfRange(tBytes, i, Math.min(tBytes.length, i + 16));
            byte[] testBytes;
            if (current.length <= 16) {
                testBytes = new byte[16];
                System.arraycopy(current, 0, testBytes, 0, current.length);
            } else {
                testBytes = tBytes;
            }
            result.add(testBytes);
        }
        return result;
    }

    public byte[] encrypt(byte[] key, byte[] data) {
        KeyExpander ke = new KeyExpander(key);
        RijndaelState state = new RijndaelState(data, ke.keys);
        encryptCore(state);

        return state.getData();
    }

    public byte[] decrypt(byte[] key, byte[] data) {
        KeyExpander ke = new KeyExpander(key);

        RijndaelState state = new RijndaelState(data, ke.keys);
        decryptCore(state);

        return state.getData();
    }

    private void encryptCore(RijndaelState state) {
        state.round = 0;
        final int numberOfRounds = 14;

        state.addRoundKey(state.getKey(state.round));

        for (state.round = 1; state.round < numberOfRounds; state.round++) {
            state.subBytes();
            state.shiftRows();
            state.mixColumns();
            state.addRoundKey(state.getKey(state.round));
        }

        state.subBytes();
        state.shiftRows();
        state.addRoundKey(state.getKey(state.round));
    }

    private void decryptCore(RijndaelState state) {
        final int numberOfRounds = state.round = 14;

        state.addRoundKey(state.getKey(state.round));

        for (state.round = numberOfRounds - 1; state.round > 0; state.round--) {
            state.inverseShiftRows();
            state.inverseSubBytes();
            state.addRoundKey(state.getKey(state.round));
            state.inverseMixColumns();
        }

        state.inverseShiftRows();
        state.inverseSubBytes();

        state.addRoundKey(state.getKey(state.round));
    }
}