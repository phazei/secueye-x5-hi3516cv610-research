package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.provision.core.mesh.GatewayMeshStrategy;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;

/* JADX INFO: compiled from: GatewayMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class P implements TimerUtils.ITimerCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ GatewayMeshStrategy f3675a;

    public P(GatewayMeshStrategy gatewayMeshStrategy) {
        this.f3675a = gatewayMeshStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
    public void onTimeout() {
        this.f3675a.getProvisionTimeoutErrorInfo();
    }
}
