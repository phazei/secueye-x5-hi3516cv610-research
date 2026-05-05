package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.u, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0487u extends ApiCallBack<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy.a f3718a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3719b;

    public C0487u(AppMeshStrategy appMeshStrategy, AppMeshStrategy.a aVar) {
        this.f3719b = appMeshStrategy;
        this.f3718a = aVar;
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onFail(int i, String str) {
        AppMeshStrategy.a aVar = this.f3718a;
        if (aVar != null) {
            aVar.onFail(-1, "recover pk is failed");
        }
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onSuccess(Object obj) {
        AppMeshStrategy.a aVar = this.f3718a;
        if (aVar == null) {
            return;
        }
        aVar.onSuccess(obj);
    }
}
