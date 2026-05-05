package datasource.channel.reqeust;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes3.dex */
public class GroupControlRequest extends BaseApiRequest {
    public String controlGroupId;
    public String deviceId;
    public String params;
    public String REQUEST_METHOD = "POST";
    public String MTOP_API_NAME = "mtop.alibaba.aicloud.iot.deviceControl";
    public String MTOP_VERSION = "1.0";
    public boolean MTOP_NEED_ECODE = false;
    public boolean MTOP_NEED_SESSION = false;
    public String API_HOST = "";
    public String API_PATH = "/living/home/controlgroup/bt/protocol/convert";
    public String API_VERSION = "1.0.0";

    public String getControlGroupId() {
        return this.controlGroupId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getParams() {
        return this.params;
    }

    public void setControlGroupId(String str) {
        this.controlGroupId = str;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setParams(String str) {
        this.params = str;
    }
}
