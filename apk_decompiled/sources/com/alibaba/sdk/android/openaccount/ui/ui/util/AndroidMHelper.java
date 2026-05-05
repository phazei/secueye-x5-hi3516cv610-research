package com.alibaba.sdk.android.openaccount.ui.ui.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public class AndroidMHelper implements IStatusBarFontHelper {
    @Override // com.alibaba.sdk.android.openaccount.ui.ui.util.IStatusBarFontHelper
    public boolean setStatusBarLightMode(Activity activity2, boolean z) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        final View decorView = activity2.getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        final int i = z ? systemUiVisibility | 8192 : systemUiVisibility | 0;
        decorView.setSystemUiVisibility(i);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.util.AndroidMHelper.1
            @Override // android.view.View.OnSystemUiVisibilityChangeListener
            public void onSystemUiVisibilityChange(int i2) {
                if ((i2 & 4) == 0) {
                    decorView.setSystemUiVisibility(i);
                }
            }
        });
        return true;
    }
}
