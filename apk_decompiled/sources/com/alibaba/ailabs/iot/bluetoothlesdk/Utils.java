package com.alibaba.ailabs.iot.bluetoothlesdk;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import java.nio.ByteBuffer;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class Utils {
    public static int SIZE_LONG = 8;

    public static String formatMacAddress(String str) {
        return TextUtils.isEmpty(str) ? "" : str.replace(":", "").toLowerCase();
    }

    public static String adapterToIotServerVersion(int i) {
        byte[] bArrInt2ByteArrayByBigEndian = com.alibaba.ailabs.iot.aisbase.Utils.int2ByteArrayByBigEndian(i);
        return (bArrInt2ByteArrayByBigEndian == null || bArrInt2ByteArrayByBigEndian.length < 4) ? "" : String.format("%d.%d.%d", Integer.valueOf(bArrInt2ByteArrayByBigEndian[1]), Integer.valueOf(bArrInt2ByteArrayByBigEndian[2]), Integer.valueOf(bArrInt2ByteArrayByBigEndian[3]));
    }

    public static byte[] longToBytes(long j) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(SIZE_LONG);
        byteBufferAllocate.putLong(0, j);
        return byteBufferAllocate.array();
    }

    public static long bytesToLong(byte[] bArr) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(SIZE_LONG);
        byteBufferAllocate.put(bArr, 0, bArr.length);
        byteBufferAllocate.flip();
        return byteBufferAllocate.getLong();
    }

    public static byte[] hexString2Bytes(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int length = str.length();
        if (length % 2 != 0) {
            str = "0" + str;
            length++;
        }
        char[] charArray = str.toUpperCase(Locale.getDefault()).toCharArray();
        byte[] bArr = new byte[length >> 1];
        for (int i = 0; i < length; i += 2) {
            bArr[i >> 1] = (byte) ((hex2Int(charArray[i]) << 4) | hex2Int(charArray[i + 1]));
        }
        return bArr;
    }

    private static int hex2Int(char c2) {
        if (c2 >= '0' && c2 <= '9') {
            return c2 - '0';
        }
        if (c2 < 'A' || c2 > 'F') {
            throw new IllegalArgumentException();
        }
        return (c2 - 'A') + 10;
    }

    public static <T> void notifySuccess(IActionListener<T> iActionListener, T t) {
        if (iActionListener != null) {
            iActionListener.onSuccess(t);
        }
    }

    public static void notifyFailed(IActionListener iActionListener, int i, String str) {
        if (iActionListener != null) {
            iActionListener.onFailure(i, str);
        }
    }
}
