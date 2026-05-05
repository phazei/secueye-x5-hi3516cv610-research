package com.aliyun.alink.business.devicecenter.utils;

import com.aliyun.alink.business.devicecenter.log.ALog;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.xiaomi.mipush.sdk.Constants;
import java.util.Random;
import tools.G711Code;

/* JADX INFO: loaded from: classes2.dex */
public class AlinkWifiSolutionUtils {
    public static int[] CRC16_TABLE = {0, 49345, 49537, 320, 49921, 960, 640, 49729, 50689, 1728, 1920, 51009, 1280, 50625, 50305, 1088, 52225, 3264, 3456, 52545, 3840, 53185, 52865, 3648, 2560, 51905, 52097, 2880, 51457, 2496, 2176, 51265, 55297, 6336, 6528, 55617, 6912, 56257, 55937, 6720, 7680, 57025, 57217, 8000, 56577, 7616, 7296, 56385, 5120, 54465, 54657, 5440, 55041, 6080, 5760, 54849, 53761, 4800, 4992, 54081, 4352, 53697, 53377, 4160, 61441, 12480, 12672, 61761, 13056, 62401, 62081, 12864, 13824, 63169, 63361, 14144, 62721, 13760, 13440, 62529, 15360, 64705, 64897, 15680, 65281, 16320, G711Code.SAMPLE_RATE_INHZ_16000, 65089, 64001, 15040, 15232, 64321, 14592, 63937, 63617, 14400, 10240, 59585, 59777, 10560, 60161, 11200, 10880, 59969, 60929, 11968, 12160, 61249, 11520, 60865, 60545, 11328, 58369, 9408, 9600, 58689, 9984, 59329, 59009, 9792, 8704, 58049, 58241, 9024, 57601, 8640, 8320, 57409, 40961, 24768, 24960, 41281, 25344, 41921, 41601, 25152, 26112, 42689, 42881, 26432, 42241, 26048, 25728, 42049, 27648, 44225, 44417, 27968, 44801, 28608, 28288, 44609, 43521, 27328, 27520, 43841, 26880, 43457, 43137, 26688, 30720, 47297, 47489, 31040, 47873, 31680, 31360, 47681, 48641, 32448, 32640, 48961, 32000, 48577, 48257, 31808, 46081, 29888, 30080, 46401, 30464, 47041, 46721, 30272, 29184, 45761, 45953, 29504, 45313, 29120, 28800, 45121, CacheDataSink.DEFAULT_BUFFER_SIZE, 37057, 37249, 20800, 37633, 21440, 21120, 37441, 38401, 22208, 22400, 38721, 21760, 38337, 38017, 21568, 39937, 23744, 23936, 40257, 24320, 40897, 40577, 24128, 23040, 39617, 39809, 23360, 39169, 22976, 22656, 38977, 34817, 18624, 18816, 35137, 19200, 35777, 35457, 19008, 19968, 36545, 36737, 20288, 36097, 19904, 19584, 35905, 17408, 33985, 34177, 17728, 34561, 18368, 18048, 34369, 33281, 17088, 17280, 33601, 16640, 33217, 32897, 16448};
    public static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static byte[] byteMerge(byte[] bArr, byte[] bArr2, int i) {
        if (bArr2 == null) {
            return bArr;
        }
        System.arraycopy(bArr2, 0, bArr, i, bArr2.length);
        return bArr;
    }

