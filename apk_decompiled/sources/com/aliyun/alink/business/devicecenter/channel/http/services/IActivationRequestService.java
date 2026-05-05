package com.aliyun.alink.business.devicecenter.channel.http.services;

import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.channel.http.RequestServiceMgr;
import com.aliyun.alink.business.devicecenter.channel.http.model.request.QrCodeActivateRequest;

/* JADX INFO: loaded from: classes.dex */
public interface IActivationRequestService extends IRequestService {
    public static final String SERVICE_NAME = "activationRequestService";

    static void registerRequestService(IActivationRequestService iActivationRequestService) {
        RequestServiceMgr.getInstance().registerRequestService("activationRequestService", iActivationRequestService);
    }

    void qrCodeActivate(QrCodeActivateRequest qrCodeActivateRequest, IRequestCallback<Object> iRequestCallback);
}
