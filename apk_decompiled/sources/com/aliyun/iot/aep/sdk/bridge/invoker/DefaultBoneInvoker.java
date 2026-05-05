package com.aliyun.iot.aep.sdk.bridge.invoker;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallMode;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultBoneInvoker implements BoneInvoker {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private BoneInvoker f4624a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private BoneInvoker f4625b;

    public DefaultBoneInvoker(BoneInvoker boneInvoker, BoneInvoker boneInvoker2) {
        this.f4624a = boneInvoker;
        this.f4625b = boneInvoker2;
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.invoker.BoneInvoker
    public void invoke(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback, BoneCallback boneCallback2) {
        if (jSContext == null) {
            throw new IllegalArgumentException("context can not be null");
        }
        if (jSContext.getCurrentActivity() == null) {
            ALog.d("DefaultBoneInvoker", "ignore call after destroy");
            return;
        }
        if (TextUtils.isEmpty(jSContext.getCurrentUrl())) {
            throw new IllegalArgumentException("jsContext.getCurrentUrl can not be empty");
        }
        if (boneCall == null) {
            throw new IllegalArgumentException("call can not be null");
        }
        if (TextUtils.isEmpty(boneCall.serviceId)) {
            throw new IllegalArgumentException("call.serviceId can not be empty");
        }
        if (TextUtils.isEmpty(boneCall.methodName)) {
            throw new IllegalArgumentException("call.methodName can not be empty");
        }
        if (boneCallback == null) {
            throw new IllegalArgumentException("syncCallback can not be null");
        }
        if (BoneCallMode.ASYNC == boneCall.mode && boneCallback2 == null) {
            throw new IllegalArgumentException("asyncCallback can not be null when call mode is async");
        }
        if (BoneCallMode.ASYNC == boneCall.mode) {
            this.f4624a.invoke(jSContext, boneCall, boneCallback, boneCallback2);
        } else {
            this.f4625b.invoke(jSContext, boneCall, boneCallback, null);
        }
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.invoker.BoneInvoker
    public void onDestroy() {
        BoneInvoker boneInvoker = this.f4624a;
        if (boneInvoker != null) {
            boneInvoker.onDestroy();
            this.f4624a = null;
        }
        BoneInvoker boneInvoker2 = this.f4625b;
        if (boneInvoker2 != null) {
            boneInvoker2.onDestroy();
            this.f4625b = null;
        }
    }
}
