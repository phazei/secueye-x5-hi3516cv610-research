package com.alibaba.sdk.android.openaccount.ui.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;

/* JADX INFO: loaded from: classes.dex */
public class AttributeUtils {
    private static final String THEME_STYLEABLE = "customAttrs";

    public static TypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet, String str) {
        return context.obtainStyledAttributes(attributeSet, ResourceUtils.getRStyleableIntArray(context, str));
    }

    public static TypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet) {
        return context.obtainStyledAttributes(attributeSet, ResourceUtils.getRStyleableIntArray(context, THEME_STYLEABLE));
    }

    public static Drawable getDrawable(Context context, TypedArray typedArray, String str, String str2) {
        return typedArray.getDrawable(ResourceUtils.getRStyleable(context, str + OpenAccountUIConstants.UNDER_LINE + str2));
    }

    public static Drawable getDrawable(Context context, TypedArray typedArray, String str) {
        return getDrawable(context, typedArray, THEME_STYLEABLE, str);
    }

    public static int getResourceId(Context context, TypedArray typedArray, String str, String str2) {
        return typedArray.getResourceId(ResourceUtils.getRStyleable(context, str + OpenAccountUIConstants.UNDER_LINE + str2), 0);
    }

    public static int getResourceId(Context context, TypedArray typedArray, String str) {
        return getResourceId(context, typedArray, THEME_STYLEABLE, str);
    }

    public static int getColor(Context context, TypedArray typedArray, String str, String str2) {
        return typedArray.getColor(ResourceUtils.getRStyleable(context, str + OpenAccountUIConstants.UNDER_LINE + str2), 0);
    }

    public static int getColor(Context context, TypedArray typedArray, String str) {
        return getColor(context, typedArray, THEME_STYLEABLE, str);
    }

    public static ColorStateList getColorStateList(Context context, TypedArray typedArray, String str, String str2) {
        return typedArray.getColorStateList(ResourceUtils.getRStyleable(context, str + OpenAccountUIConstants.UNDER_LINE + str2));
    }

    public static ColorStateList getColorStateList(Context context, TypedArray typedArray, String str) {
        return getColorStateList(context, typedArray, THEME_STYLEABLE, str);
    }

    public static int getInt(Context context, TypedArray typedArray, String str, String str2) {
        return typedArray.getInt(ResourceUtils.getRStyleable(context, str + OpenAccountUIConstants.UNDER_LINE + str2), 0);
    }

    public static int getInt(Context context, TypedArray typedArray, String str) {
        return getInt(context, typedArray, THEME_STYLEABLE, str);
    }

    public static boolean getBoolean(Context context, TypedArray typedArray, String str, String str2) {
        return typedArray.getBoolean(ResourceUtils.getRStyleable(context, str + OpenAccountUIConstants.UNDER_LINE + str2), false);
    }

    public static boolean getBoolean(Context context, TypedArray typedArray, String str) {
        return getBoolean(context, typedArray, THEME_STYLEABLE, str);
    }

    public static String getString(Context context, TypedArray typedArray, String str, String str2) {
        return typedArray.getString(ResourceUtils.getRStyleable(context, str + OpenAccountUIConstants.UNDER_LINE + str2));
    }

    public static String getString(Context context, TypedArray typedArray, String str) {
        return getString(context, typedArray, THEME_STYLEABLE, str);
    }
}
