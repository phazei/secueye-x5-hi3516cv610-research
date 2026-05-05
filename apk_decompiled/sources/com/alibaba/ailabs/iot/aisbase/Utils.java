package com.alibaba.ailabs.iot.aisbase;

import android.text.TextUtils;
import com.alibaba.ailabs.tg.storage.IOUtils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public class Utils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2534a = "Utils";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static char[] f2535b = "0123456789ABCDEF".toCharArray();

    public static int adapterToAisVersion(String str) {
        try {
            if (!TextUtils.isEmpty(str) && str.length() >= 5 && str.length() <= 8) {
                String[] strArrSplit = str.split("\\.");
                if (strArrSplit.length != 3) {
                    return 0;
                }
                int[] iArr = {Integer.parseInt(strArrSplit[0]), Integer.parseInt(strArrSplit[1]), Integer.parseInt(strArrSplit[2])};
                return (iArr[0] << 16) | (iArr[1] << 8) | iArr[2];
            }
        } catch (NumberFormatException unused) {
        }
        return 0;
    }

    public static String adapterToOsUpdateVersion(int i) {
        byte[] bArrInt2ByteArrayByBigEndian = int2ByteArrayByBigEndian(i);
        LogUtils.d(f2534a, "osUpdate version: " + ConvertUtils.bytes2HexString(bArrInt2ByteArrayByBigEndian));
        return (bArrInt2ByteArrayByBigEndian == null || bArrInt2ByteArrayByBigEndian.length < 4) ? "" : String.format("%d.%d.%d-S-00000000.0000", Integer.valueOf(bArrInt2ByteArrayByBigEndian[1] & 255), Integer.valueOf(bArrInt2ByteArrayByBigEndian[2] & 255), Integer.valueOf(bArrInt2ByteArrayByBigEndian[3] & 255));
    }

    public static String byte2String(byte b2, boolean z) {
        StringBuilder sb;
        String str;
        int i = b2 & 255;
        char[] cArr = f2535b;
        char[] cArr2 = {cArr[i >>> 4], cArr[i & 15]};
        if (z) {
            sb = new StringBuilder();
            str = "0x";
        } else {
            sb = new StringBuilder();
            str = "";
        }
        sb.append(str);
        sb.append(String.valueOf(cArr2));
        return sb.toString();
    }

    public static int byteArray2Int(byte[] bArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.BIG_ENDIAN);
        return byteBufferWrap.getInt();
    }

    public static int byteArray2IntByLittleEndian(byte[] bArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.LITTLE_ENDIAN);
        return byteBufferWrap.getInt();
    }

    public static String formatMacAddress(byte[] bArr) {
        StringBuilder sb = new StringBuilder(18);
        for (byte b2 : bArr) {
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(String.format("%02x", Byte.valueOf(b2)));
        }
        return sb.toString().toUpperCase();
    }

    public static int genCrc16CCITT(byte[] bArr, int i, int i2) {
        int i3 = 65535;
        while (i < i2) {
            int i4 = (((i3 << 8) | (i3 >>> 8)) & 65535) ^ (bArr[i] & 255);
            int i5 = i4 ^ ((i4 & 255) >> 4);
            int i6 = i5 ^ ((i5 << 12) & 65535);
            i3 = i6 ^ (((i6 & 255) << 5) & 65535);
            i++;
        }
        return i3 & 65535;
    }

    public static byte[] int2ByteArrayByBigEndian(int i) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(i).array();
    }

    public static byte[] int2ByteArrayByLittleEndian(int i) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array();
    }

    public static String md5(File file) throws Throwable {
        FileInputStream fileInputStream;
        FileChannel channel;
        byte[] bArrDigest;
        MessageDigest messageDigest;
        FileChannel fileChannel = null;
        try {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                fileInputStream = new FileInputStream(file);
            } catch (Throwable th) {
                th = th;
            }
            try {
                channel = fileInputStream.getChannel();
            } catch (IOException e) {
                e = e;
                channel = null;
            } catch (NoSuchAlgorithmException e2) {
                e = e2;
            } catch (Throwable th2) {
                th = th2;
                channel = null;
                IOUtils.closeQuietly(fileInputStream);
                IOUtils.closeQuietly(channel);
                throw th;
            }
            try {
                messageDigest.update(channel.map(FileChannel.MapMode.READ_ONLY, 0L, file.length()));
                bArrDigest = messageDigest.digest();
                IOUtils.closeQuietly(fileInputStream);
                IOUtils.closeQuietly(channel);
            } catch (IOException e3) {
                e = e3;
                e.printStackTrace();
                IOUtils.closeQuietly(fileInputStream);
                IOUtils.closeQuietly(channel);
                bArrDigest = null;
            } catch (NoSuchAlgorithmException e4) {
                e = e4;
                fileChannel = channel;
                try {
                    throw new RuntimeException(e);
                } catch (Throwable th3) {
                    th = th3;
                    channel = fileChannel;
                    IOUtils.closeQuietly(fileInputStream);
                    IOUtils.closeQuietly(channel);
                    throw th;
                }
            }
        } catch (IOException e5) {
            e = e5;
            fileInputStream = null;
            channel = null;
        } catch (NoSuchAlgorithmException e6) {
            e = e6;
            fileInputStream = null;
        } catch (Throwable th4) {
            th = th4;
            fileInputStream = null;
            channel = null;
        }
        if (bArrDigest == null) {
            return null;
        }
        return ConvertUtils.bytes2HexString(bArrDigest);
    }

    public static String adapterToOsUpdateVersion(byte[] bArr) {
        return (bArr == null || bArr.length < 4) ? "" : String.format("%d.%d.%d-S-00000000.0000", Integer.valueOf(bArr[2] & 255), Integer.valueOf(bArr[1] & 255), Integer.valueOf(bArr[0] & 255));
    }
}
