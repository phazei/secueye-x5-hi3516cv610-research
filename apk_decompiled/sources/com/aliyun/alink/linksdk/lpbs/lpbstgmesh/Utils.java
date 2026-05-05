package com.aliyun.alink.linksdk.lpbs.lpbstgmesh;

/* JADX INFO: loaded from: classes2.dex */
public class Utils {
    public static String LPBS_TGMESH_PLUGINID = "com.aliyun.alink.linksdk.lpbs.lpbstgmesh";
    public static String TAG = "[lpbs_tgmesh]";

    public static byte[] toByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }
}
