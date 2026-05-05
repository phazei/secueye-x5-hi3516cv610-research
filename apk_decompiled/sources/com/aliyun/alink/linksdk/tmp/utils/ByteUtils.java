package com.aliyun.alink.linksdk.tmp.utils;

/* JADX INFO: loaded from: classes2.dex */
public class ByteUtils {
    public static byte[] int32ToByte(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

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
