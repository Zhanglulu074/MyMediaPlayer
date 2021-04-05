package com.example.mediaplayer.util;

import java.io.UnsupportedEncodingException;

public class Test {
    public static String test(byte[] bytes) throws UnsupportedEncodingException {
        String res = new String(bytes, "GBK");
        String lrcText = res.replaceAll("&#58;", ":")
                .replaceAll("&#10;", "\n")
                .replaceAll("&#46;", ".")
                .replaceAll("&#32;", " ")
                .replaceAll("&#45;", "-")
                .replaceAll("&#13;", "\r").replaceAll("&#39;", "'");
        return lrcText;
    }
}
