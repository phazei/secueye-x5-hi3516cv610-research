package com.alibaba.ailabs.tg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.tg.storage.IOUtils;
import com.alibaba.ailabs.tg.utils.constant.TimeConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class ConvertUtils {
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static double byte2MemorySize(long j, int i) {
        if (j < 0) {
            return -1.0d;
        }
        return j / ((double) i);
    }

    public static long memorySize2Byte(long j, int i) {
        if (j < 0) {
            return -1L;
        }
        return j * ((long) i);
    }

    public static long timeSpan2Millis(long j, int i) {
        return j * ((long) i);
    }

    private ConvertUtils() {
    }

    public static String bytes2Bits(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b2 : bArr) {
            for (int i = 7; i >= 0; i--) {
                sb.append(((b2 >> i) & 1) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    public static byte[] bits2Bytes(String str) {
        int length = str.length() % 8;
        int length2 = str.length() / 8;
        if (length != 0) {
            while (length < 8) {
                str = "0" + str;
                length++;
            }
            length2++;
        }
        byte[] bArr = new byte[length2];
        for (int i = 0; i < length2; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                bArr[i] = (byte) (bArr[i] << 1);
                bArr[i] = (byte) (bArr[i] | (str.charAt((i * 8) + i2) - '0'));
            }
        }
        return bArr;
    }

    public static char[] bytes2Chars(byte[] bArr) {
        int length;
        if (bArr == null || (length = bArr.length) <= 0) {
            return null;
        }
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            cArr[i] = (char) (bArr[i] & 255);
        }
        return cArr;
    }

    public static byte[] chars2Bytes(char[] cArr) {
        if (cArr == null || cArr.length <= 0) {
            return null;
        }
        int length = cArr.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) cArr[i];
        }
        return bArr;
    }

    public static String bytes2HexString(byte[] bArr) {
        int length;
        if (bArr == null || (length = bArr.length) <= 0) {
            return null;
        }
        char[] cArr = new char[length << 1];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            char[] cArr2 = hexDigits;
            cArr[i] = cArr2[(bArr[i2] >>> 4) & 15];
            i = i3 + 1;
            cArr[i3] = cArr2[bArr[i2] & 15];
        }
        return new String(cArr);
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

    @SuppressLint({"DefaultLocale"})
    public static String byte2FitMemorySize(long j) {
        return j < 0 ? "shouldn't be less than zero!" : j < 1024 ? String.format("%.3fB", Double.valueOf(j)) : j < 1048576 ? String.format("%.3fKB", Double.valueOf(j / 1024.0d)) : j < 1073741824 ? String.format("%.3fMB", Double.valueOf(j / 1048576.0d)) : String.format("%.3fGB", Double.valueOf(j / 1.073741824E9d));
    }

    public static long millis2TimeSpan(long j, int i) {
        return j / ((long) i);
    }

    @SuppressLint({"DefaultLocale"})
    public static String millis2FitTimeSpan(long j, int i) {
        if (j <= 0 || i <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] strArr = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] iArr = {TimeConstants.DAY, TimeConstants.HOUR, 60000, 1000, 1};
        int iMin = Math.min(i, 5);
        for (int i2 = 0; i2 < iMin; i2++) {
            if (j >= iArr[i2]) {
                long j2 = j / ((long) iArr[i2]);
                j -= ((long) iArr[i2]) * j2;
                sb.append(j2);
                sb.append(strArr[i2]);
            }
        }
        return sb.toString();
    }

    public static ByteArrayOutputStream input2OutputStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStream.read(bArr, 0, 1024);
                if (i == -1) {
                    return byteArrayOutputStream;
                }
                byteArrayOutputStream.write(bArr, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static int px2dip(Context context, float f) {
        if (context == null) {
            LogUtils.w("dimen util's context is null.");
            return (int) ((f / 2.0f) + 0.5f);
        }
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dip2px(Context context, float f) {
        if (context == null) {
            LogUtils.w("dimen util's context is null.");
            return (int) ((f * 2.0f) + 0.5f);
        }
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public ByteArrayInputStream output2InputStream(OutputStream outputStream) {
        if (outputStream == null) {
            return null;
        }
        return new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
    }

    public static byte[] inputStream2Bytes(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        return input2OutputStream(inputStream).toByteArray();
    }

    public static InputStream bytes2InputStream(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        return new ByteArrayInputStream(bArr);
    }

    public static byte[] outputStream2Bytes(OutputStream outputStream) {
        if (outputStream == null) {
            return null;
        }
        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r1v0, types: [int] */
    public static OutputStream bytes2OutputStream(byte[] bArr) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        ?? r0 = 0;
        if (bArr != null) {
            ?? length = bArr.length;
            try {
                if (length > 0) {
                    try {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        try {
                            byteArrayOutputStream.write(bArr);
                            IOUtils.closeQuietly(byteArrayOutputStream);
                            return byteArrayOutputStream;
                        } catch (IOException e) {
                            e = e;
                            e.printStackTrace();
                            IOUtils.closeQuietly(byteArrayOutputStream);
                            return null;
                        }
                    } catch (IOException e2) {
                        e = e2;
                        byteArrayOutputStream = null;
                    } catch (Throwable th) {
                        th = th;
                        IOUtils.closeQuietly((Closeable) r0);
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                r0 = length;
            }
        }
        return null;
    }

    public static String inputStream2String(InputStream inputStream, String str) {
        if (inputStream == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new String(inputStream2Bytes(inputStream), str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream string2InputStream(String str, String str2) {
        if (str == null || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            return new ByteArrayInputStream(str.getBytes(str2));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String outputStream2String(OutputStream outputStream, String str) {
        if (outputStream == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new String(outputStream2Bytes(outputStream), str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static OutputStream string2OutputStream(String str, String str2) {
        if (str == null || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            return bytes2OutputStream(str.getBytes(str2));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap bytes2Bitmap(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmapCreateBitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmapCreateBitmap = Bitmap.createBitmap(1, 1, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } else {
            bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static Drawable bitmap2Drawable(@NonNull Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat compressFormat) {
        if (drawable == null) {
            return null;
        }
        return bitmap2Bytes(drawable2Bitmap(drawable), compressFormat);
    }

    public static Drawable bytes2Drawable(@NonNull Context context, byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return bitmap2Drawable(context, bytes2Bitmap(bArr));
    }

    public static Bitmap view2Bitmap(View view2) {
        if (view2 == null) {
            return null;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(view2.getWidth(), view2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Drawable background = view2.getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view2.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static int strToInt(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static int strToInt(String str) {
        return strToInt(str, 0);
    }

    public static long strToLong(String str, long j) {
        if (TextUtils.isEmpty(str)) {
            return j;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return j;
        }
    }

    public static long strToLong(String str) {
        return strToLong(str, 0L);
    }
}