    public static String bytesToHexString(byte[] bArr) {
        char[] cArr = new char[bArr.length * 2];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            int i3 = i * 2;
            char[] cArr2 = hexArray;
            cArr[i3] = cArr2[i2 >>> 4];
            cArr[i3 + 1] = cArr2[i2 & 15];
        }
        return new String(cArr);
    }

    public static byte[] eightBitsToSevenBits(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        byte[] bArr2 = new byte[(bArr.length * 8) + 6];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = i + 1;
            bArr2[i] = (byte) (bArr[i2] & 1);
            int i4 = i3 + 1;
            bArr2[i3] = (byte) ((bArr[i2] >> 1) & 1);
            int i5 = i4 + 1;
            bArr2[i4] = (byte) ((bArr[i2] >> 2) & 1);
            int i6 = i5 + 1;
            bArr2[i5] = (byte) ((bArr[i2] >> 3) & 1);
            int i7 = i6 + 1;
            bArr2[i6] = (byte) ((bArr[i2] >> 4) & 1);
            int i8 = i7 + 1;
            bArr2[i7] = (byte) ((bArr[i2] >> 5) & 1);
            int i9 = i8 + 1;
            bArr2[i8] = (byte) ((bArr[i2] >> 6) & 1);
            i = i9 + 1;
            bArr2[i9] = (byte) ((bArr[i2] >> 7) & 1);
        }
        int length = ((bArr.length * 8) + 6) / 7;
        byte[] bArr3 = new byte[length];
        for (int i10 = 0; i10 < length; i10++) {
            int i11 = i10 * 7;
            bArr3[i10] = (byte) ((bArr2[i11 + 6] << 6) | bArr2[i11] | (bArr2[i11 + 1] << 1) | (bArr2[i11 + 2] << 2) | (bArr2[i11 + 3] << 3) | (bArr2[i11 + 4] << 4) | (bArr2[i11 + 5] << 5));
        }
        return bArr3;
    }

    public static byte[] eightBitsToSixBits(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        byte[] bArr2 = new byte[(bArr.length * 8) + 5];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = i + 1;
            bArr2[i] = (byte) (bArr[i2] & 1);
            int i4 = i3 + 1;
            bArr2[i3] = (byte) ((bArr[i2] >> 1) & 1);
            int i5 = i4 + 1;
            bArr2[i4] = (byte) ((bArr[i2] >> 2) & 1);
            int i6 = i5 + 1;
            bArr2[i5] = (byte) ((bArr[i2] >> 3) & 1);
            int i7 = i6 + 1;
            bArr2[i6] = (byte) ((bArr[i2] >> 4) & 1);
            int i8 = i7 + 1;
            bArr2[i7] = (byte) ((bArr[i2] >> 5) & 1);
            int i9 = i8 + 1;
            bArr2[i8] = (byte) ((bArr[i2] >> 6) & 1);
            i = i9 + 1;
            bArr2[i9] = (byte) ((bArr[i2] >> 7) & 1);
        }
        int length = ((bArr.length * 8) + 5) / 6;
        byte[] bArr3 = new byte[length];
        for (int i10 = 0; i10 < length; i10++) {
            int i11 = i10 * 6;
            bArr3[i10] = (byte) ((bArr2[i11 + 5] << 5) | bArr2[i11] | (bArr2[i11 + 1] << 1) | (bArr2[i11 + 2] << 2) | (bArr2[i11 + 3] << 3) | (bArr2[i11 + 4] << 4));
        }
        return bArr3;
    }

    public static byte[] getCrc(byte[] bArr) {
        int i = 0;
        for (byte b2 : bArr) {
            i = CRC16_TABLE[(i ^ b2) & 255] ^ (i >>> 8);
        }
        return shortToByteArray((short) i);
    }

    public static String getInetAddress(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static String getRandomString(int i) {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt(62)));
        }
        return stringBuffer.toString();
    }

    public static byte[] hexStringTobytes(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static String longToIp(long j) {
        return (j >>> 24) + "." + String.valueOf((16777215 & j) >>> 16) + "." + String.valueOf((65535 & j) >>> 8) + "." + String.valueOf(j & 255);
    }

    public static void printByteArray(byte[] bArr) {
        ALog.d("AlinkWifiSolutionUtils", "send byte array:");
        StringBuilder sb = new StringBuilder();
        sb.append("start");
        for (byte b2 : bArr) {
            sb.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER + ((int) b2) + Constants.ACCEPT_TIME_SEPARATOR_SERVER);
        }
        ALog.d("AlinkWifiSolutionUtils", sb.toString());
    }

    public static byte[] shortToByteArray(short s) {
        return new byte[]{(byte) ((s >> 12) & 15), (byte) ((s >> 8) & 15), (byte) ((s >> 4) & 15), (byte) (s & 15)};
    }
}
