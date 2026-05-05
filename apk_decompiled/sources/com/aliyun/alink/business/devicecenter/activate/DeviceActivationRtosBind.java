package com.aliyun.alink.business.devicecenter.activate;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.http.DCError;
import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.channel.http.RequestServiceMgr;
import com.aliyun.alink.business.devicecenter.channel.http.model.request.QrCodeActivateRequest;
import com.aliyun.alink.business.devicecenter.channel.http.mtop.data.BindIotDeviceResult;
import com.aliyun.alink.business.devicecenter.channel.http.services.IActivationRtosRequestService;
import com.aliyun.alink.business.devicecenter.log.ALog;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DeviceActivationRtosBind implements IDeviceActivationRtosBind {

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final DeviceActivationRtosBind f3242a = new DeviceActivationRtosBind();
    }

    public static DeviceActivationRtosBind getInstance() {
        return SingletonHolder.f3242a;
    }

    @Override // com.aliyun.alink.business.devicecenter.activate.IDeviceActivationRtosBind
    public void activateDevice(Map<String, Object> map, final IActivateRtosDeviceCallback iActivateRtosDeviceCallback) {
        ALog.i("DeviceActivationRtosBind", "activateInfo: " + map);
        if (iActivateRtosDeviceCallback == null) {
            ALog.w("DeviceActivationRtosBind", "callback is null");
            throw new IllegalArgumentException("callback is null");
        }
        IActivationRtosRequestService iActivationRtosRequestService = (IActivationRtosRequestService) RequestServiceMgr.getInstance().getRequestService("rtosBindRequestService");
        if (iActivationRtosRequestService == null) {
            ALog.w("DeviceActivationRtosBind", "activationRequestService not exist");
            throw new IllegalStateException("need register activationRequestService first");
        }
        QrCodeActivateRequest qrCodeActivateRequestBuild = QrCodeActivateRequest.builder().build();
        qrCodeActivateRequestBuild.setExtraInfo(map);
        iActivationRtosRequestService.qrCodeActivate(qrCodeActivateRequestBuild, new IRequestCallback<BindIotDeviceResult>() { // from class: com.aliyun.alink.business.devicecenter.activate.DeviceActivationRtosBind.1
            @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
            public void onFail(DCError dCError, BindIotDeviceResult bindIotDeviceResult) {
                ALog.w("DeviceActivationRtosBind", "dcError: " + dCError + " response: " + bindIotDeviceResult);
                iActivateRtosDeviceCallback.onFailed(new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, 101623, DeviceActivationRtosBind.this.a(dCError)));
            }

            @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
            public void onSuccess(BindIotDeviceResult bindIotDeviceResult) {
                ALog.i("DeviceActivationRtosBind", "response: " + bindIotDeviceResult);
                iActivateRtosDeviceCallback.onSuccess(bindIotDeviceResult);
            }
        });
    }

    @Override // com.aliyun.alink.business.devicecenter.activate.IDeviceActivationRtosBind
    public void init(IActivationRtosRequestService iActivationRtosRequestService) {
        RequestServiceMgr.getInstance().registerRequestService("rtosBindRequestService", iActivationRtosRequestService);
    }

    public final String a(DCError dCError) {
        return "DCError: {code: " + dCError.code + ", subCode: " + dCError.subCode + ", message: " + dCError.message + "}";
    }
}
