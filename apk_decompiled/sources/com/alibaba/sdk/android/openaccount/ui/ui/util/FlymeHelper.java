package com.alibaba.sdk.android.openaccount.ui.ui.util;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
public class FlymeHelper implements IStatusBarFontHelper {
    @Override // com.alibaba.sdk.android.openaccount.ui.ui.util.IStatusBarFontHelper
    public boolean setStatusBarLightMode(Activity activity2, boolean z) {
        Window window = activity2.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams attributes = window.getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                window.setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }
}
