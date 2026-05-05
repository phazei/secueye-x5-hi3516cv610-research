package datasource.implemention.feiyan.request;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanUpdateDeviceVersionRequest extends BaseApiRequest {
    public String firmwareVersion;
    public String iotId;
    public String REQUEST_METHOD = "POST";
    public String API_HOST = "";
    public String authInfo = null;
    public String API_PATH = "/thing/ota/version/reportByUser";
    public String API_VERSION = "1.0.2";

    public FeiyanUpdateDeviceVersionRequest(String str, String str2) {
        this.iotId = null;
        this.firmwareVersion = null;
        this.iotId = str;
        this.firmwareVersion = str2;
    }

    public String getAPI_HOST() {
        return this.API_HOST;
    }

    public String getAPI_PATH() {
        return this.API_PATH;
    }

    public String getAPI_VERSION() {
        return this.API_VERSION;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public String getIotId() {
        return this.iotId;
    }

    public String getREQUEST_METHOD() {
        return this.REQUEST_METHOD;
    }

    public void setAPI_HOST(String str) {
        this.API_HOST = str;
    }

    public void setAPI_PATH(String str) {
        this.API_PATH = str;
    }

    public void setAPI_VERSION(String str) {
        this.API_VERSION = str;
    }

    public void setAuthInfo(String str) {
        this.authInfo = str;
    }

    public void setFirmwareVersion(String str) {
        this.firmwareVersion = str;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public void setREQUEST_METHOD(String str) {
        this.REQUEST_METHOD = str;
    }
}
