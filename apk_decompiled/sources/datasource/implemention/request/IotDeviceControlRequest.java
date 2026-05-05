package datasource.implemention.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IotDeviceControlRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.alibaba.aicloud.iot.deviceControl";
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String devId = null;
    public String skillId = null;
    public String params = null;
    public String authInfo = null;

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public String getDevId() {
        return this.devId;
    }

    public String getParams() {
        return this.params;
    }

    public String getSkillId() {
        return this.skillId;
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

    public void setDevId(String str) {
        this.devId = str;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public void setParams(String str) {
        this.params = str;
    }

    public void setSkillId(String str) {
        this.skillId = str;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }
}
