package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.http.TransitoryClient;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.o, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AlinkBroadcastConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0481o implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ RunnableC0482p f3710a;

    public C0481o(RunnableC0482p runnableC0482p) {
        this.f3710a = runnableC0482p;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        ALog.w(AlinkBroadcastConfigStrategy.TAG, "startConfig getCipher onFailure e=" + exc);
        this.f3710a.f3712b.provisionErrorInfo = new DCErrorCode(NativeProtocol.ERROR_NETWORK_ERROR, DCErrorCode.PF_NETWORK_ERROR).setSubcode(DCErrorCode.SUBCODE_API_REQUEST_ON_FAILURE).setMsg("getCipherError:" + exc);
        this.f3710a.f3712b.provisionResultCallback(null);
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        if (ioTResponse == null || ioTResponse.getCode() != 200) {
            ALog.w(AlinkBroadcastConfigStrategy.TAG, "startConfig getCipher BC onResponse data null. request=" + TransitoryClient.getInstance().requestToStr(ioTRequest) + ",response=" + TransitoryClient.getInstance().responseToStr(ioTResponse));
            if (ioTResponse == null) {
                this.f3710a.f3712b.provisionErrorInfo = new DCErrorCode(NativeProtocol.ERROR_NETWORK_ERROR, DCErrorCode.PF_SERVER_FAIL).setSubcode(DCErrorCode.SUBCODE_SRE_RESPONSE_EMPTY).setMsg("getCipherError");
            } else {
                this.f3710a.f3712b.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setSubcode(ioTResponse.getCode()).setMsg(ioTResponse.getLocalizedMsg());
            }
            this.f3710a.f3712b.provisionResultCallback(null);
            return;
        }
        this.f3710a.f3712b.securityAesKey = String.valueOf(ioTResponse.getData());
        if (!TextUtils.isEmpty(this.f3710a.f3712b.securityAesKey)) {
            this.f3710a.f3712b.mConfigParams.productEncryptKey = this.f3710a.f3712b.securityAesKey;
            RunnableC0482p runnableC0482p = this.f3710a;
            runnableC0482p.f3712b.provisioning(runnableC0482p.f3711a);
            return;
        }
        ALog.w(AlinkBroadcastConfigStrategy.TAG, "startConfig getCipher BC onResponse securityAesKey fail. request=" + TransitoryClient.getInstance().requestToStr(ioTRequest) + ",response=" + TransitoryClient.getInstance().responseToStr(ioTResponse));
        this.f3710a.f3712b.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setSubcode(DCErrorCode.SUBCODE_SRE_KEY_EMPTY).setMsg("getCipherBAesNull");
        this.f3710a.f3712b.provisionResultCallback(null);
    }
}
