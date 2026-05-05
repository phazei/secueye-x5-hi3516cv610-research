package com.aliyun.alink.business.devicecenter.biz.model.request;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes.dex */
public class MeshDeviceDiscoveryTriggerRequest extends BaseApiRequest {
    public String gatewayIotId;
    public boolean stopSearch;
    public String REQUEST_METHOD = "POST";
    public String API_HOST = "";
    public String API_PATH = AlinkConstants.HTTP_PATH_TRIGGER_MESH_DISCOVER;
    public String API_VERSION = "1.0.1";

    public String getGatewayIotId() {
        return this.gatewayIotId;
    }

    public boolean getStopSearch() {
        return this.stopSearch;
    }

    public void setGatewayIotId(String str) {
        this.gatewayIotId = str;
    }

    public void setStopSearch(boolean z) {
        this.stopSearch = z;
    }
}
