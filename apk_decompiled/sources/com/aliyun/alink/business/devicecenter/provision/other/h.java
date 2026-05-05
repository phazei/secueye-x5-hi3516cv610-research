package com.aliyun.alink.business.devicecenter.provision.other;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProtocolVersion;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftAPConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;

/* JADX INFO: compiled from: SoftAPConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class h implements IAlcsCoAPReqHandler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ i f3736a;

    public h(i iVar) {
        this.f3736a = iVar;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler
    public void onReqComplete(AlcsCoAPContext alcsCoAPContext, int i, AlcsCoAPResponse alcsCoAPResponse) {
        ALog.d(SoftAPConfigStrategy.TAG, "onReqComplete() called with: coapContext = [" + alcsCoAPContext + "], flag = [" + i + "], response = [" + alcsCoAPResponse + "]");
        CoAPClient.getInstance().printResponse(alcsCoAPContext, alcsCoAPResponse);
        try {
            if (this.f3736a.f3737a.discoveryFuture != null && !this.f3736a.f3737a.discoveryFuture.isCancelled()) {
                if (this.f3736a.f3737a.mConfigParams == null || alcsCoAPResponse == null || TextUtils.isEmpty(alcsCoAPResponse.getPayloadString())) {
                    return;
                }
                try {
                    String str = SoftAPConfigStrategy.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("payload=");
                    sb.append(alcsCoAPResponse.getPayloadString());
                    ALog.llog((byte) 3, str, sb.toString());
                    JSONObject object = JSONObject.parseObject(alcsCoAPResponse.getPayloadString());
                    if (object == null) {
                        ALog.w(SoftAPConfigStrategy.TAG, "SAP_SEND_CONNECT_INFO invalid device, info empty.");
                        return;
                    }
                    this.f3736a.f3737a.recvSwitchAPAck.set(true);
                    this.f3736a.f3737a.recvSwitchAPAckTime.set(System.currentTimeMillis());
                    JSONObject jSONObject = object.getJSONObject("data");
                    if (jSONObject != null && !TextUtils.isEmpty(jSONObject.getString("productKey"))) {
                        DeviceInfo deviceInfoConvertLocalDevice = DeviceInfo.convertLocalDevice(jSONObject);
                        if (TextUtils.isEmpty(this.f3736a.f3737a.mConfigParams.productKey) && ProtocolVersion.NO_PRODUCT.getVersion().equals(this.f3736a.f3737a.mConfigParams.protocolVersion)) {
                            this.f3736a.f3737a.mConfigParams.productKey = deviceInfoConvertLocalDevice.productKey;
                        }
                        if (this.f3736a.f3737a.mConfigParams == null || !deviceInfoConvertLocalDevice.productKey.equals(this.f3736a.f3737a.mConfigParams.productKey)) {
                            ALog.w(SoftAPConfigStrategy.TAG, "SAP_SEND_CONNECT_INFO productKey not match.");
                            return;
                        }
                        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
                        String string = jSONObject.getString(AlinkConstants.KEY_TOKEN_TYPE);
                        if ("0".equals(string)) {
                            this.f3736a.f3737a.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
                            this.f3736a.f3737a.updateBackupCheckType(this.f3736a.f3737a.deviceReportTokenType);
                        } else if ("1".equals(string)) {
                            this.f3736a.f3737a.deviceReportTokenType = DeviceReportTokenType.CLOUD_TOKEN;
                            this.f3736a.f3737a.updateBackupCheckType(this.f3736a.f3737a.deviceReportTokenType);
                        } else if (this.f3736a.f3737a.sendAppToken2DeviceAB.get()) {
                            this.f3736a.f3737a.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
                            this.f3736a.f3737a.updateBackupCheckType(this.f3736a.f3737a.deviceReportTokenType);
                        } else {
                            this.f3736a.f3737a.deviceReportTokenType = DeviceReportTokenType.UNKNOWN;
                            this.f3736a.f3737a.updateBackupCheckType(this.f3736a.f3737a.deviceReportTokenType);
                        }
                        if (deviceInfoConvertLocalDevice != null) {
                            this.f3736a.f3737a.notifySupportProvisionService(deviceInfoConvertLocalDevice);
                        }
                        this.f3736a.f3737a.cancelRequest(this.f3736a.f3737a.getDeviceInfoRequest, this.f3736a.f3737a.deviceInfoCoapMessageId);
                        this.f3736a.f3737a.cancelTask();
                        this.f3736a.f3737a.isSendingConnectInfo.set(false);
                        this.f3736a.f3737a.mConfigParams.deviceName = deviceInfoConvertLocalDevice.deviceName;
                        String[] strArr = new String[2];
                        strArr[0] = AlinkConstants.KEY_DN;
                        strArr[1] = this.f3736a.f3737a.mConfigParams.deviceName;
                        DCUserTrack.addTrackData(strArr);
                        String[] strArr2 = new String[2];
                        strArr2[0] = AlinkConstants.KEY_END_TIME_SWITCH_AP;
                        strArr2[1] = String.valueOf(System.currentTimeMillis());
                        DCUserTrack.addTrackData(strArr2);
                        String str2 = SoftAPConfigStrategy.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("receive switchap ack, tokenType=");
                        sb2.append(string);
                        ALog.i(str2, sb2.toString());
                        PerformanceLog.trace(SoftAPConfigStrategy.TAG, "switchapAck");
                        if (this.f3736a.f3737a.isRecoveringWiFi.compareAndSet(false, true)) {
                            this.f3736a.f3737a.recoverWifiConnect("switchApAck", true);
                            return;
                        }
                        return;
                    }
                    ALog.w(SoftAPConfigStrategy.TAG, "SAP_SEND_CONNECT_INFO invalid device, data empty.");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    String str3 = SoftAPConfigStrategy.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("getCoapResponse FastJson parse generic object failed.");
                    sb3.append(e);
                    ALog.w(str3, sb3.toString());
                    return;
                }
            }
            ALog.i(SoftAPConfigStrategy.TAG, "SAP task finished or canceled, ignore.");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
