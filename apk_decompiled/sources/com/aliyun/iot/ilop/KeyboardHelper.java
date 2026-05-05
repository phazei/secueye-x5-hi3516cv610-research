package com.aliyun.iot.ilop;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import tools.LocationUtil;

/* JADX INFO: loaded from: classes2.dex */
public class KeyboardHelper {
    public static KeyboardHelper newInstance() {
        return new KeyboardHelper();
    }

    public void hideSoftInputForHw(Activity activity2, View view2) {
        InputMethodManager inputMethodManager;
        if (!LocationUtil.MANUFACTURER_HUAWEI.equalsIgnoreCase(Build.MANUFACTURER) || Build.VERSION.SDK_INT < 27 || (inputMethodManager = (InputMethodManager) activity2.getSystemService("input_method")) == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(view2.getWindowToken(), 2);
    }
}
