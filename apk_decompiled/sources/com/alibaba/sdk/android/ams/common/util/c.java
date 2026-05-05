package com.alibaba.sdk.android.ams.common.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final byte[] f2838a = {-128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final c f2839b = new c();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final char[] f2840c = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long[] f2841d = new long[4];
    private long[] e = new long[2];
    private byte[] f = new byte[64];
    private byte[] g = new byte[16];

    private c() {
        b();
    }

    public static long a(byte b2) {
        int i = b2;
        if (b2 < 0) {
            i = b2 & 255;
        }
        return i;
    }

    private long a(long j, long j2, long j3) {
        return ((~j) & j3) | (j2 & j);
    }

    private long a(long j, long j2, long j3, long j4, long j5, long j6, long j7) {
        int iA = (int) (a(j2, j3, j4) + j5 + j7 + j);
        return ((long) ((iA >>> ((int) (32 - j6))) | (iA << ((int) j6)))) + j2;
    }

    public static c a() {
        return f2839b;
    }

    public static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            sb.append(f2840c[(bArr[i] & 240) >>> 4]);
            sb.append(f2840c[bArr[i] & 15]);
        }
        return sb.toString();
    }

    private void a(byte[] bArr, int i) {
        int i2;
        byte[] bArr2 = new byte[64];
        long[] jArr = this.e;
        int i3 = ((int) (jArr[0] >>> 3)) & 63;
        long j = i << 3;
        long j2 = jArr[0] + j;
        jArr[0] = j2;
        if (j2 < j) {
            jArr[1] = jArr[1] + 1;
        }
        long[] jArr2 = this.e;
        jArr2[1] = jArr2[1] + ((long) (i >>> 29));
        int i4 = 64 - i3;
        if (i >= i4) {
            a(this.f, bArr, i3, 0, i4);
            b(this.f);
            while (i4 + 63 < i) {
                a(bArr2, bArr, 0, i4, 64);
                b(bArr2);
                i4 += 64;
            }
            i3 = 0;
            i2 = i4;
        } else {
            i2 = 0;
        }
        a(this.f, bArr, i3, i2, i - i2);
    }

    private void a(byte[] bArr, byte[] bArr2, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i + i4] = bArr2[i2 + i4];
        }
    }

    private void a(byte[] bArr, long[] jArr, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3 += 4) {
            bArr[i3] = (byte) (jArr[i2] & 255);
            bArr[i3 + 1] = (byte) ((jArr[i2] >>> 8) & 255);
            bArr[i3 + 2] = (byte) ((jArr[i2] >>> 16) & 255);
            bArr[i3 + 3] = (byte) ((jArr[i2] >>> 24) & 255);
            i2++;
        }
    }

    private void a(long[] jArr, byte[] bArr, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3 += 4) {
            jArr[i2] = a(bArr[i3]) | (a(bArr[i3 + 1]) << 8) | (a(bArr[i3 + 2]) << 16) | (a(bArr[i3 + 3]) << 24);
            i2++;
        }
    }

    private long b(long j, long j2, long j3) {
        return (j & j3) | (j2 & (~j3));
    }

    private long b(long j, long j2, long j3, long j4, long j5, long j6, long j7) {
        int iB = (int) (b(j2, j3, j4) + j5 + j7 + j);
        return ((long) ((iB >>> ((int) (32 - j6))) | (iB << ((int) j6)))) + j2;
    }

    public static String b(byte b2) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        return new String(new char[]{cArr[(b2 >>> 4) & 15], cArr[b2 & 15]});
    }

    private void b() {
        long[] jArr = this.e;
        jArr[0] = 0;
        jArr[1] = 0;
        long[] jArr2 = this.f2841d;
        jArr2[0] = 1732584193;
        jArr2[1] = 4023233417L;
        jArr2[2] = 2562383102L;
        jArr2[3] = 271733878;
    }

    private void b(byte[] bArr) {
        long[] jArr = this.f2841d;
        long j = jArr[0];
        long j2 = jArr[1];
        long j3 = jArr[2];
        long j4 = jArr[3];
        long[] jArr2 = new long[16];
        a(jArr2, bArr, 64);
        long jA = a(j, j2, j3, j4, jArr2[0], 7L, 3614090360L);
        long jA2 = a(j4, jA, j2, j3, jArr2[1], 12L, 3905402710L);
        long jA3 = a(j3, jA2, jA, j2, jArr2[2], 17L, 606105819L);
        long jA4 = a(j2, jA3, jA2, jA, jArr2[3], 22L, 3250441966L);
        long jA5 = a(jA, jA4, jA3, jA2, jArr2[4], 7L, 4118548399L);
        long jA6 = a(jA2, jA5, jA4, jA3, jArr2[5], 12L, 1200080426L);
        long jA7 = a(jA3, jA6, jA5, jA4, jArr2[6], 17L, 2821735955L);
        long jA8 = a(jA4, jA7, jA6, jA5, jArr2[7], 22L, 4249261313L);
        long jA9 = a(jA5, jA8, jA7, jA6, jArr2[8], 7L, 1770035416L);
        long jA10 = a(jA6, jA9, jA8, jA7, jArr2[9], 12L, 2336552879L);
        long jA11 = a(jA7, jA10, jA9, jA8, jArr2[10], 17L, 4294925233L);
        long jA12 = a(jA8, jA11, jA10, jA9, jArr2[11], 22L, 2304563134L);
        long jA13 = a(jA9, jA12, jA11, jA10, jArr2[12], 7L, 1804603682L);
        long jA14 = a(jA10, jA13, jA12, jA11, jArr2[13], 12L, 4254626195L);
        long jA15 = a(jA11, jA14, jA13, jA12, jArr2[14], 17L, 2792965006L);
        long jA16 = a(jA12, jA15, jA14, jA13, jArr2[15], 22L, 1236535329L);
        long jB = b(jA13, jA16, jA15, jA14, jArr2[1], 5L, 4129170786L);
        long jB2 = b(jA14, jB, jA16, jA15, jArr2[6], 9L, 3225465664L);
        long jB3 = b(jA15, jB2, jB, jA16, jArr2[11], 14L, 643717713L);
        long jB4 = b(jA16, jB3, jB2, jB, jArr2[0], 20L, 3921069994L);
        long jB5 = b(jB, jB4, jB3, jB2, jArr2[5], 5L, 3593408605L);
        long jB6 = b(jB2, jB5, jB4, jB3, jArr2[10], 9L, 38016083L);
        long jB7 = b(jB3, jB6, jB5, jB4, jArr2[15], 14L, 3634488961L);
        long jB8 = b(jB4, jB7, jB6, jB5, jArr2[4], 20L, 3889429448L);
        long jB9 = b(jB5, jB8, jB7, jB6, jArr2[9], 5L, 568446438L);
        long jB10 = b(jB6, jB9, jB8, jB7, jArr2[14], 9L, 3275163606L);
        long jB11 = b(jB7, jB10, jB9, jB8, jArr2[3], 14L, 4107603335L);
        long jB12 = b(jB8, jB11, jB10, jB9, jArr2[8], 20L, 1163531501L);
        long jB13 = b(jB9, jB12, jB11, jB10, jArr2[13], 5L, 2850285829L);
        long jB14 = b(jB10, jB13, jB12, jB11, jArr2[2], 9L, 4243563512L);
        long jB15 = b(jB11, jB14, jB13, jB12, jArr2[7], 14L, 1735328473L);
        long jB16 = b(jB12, jB15, jB14, jB13, jArr2[12], 20L, 2368359562L);
        long jC = c(jB13, jB16, jB15, jB14, jArr2[5], 4L, 4294588738L);
        long jC2 = c(jB14, jC, jB16, jB15, jArr2[8], 11L, 2272392833L);
        long jC3 = c(jB15, jC2, jC, jB16, jArr2[11], 16L, 1839030562L);
        long jC4 = c(jB16, jC3, jC2, jC, jArr2[14], 23L, 4259657740L);
        long jC5 = c(jC, jC4, jC3, jC2, jArr2[1], 4L, 2763975236L);
        long jC6 = c(jC2, jC5, jC4, jC3, jArr2[4], 11L, 1272893353L);
        long jC7 = c(jC3, jC6, jC5, jC4, jArr2[7], 16L, 4139469664L);
        long jC8 = c(jC4, jC7, jC6, jC5, jArr2[10], 23L, 3200236656L);
        long jC9 = c(jC5, jC8, jC7, jC6, jArr2[13], 4L, 681279174L);
        long jC10 = c(jC6, jC9, jC8, jC7, jArr2[0], 11L, 3936430074L);
        long jC11 = c(jC7, jC10, jC9, jC8, jArr2[3], 16L, 3572445317L);
        long jC12 = c(jC8, jC11, jC10, jC9, jArr2[6], 23L, 76029189L);
        long jC13 = c(jC9, jC12, jC11, jC10, jArr2[9], 4L, 3654602809L);
        long jC14 = c(jC10, jC13, jC12, jC11, jArr2[12], 11L, 3873151461L);
        long jC15 = c(jC11, jC14, jC13, jC12, jArr2[15], 16L, 530742520L);
        long jC16 = c(jC12, jC15, jC14, jC13, jArr2[2], 23L, 3299628645L);
        long jD = d(jC13, jC16, jC15, jC14, jArr2[0], 6L, 4096336452L);
        long jD2 = d(jC14, jD, jC16, jC15, jArr2[7], 10L, 1126891415L);
        long jD3 = d(jC15, jD2, jD, jC16, jArr2[14], 15L, 2878612391L);
        long jD4 = d(jC16, jD3, jD2, jD, jArr2[5], 21L, 4237533241L);
        long jD5 = d(jD, jD4, jD3, jD2, jArr2[12], 6L, 1700485571L);
        long jD6 = d(jD2, jD5, jD4, jD3, jArr2[3], 10L, 2399980690L);
        long jD7 = d(jD3, jD6, jD5, jD4, jArr2[10], 15L, 4293915773L);
        long jD8 = d(jD4, jD7, jD6, jD5, jArr2[1], 21L, 2240044497L);
        long jD9 = d(jD5, jD8, jD7, jD6, jArr2[8], 6L, 1873313359L);
        long jD10 = d(jD6, jD9, jD8, jD7, jArr2[15], 10L, 4264355552L);
        long jD11 = d(jD7, jD10, jD9, jD8, jArr2[6], 15L, 2734768916L);
        long jD12 = d(jD8, jD11, jD10, jD9, jArr2[13], 21L, 1309151649L);
        long jD13 = d(jD9, jD12, jD11, jD10, jArr2[4], 6L, 4149444226L);
        long jD14 = d(jD10, jD13, jD12, jD11, jArr2[11], 10L, 3174756917L);
        long jD15 = d(jD11, jD14, jD13, jD12, jArr2[2], 15L, 718787259L);
        long jD16 = d(jD12, jD15, jD14, jD13, jArr2[9], 21L, 3951481745L);
        long[] jArr3 = this.f2841d;
        jArr3[0] = jArr3[0] + jD13;
        jArr3[1] = jArr3[1] + jD16;
        jArr3[2] = jArr3[2] + jD15;
        jArr3[3] = jArr3[3] + jD14;
    }

    private long c(long j, long j2, long j3) {
        return (j ^ j2) ^ j3;
    }

    private long c(long j, long j2, long j3, long j4, long j5, long j6, long j7) {
        int iC = (int) (c(j2, j3, j4) + j5 + j7 + j);
        return ((long) ((iC >>> ((int) (32 - j6))) | (iC << ((int) j6)))) + j2;
    }

    private void c() {
        byte[] bArr = new byte[8];
        a(bArr, this.e, 8);
        int i = ((int) (this.e[0] >>> 3)) & 63;
        a(f2838a, i < 56 ? 56 - i : 120 - i);
        a(bArr, 8);
        a(this.g, this.f2841d, 16);
    }

    private long d(long j, long j2, long j3) {
        return (j | (~j3)) ^ j2;
    }

    private long d(long j, long j2, long j3, long j4, long j5, long j6, long j7) {
        int iD = (int) (d(j2, j3, j4) + j5 + j7 + j);
        return ((long) ((iD >>> ((int) (32 - j6))) | (iD << ((int) j6)))) + j2;
    }

    public String a(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            messageDigest = null;
        }
        messageDigest.update(str.getBytes(Charset.forName("UTF-8")));
        return a(messageDigest.digest());
    }

    public String b(String str) {
        b();
        a(str.getBytes(Charset.forName("UTF-8")), str.length());
        c();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(b(this.g[i]));
        }
        return sb.toString();
    }
}
