package com.aliyun.iot.aep.sdk.bridge.core.context;

import android.app.Activity;

/* JADX INFO: loaded from: classes2.dex */
public interface ActivityLifeCircleManager {

    public interface ActivityLifeCircleListener {
        void onPause(Activity activity2);

        void onResume(Activity activity2);

        void onStart(Activity activity2);

        void onStop(Activity activity2);
    }

    void addActivityLifeCircleListener(ActivityLifeCircleListener activityLifeCircleListener);

    void removeActivityLifeCircleListener(ActivityLifeCircleListener activityLifeCircleListener);
}
