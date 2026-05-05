package com.aliyun.alink.linksdk.alcs.lpbs.utils;

/* JADX INFO: loaded from: classes2.dex */
public class TextHelper {
    public static String byte2hex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (bArr != null) {
            for (byte b2 : bArr) {
                sb.append(String.format("%02X", Integer.valueOf(b2 & 255)));
            }
        }
        return sb.toString();
    }
}
