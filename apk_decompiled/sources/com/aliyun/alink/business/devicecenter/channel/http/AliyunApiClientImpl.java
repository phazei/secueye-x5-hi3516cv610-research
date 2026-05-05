package com.aliyun.alink.business.devicecenter.channel.http;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.ContextHolder;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;

/* JADX INFO: loaded from: classes.dex */
public class AliyunApiClientImpl implements IApiClient {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IoTAPIClient f3468a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IoTCallback f3469b = null;

    public AliyunApiClientImpl() {
        this.f3468a = null;
        this.f3468a = new IoTAPIClientFactory().getClient();
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.http.IApiClient
    public void send(IRequest iRequest, Class<?> cls, final IRequestCallback iRequestCallback) {
        try {
            if (iRequest instanceof IoTRequest) {
                this.f3469b = new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.channel.http.AliyunApiClientImpl.1
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                        ALog.w("IAliyunApiClientImpl", "onFailure() called with: ioTRequest = [" + TransitoryClient.getInstance().requestToStr(ioTRequest) + "], e = [" + exc + "]");
                        if (iRequestCallback != null) {
                            if (NetworkTypeUtils.isNetworkAvailable(ContextHolder.getInstance().getAppContext())) {
                                iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_NETWORK_ERROR), "aliyun api client fail, network unavailable", exc), null);
                            } else {
                                iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_UNKNOWN_ERROR), "aliyun api client fail", exc), null);
                            }
                        }
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        ALog.d("IAliyunApiClientImpl", "onResponse() called with: ioTRequest = [" + TransitoryClient.getInstance().requestToStr(ioTRequest) + "], ioTResponse = [" + TransitoryClient.getInstance().responseToStr(ioTResponse) + "]");
                        if (iRequestCallback != null) {
                            if (ioTResponse != null && ioTResponse.getCode() == 200) {
                                iRequestCallback.onSuccess(ioTResponse);
                            } else {
                                iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_SERVER_FAIL), "aliyun api client onresponse"), ioTResponse);
                            }
                        }
                    }
                };
                this.f3468a.send((IoTRequest) iRequest, this.f3469b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (iRequestCallback != null) {
                iRequestCallback.onFail(new DCError(String.valueOf(DCErrorCode.PF_UNKNOWN_ERROR), "aliyun api client exception", e), null);
            }
        }
    }
}
