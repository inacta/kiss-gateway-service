package com.proofx.gateway.core.util;

import com.google.common.primitives.Bytes;

public class TagUtils {

    static final String HEXES = "0123456789ABCDEF";
    public static String bytesToString( byte [] raw ) {
        if ( raw == null ) {
            return null;
        }
        final StringBuilder hex = new StringBuilder( 2 * raw.length );
        for ( final byte b : raw ) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    /* s must be an even-length string. */
    public static byte[] stringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] shiftLeft(byte[] data) {

        StringBuilder sb = new StringBuilder();

        for (byte b : data) {
            String s = Integer.toBinaryString(0x100 + b);
            sb.append(s.subSequence(s.length() - 8, s.length()));
        }

        String s = sb.substring(1) + "0";

        byte[] a = new byte[s.length() / 8];

        for (int index = 0, i = 0; i < s.length(); index++, i += 8) {
            a[index] = (byte) Integer.parseInt(s.substring(i, i + 8), 2);
        }

        return a;
    }

    // Pad byte array to n*multiple size
    public static byte[] pad(byte[] data, int multiple) {

        if (multiple <= 0) {
            // No padding
            return data;
        }

        if (data.length % multiple == 0) {
            return data;
        }

        int padding;
        if (data.length < multiple) {
            padding = multiple - data.length;
        } else {
            padding = ((data.length / multiple + 1) * multiple) - data.length;
        }

        return Bytes.concat(data, new byte[padding]);
    }

    public static byte[] xor(byte[] data1, byte[] data2) {
        byte[] result = new byte[Math.max(data1.length, data2.length)];
        for (int i = 0; i < result.length; i++) {
            result[i] = ((byte) (data1[i] ^ data2[i]));
        }

        return result;
    }

    public static byte[] MSBtoLSB(byte[] arr) {
        for(int i = 0; i < arr.length / 2; i++) {
            byte temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
        return arr;
    }
}
