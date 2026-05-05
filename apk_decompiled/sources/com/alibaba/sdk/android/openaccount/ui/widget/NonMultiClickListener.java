package com.alibaba.sdk.android.openaccount.ui.widget;

import android.util.Log;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public abstract class NonMultiClickListener implements View.OnClickListener {
    private static final int MIN_GAP = 1000;
    private static String TAG = "login.nonClick";
    private long lastClickTime;

    public abstract void onMonMultiClick(View view2);

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.lastClickTime > 1000) {
            this.lastClickTime = jCurrentTimeMillis;
            onMonMultiClick(view2);
        } else {
            Log.d(TAG, "click too fast");
        }
    }
}
