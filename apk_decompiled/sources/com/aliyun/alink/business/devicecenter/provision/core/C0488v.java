package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.v, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0488v implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy.a f3720a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3721b;

    public C0488v(AppMeshStrategy appMeshStrategy, AppMeshStrategy.a aVar) {
        this.f3721b = appMeshStrategy;
        this.f3720a = aVar;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        AppMeshStrategy.a aVar = this.f3720a;
        if (aVar != null) {
            aVar.onFail(-1, exc.getMessage());
        }
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        if (this.f3720a == null) {
            return;
        }
        if (ioTResponse == null || ioTResponse.getCode() != 200) {
            this.f3720a.onFail(ioTResponse == null ? 0 : ioTResponse.getCode(), ioTResponse == null ? "" : ioTResponse.getLocalizedMsg());
        } else if (ioTResponse.getData() != null) {
            this.f3720a.onSuccess(ioTResponse.getData());
        } else {
            this.f3720a.onSuccess(null);
        }
    }
}
