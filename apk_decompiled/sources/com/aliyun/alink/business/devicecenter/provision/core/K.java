package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentAppMeshStrategy;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;

/* JADX INFO: compiled from: ConcurrentAppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class K implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConcurrentAppMeshStrategy.a f3669a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ConcurrentAppMeshStrategy f3670b;

    public K(ConcurrentAppMeshStrategy concurrentAppMeshStrategy, ConcurrentAppMeshStrategy.a aVar) {
        this.f3670b = concurrentAppMeshStrategy;
        this.f3669a = aVar;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        ConcurrentAppMeshStrategy.a aVar = this.f3669a;
        if (aVar != null) {
            aVar.onFail(-1, exc.getMessage());
        }
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        if (this.f3669a == null) {
            return;
        }
        if (ioTResponse == null || ioTResponse.getCode() != 200) {
            this.f3669a.onFail(ioTResponse == null ? 0 : ioTResponse.getCode(), ioTResponse == null ? "" : ioTResponse.getLocalizedMsg());
        } else if (ioTResponse.getData() != null) {
            this.f3669a.onSuccess(ioTResponse.getData());
        } else {
            this.f3669a.onSuccess(null);
        }
    }
}
