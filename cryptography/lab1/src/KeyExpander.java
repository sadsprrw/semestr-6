import java.util.Arrays;

public class KeyExpander {
    byte[][] keys;

    public KeyExpander(byte[] key) {
        keyExpansion(key);
    }

    private void keyExpansionCore(byte[] in, int round) {
        byte low = in[0];
        in[0] = in[1];
        in[1] = in[2];
        in[2] = in[3];
        in[3] = low;

        in[0] = (byte) Constants.S_BOX[Byte.toUnsignedInt(in[0])];
        in[1] = (byte) Constants.S_BOX[Byte.toUnsignedInt(in[1])];
        in[2] = (byte) Constants.S_BOX[Byte.toUnsignedInt(in[2])];
        in[3] = (byte) Constants.S_BOX[Byte.toUnsignedInt(in[3])];

        in[0] ^= (byte) Constants.RCON[round];
    }

    private void keyExpansion(byte[] initialKey) {
        System.out.println("initKey = " + Arrays.toString(initialKey) + "length:" + String.valueOf(initialKey.length));
        final int n = 32;
        final int b = 240;
        byte[] schedule = new byte[n - 16 + b];

        System.out.println("for");
        for (int i = 0; i < n; ++i) {
            schedule[i] = initialKey[i % initialKey.length];
            System.out.println("  schedule[" + String.valueOf(i) + "]= " + schedule[i]);
        }

        int bytesGenerated = n;
        int rconIteration = 1;

        System.out.println("generateSchedule(" + String.valueOf(n) + ", " + String.valueOf(b) + ", " + Arrays.toString(schedule) + ", " + String.valueOf(bytesGenerated) + ", " + String.valueOf(rconIteration) + ")");
        generateSchedule(n, b, schedule, bytesGenerated, rconIteration);
        System.out.println("Back");
        System.out.println("schedule = " + Arrays.toString(schedule));

        this.keys = new byte[16][16];
        System.out.println("for");
        for (int i = 0; i < schedule.length; ++i) {
            this.keys[i / 16][i % 16] = schedule[i];
            System.out.println("   keys[" + String.valueOf(i/16) + "][" + String.valueOf(1%16) + "]= " + schedule[i]);
        }
    }

    private void generateSchedule(int n, int b, byte[] schedule, int bytesGenerated, int rconIteration) {
        while (bytesGenerated < b) {
            System.out.println("bytesGen = " + String.valueOf(bytesGenerated) + " b = " + String.valueOf(b));
            byte[] temp = new byte[4];
            System.arraycopy(schedule, bytesGenerated - 4, temp, 0, 4);
            System.out.println("copied = " + Arrays.toString(temp));
            if (bytesGenerated % n == 0) {
                keyExpansionCore(temp, rconIteration);
                rconIteration += 1;
            } else {
                for (int i = 0; i < 4; ++i) {
                    temp[i] = (byte) Constants.S_BOX[Byte.toUnsignedInt(temp[i])];
                }
            }
            for (int i = 0; i < 4; ++i) {
                schedule[bytesGenerated + i] = (byte) (temp[i] ^ schedule[bytesGenerated + i - n]);
            }
            bytesGenerated += 4;

            for (int k = 0; k < 3; ++k) {
                System.arraycopy(schedule, bytesGenerated - 4, temp, 0, 4);

                for (int i = 0; i < 4; ++i) {
                    schedule[bytesGenerated + i] = (byte) (temp[i] ^ schedule[bytesGenerated + i - n]);
                }
                bytesGenerated += 4;
            }
        }
    }
}