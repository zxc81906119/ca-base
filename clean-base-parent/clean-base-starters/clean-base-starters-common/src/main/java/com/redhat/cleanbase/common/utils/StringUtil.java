package com.redhat.cleanbase.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * <pre>
 * String Util
 * </pre>
 *
 * @author Yahuang
 * @version <ul>
 * <li>2023年8月25日,Yahuang,new
 * </ul>
 * @since 2023年8月25日
 */
public final class StringUtil extends StringUtils {

    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    final static public int PADDING_TYPE_RIGHT = 0;

    final static public int PADDING_TYPE_LEFT = 1;
    public static final String DEFAULT_SPACE_SEPARATOR = " ";
    public static final String DEFAULT_NEXT_LINE_STRING = "\n";
    private static final String HEX_STRING_TABLE = "0123456789abcdef";
    private static final char[] HEX_CHARS = HEX_STRING_TABLE
            .toCharArray();

    /**
     * 字串由右邊補字元至指定長度
     *
     * @param value       String 原始字串
     * @param length      int 字串總長度
     * @param paddingChar 字元
     * @return String
     */
    static public String rightPadding(String value, int length,
                                      char paddingChar) {
        return padding(value, length, PADDING_TYPE_RIGHT, paddingChar);
    }

    /**
     * 字串由左邊補字元至指定長度
     *
     * @param value       String 原始字串
     * @param length      int 字串總長度
     * @param paddingChar 字元
     * @return String
     */
    static public String leftPadding(String value, int length,
                                     char paddingChar) {
        return padding(value, length, PADDING_TYPE_LEFT, paddingChar);
    }

    /**
     * 字串補字元至指定長度
     *
     * @param value       String 原始字串
     * @param length      int 字串總長度
     * @param paddingType int 補空白類別 右邊->0 左邊->1
     * @param paddingChar 字元
     * @return String
     */
    static public String padding(String value, int length,
                                 int paddingType, char paddingChar) {
        if (value == null) {
            value = "";
        }
        if (value.length() == length) {
            return value;
        }
        if (value.length() < length) {
            char[] chars = new char[length - value.length()];
            Arrays.fill(chars, paddingChar);
            if (paddingType == PADDING_TYPE_RIGHT) {
                return value + new String(chars);
            } else {
                return new String(chars) + value;
            }
        } else {
            if (paddingType == PADDING_TYPE_RIGHT) {
                return value.substring(0, length);
            } else {
                return value.substring(value.length() - length);
            }
        }
    }

    public static final String toHexString(byte[] b) {
        return toHexString(b, 0, b.length);
    }

    public static final String toHexString(byte[] b, int start,
                                           int len) {
        return toHexString(b, start, len, 0, 0);
    }

    public static final String toHexString(byte[] b, int start, int len,
                                           int separatorWhenLength, int nextLineWhenLength) {
        return toHexString(b, start, len, separatorWhenLength,
                DEFAULT_SPACE_SEPARATOR, nextLineWhenLength,
                DEFAULT_NEXT_LINE_STRING);
    }

    public static final String toHexString(byte[] b, int start, int len,
                                           int separatorWhenLength, String separator,
                                           int nextLineWhenLength, String nextLine) {

        if (b == null || b.length == 0 || len == 0)
            return "";

        StringBuilder s = new StringBuilder();
        int count = 0;
        for (int i = start; i < b.length && i < start + len; i++) {
            if (count > 0) {
                if (nextLineWhenLength > 0
                        && count % nextLineWhenLength == 0)
                    s.append(nextLine);
                else if (separatorWhenLength > 0
                        && count % separatorWhenLength == 0)
                    s.append(separator);
            }
            count++;
            s.append(HEX_CHARS[b[i] >>> 4 & 0x0f]);
            s.append(HEX_CHARS[b[i] & 0x0f]);
        }

        return s.toString();
    }

    /**
     * base64 getByte(CarSet)之 bytearray 解碼成 bytearray
     *
     * @param encoded bytearray
     * @return byte[]
     */
    public static byte[] base64UrlDecode(byte[] encoded) {
        return Base64.getUrlDecoder().decode(encoded);
    }

    /**
     * base64 getByte(CarSet)之 bytearray 編碼成 bytearray
     *
     * @param decoded bytearray
     * @return byte[]
     */
    private static byte[] base64UrlEncode(byte[] decoded) {
        return Base64.getUrlEncoder().encode(decoded);
    }

    public static String base64UrlDecodeString(byte[] encoded) {
        return new String(base64UrlDecode(encoded),
                StandardCharsets.UTF_8);
    }

    public static String base64UrlEncodeString(byte[] decoded) {
        return new String(base64UrlEncode(decoded),
                StandardCharsets.UTF_8);
    }

    /**
     * Get base64 String by bytearray
     *
     * @param bytes 傳入bytearray格式之資料
     * @return String base64格式字串
     */
    public static String bytesToBase64(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

    /**
     * Decode base64 String
     *
     * @param base64String 傳入base64 格式之String
     * @return String decoded 後的字串
     */
    public static String base64ToBytes(String base64String) {
        return new String(Base64.getDecoder().decode(base64String.getBytes()), StandardCharsets.UTF_8);
    }

    public static int str2Int(String string) {
        return Integer.parseInt(string);
    }

}
