package datasource.implemention.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class AuthCheckAndGetBleKeyForBLEDeviceRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.alibaba.aicloud.deviceTripleAuth.cipherCheckThenGetKey";
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String authInfo = null;
    public String productId = null;
    public String deviceId = null;
    public String digest = null;
    public String deviceType = "iot";

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getDigest() {
        return this.digest;
    }

    public String getProductId() {
        return this.productId;
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

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setDeviceType(String str) {
        this.deviceType = str;
    }

    public void setDigest(String str) {
        this.digest = str;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public void setProductId(String str) {
        this.productId = str;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }
}
