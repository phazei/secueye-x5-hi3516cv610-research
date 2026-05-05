package com.alibaba.sdk.android.openaccount.util;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class WidgetUtils {
    @SuppressLint({"NewApi"})
    public static void setBackgroundDrawable(View view2, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            view2.setBackgroundDrawable(drawable);
        } else {
            view2.setBackground(drawable);
        }
    }
}
