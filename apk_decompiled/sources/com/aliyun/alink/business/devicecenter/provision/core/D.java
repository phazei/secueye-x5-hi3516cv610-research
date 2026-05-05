package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.mesh.delegate.OnReadyToBindHandler;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;
import datasource.MeshConfigCallback;

/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class D implements OnReadyToBindHandler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3659a;

    public D(AppMeshStrategy appMeshStrategy) {
        this.f3659a = appMeshStrategy;
    }

    @Override // com.alibaba.ailabs.iot.mesh.delegate.OnReadyToBindHandler
    public void onReadyToBind(String str, MeshConfigCallback<Boolean> meshConfigCallback) {
        ALog.d(AppMeshStrategy.TAG, "mesh sdk onReadyToBind: " + str);
        if (this.f3659a.provisionHasStopped.get()) {
            return;
        }
        if (meshConfigCallback != null) {
            meshConfigCallback.onSuccess(true);
        }
        ALog.d(AppMeshStrategy.TAG, "provision success form mesh sdk.");
        this.f3659a.provisionSuccess();
    }
}
