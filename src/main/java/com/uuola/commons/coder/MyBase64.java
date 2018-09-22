package com.uuola.commons.coder;

import com.uuola.commons.constant.CST_ENCODING;
import com.uuola.commons.exception.BusinessException;

/**
 * @see  <pre>这是一个更改过的BASE64 只能用于此类的编码解码
 * Init '+' and '/' -> '@' '_' , Z with z , N with n ... exchange 1,2.. exchange
 * </pre>
 * @author txdnet
 */
public class MyBase64 {
// Mapping table from 6-bit nibbles to MyBase64 characters.

    private static char[] map1 = {
        'Z', 'A', 'c', '0', '2', 'f', 'G', '7', 'I', 'j', 'K', '9', 'M', 'n', 'O', 'P', 'Q', '4', 's', 't', 'U', 'V', '1', 'X', 'y', 'z',
        'a', 'N', 'F', '8', 'E', 'C', 'g', 'H', 'i', 'J', 'k', 'l', 'm', 'b', 'o', 'p', 'v', 'R', 'S', 'T', 'u', 'q', 'w', '6', 'W', 'B',
        'Y', '3', 'd', 'e', 'x', '5', 'r', 'D', 'h', 'L', '@', '_'};
// Mapping table from MyBase64 characters to 6-bit nibbles.
    private static byte[] map2 = new byte[128];

    static {
        for (int i = 0; i < map2.length; i++) {
            map2[i] = -1;
        }
        for (int i = 0; i < 64; i++) {
            map2[map1[i]] = (byte) i;
        }
    }

    /**
     * Encodes a string into MyBase64 format. No blanks or line breaks are
     * inserted.
     *
     * @param s a String to be encoded.
     * @return A String with the MyBase64 encoded data.
     */
    public static String encode(String s) throws BusinessException {
        try {
            return new String(getEncodeChar(s.getBytes(CST_ENCODING.UTF8)));
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Encodes a byte array into MyBase64 format. No blanks or line breaks are
     * inserted.
     *
     * @param in an array containing the data bytes to be encoded.
     * @return A character array with the MyBase64 encoded data.
     */
    private static char[] getEncodeChar(byte[] in) {
        int iLen = in.length;
        int oDataLen = (iLen * 4 + 2) / 3;       // output length without padding
        int oLen = ((iLen + 2) / 3) * 4;         // output length including padding
        char[] out = new char[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
            int i0 = in[ip++] & 0xff;
            int i1 = ip < iLen ? in[ip++] & 0xff : 0;
            int i2 = ip < iLen ? in[ip++] & 0xff : 0;
            int o0 = i0 >>> 2;
            int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
            int o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            op++;
            out[op] = op < oDataLen ? map1[o3] : '=';
            op++;
        }
        return out;
    }

    /**
     * Decodes a MyBase64 string.
     *
     * @param s a MyBase64 String to be decoded.
     * @return A String containing the decoded data.
     * @throws IllegalArgumentException if the input is not valid MyBase64
     * encoded data.
     */
    public static String decode(String s) throws BusinessException {
        try {
            return new String(getDecodeByte(s.toCharArray()), CST_ENCODING.UTF8);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Decodes MyBase64 data. No blanks or line breaks are allowed within the
     * MyBase64 encoded data.
     *
     * @param in a character array containing the MyBase64 encoded data.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException if the input is not valid MyBase64
     * encoded data.
     */
    private static byte[] getDecodeByte(char[] in) {
        int iLen = in.length;
        if (iLen % 4 != 0) {
            return null;
        }
        while (iLen > 0 && in[iLen - 1] == '=') {
            iLen--;
        }
        int oLen = (iLen * 3) / 4;
        byte[] out = new byte[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
            int i0 = in[ip++];
            int i1 = in[ip++];
            int i2 = ip < iLen ? in[ip++] : 'A';
            int i3 = ip < iLen ? in[ip++] : 'A';
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
                return null;
            }
            int b0 = map2[i0];
            int b1 = map2[i1];
            int b2 = map2[i2];
            int b3 = map2[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
                return null;
            }
            int o0 = (b0 << 2) | (b1 >>> 4);
            int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
            int o2 = ((b2 & 3) << 6) | b3;
            out[op++] = (byte) o0;
            if (op < oLen) {
                out[op++] = (byte) o1;
            }
            if (op < oLen) {
                out[op++] = (byte) o2;
            }
        }
        return out;
    }
}