package com.aliyun.alink.business.devicecenter.provision.other;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.http.TransitoryClient;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.model.CheckTokenModel;
import com.aliyun.alink.business.devicecenter.provision.other.zero.BatchZeroConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.other.r, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BatchZeroConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0493r implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ s f3747a;

    public C0493r(s sVar) {
        this.f3747a = sVar;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        PerformanceLog.trace(BatchZeroConfigStrategy.TAG, "reqEnrolleeResult", PerformanceLog.getJsonObject("result", "fail"));
        DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_REQUEST_ENROLLEE, String.valueOf(System.currentTimeMillis()));
        this.f3747a.f3748a.provisionErrorInfo = new DCErrorCode(NativeProtocol.ERROR_NETWORK_ERROR, DCErrorCode.PF_NETWORK_ERROR).setSubcode(DCErrorCode.SUBCODE_API_REQUEST_ON_FAILURE).setMsg("BZApiClientError:" + exc);
        this.f3747a.f3748a.provisionResultCallback(null);
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_REQUEST_ENROLLEE, String.valueOf(System.currentTimeMillis()));
        if (!this.f3747a.f3748a.waitForResult.get()) {
            ALog.d(BatchZeroConfigStrategy.TAG, "request enrollee bz onresponse waitForResult=false, return.");
            return;
        }
        if (ioTResponse != null && ioTResponse.getCode() == 200) {
            PerformanceLog.trace(BatchZeroConfigStrategy.TAG, "reqEnrolleeResult", PerformanceLog.getJsonObject("result", "success", "alinkid", TransitoryClient.getInstance().getTraceId(ioTResponse)));
            ALog.i(BatchZeroConfigStrategy.TAG, "BZero requestEnrollee success.");
            if (this.f3747a.f3748a.mConfigParams != null) {
                BatchZeroConfigStrategy batchZeroConfigStrategy = this.f3747a.f3748a;
                batchZeroConfigStrategy.startBackupCheck(true, 5L, CheckTokenModel.getCheckModelList(batchZeroConfigStrategy.batchEnrolleeDeviceList, this.f3747a.f3748a.mConfigParams.bindToken));
            }
            if (this.f3747a.f3748a.provisionErrorInfo != null) {
                this.f3747a.f3748a.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL).setMsg("noConnectApOrCheckTokenSuccess");
                return;
            }
            return;
        }
        PerformanceLog.trace(BatchZeroConfigStrategy.TAG, "reqEnrolleeResult", PerformanceLog.getJsonObject("result", "fail", "alinkid", TransitoryClient.getInstance().getTraceId(ioTResponse)));
        ALog.w(BatchZeroConfigStrategy.TAG, "BZeroRequestEnrolleeFail request=" + TransitoryClient.getInstance().requestToStr(ioTRequest) + ",response=" + TransitoryClient.getInstance().responseToStr(ioTResponse));
        if (ioTResponse == null) {
            this.f3747a.f3748a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setSubcode(DCErrorCode.SUBCODE_SRE_RESPONSE_EMPTY).setMsg("getCipherError");
        } else {
            this.f3747a.f3748a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setSubcode(ioTResponse.getCode()).setMsg(ioTResponse.getLocalizedMsg());
        }
        this.f3747a.f3748a.provisionResultCallback(null);
    }
}
