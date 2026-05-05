package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.GatewayMeshStrategy;

/* JADX INFO: compiled from: GatewayMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class U implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f3679a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ GatewayMeshStrategy f3680b;

    public U(GatewayMeshStrategy gatewayMeshStrategy, String str) {
        this.f3680b = gatewayMeshStrategy;
        this.f3679a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f3680b.provisionHasStopped.get()) {
            return;
        }
        ProvisionRepository.getMeshProvisionResult(this.f3679a, new T(this));
    }
}
