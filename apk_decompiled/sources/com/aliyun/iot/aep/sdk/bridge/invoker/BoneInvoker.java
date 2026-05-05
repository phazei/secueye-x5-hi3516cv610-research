package com.aliyun.iot.aep.sdk.bridge.invoker;

import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;

/* JADX INFO: loaded from: classes2.dex */
public interface BoneInvoker {
    void invoke(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback, BoneCallback boneCallback2);

    void onDestroy();
}
