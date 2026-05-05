package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentAppMeshStrategy;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;

/* JADX INFO: compiled from: ConcurrentAppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class J extends ApiCallBack<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConcurrentAppMeshStrategy.a f3667a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ConcurrentAppMeshStrategy f3668b;

    public J(ConcurrentAppMeshStrategy concurrentAppMeshStrategy, ConcurrentAppMeshStrategy.a aVar) {
        this.f3668b = concurrentAppMeshStrategy;
        this.f3667a = aVar;
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onFail(int i, String str) {
        ConcurrentAppMeshStrategy.a aVar = this.f3667a;
        if (aVar != null) {
            aVar.onFail(-1, "recover pk is failed");
        }
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onSuccess(Object obj) {
        ConcurrentAppMeshStrategy.a aVar = this.f3667a;
        if (aVar == null) {
            return;
        }
        aVar.onSuccess(obj);
    }
}
