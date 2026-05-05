package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.mesh.AuthInfoListener;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;

/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class B implements AuthInfoListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3657a;

    public B(AppMeshStrategy appMeshStrategy) {
        this.f3657a = appMeshStrategy;
    }

    @Override // com.alibaba.ailabs.iot.mesh.AuthInfoListener
    public String getAuthInfo() {
        return "";
    }
}
