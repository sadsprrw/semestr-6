public class RijndaelState {
    int round;
    private byte[] data;
    private byte[][] keys;

    public byte[] getKey(int i) {
        if (i >= 0 && i < keys.length)
            return keys[i];
        else return null;
    }

    public byte[] getData() {
        return this.data.clone();
    }

    public void apply(byte[] data) {
        System.arraycopy(data, 0, this.data, 0, 16);
    }

    public RijndaelState(byte[] data, byte[][] keys) {
        this.round = 0;
        this.data = new byte[16];
        this.keys = keys;
        apply(data);
    }

    public void addRoundKey(byte[] key) {
        for (int i = 0; i < 16; ++i) {
            this.data[i] ^= key[i];
        }
    }

    public void subBytes() {
        for (int i = 0; i < 16; ++i) {
            this.data[i] = (byte) Constants.S_BOX[Byte.toUnsignedInt(this.data[i])];
        }
    }

    public void inverseSubBytes() {
        for (int i = 0; i < 16; ++i) {
            this.data[i] = (byte) Constants.INVERSE_S_BOX[Byte.toUnsignedInt(this.data[i])];
        }
    }


    public void shiftRows() {
        byte[] shift = new byte[16];

        byte[] index = {
                0, 5, 10, 15,
                4, 9, 14, 3,
                8, 13, 2, 7,
                12, 1, 6, 11
        };

        for (int i = 0; i < 16; ++i) {
            shift[i] = this.data[index[i]];
        }

        this.apply(shift);
    }

    public void inverseShiftRows() {
        byte[] shift = new byte[16];

        byte[] index = {
                0, 13, 10, 7,
                4, 1, 14, 11,
                8, 5, 2, 15,
                12, 9, 6, 3
        };

        for (int i = 0; i < 16; ++i) {
            shift[i] = this.data[index[i]];
        }

        this.apply(shift);
    }

    public void mixColumns() {
        byte[] mix = new byte[16];

        for (int col = 0; col < 4; ++col) {
            int idx0 = 0 + 4 * col; // 0 1 2 3
            int idx1 = 1 + 4 * col; // 4 5 6 7
            int idx2 = 2 + 4 * col; // 8 9 A B
            int idx3 = 3 + 4 * col; // C D E F

            mix[idx0] = (byte)
                    (Constants.MUL_2[Byte.toUnsignedInt(this.data[idx0])]
                            ^ Constants.MUL_3[Byte.toUnsignedInt(this.data[idx1])]
                            ^ this.data[idx2]
                            ^ this.data[idx3]);

            mix[idx1] = (byte)
                    (this.data[idx0]
                            ^ Constants.MUL_2[Byte.toUnsignedInt(this.data[idx1])]
                            ^ Constants.MUL_3[Byte.toUnsignedInt(this.data[idx2])]
                            ^ this.data[idx3]);

            mix[idx2] = (byte)
                    (this.data[idx0]
                            ^ this.data[idx1]
                            ^ Constants.MUL_2[Byte.toUnsignedInt(this.data[idx2])]
                            ^ Constants.MUL_3[Byte.toUnsignedInt(this.data[idx3])]);

            mix[idx3] = (byte)
                    (Constants.MUL_3[Byte.toUnsignedInt(this.data[idx0])]
                            ^ this.data[idx1]
                            ^ this.data[idx2]
                            ^ Constants.MUL_2[Byte.toUnsignedInt(this.data[idx3])]);
        }

        this.apply(mix);
    }

    public void inverseMixColumns() {
        byte[] mix = new byte[16];

        for (int col = 0; col < 4; ++col) {
            int idx0 = 0 + 4 * col;
            int idx1 = 1 + 4 * col;
            int idx2 = 2 + 4 * col;
            int idx3 = 3 + 4 * col;

            mix[idx0] = (byte)
                    (Constants.MUL_14[Byte.toUnsignedInt(this.data[idx0])]
                            ^ Constants.MUL_11[Byte.toUnsignedInt(this.data[idx1])]
                            ^ Constants.MUL_13[Byte.toUnsignedInt(this.data[idx2])]
                            ^ Constants.MUL_9[Byte.toUnsignedInt(this.data[idx3])]);
            mix[idx1] = (byte)
                    (Constants.MUL_9[Byte.toUnsignedInt(this.data[idx0])]
                            ^ Constants.MUL_14[Byte.toUnsignedInt(this.data[idx1])]
                            ^ Constants.MUL_11[Byte.toUnsignedInt(this.data[idx2])]
                            ^ Constants.MUL_13[Byte.toUnsignedInt(this.data[idx3])]);
            mix[idx2] = (byte)
                    (Constants.MUL_13[Byte.toUnsignedInt(this.data[idx0])]
                            ^ Constants.MUL_9[Byte.toUnsignedInt(this.data[idx1])]
                            ^ Constants.MUL_14[Byte.toUnsignedInt(this.data[idx2])]
                            ^ Constants.MUL_11[Byte.toUnsignedInt(this.data[idx3])]);
            mix[idx3] = (byte)
                    (Constants.MUL_11[Byte.toUnsignedInt(this.data[idx0])]
                            ^ Constants.MUL_13[Byte.toUnsignedInt(this.data[idx1])]
                            ^ Constants.MUL_9[Byte.toUnsignedInt(this.data[idx2])]
                            ^ Constants.MUL_14[Byte.toUnsignedInt(this.data[idx3])]);
        }
        this.apply(mix);
    }
}