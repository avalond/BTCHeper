package com.ruitu.btchelper.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class DataUtil {
    public static String changeToNoPoint(String str) {
        if (str == null)
            return null;
        try {
            double d = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("#####0");
            return df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String changeToTwoPoint(String str) {
        if (str == null)
            return null;
        try {
            double d = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("#####0.00");
            return df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String changeToFivePoint(String str) {
        if (str == null)
            return null;
        try {
            double d = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("#####0.00000");
            return df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String changeDoubleToDigit(double d, int num) {
        try {
            DecimalFormat dFormat = new DecimalFormat("#####0.00");
            dFormat.setMaximumFractionDigits(num);
            return dFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMd5(String plainText) {
        try {
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
