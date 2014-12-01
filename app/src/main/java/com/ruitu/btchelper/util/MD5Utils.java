package com.ruitu.btchelper.util;

import java.security.MessageDigest;
/**
 * 32位加密
 * @author Administrator
 *
 * 下午2:14:45
 */
public class MD5Utils {
    public static String getMD5Str(String plainText) throws Exception {
        if (plainText == null)
            return null;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plainText.getBytes());
        byte b[] = md.digest();

        int i;

        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        System.out.println("result: " + buf.toString());// 32位的加密
        return buf.toString();
    }
}
