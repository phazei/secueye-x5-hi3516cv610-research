package com.alibaba.ailabs.tg.utils;

import android.os.Build;
import android.text.Html;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/* JADX INFO: loaded from: classes.dex */
public final class EncodeUtils {
    private EncodeUtils() {
    }

    public static String urlEncode(String str) {
        return urlEncode(str, "UTF-8");
    }

    public static String urlEncode(String str, String str2) {
        try {
            return URLEncoder.encode(str, str2);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String urlDecode(String str) {
        return urlDecode(str, "UTF-8");
    }

    public static String urlDecode(String str, String str2) {
        try {
            return URLDecoder.decode(str, str2);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] base64Encode(String str) {
        return base64Encode(str.getBytes());
    }

    public static byte[] base64Encode(byte[] bArr) {
        return Base64.encode(bArr, 2);
    }

    public static String base64Encode2String(byte[] bArr) {
        return Base64.encodeToString(bArr, 2);
    }

    public static byte[] base64Decode(String str) {
        return Base64.decode(str, 2);
    }

    public static byte[] base64Decode(byte[] bArr) {
        return Base64.decode(bArr, 2);
    }

    public static String htmlEncode(CharSequence charSequence) {
        StringBuilder sb = new StringBuilder();
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = charSequence.charAt(i);
            if (cCharAt != '\"') {
                if (cCharAt != '<') {
                    if (cCharAt == '>') {
                        sb.append("&gt;");
                    } else {
                        switch (cCharAt) {
                            case '&':
                                sb.append("&amp;");
                                break;
                            case '\'':
                                sb.append("&#39;");
                                break;
                            default:
                                sb.append(cCharAt);
                                break;
                        }
                    }
                } else {
                    sb.append("&lt;");
                }
            } else {
                sb.append("&quot;");
            }
        }
        return sb.toString();
    }

    public static CharSequence htmlDecode(String str) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(str, 0);
        }
        return Html.fromHtml(str);
    }
}
