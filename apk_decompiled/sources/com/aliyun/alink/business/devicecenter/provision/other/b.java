package com.aliyun.alink.business.devicecenter.provision.other;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.channel.coap.response.CoapResponsePayload;
import com.aliyun.alink.business.devicecenter.diagnose.SoftApDiagnose;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftAPConfigStrategy;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;

/* JADX INFO: compiled from: SoftAPConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements IAlcsCoAPReqHandler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SoftAPConfigStrategy f3730a;

    public b(SoftAPConfigStrategy softAPConfigStrategy) {
        this.f3730a = softAPConfigStrategy;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler
    public void onReqComplete(AlcsCoAPContext alcsCoAPContext, int i, AlcsCoAPResponse alcsCoAPResponse) {
        CoAPClient.getInstance().printResponse(alcsCoAPContext, alcsCoAPResponse);
        if (alcsCoAPResponse == null || TextUtils.isEmpty(alcsCoAPResponse.getPayloadString())) {
            return;
        }
        ALog.llog((byte) 3, SoftAPConfigStrategy.TAG, "getDeviceStatus responseString=" + alcsCoAPResponse.getPayloadString());
        try {
            CoapResponsePayload coapResponsePayload = (CoapResponsePayload) JSONObject.parseObject(alcsCoAPResponse.getPayloadString(), new a(this).getType(), new Feature[0]);
            if (coapResponsePayload == null || coapResponsePayload.data == 0) {
                return;
            }
            SoftApDiagnose.getInstance().stopDiagnose();
            SoftApDiagnose.getInstance().startDiagnose(((DeviceInfo) coapResponsePayload.data).productKey, ((DeviceInfo) coapResponsePayload.data).deviceName, 30);
            this.f3730a.notifySupportProvisionService((DeviceInfo) coapResponsePayload.data);
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(SoftAPConfigStrategy.TAG, "getDeviceErrorCode device.errcode.get parsePayloadException= " + e);
        }
    }
}
