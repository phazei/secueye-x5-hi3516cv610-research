package com.aliyun.alink.business.devicecenter.biz.model.request;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TriggerGatewayBatchProvisionRequest extends BaseApiRequest {
    public List<Object> discoveredDeviceList;
    public String gatewayDeviceName;
    public String gatewayIotId;
    public String gatewayProductKey;
    public String REQUEST_METHOD = "POST";
    public String API_HOST = "";
    public String API_PATH = "/living/awss/bt/mesh/gateway/provision/devices/trigger";
    public String API_VERSION = "1.0.0";

    public List<Object> getDiscoveredDeviceList() {
        return this.discoveredDeviceList;
    }

    public String getGatewayDeviceName() {
        return this.gatewayDeviceName;
    }

    public String getGatewayIotId() {
        return this.gatewayIotId;
    }

    public String getGatewayProductKey() {
        return this.gatewayProductKey;
    }

    public void setDiscoveredDeviceList(List<Object> list) {
        this.discoveredDeviceList = list;
    }

    public void setGatewayDeviceName(String str) {
        this.gatewayDeviceName = str;
    }

    public void setGatewayIotId(String str) {
        this.gatewayIotId = str;
    }

    public void setGatewayProductKey(String str) {
        this.gatewayProductKey = str;
    }
}
