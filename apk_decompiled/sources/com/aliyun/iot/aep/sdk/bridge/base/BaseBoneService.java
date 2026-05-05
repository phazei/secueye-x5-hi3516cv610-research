package com.aliyun.iot.aep.sdk.bridge.base;

import android.content.Context;
import com.aliyun.iot.aep.sdk.bridge.a;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneService;

/* JADX INFO: loaded from: classes2.dex */
public class BaseBoneService implements BoneService {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    a f4602a = new a();
    private boolean isBoneInit = true;

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
    public void onInitialize(Context context) {
        if (this.isBoneInit) {
            this.f4602a.a(this);
        }
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
    public final boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
        try {
            if (this.isBoneInit) {
                return this.f4602a.a(this, jSContext, boneCall, boneCallback);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            boneCallback.failed("608", "method invoke failed", boneCall.methodName + " execute failed");
            return false;
        }
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
    public void onDestroy() {
        if (this.isBoneInit) {
            this.f4602a.a();
        }
    }
}
