package com.aliyun.iot.aep.sdk.bridge.core.context;

import android.app.Activity;
import android.content.Intent;

/* JADX INFO: loaded from: classes2.dex */
public interface OnActivityResultManager {

    public interface OnActivityResultListener {
        void onActivityResult(Activity activity2, int i, int i2, Intent intent);
    }

    void addOnActivityResultListener(OnActivityResultListener onActivityResultListener);

    void removeOnActivityResultListener(OnActivityResultListener onActivityResultListener);
}
