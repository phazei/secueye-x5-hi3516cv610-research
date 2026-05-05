package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentGateMeshStrategy;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;

/* JADX INFO: compiled from: ConcurrentGateMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class L implements TimerUtils.ITimerCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConcurrentGateMeshStrategy f3671a;

    public L(ConcurrentGateMeshStrategy concurrentGateMeshStrategy) {
        this.f3671a = concurrentGateMeshStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
    public void onTimeout() {
        ALog.d(ConcurrentGateMeshStrategy.TAG, "startProvisionTimer() onTimeout");
        this.f3671a.getProvisionTimeoutErrorInfo();
    }
}
