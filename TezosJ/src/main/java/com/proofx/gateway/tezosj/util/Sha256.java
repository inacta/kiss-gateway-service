package com.proofx.gateway.tezosj.util;

import com.proofx.gateway.tezosj.exceptions.TezosJRuntimeException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@SuppressWarnings({"java:S1176"})
public class Sha256 {

    private Sha256() {}

    public static final int LENGTH = 32; // bytes

    /**
     * Returns a new SHA-256 MessageDigest instance.
     * <p>
     * This is a convenience method which wraps the checked
     * exception that can never occur with a RuntimeException.
     * <p>
     * return a new SHA-256 MessageDigest instance
     */
    public static MessageDigest newDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new TezosJRuntimeException("No hashing algorithm");
        }
    }

    /**
     * Calculates the SHA-256 hash of the given bytes.
     * <p>
     * param input the bytes to hash
     * return the hash (in big-endian order)
     */
    public static byte[] hash(byte[] input) {
        return hash(input, 0, input.length);
    }

    /**
     * Calculates the SHA-256 hash of the given byte range.
     * <p>
     * param input the array containing the bytes to hash
     * param offset the offset within the array of the bytes to hash
     * param length the number of bytes to hash
     * return the hash (in big-endian order)
     */
    public static byte[] hash(byte[] input, int offset, int length) {
        MessageDigest digest = newDigest();
        digest.update(input, offset, length);
        return digest.digest();
    }

    /**
     * - for Tezos - Calculates the SHA-256 hash of the given bytes,
     * and then hashes the resulting hash again.
     * <p>
     * param input the bytes to hash
     * return the double-hash (in big-endian order)
     */
    public static byte[] hashTwiceThenFirstFourOnly(byte[] input) {
        byte[] firstFourOfDoubleChecksum = new byte[4];
        byte[] sha256DoubleHash = hashTwice(input);
        System.arraycopy(sha256DoubleHash, 0, firstFourOfDoubleChecksum, 0, 4);
        return firstFourOfDoubleChecksum;
    }


    public static byte[] hashTwiceTezos(byte[] input) {
        byte[] tezosAddress = new byte[20];
        byte[] sha256DoubleHash = hashTwice(input);
        System.arraycopy(sha256DoubleHash, 0, tezosAddress, 0, 19);
        return tezosAddress;
    }


    /**
     * Calculates the SHA-256 hash of the given bytes,
     * and then hashes the resulting hash again.
     * <p>
     * param input the bytes to hash
     * return the double-hash (in big-endian order)
     */
    public static byte[] hashTwice(byte[] input) {
        return hashTwice(input, 0, input.length);
    }

    /**
     * Calculates the SHA-256 hash of the given byte range,
     * and then hashes the resulting hash again.
     * <p>
     * param input the array containing the bytes to hash
     * param offset the offset within the array of the bytes to hash
     * param length the number of bytes to hash
     * return the double-hash (in big-endian order)
     */
    public static byte[] hashTwice(byte[] input, int offset, int length) {
        MessageDigest digest = newDigest();
        digest.update(input, offset, length);
        return digest.digest(digest.digest());
    }

    /**
     * Calculates the hash of hash on the given byte ranges. This is equivalent to
     * concatenating the two ranges and then passing the result to { link #hashTwice(byte[])}.
     */
    public static byte[] hashTwice(byte[] input1, int offset1, int length1,
                                   byte[] input2, int offset2, int length2) {
        MessageDigest digest = newDigest();
        digest.update(input1, offset1, length1);
        digest.update(input2, offset2, length2);
        return digest.digest(digest.digest());
    }

}
