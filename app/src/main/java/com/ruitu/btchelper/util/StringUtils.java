package com.ruitu.btchelper.util;

public class StringUtils {
    public static String replaceFileName(String filename) {
        return DataHelper.FILE_NAME.replace("{filename}", filename);
    }

    public static String replaceFileNameAndPrex(String filename) {
        return DataHelper.FILE_NAME.replace("{filename}.txt", filename);
    }
}
