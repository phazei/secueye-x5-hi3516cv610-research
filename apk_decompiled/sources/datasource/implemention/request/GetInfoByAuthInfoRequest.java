package datasource.implemention.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class GetInfoByAuthInfoRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.alibaba.aicloud.common.getInfoByAuthInfo";
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String args = null;
    public String func = null;
    public String module = null;
    public String authInfo = null;

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getArgs() {
        return this.args;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public String getFunc() {
        return this.func;
    }

    public String getModule() {
        return this.module;
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

    public void setArgs(String str) {
        this.args = str;
    }

    public void setAuthInfo(String str) {
        this.authInfo = str;
    }

    public void setFunc(String str) {
        this.func = str;
    }

    public void setModule(String str) {
        this.module = str;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }
}
