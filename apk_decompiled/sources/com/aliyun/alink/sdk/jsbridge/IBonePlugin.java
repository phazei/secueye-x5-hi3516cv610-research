package com.aliyun.alink.sdk.jsbridge;

import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes2.dex */
public interface IBonePlugin {
    boolean call(String str, Object[] objArr, BoneCallback boneCallback);

    void destroy();

    void onActivityResult(int i, int i2, Intent intent);

    void onDestroy();

    void onInitialize(Context context, IJSBridge iJSBridge);

    void onPause();

    void onResume();
}
