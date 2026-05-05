package com.aliyun.alink.business.devicecenter.channel.http.services;

import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.channel.http.RequestServiceMgr;
import com.aliyun.alink.business.devicecenter.channel.http.model.request.QrCodeActivateRequest;

/* JADX INFO: loaded from: classes.dex */
public interface IStaticBindRequestService extends IRequestService {
    public static final String SERVICE_NAME = "staticBindRequestService";

    static void registerRequestService(IStaticBindRequestService iStaticBindRequestService) {
        RequestServiceMgr.getInstance().registerRequestService("staticBindRequestService", iStaticBindRequestService);
    }

    void qrCodeStaticBind(QrCodeActivateRequest qrCodeActivateRequest, IRequestCallback<Object> iRequestCallback);
}
