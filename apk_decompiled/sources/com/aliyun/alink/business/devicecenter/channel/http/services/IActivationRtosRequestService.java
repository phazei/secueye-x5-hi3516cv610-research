package com.aliyun.alink.business.devicecenter.channel.http.services;

import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.channel.http.RequestServiceMgr;
import com.aliyun.alink.business.devicecenter.channel.http.model.request.QrCodeActivateRequest;
import com.aliyun.alink.business.devicecenter.channel.http.mtop.data.BindIotDeviceResult;

/* JADX INFO: loaded from: classes.dex */
public interface IActivationRtosRequestService extends IRequestService {
    public static final String SERVICE_NAME = "rtosBindRequestService";

    static void registerRequestService(IActivationRtosRequestService iActivationRtosRequestService) {
        RequestServiceMgr.getInstance().registerRequestService("rtosBindRequestService", iActivationRtosRequestService);
    }

    void qrCodeActivate(QrCodeActivateRequest qrCodeActivateRequest, IRequestCallback<BindIotDeviceResult> iRequestCallback);
}
