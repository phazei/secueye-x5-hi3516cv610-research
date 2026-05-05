package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;

/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class A implements TimerUtils.ITimerCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3656a;

    public A(AppMeshStrategy appMeshStrategy) {
        this.f3656a = appMeshStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
    public void onTimeout() {
        this.f3656a.getProvisionTimeoutErrorInfo();
    }
}
