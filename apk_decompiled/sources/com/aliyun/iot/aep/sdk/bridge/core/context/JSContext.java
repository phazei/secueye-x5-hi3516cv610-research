package com.aliyun.iot.aep.sdk.bridge.core.context;

import android.app.Activity;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public interface JSContext extends ActivityLifeCircleManager, OnActivityResultManager, OnNewIntentManager {
    void emitter(String str, JSONObject jSONObject);

    Activity getCurrentActivity();

    String getCurrentUrl();

    String getId();

    void reload();
}
