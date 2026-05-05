package com.aliyun.iot.aep.sdk.bridge.core.context;

import android.app.Activity;
import android.content.Intent;

/* JADX INFO: loaded from: classes2.dex */
public interface OnNewIntentManager {

    public interface OnNewIntentListener {
        void onNewIntent(Activity activity2, Intent intent);
    }

    void addOnNewIntentListener(OnNewIntentListener onNewIntentListener);

    void removeOnNewIntentListener(OnNewIntentListener onNewIntentListener);
}
