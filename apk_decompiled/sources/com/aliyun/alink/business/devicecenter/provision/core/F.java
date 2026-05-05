package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.mesh.AuthInfoListener;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentAppMeshStrategy;

/* JADX INFO: compiled from: ConcurrentAppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class F implements AuthInfoListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConcurrentAppMeshStrategy f3662a;

    public F(ConcurrentAppMeshStrategy concurrentAppMeshStrategy) {
        this.f3662a = concurrentAppMeshStrategy;
    }

    @Override // com.alibaba.ailabs.iot.mesh.AuthInfoListener
    public String getAuthInfo() {
        return "";
    }
}
