package cn.yangself.leigodcontrol.utils;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * MD5工具类
 */
@Component
public class DigestUtils {
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final char[] DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    public static String md5Hex(byte[] data){
        return md5Hex(data, false);
    }

    public static String md5Hex(String str){
        try {
            return md5Hex(str.getBytes("UTF-8"), false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Hex(byte[] data, boolean isUpper){
        try {
            return new String(encodeHex(MessageDigest.getInstance("MD5").digest(data), isUpper ? DIGITS_UPPER : DIGITS_LOWER));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
