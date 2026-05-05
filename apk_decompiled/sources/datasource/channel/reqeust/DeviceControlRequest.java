package datasource.channel.reqeust;

import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ILpbsCloudProxy;
import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceControlRequest extends BaseApiRequest {
    public String REQUEST_METHOD = "POST";
    public String MTOP_API_NAME = "mtop.alibaba.aicloud.iot.deviceControl";
    public String MTOP_VERSION = "1.0";
    public boolean MTOP_NEED_ECODE = false;
    public boolean MTOP_NEED_SESSION = false;
    public String API_HOST = "";
    public String API_PATH = ILpbsCloudProxy.TOPIC_CONVERT_TGMESH_PARAMS;
    public String API_VERSION = "1.0.0";
    public String deviceId = null;
    public String params = null;

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getParams() {
        return this.params;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setParams(String str) {
        this.params = str;
    }
}
