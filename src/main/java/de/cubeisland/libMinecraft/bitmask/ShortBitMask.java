package de.cubeisland.libMinecraft.bitmask;

/**
 * Represents a bitmask with 16 bits
 *
 * @author Phillip Schichtel
 */
public class ShortBitMask {
    private short mask;

    public ShortBitMask() {
        this((short) 0);
    }

    public ShortBitMask(short mask) {
        this.mask = mask;
    }

    public short get() {
        return mask;
    }

    public short set(short bits) {
        return mask |= bits;
    }

    public short reset() {
        return this.reset((short) 0);
    }

    public short reset(short mask) {
        return this.mask = mask;
    }

    public short unset(short bits) {
        return mask &= ~bits;
    }

    public short toggle(short bits) {
        return mask ^= bits;
    }

    public boolean isset(short bits) {
        return (mask & bits) == bits;
    }
}
