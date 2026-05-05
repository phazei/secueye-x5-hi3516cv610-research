package com.alibaba.sdk.android.emas;

import android.os.Build;
import android.text.TextUtils;
import com.alibaba.sdk.android.tbrest.utils.Base64;

/* JADX INFO: compiled from: EncrytUtils.java */
/* JADX INFO: loaded from: classes.dex */
public final class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final char[] f2890a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String aesEncrypt(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            return Base64.encodeBase64String(a(str, str2));
        } catch (Exception unused) {
            return null;
        }
    }

    public static String aesDecrypt(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            return a(Base64.decode(str2), str);
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] a(String str, String str2) throws Exception {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                byte[] bArrA = b.a().a(str2.getBytes());
                if (bArrA != null) {
                    return bArrA;
                }
            } catch (Exception unused) {
            }
        }
        return a.a(str, str2).getBytes();
    }

    private static String a(byte[] bArr, String str) throws Exception {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                byte[] bArrB = b.a().b(bArr);
                if (bArrB != null) {
                    return new String(bArrB);
                }
            } catch (Exception unused) {
            }
        }
        return a.b(str, new String(bArr));
    }
}
