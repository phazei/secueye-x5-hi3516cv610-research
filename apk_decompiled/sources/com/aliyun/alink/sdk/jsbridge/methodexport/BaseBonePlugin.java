package com.aliyun.alink.sdk.jsbridge.methodexport;

import android.content.Context;
import android.content.Intent;
import com.aliyun.alink.sdk.jsbridge.BoneCallback;
import com.aliyun.alink.sdk.jsbridge.IBonePlugin;
import com.aliyun.alink.sdk.jsbridge.IJSBridge;
import com.aliyun.alink.sdk.jsbridge.a;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseBonePlugin implements IBonePlugin {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    a f4499a = new a();
    protected Context context;
    protected IJSBridge jsBridge;

    @Override // com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    @Override // com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void onDestroy() {
    }

    @Override // com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void onPause() {
    }

    @Override // com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void onResume() {
    }

    @Override // com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public boolean call(String str, Object[] objArr, BoneCallback boneCallback) {
        return this.f4499a.a(this, str, objArr, boneCallback);
    }

    @Override // com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void onInitialize(Context context, IJSBridge iJSBridge) {
        this.context = context;
        this.jsBridge = iJSBridge;
        this.f4499a.a(this);
    }

    @Override // com.aliyun.alink.sdk.jsbridge.IBonePlugin
    public void destroy() {
        this.f4499a.a();
        this.context = null;
        this.jsBridge = null;
    }
}
