package com.aliyun.alink.business.devicecenter.provision.other;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.http.TransitoryClient;
import com.aliyun.alink.business.devicecenter.config.model.BackupCheckType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.other.zero.AlinkZeroConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.facebook.internal.NativeProtocol;
import java.util.EnumSet;

/* JADX INFO: compiled from: AlinkZeroConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class o implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ p f3744a;

    public o(p pVar) {
        this.f3744a = pVar;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        PerformanceLog.trace(AlinkZeroConfigStrategy.TAG, "reqEnrolleeResult", PerformanceLog.getJsonObject("result", "fail"));
        DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_REQUEST_ENROLLEE, String.valueOf(System.currentTimeMillis()));
        this.f3744a.f3745a.provisionErrorInfo = new DCErrorCode(NativeProtocol.ERROR_NETWORK_ERROR, DCErrorCode.PF_NETWORK_ERROR).setSubcode(DCErrorCode.SUBCODE_API_REQUEST_ON_FAILURE).setMsg("ZApiClientError:" + exc);
        this.f3744a.f3745a.provisionResultCallback(null);
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_REQUEST_ENROLLEE, String.valueOf(System.currentTimeMillis()));
        if (ioTResponse != null && ioTResponse.getCode() == 200) {
            PerformanceLog.trace(AlinkZeroConfigStrategy.TAG, "reqEnrolleeResult", PerformanceLog.getJsonObject("result", "success", "alinkid", TransitoryClient.getInstance().getTraceId(ioTResponse)));
            ALog.i(AlinkZeroConfigStrategy.TAG, "Zero requestEnrollee success.");
            this.f3744a.f3745a.updateBackupCheckTypeSet(EnumSet.of(BackupCheckType.CHECK_COAP_GET, BackupCheckType.CHECK_APP_TOKEN));
            this.f3744a.f3745a.startBackupCheck(true, 0L);
            if (this.f3744a.f3745a.provisionErrorInfo != null) {
                this.f3744a.f3745a.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL).setMsg("noConnectApOrCheckTokenSuccess");
                return;
            }
            return;
        }
        PerformanceLog.trace(AlinkZeroConfigStrategy.TAG, "reqEnrolleeResult", PerformanceLog.getJsonObject("result", "fail", "alinkid", TransitoryClient.getInstance().getTraceId(ioTResponse)));
        ALog.w(AlinkZeroConfigStrategy.TAG, "ZeroRequestEnrolleeFail request=" + TransitoryClient.getInstance().requestToStr(ioTRequest) + ",response=" + TransitoryClient.getInstance().responseToStr(ioTResponse));
        if (ioTResponse == null) {
            this.f3744a.f3745a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setSubcode(DCErrorCode.SUBCODE_SRE_RESPONSE_EMPTY).setMsg("getCipherError");
        } else {
            this.f3744a.f3745a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setSubcode(ioTResponse.getCode()).setMsg(ioTResponse.getLocalizedMsg());
        }
        this.f3744a.f3745a.provisionResultCallback(null);
    }
}
