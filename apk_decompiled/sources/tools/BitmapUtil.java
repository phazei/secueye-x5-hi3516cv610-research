package tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/* JADX INFO: loaded from: classes4.dex */
public class BitmapUtil {
    public static Bitmap decodeUri(Context context, Uri uri, int i, int i2) throws Throwable {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        readBitmapScale(context, uri, options);
        int i3 = 1;
        for (int i4 = 0; i4 < Integer.MAX_VALUE && ((options.outWidth / i3 > i && options.outWidth / i3 > ((double) i) * 1.4d) || (options.outHeight / i3 > i2 && options.outHeight / i3 > ((double) i2) * 1.4d)); i4++) {
            i3++;
        }
        options.inSampleSize = i3;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            return readBitmapData(context, uri, options);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private static void readBitmapScale(Context context, Uri uri, BitmapFactory.Options options) throws Throwable {
        String str;
        StringBuilder sb;
        InputStream inputStreamOpenInputStream;
        if (uri == null) {
            return;
        }
        String scheme = uri.getScheme();
        if ("content".equals(scheme) || "file".equals(scheme)) {
            InputStream inputStream = null;
            try {
                try {
                    inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
                if (inputStreamOpenInputStream != null) {
                    try {
                        inputStreamOpenInputStream.close();
                        return;
                    } catch (IOException e2) {
                        e = e2;
                        str = "readBitmapScale";
                        sb = new StringBuilder();
                        sb.append("Unable to close content: ");
                        sb.append(uri);
                        Log.e(str, sb.toString(), e);
                        return;
                    }
                }
                return;
            } catch (Exception e3) {
                e = e3;
                inputStream = inputStreamOpenInputStream;
                Log.w("readBitmapScale", "Unable to open content: " + uri, e);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        return;
                    } catch (IOException e4) {
                        e = e4;
                        str = "readBitmapScale";
                        sb = new StringBuilder();
                        sb.append("Unable to close content: ");
                        sb.append(uri);
                        Log.e(str, sb.toString(), e);
                        return;
                    }
                }
                return;
            } catch (Throwable th2) {
                th = th2;
                inputStream = inputStreamOpenInputStream;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e5) {
                        Log.e("readBitmapScale", "Unable to close content: " + uri, e5);
                    }
                }
                throw th;
            }
        }
        if ("android.resource".equals(scheme)) {
            Log.e("readBitmapScale", "Unable to close content: " + uri);
            return;
        }
        Log.e("readBitmapScale", "Unable to close content: " + uri);
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00b9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.graphics.Bitmap readBitmapData(android.content.Context r4, android.net.Uri r5, android.graphics.BitmapFactory.Options r6) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 213
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: tools.BitmapUtil.readBitmapData(android.content.Context, android.net.Uri, android.graphics.BitmapFactory$Options):android.graphics.Bitmap");
    }

    public static Bitmap compoundBitmap(File[] fileArr, int i) {
        Bitmap bitmapGetThumbImage;
        boolean z = true;
        if (i == 1) {
            if (fileArr.length > 0) {
                return GetThumbImage(fileArr[0].toString());
            }
            return null;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(480, 320, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < fileArr.length) {
            int i5 = i3 * PsExtractor.VIDEO_STREAM_MASK;
            int i6 = 160 * i4;
            try {
                bitmapGetThumbImage = GetThumbImage(fileArr[i2].toString());
            } catch (Exception e) {
                e.printStackTrace();
                bitmapGetThumbImage = null;
            }
            if (bitmapGetThumbImage != null) {
                Paint paint = new Paint();
                paint.setAntiAlias(z);
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
                canvas.drawBitmap(bitmapGetThumbImage, (Rect) null, new RectF(i5, i6, i5 + PsExtractor.VIDEO_STREAM_MASK, i6 + 160), paint);
            }
            i3++;
            if ((i2 + 3) % 2 == 0) {
                i4++;
                i3 = 0;
            }
            i2++;
            z = true;
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap GetThumbImage(String str) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            return BitmapFactory.decodeFile(str, options);
        } catch (RuntimeException e) {
            System.out.println("RuntimeException获取缩略图出错：" + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            System.out.println("获取缩略图出错：" + e2.getMessage());
            e2.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCodeBitmap(String str, int i, int i2, String str2, String str3, String str4, int i3, int i4) {
        if (TextUtils.isEmpty(str) || i < 0 || i2 < 0) {
            return null;
        }
        try {
            Hashtable hashtable = new Hashtable();
            if (!TextUtils.isEmpty(str2)) {
                hashtable.put(EncodeHintType.CHARACTER_SET, str2);
            }
            if (!TextUtils.isEmpty(str3)) {
                hashtable.put(EncodeHintType.ERROR_CORRECTION, str3);
            }
            if (!TextUtils.isEmpty(str4)) {
                hashtable.put(EncodeHintType.MARGIN, str4);
            }
            BitMatrix bitMatrixEncode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i2, hashtable);
            int[] iArr = new int[i * i2];
            for (int i5 = 0; i5 < i2; i5++) {
                for (int i6 = 0; i6 < i; i6++) {
                    if (bitMatrixEncode.get(i6, i5)) {
                        iArr[(i5 * i) + i6] = i3;
                    } else {
                        iArr[(i5 * i) + i6] = i4;
                    }
                }
            }
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            bitmapCreateBitmap.setPixels(iArr, 0, i, 0, 0, i, i2);
            return bitmapCreateBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap LoadBitmapFromView(View view2) {
        view2.setDrawingCacheEnabled(true);
        view2.setDrawingCacheQuality(1048576);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(view2.getWidth(), view2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        view2.draw(canvas);
        return bitmapCreateBitmap;
    }
}
