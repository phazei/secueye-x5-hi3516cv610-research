package com.aliyun.alink.business.devicecenter.discover;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.config.BaseCoAPNotifyHandler;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;

/* JADX INFO: loaded from: classes.dex */
public class CoAPDeviceInfoNotifyHandler extends BaseCoAPNotifyHandler<IDeviceInfoNotifyListener, DeviceInfo> {
    public CoAPDeviceInfoNotifyHandler(IDeviceInfoNotifyListener iDeviceInfoNotifyListener) {
        super(iDeviceInfoNotifyListener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void a(AlcsCoAPRequest alcsCoAPRequest) {
        T t;
        ALog.d("CoAPDeviceInfoNotifyHan", "handleDeviceInfoNotify request=" + alcsCoAPRequest);
        CoapRequestPayload<K> coapRequestPayload = this.payload;
        if (coapRequestPayload != 0 && (t = coapRequestPayload.params) != 0) {
            DeviceInfo deviceInfo = (DeviceInfo) t;
            deviceInfo.id = AlinkHelper.getHalfMacFromMac(deviceInfo.mac);
            ((IDeviceInfoNotifyListener) this.configCallback).onDeviceFound(deviceInfo);
        }
        ack(alcsCoAPRequest);
    }

    @Override // com.aliyun.alink.business.devicecenter.config.BaseCoAPNotifyHandler, com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler
    public void onRecRequest(AlcsCoAPContext alcsCoAPContext, AlcsCoAPRequest alcsCoAPRequest) {
        super.onRecRequest(alcsCoAPContext, alcsCoAPRequest);
        CoapRequestPayload<K> coapRequestPayload = this.payload;
        if (coapRequestPayload == 0 || !AlinkConstants.COAP_METHOD_DEVICE_INFO_NOTIFY.equals(coapRequestPayload.method)) {
            return;
        }
        a(alcsCoAPRequest);
    }

    @Override // com.aliyun.alink.business.devicecenter.config.BaseCoAPNotifyHandler
    public void parsePayload(AlcsCoAPRequest alcsCoAPRequest) {
        super.parsePayload(alcsCoAPRequest);
        this.payload = (CoapRequestPayload) JSONObject.parseObject(alcsCoAPRequest.getPayloadString(), new TypeReference<CoapRequestPayload<DeviceInfo>>() { // from class: com.aliyun.alink.business.devicecenter.discover.CoAPDeviceInfoNotifyHandler.1
        }.getType(), new Feature[0]);
    }
}
