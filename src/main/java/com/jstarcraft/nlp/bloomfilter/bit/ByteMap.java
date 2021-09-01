package com.jstarcraft.nlp.bloomfilter.bit;

/**
 * 
 * @author Birdy
 *
 */
public class ByteMap implements BitMap<byte[]> {

    private byte[] bits;

    private int capacity;

    private int size;

    public ByteMap(int capacity) {
        assert capacity > 0;
        int elements = capacity % Byte.SIZE == 0 ? capacity / Byte.SIZE : capacity / Byte.SIZE + 1;
        this.bits = new byte[elements];
        this.capacity = capacity;
        this.size = 0;
    }

    @Override
    public boolean get(int index) {
        int row = index / Byte.SIZE;
        int column = index % Byte.SIZE;
        return ((bits[row] >>> column) & 1) == 1;
    }

    @Override
    public void set(int index) {
        int row = index / Byte.SIZE;
        int column = index % Byte.SIZE;
        if (((bits[row] >>> column) & 1) == 0) {
            bits[row] |= (1 << column);
            size++;
        }
    }

    @Override
    public void unset(int index) {
        int row = index / Byte.SIZE;
        int column = index % Byte.SIZE;
        if (((bits[row] >>> column) & 1) == 1) {
            bits[row] &= ~(1 << column);
            size--;
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public byte[] bits() {
        return bits;
    }

}