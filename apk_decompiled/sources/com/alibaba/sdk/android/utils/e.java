package com.alibaba.sdk.android.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: compiled from: Toolkit.java */
/* JADX INFO: loaded from: classes.dex */
public class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final char[] f3230a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    public static boolean m33a(String str) {
        return str == null || str.length() == 0;
    }

    public static void a(double d2) {
        try {
            Thread.sleep((long) (d2 * 1000.0d));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String a(String str) throws NoSuchAlgorithmException {
        return a(MessageDigest.getInstance("MD5").digest(str.getBytes()));
    }

    public static String b(String str) throws NoSuchAlgorithmException {
        return a(MessageDigest.getInstance(MessageDigestAlgorithms.SHA_1).digest(str.getBytes()));
    }

    public static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            sb.append(f3230a[(bArr[i] & 240) >>> 4]);
            sb.append(f3230a[bArr[i] & 15]);
        }
        return sb.toString();
    }
}
