package com.alibaba.ailabs.tg.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.alibaba.ailabs.tg.storage.IOUtils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class ImageUtils {
    private ImageUtils() {
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

    public static Bitmap getBitmap(File file) {
        if (file == null) {
            return null;
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmap(File file, int i, int i2) {
        if (file == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static Bitmap getBitmap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return BitmapFactory.decodeFile(str);
    }

    public static Bitmap getBitmap(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    public static Bitmap getBitmap(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap getBitmap(InputStream inputStream, int i, int i2) {
        if (inputStream == null) {
            return null;
        }
        return getBitmap(input2Byte(inputStream), 0, i, i2);
    }

    public static Bitmap getBitmap(byte[] bArr, int i) {
        if (bArr.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bArr, i, bArr.length);
    }

    public static Bitmap getBitmap(byte[] bArr, int i, int i2, int i3) {
        if (bArr.length == 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, i, bArr.length, options);
        options.inSampleSize = calculateInSampleSize(options, i2, i3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bArr, i, bArr.length, options);
    }

    public static Bitmap getBitmap(@NonNull Context context, @DrawableRes int i) {
        Drawable drawable = ContextCompat.getDrawable(context, i);
        Canvas canvas = new Canvas();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmapCreateBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static Bitmap getBitmap(@NonNull Context context, @DrawableRes int i, int i2, int i3) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Resources resources = context.getResources();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInSampleSize(options, i2, i3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    public static Bitmap getBitmap(FileDescriptor fileDescriptor) {
        if (fileDescriptor == null) {
            return null;
        }
        return BitmapFactory.decodeFileDescriptor(fileDescriptor);
    }

    public static Bitmap getBitmap(FileDescriptor fileDescriptor, int i, int i2) {
        if (fileDescriptor == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static Bitmap drawColor(@NonNull Bitmap bitmap, @ColorInt int i) {
        return drawColor(bitmap, i, false);
    }

    public static Bitmap drawColor(@NonNull Bitmap bitmap, @ColorInt int i, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        if (!z) {
            bitmap = bitmap.copy(bitmap.getConfig(), true);
        }
        new Canvas(bitmap).drawColor(i, PorterDuff.Mode.DARKEN);
        return bitmap;
    }

    public static Bitmap scale(Bitmap bitmap, int i, int i2) {
        return scale(bitmap, i, i2, false);
    }

    public static Bitmap scale(Bitmap bitmap, int i, int i2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i2, true);
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateScaledBitmap;
    }

    public static Bitmap scale(Bitmap bitmap, float f, float f2) {
        return scale(bitmap, f, f2, false);
    }

    public static Bitmap scale(Bitmap bitmap, float f, float f2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(f, f2);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap clip(Bitmap bitmap, int i, int i2, int i3, int i4) {
        return clip(bitmap, i, i2, i3, i4, false);
    }

    public static Bitmap clip(Bitmap bitmap, int i, int i2, int i3, int i4, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, i, i2, i3, i4);
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap skew(Bitmap bitmap, float f, float f2) {
        return skew(bitmap, f, f2, 0.0f, 0.0f, false);
    }

    public static Bitmap skew(Bitmap bitmap, float f, float f2, boolean z) {
        return skew(bitmap, f, f2, 0.0f, 0.0f, z);
    }

    public static Bitmap skew(Bitmap bitmap, float f, float f2, float f3, float f4) {
        return skew(bitmap, f, f2, f3, f4, false);
    }

    public static Bitmap skew(Bitmap bitmap, float f, float f2, float f3, float f4, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setSkew(f, f2, f3, f4);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap rotate(Bitmap bitmap, int i, float f, float f2) {
        return rotate(bitmap, i, f, f2, false);
    }

    public static Bitmap rotate(Bitmap bitmap, int i, float f, float f2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        if (i == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(i, f, f2);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static int getRotateDegree(String str) {
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 1);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt != 6) {
                return attributeInt != 8 ? 0 : 270;
            }
            return 90;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap toRound(Bitmap bitmap) {
        return toRound(bitmap, 0, 0, false);
    }

    public static Bitmap toRound(Bitmap bitmap, boolean z) {
        return toRound(bitmap, 0, 0, z);
    }

    public static Bitmap toRound(Bitmap bitmap, @IntRange(from = 0) int i, @ColorInt int i2) {
        return toRound(bitmap, i, i2, false);
    }

    public static Bitmap toRound(Bitmap bitmap, @IntRange(from = 0) int i, @ColorInt int i2, boolean z) {
        if (isEmptyBitmap(bitmap) || bitmap.getConfig() == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int iMin = Math.min(width, height);
        Paint paint = new Paint(1);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        float f = iMin;
        float f2 = f / 2.0f;
        float f3 = width;
        float f4 = height;
        RectF rectF = new RectF(0.0f, 0.0f, f3, f4);
        rectF.inset((width - iMin) / 2.0f, (height - iMin) / 2.0f);
        Matrix matrix = new Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        matrix.preScale(f / f3, f / f4);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        canvas.drawRoundRect(rectF, f2, f2, paint);
        if (i > 0) {
            paint.setShader(null);
            paint.setColor(i2);
            paint.setStyle(Paint.Style.STROKE);
            float f5 = i;
            paint.setStrokeWidth(f5);
            canvas.drawCircle(f3 / 2.0f, f4 / 2.0f, f2 - (f5 / 2.0f), paint);
        }
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, float f) {
        return toRoundCorner(bitmap, f, 0, 0, false);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, float f, boolean z) {
        return toRoundCorner(bitmap, f, 0, 0, z);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, float f, @IntRange(from = 0) int i, @ColorInt int i2) {
        return toRoundCorner(bitmap, f, i, i2, false);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, float f, @IntRange(from = 0) int i, @ColorInt int i2, boolean z) {
        if (isEmptyBitmap(bitmap) || bitmap.getConfig() == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Paint paint = new Paint(1);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        RectF rectF = new RectF(0.0f, 0.0f, width, height);
        float f2 = i;
        float f3 = f2 / 2.0f;
        rectF.inset(f3, f3);
        canvas.drawRoundRect(rectF, f, f, paint);
        if (i > 0) {
            paint.setShader(null);
            paint.setColor(i2);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(f2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawRoundRect(rectF, f, f, paint);
        }
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap addCornerBorder(Bitmap bitmap, @IntRange(from = 1) int i, @ColorInt int i2, @FloatRange(from = 0.0d) float f) {
        return addBorder(bitmap, i, i2, false, f, false);
    }

    public static Bitmap addCornerBorder(Bitmap bitmap, @IntRange(from = 1) int i, @ColorInt int i2, @FloatRange(from = 0.0d) float f, boolean z) {
        return addBorder(bitmap, i, i2, false, f, z);
    }

    public static Bitmap addCircleBorder(Bitmap bitmap, @IntRange(from = 1) int i, @ColorInt int i2) {
        return addBorder(bitmap, i, i2, true, 0.0f, false);
    }

    public static Bitmap addCircleBorder(Bitmap bitmap, @IntRange(from = 1) int i, @ColorInt int i2, boolean z) {
        return addBorder(bitmap, i, i2, true, 0.0f, z);
    }

    private static Bitmap addBorder(Bitmap bitmap, @IntRange(from = 1) int i, @ColorInt int i2, boolean z, float f, boolean z2) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        if (!z2) {
            bitmap = bitmap.copy(bitmap.getConfig(), true);
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(1);
        paint.setColor(i2);
        paint.setStyle(Paint.Style.STROKE);
        float f2 = i;
        paint.setStrokeWidth(f2);
        if (z) {
            canvas.drawCircle(width / 2.0f, height / 2.0f, (Math.min(width, height) / 2.0f) - (f2 / 2.0f), paint);
        } else {
            float f3 = i >> 1;
            canvas.drawRoundRect(new RectF(f3, f3, width - r5, height - r5), f, f, paint);
        }
        return bitmap;
    }

    public static Bitmap addReflection(Bitmap bitmap, int i) {
        return addReflection(bitmap, i, false);
    }

    public static Bitmap addReflection(Bitmap bitmap, int i, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, height - i, width, i, matrix, false);
        Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(width, height + i, bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapCreateBitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        float f = height + 0;
        canvas.drawBitmap(bitmapCreateBitmap, 0.0f, f, (Paint) null);
        Paint paint = new Paint(1);
        paint.setShader(new LinearGradient(0.0f, height, 0.0f, bitmapCreateBitmap2.getHeight() + 0, 1895825407, 16777215, Shader.TileMode.MIRROR));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0f, f, width, bitmapCreateBitmap2.getHeight(), paint);
        if (!bitmapCreateBitmap.isRecycled()) {
            bitmapCreateBitmap.recycle();
        }
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap2;
    }

    public static Bitmap addTextWatermark(Bitmap bitmap, String str, int i, @ColorInt int i2, float f, float f2) {
        return addTextWatermark(bitmap, str, i, i2, f, f2, false);
    }

    public static Bitmap addTextWatermark(Bitmap bitmap, String str, float f, @ColorInt int i, float f2, float f3, boolean z) {
        if (isEmptyBitmap(bitmap) || str == null) {
            return null;
        }
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), true);
        Paint paint = new Paint(1);
        Canvas canvas = new Canvas(bitmapCopy);
        paint.setColor(i);
        paint.setTextSize(f);
        paint.getTextBounds(str, 0, str.length(), new Rect());
        canvas.drawText(str, f2, f3 + f, paint);
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCopy;
    }

    public static Bitmap addImageWatermark(Bitmap bitmap, Bitmap bitmap2, int i, int i2, int i3) {
        return addImageWatermark(bitmap, bitmap2, i, i2, i3, false);
    }

    public static Bitmap addImageWatermark(Bitmap bitmap, Bitmap bitmap2, int i, int i2, int i3, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), true);
        if (!isEmptyBitmap(bitmap2)) {
            Paint paint = new Paint(1);
            Canvas canvas = new Canvas(bitmapCopy);
            paint.setAlpha(i3);
            canvas.drawBitmap(bitmap2, i, i2, paint);
        }
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCopy;
    }

    public static Bitmap toAlpha(Bitmap bitmap) {
        return toAlpha(bitmap, false);
    }

    public static Bitmap toAlpha(Bitmap bitmap, Boolean bool) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        Bitmap bitmapExtractAlpha = bitmap.extractAlpha();
        if (bool.booleanValue() && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapExtractAlpha;
    }

    public static Bitmap toGray(Bitmap bitmap) {
        return toGray(bitmap, false);
    }

    public static Bitmap toGray(Bitmap bitmap, boolean z) {
        if (isEmptyBitmap(bitmap) || bitmap.getConfig() == null) {
            return null;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap fastBlur(@NonNull Context context, Bitmap bitmap, @FloatRange(from = 0.0d, fromInclusive = false, to = 1.0d) float f, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float f2) {
        return fastBlur(context, bitmap, f, f2, false);
    }

    public static Bitmap fastBlur(@NonNull Context context, Bitmap bitmap, @FloatRange(from = 0.0d, fromInclusive = false, to = 1.0d) float f, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float f2, boolean z) throws Throwable {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(f, f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Paint paint = new Paint(3);
        Canvas canvas = new Canvas();
        paint.setColorFilter(new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_ATOP));
        canvas.scale(f, f);
        canvas.drawBitmap(bitmapCreateBitmap, 0.0f, 0.0f, paint);
        Bitmap bitmapRenderScriptBlur = renderScriptBlur(context, bitmapCreateBitmap, f2, z);
        if (f == 1.0f) {
            if (z && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return bitmapRenderScriptBlur;
        }
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapRenderScriptBlur, width, height, true);
        if (!bitmapRenderScriptBlur.isRecycled()) {
            bitmapRenderScriptBlur.recycle();
        }
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmapCreateScaledBitmap;
    }

    public static Bitmap renderScriptBlur(Context context, Bitmap bitmap, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float f) {
        return renderScriptBlur(context, bitmap, f, false);
    }

    public static Bitmap renderScriptBlur(@NonNull Context context, Bitmap bitmap, @FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float f, boolean z) throws Throwable {
        RenderScript renderScriptCreate;
        if (!z) {
            bitmap = bitmap.copy(bitmap.getConfig(), true);
        }
        try {
            renderScriptCreate = RenderScript.create(context);
            try {
                renderScriptCreate.setMessageHandler(new RenderScript.RSMessageHandler());
                Allocation allocationCreateFromBitmap = Allocation.createFromBitmap(renderScriptCreate, bitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
                Allocation allocationCreateTyped = Allocation.createTyped(renderScriptCreate, allocationCreateFromBitmap.getType());
                ScriptIntrinsicBlur scriptIntrinsicBlurCreate = ScriptIntrinsicBlur.create(renderScriptCreate, Element.U8_4(renderScriptCreate));
                scriptIntrinsicBlurCreate.setInput(allocationCreateFromBitmap);
                scriptIntrinsicBlurCreate.setRadius(f);
                scriptIntrinsicBlurCreate.forEach(allocationCreateTyped);
                allocationCreateTyped.copyTo(bitmap);
                if (renderScriptCreate != null) {
                    renderScriptCreate.destroy();
                }
                return bitmap;
            } catch (Throwable th) {
                th = th;
                if (renderScriptCreate != null) {
                    renderScriptCreate.destroy();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            renderScriptCreate = null;
        }
    }

    public static Bitmap stackBlur(Bitmap bitmap, int i) {
        return stackBlur(bitmap, i, false);
    }

    public static Bitmap stackBlur(Bitmap bitmap, int i, boolean z) {
        int i2;
        int i3;
        int[] iArr;
        Bitmap bitmapCopy = z ? bitmap : bitmap.copy(bitmap.getConfig(), true);
        int i4 = i < 1 ? 1 : i;
        int width = bitmapCopy.getWidth();
        int height = bitmapCopy.getHeight();
        int i5 = width * height;
        int[] iArr2 = new int[i5];
        bitmapCopy.getPixels(iArr2, 0, width, 0, 0, width, height);
        int i6 = width - 1;
        int i7 = height - 1;
        int i8 = i4 + i4 + 1;
        int[] iArr3 = new int[i5];
        int[] iArr4 = new int[i5];
        int[] iArr5 = new int[i5];
        int[] iArr6 = new int[Math.max(width, height)];
        int i9 = (i8 + 1) >> 1;
        int i10 = i9 * i9;
        int i11 = i10 * 256;
        int[] iArr7 = new int[i11];
        for (int i12 = 0; i12 < i11; i12++) {
            iArr7[i12] = i12 / i10;
        }
        int[][] iArr8 = (int[][]) Array.newInstance((Class<?>) int.class, i8, 3);
        int i13 = i4 + 1;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        while (i14 < height) {
            Bitmap bitmap2 = bitmapCopy;
            int i17 = -i4;
            int i18 = 0;
            int i19 = 0;
            int i20 = 0;
            int i21 = 0;
            int i22 = 0;
            int i23 = 0;
            int i24 = 0;
            int i25 = 0;
            int i26 = 0;
            while (i17 <= i4) {
                int i27 = i7;
                int i28 = height;
                int i29 = iArr2[i15 + Math.min(i6, Math.max(i17, 0))];
                int[] iArr9 = iArr8[i17 + i4];
                iArr9[0] = (i29 & 16711680) >> 16;
                iArr9[1] = (i29 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr9[2] = i29 & 255;
                int iAbs = i13 - Math.abs(i17);
                i18 += iArr9[0] * iAbs;
                i19 += iArr9[1] * iAbs;
                i20 += iArr9[2] * iAbs;
                if (i17 > 0) {
                    i24 += iArr9[0];
                    i25 += iArr9[1];
                    i26 += iArr9[2];
                } else {
                    i21 += iArr9[0];
                    i22 += iArr9[1];
                    i23 += iArr9[2];
                }
                i17++;
                height = i28;
                i7 = i27;
            }
            int i30 = i7;
            int i31 = height;
            int i32 = i4;
            int i33 = 0;
            while (i33 < width) {
                iArr3[i15] = iArr7[i18];
                iArr4[i15] = iArr7[i19];
                iArr5[i15] = iArr7[i20];
                int i34 = i18 - i21;
                int i35 = i19 - i22;
                int i36 = i20 - i23;
                int[] iArr10 = iArr8[((i32 - i4) + i8) % i8];
                int i37 = i21 - iArr10[0];
                int i38 = i22 - iArr10[1];
                int i39 = i23 - iArr10[2];
                if (i14 == 0) {
                    iArr = iArr7;
                    iArr6[i33] = Math.min(i33 + i4 + 1, i6);
                } else {
                    iArr = iArr7;
                }
                int i40 = iArr2[i16 + iArr6[i33]];
                iArr10[0] = (i40 & 16711680) >> 16;
                iArr10[1] = (i40 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                iArr10[2] = i40 & 255;
                int i41 = i24 + iArr10[0];
                int i42 = i25 + iArr10[1];
                int i43 = i26 + iArr10[2];
                i18 = i34 + i41;
                i19 = i35 + i42;
                i20 = i36 + i43;
                i32 = (i32 + 1) % i8;
                int[] iArr11 = iArr8[i32 % i8];
                i21 = i37 + iArr11[0];
                i22 = i38 + iArr11[1];
                i23 = i39 + iArr11[2];
                i24 = i41 - iArr11[0];
                i25 = i42 - iArr11[1];
                i26 = i43 - iArr11[2];
                i15++;
                i33++;
                iArr7 = iArr;
            }
            i16 += width;
            i14++;
            height = i31;
            bitmapCopy = bitmap2;
            i7 = i30;
        }
        int i44 = i7;
        Bitmap bitmap3 = bitmapCopy;
        int i45 = height;
        int[] iArr12 = iArr7;
        int i46 = 0;
        while (i46 < width) {
            int i47 = -i4;
            int i48 = i47 * width;
            int i49 = 0;
            int i50 = 0;
            int i51 = 0;
            int i52 = 0;
            int i53 = 0;
            int i54 = 0;
            int i55 = 0;
            int i56 = 0;
            int i57 = 0;
            while (i47 <= i4) {
                int[] iArr13 = iArr6;
                int iMax = Math.max(0, i48) + i46;
                int[] iArr14 = iArr8[i47 + i4];
                iArr14[0] = iArr3[iMax];
                iArr14[1] = iArr4[iMax];
                iArr14[2] = iArr5[iMax];
                int iAbs2 = i13 - Math.abs(i47);
                i49 += iArr3[iMax] * iAbs2;
                i50 += iArr4[iMax] * iAbs2;
                i51 += iArr5[iMax] * iAbs2;
                if (i47 > 0) {
                    i55 += iArr14[0];
                    i56 += iArr14[1];
                    i57 += iArr14[2];
                    i3 = i44;
                } else {
                    i52 += iArr14[0];
                    i53 += iArr14[1];
                    i54 += iArr14[2];
                    i3 = i44;
                }
                if (i47 < i3) {
                    i48 += width;
                }
                i47++;
                i44 = i3;
                iArr6 = iArr13;
            }
            int[] iArr15 = iArr6;
            int i58 = i44;
            int i59 = i45;
            int i60 = i56;
            int i61 = i57;
            int i62 = 0;
            int i63 = i4;
            int i64 = i55;
            int i65 = i54;
            int i66 = i53;
            int i67 = i52;
            int i68 = i51;
            int i69 = i50;
            int i70 = i49;
            int i71 = i46;
            while (i62 < i59) {
                iArr2[i71] = (iArr2[i71] & ViewCompat.MEASURED_STATE_MASK) | (iArr12[i70] << 16) | (iArr12[i69] << 8) | iArr12[i68];
                int i72 = i70 - i67;
                int i73 = i69 - i66;
                int i74 = i68 - i65;
                int[] iArr16 = iArr8[((i63 - i4) + i8) % i8];
                int i75 = i67 - iArr16[0];
                int i76 = i66 - iArr16[1];
                int i77 = i65 - iArr16[2];
                if (i46 == 0) {
                    i2 = i4;
                    iArr15[i62] = Math.min(i62 + i13, i58) * width;
                } else {
                    i2 = i4;
                }
                int i78 = iArr15[i62] + i46;
                iArr16[0] = iArr3[i78];
                iArr16[1] = iArr4[i78];
                iArr16[2] = iArr5[i78];
                int i79 = i64 + iArr16[0];
                int i80 = i60 + iArr16[1];
                int i81 = i61 + iArr16[2];
                i70 = i72 + i79;
                i69 = i73 + i80;
                i68 = i74 + i81;
                i63 = (i63 + 1) % i8;
                int[] iArr17 = iArr8[i63];
                i67 = i75 + iArr17[0];
                i66 = i76 + iArr17[1];
                i65 = i77 + iArr17[2];
                i64 = i79 - iArr17[0];
                i60 = i80 - iArr17[1];
                i61 = i81 - iArr17[2];
                i71 += width;
                i62++;
                i4 = i2;
            }
            i46++;
            i44 = i58;
            i45 = i59;
            iArr6 = iArr15;
        }
        bitmap3.setPixels(iArr2, 0, width, 0, 0, width, i45);
        return bitmap3;
    }

    public static boolean save(Bitmap bitmap, String str, Bitmap.CompressFormat compressFormat) {
        return save(bitmap, FileUtils.getFileByPath(str), compressFormat, false);
    }

    public static boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat compressFormat) {
        return save(bitmap, file, compressFormat, false);
    }

    public static boolean save(Bitmap bitmap, String str, Bitmap.CompressFormat compressFormat, boolean z) {
        return save(bitmap, FileUtils.getFileByPath(str), compressFormat, z);
    }

    public static boolean save(Bitmap bitmap, File file, Bitmap.CompressFormat compressFormat, boolean z) throws Throwable {
        BufferedOutputStream bufferedOutputStream;
        boolean zCompress = false;
        if (isEmptyBitmap(bitmap) || !FileUtils.createFileByDeleteOldFile(file)) {
            return false;
        }
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            zCompress = bitmap.compress(compressFormat, 100, bufferedOutputStream);
            if (z && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            IOUtils.closeQuietly(bufferedOutputStream);
        } catch (IOException e2) {
            e = e2;
            bufferedOutputStream2 = bufferedOutputStream;
            e.printStackTrace();
            IOUtils.closeQuietly(bufferedOutputStream2);
        } catch (Throwable th2) {
            th = th2;
            bufferedOutputStream2 = bufferedOutputStream;
            IOUtils.closeQuietly(bufferedOutputStream2);
            throw th;
        }
        return zCompress;
    }

    public static boolean isImage(File file) {
        return file != null && isImage(file.getPath());
    }

    public static boolean isImage(String str) {
        String upperCase = str.toUpperCase(Locale.getDefault());
        return upperCase.endsWith(".PNG") || upperCase.endsWith(".JPG") || upperCase.endsWith(".JPEG") || upperCase.endsWith(".BMP") || upperCase.endsWith(".GIF") || upperCase.endsWith(".WEBP");
    }

    public static String getImageType(String str) {
        return getImageType(FileUtils.getFileByPath(str));
    }

    public static String getImageType(File file) throws Throwable {
        FileInputStream fileInputStream;
        String imageType;
        FileInputStream fileInputStream2 = null;
        if (file == null) {
            return null;
        }
        try {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (IOException e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            imageType = getImageType(fileInputStream);
        } catch (IOException e2) {
            e = e2;
            fileInputStream2 = fileInputStream;
            e.printStackTrace();
            IOUtils.closeQuietly(fileInputStream2);
        } catch (Throwable th2) {
            th = th2;
            fileInputStream2 = fileInputStream;
            IOUtils.closeQuietly(fileInputStream2);
            throw th;
        }
        if (imageType != null) {
            IOUtils.closeQuietly(fileInputStream);
            return imageType;
        }
        IOUtils.closeQuietly(fileInputStream);
        return getFileExtension(file.getAbsolutePath()).toUpperCase(Locale.getDefault());
    }

    private static String getFileExtension(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int iLastIndexOf = str.lastIndexOf(46);
        return (iLastIndexOf == -1 || str.lastIndexOf(File.separator) >= iLastIndexOf) ? "" : str.substring(iLastIndexOf + 1);
    }

    private static String getImageType(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            byte[] bArr = new byte[8];
            if (inputStream.read(bArr, 0, 8) != -1) {
                return getImageType(bArr);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getImageType(byte[] bArr) {
        if (isJPEG(bArr)) {
            return "JPEG";
        }
        if (isGIF(bArr)) {
            return "GIF";
        }
        if (isPNG(bArr)) {
            return "PNG";
        }
        if (isBMP(bArr)) {
            return "BMP";
        }
        return null;
    }

    private static boolean isJPEG(byte[] bArr) {
        return bArr.length >= 2 && bArr[0] == -1 && bArr[1] == -40;
    }

    private static boolean isGIF(byte[] bArr) {
        return bArr.length >= 6 && bArr[0] == 71 && bArr[1] == 73 && bArr[2] == 70 && bArr[3] == 56 && (bArr[4] == 55 || bArr[4] == 57) && bArr[5] == 97;
    }

    private static boolean isPNG(byte[] bArr) {
        return bArr.length >= 8 && bArr[0] == -119 && bArr[1] == 80 && bArr[2] == 78 && bArr[3] == 71 && bArr[4] == 13 && bArr[5] == 10 && bArr[6] == 26 && bArr[7] == 10;
    }

    private static boolean isBMP(byte[] bArr) {
        return bArr.length >= 2 && bArr[0] == 66 && bArr[1] == 77;
    }

    private static boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }

    public static Bitmap compressByScale(Bitmap bitmap, int i, int i2) {
        return scale(bitmap, i, i2, false);
    }

    public static Bitmap compressByScale(Bitmap bitmap, int i, int i2, boolean z) {
        return scale(bitmap, i, i2, z);
    }

    public static Bitmap compressByScale(Bitmap bitmap, float f, float f2) {
        return scale(bitmap, f, f2, false);
    }

    public static Bitmap compressByScale(Bitmap bitmap, float f, float f2, boolean z) {
        return scale(bitmap, f, f2, z);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, @IntRange(from = 0, to = 100) int i) {
        return compressByQuality(bitmap, i, false);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, @IntRange(from = 0, to = 100) int i, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, long j) {
        return compressByQuality(bitmap, j, false);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, long j, boolean z) {
        byte[] byteArray;
        if (isEmptyBitmap(bitmap) || j <= 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        if (byteArrayOutputStream.size() <= j) {
            byteArray = byteArrayOutputStream.toByteArray();
        } else {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
            if (byteArrayOutputStream.size() >= j) {
                byteArray = byteArrayOutputStream.toByteArray();
            } else {
                int i2 = 0;
                int i3 = 0;
                while (i2 < i) {
                    i3 = (i2 + i) / 2;
                    byteArrayOutputStream.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, i3, byteArrayOutputStream);
                    long size = byteArrayOutputStream.size();
                    if (size == j) {
                        break;
                    }
                    if (size > j) {
                        i = i3 - 1;
                    } else {
                        i2 = i3 + 1;
                    }
                }
                if (i == i3 - 1) {
                    byteArrayOutputStream.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, i2, byteArrayOutputStream);
                }
                byteArray = byteArrayOutputStream.toByteArray();
            }
        }
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap compressBySampleSize(Bitmap bitmap, int i) {
        return compressBySampleSize(bitmap, i, false);
    }

    public static Bitmap compressBySampleSize(Bitmap bitmap, int i, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = i;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    public static Bitmap compressBySampleSize(Bitmap bitmap, int i, int i2) {
        return compressBySampleSize(bitmap, i, i2, false);
    }

    public static Bitmap compressBySampleSize(Bitmap bitmap, int i, int i2, boolean z) {
        if (isEmptyBitmap(bitmap)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        if (z && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        while (true) {
            i4 >>= 1;
            if (i4 < i || (i3 = i3 >> 1) < i2) {
                break;
            }
            i5 <<= 1;
        }
        return i5;
    }

    private static byte[] input2Byte(InputStream inputStream) {
        try {
            if (inputStream == null) {
                return null;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStream.read(bArr, 0, 1024);
                if (i != -1) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
