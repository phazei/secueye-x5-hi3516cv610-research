package com.aliyun.alink.business.devicecenter.channel.http.mtop;

import com.alibaba.ailabs.tg.mtop.OnResponseListener;
import com.alibaba.ailabs.tg.network.mtop.inner.MtopHelper;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.http.DCError;
import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.channel.http.model.request.QrCodeActivateRequest;
import com.aliyun.alink.business.devicecenter.channel.http.mtop.request.DeviceBindRequest;
import com.aliyun.alink.business.devicecenter.channel.http.mtop.response.DeviceBindResp;
import com.aliyun.alink.business.devicecenter.channel.http.services.IActivationRequestService;
import java.util.Map;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes.dex */
public class DefaultMtopActivationRequestService implements IActivationRequestService {
    @Override // com.aliyun.alink.business.devicecenter.channel.http.services.IActivationRequestService
    public void qrCodeActivate(QrCodeActivateRequest qrCodeActivateRequest, final IRequestCallback<Object> iRequestCallback) {
        Map<String, Object> extraInfo = qrCodeActivateRequest.getExtraInfo();
        if (!extraInfo.containsKey("authInfo") || !extraInfo.containsKey(AlinkConstants.KEY_DEVICE_INFO)) {
            iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_PARAMS_ERROR), "authInfo or deviceInfo not exist"), null);
            return;
        }
        DeviceBindRequest deviceBindRequest = new DeviceBindRequest();
        deviceBindRequest.setAuthInfo((String) extraInfo.get("authInfo"));
        deviceBindRequest.setDeviceInfo((String) extraInfo.get(AlinkConstants.KEY_DEVICE_INFO));
        MtopHelper.getInstance().asyncRequestApi(deviceBindRequest, DeviceBindResp.class, new OnResponseListener() { // from class: com.aliyun.alink.business.devicecenter.channel.http.mtop.DefaultMtopActivationRequestService.1
            public void onResponseFailed(int i, String str, String str2) {
                iRequestCallback.onFail(new DCError(str, str2), null);
            }

            public void onResponseSuccess(BaseOutDo baseOutDo, int i) {
                iRequestCallback.onSuccess(baseOutDo);
            }
        }, 0);
    }
}
