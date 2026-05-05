package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.alcs.api.ICAConnectListener;
import com.aliyun.alink.linksdk.alcs.api.ICAMsgListener;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthServerParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAConnectParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.ica.ICASetupRequestPayload;
import com.aliyun.alink.linksdk.alcs.lpbs.utils.RandomUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.utils.TopicUtils;
import com.aliyun.alink.linksdk.alcs.pal.ica.ICAAlcsNative;

/* JADX INFO: compiled from: ICAProvisionerImpl.java */
/* JADX INFO: loaded from: classes2.dex */
public class e implements d {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private ICADiscoveryDeviceInfo f4049d;

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.d
    public void a(ICADiscoveryDeviceInfo iCADiscoveryDeviceInfo, ICAConnectListener iCAConnectListener) {
        this.f4049d = iCADiscoveryDeviceInfo;
        ICAAlcsNative.connectDevice(iCADiscoveryDeviceInfo.addr, iCADiscoveryDeviceInfo.port, new ICAConnectParams(new ICADeviceInfo(iCADiscoveryDeviceInfo.deviceInfo.productKey, iCADiscoveryDeviceInfo.deviceInfo.deviceName), iCADiscoveryDeviceInfo.pal, b.a("Xtau@iot", "Yx3DdsyetbSezlvc", "0")), iCAConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.d
    public void a() {
        ICAAlcsNative.disConnectDevice(this.f4049d.deviceInfo);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.b.d
    public boolean a(ICAAuthServerParams iCAAuthServerParams, ICAMsgListener iCAMsgListener) {
        ICAReqMessage iCAReqMessage = new ICAReqMessage();
        ICASetupRequestPayload iCASetupRequestPayload = new ICASetupRequestPayload();
        ICASetupRequestPayload.ICASetupData iCASetupData = new ICASetupRequestPayload.ICASetupData();
        ICASetupRequestPayload.ICAProvisionAuthData iCAProvisionAuthData = new ICASetupRequestPayload.ICAProvisionAuthData();
        iCAProvisionAuthData.authCode = iCAAuthServerParams.authCode;
        iCAProvisionAuthData.authSecret = iCAAuthServerParams.authSecret;
        iCAProvisionAuthData.productKey = this.f4049d.deviceInfo.productKey;
        iCAProvisionAuthData.deviceName = this.f4049d.deviceInfo.deviceName;
        iCASetupData.configValue.add(iCAProvisionAuthData);
        iCASetupData.configType = "ServerAuthInfo";
        iCASetupRequestPayload.params = iCASetupData;
        iCASetupRequestPayload.id = String.valueOf(RandomUtils.getNextInt());
        iCASetupRequestPayload.method = "core.service.setup";
        iCASetupRequestPayload.version = "1.0";
        String jSONString = JSONObject.toJSONString(iCASetupRequestPayload);
        iCAReqMessage.deviceInfo = this.f4049d.deviceInfo;
        iCAReqMessage.topic = TopicUtils.formatTopicByMethod(this.f4049d.deviceInfo.productKey, this.f4049d.deviceInfo.deviceName, TopicUtils.PATH_PRE_DEV, "core.service.setup");
        iCAReqMessage.payload = jSONString.getBytes();
        iCAReqMessage.type = 0;
        iCAReqMessage.code = 3;
        return ICAAlcsNative.sendRequest(iCAReqMessage, iCAMsgListener);
    }
}
