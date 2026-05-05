package datasource.implemention.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IotDeleteDeviceRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.alibaba.aicloud.iot.deleteDevice";
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String unbindRequestJSONString = null;
    public String deviceInfo = null;
    public String authInfo = null;

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public String getDeviceInfo() {
        return this.deviceInfo;
    }

    public String getUnbindRequestJSONString() {
        return this.unbindRequestJSONString;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    public boolean isNEED_ECODE() {
        return this.NEED_ECODE;
    }

    public boolean isNEED_SESSION() {
        return this.NEED_SESSION;
    }

    public void setAPI_NAME(String str) {
        this.API_NAME = str;
    }

    public void setAuthInfo(String str) {
        this.authInfo = str;
    }

    public void setDeviceInfo(String str) {
        this.deviceInfo = str;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public void setUnbindRequestJSONString(String str) {
        this.unbindRequestJSONString = str;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }
}
