package com.aliyun.iot.link.ui.component.wheelview;

import android.content.res.Resources;
import android.util.TypedValue;

/* JADX INFO: loaded from: classes2.dex */
public class DimensionUtil {
    public static float dip2px(float f) {
        return TypedValue.applyDimension(1, f, Resources.getSystem().getDisplayMetrics());
    }

    public static float sp2px(float f) {
        return TypedValue.applyDimension(2, f, Resources.getSystem().getDisplayMetrics());
    }
}
