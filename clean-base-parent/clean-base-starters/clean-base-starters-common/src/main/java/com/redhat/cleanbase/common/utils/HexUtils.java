package com.redhat.cleanbase.common.utils;

import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Hex;

public class HexUtils {
    public static byte[] decode(String hexString)
            throws DecoderException {
        if (hexString != null) {
            return decode(hexString.toCharArray());
        } else {
            return null;
        }
    }

    public static byte[] decode(char[] chars) throws DecoderException {
        if (chars != null) {
            return Hex.decode(new String(chars));
        }
        return null;
    }

    public static char[] encode(byte[] bytes) {
        if (bytes != null) {
            return new String(Hex.encode(bytes)).toCharArray();
        }
        return null;
    }

    public static String encodeToString(byte[] bytes) {
        if (bytes != null) {
            return new String(Hex.encode(bytes));
        }
        return null;
    }

    /**
     * Get Hex String by bytearray
     *
     * @param bytes
     *            傳入bytearray格式之資料
     * @return String 16進位格式字串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    /**
     * Get bytearray by Hex String
     *
     * @param hexString
     *            hexString 傳入16進位格式字串
     * @return byte[] bytearray格式之資料
     */
    public static byte[] hexToBytes(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
    
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character
                    .digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
    
        return byteArray;
    }
}
