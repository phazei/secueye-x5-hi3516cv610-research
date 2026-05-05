package com.aliyun.alink.business.devicecenter.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;

/* JADX INFO: loaded from: classes.dex */
public class CoAPProvisionOverNotifyHandler extends BaseCoAPNotifyHandler<IDeviceInfoNotifyListener, DeviceInfo> {
    public CoAPProvisionOverNotifyHandler(IDeviceInfoNotifyListener iDeviceInfoNotifyListener) {
        super(iDeviceInfoNotifyListener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.business.devicecenter.config.BaseCoAPNotifyHandler, com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler
    public void onRecRequest(AlcsCoAPContext alcsCoAPContext, AlcsCoAPRequest alcsCoAPRequest) {
        T t;
        super.onRecRequest(alcsCoAPContext, alcsCoAPRequest);
        CoapRequestPayload<K> coapRequestPayload = this.payload;
        if (coapRequestPayload == 0 || (t = coapRequestPayload.params) == 0) {
            ALog.w("CoAPDeviceInfoNotifyHan", "payload format error.");
            return;
        }
        DeviceInfo deviceInfo = (DeviceInfo) t;
        if (AlinkConstants.COAP_METHOD_NOTIFY_PROVISION_SUCCESS.equals(coapRequestPayload.method) || AlinkConstants.COAP_METHOD_DEVICE_INFO_NOTIFY.equals(this.payload.method)) {
            ALog.d("CoAPDeviceInfoNotifyHan", "provision success from " + this.payload.method + ". request=" + alcsCoAPRequest);
            T t2 = this.configCallback;
            if (t2 != 0) {
                ((IDeviceInfoNotifyListener) t2).onDeviceFound(deviceInfo);
            }
            ack(alcsCoAPRequest);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.BaseCoAPNotifyHandler
    public void parsePayload(AlcsCoAPRequest alcsCoAPRequest) {
        super.parsePayload(alcsCoAPRequest);
        this.payload = (CoapRequestPayload) JSONObject.parseObject(alcsCoAPRequest.getPayloadString(), new TypeReference<CoapRequestPayload<DeviceInfo>>() { // from class: com.aliyun.alink.business.devicecenter.config.CoAPProvisionOverNotifyHandler.1
        }.getType(), new Feature[0]);
    }
}
