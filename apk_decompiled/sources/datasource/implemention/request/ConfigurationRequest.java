package datasource.implemention.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class ConfigurationRequest implements IMTOPDataObject {
    public String authInfo;
    public String macAddress;
    public String productKey;
    public String unicastAddresses;
    public boolean NEED_SESSION = true;
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public String API_NAME = "mtop.alibaba.aicloud.iot.configuration";

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public boolean getNEED_ECODE() {
        return this.NEED_ECODE;
    }

    public boolean getNEED_SESSION() {
        return this.NEED_SESSION;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public String getUnicastAddresses() {
        return this.unicastAddresses;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    public void setAPI_NAME(String str) {
        this.API_NAME = str;
    }

    public void setAuthInfo(String str) {
        this.authInfo = str;
    }

    public void setMacAddress(String str) {
        this.macAddress = str;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setUnicastAddresses(String str) {
        this.unicastAddresses = str;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }
}
