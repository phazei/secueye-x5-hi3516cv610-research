package datasource.implemention.feiyan.request;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanAuthCipherCheckThenGetKeyForBLEDeviceRequest extends BaseApiRequest {
    public String deviceId;
    public String digest;
    public String productId;
    public String REQUEST_METHOD = "POST";
    public String MTOP_API_NAME = "mtop.alibaba.aicloud.deviceTripleAuth.cipherCheckThenGetKey";
    public String MTOP_VERSION = "1.0";
    public boolean MTOP_NEED_ECODE = false;
    public boolean MTOP_NEED_SESSION = false;
    public String API_HOST = "";
    public String API_PATH = "/living/awss/ble/cipherandkey/get";
    public String API_VERSION = "1.0.0";

    public FeiyanAuthCipherCheckThenGetKeyForBLEDeviceRequest(String str, String str2, String str3) {
        this.productId = str;
        this.deviceId = str2;
        this.digest = str3;
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

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getDigest() {
        return this.digest;
    }

    public String getMTOP_API_NAME() {
        return this.MTOP_API_NAME;
    }

    public String getMTOP_VERSION() {
        return this.MTOP_VERSION;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getREQUEST_METHOD() {
        return this.REQUEST_METHOD;
    }

    public boolean isMTOP_NEED_ECODE() {
        return this.MTOP_NEED_ECODE;
    }

    public boolean isMTOP_NEED_SESSION() {
        return this.MTOP_NEED_SESSION;
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

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setDigest(String str) {
        this.digest = str;
    }

    public void setMTOP_API_NAME(String str) {
        this.MTOP_API_NAME = str;
    }

    public void setMTOP_NEED_ECODE(boolean z) {
        this.MTOP_NEED_ECODE = z;
    }

    public void setMTOP_NEED_SESSION(boolean z) {
        this.MTOP_NEED_SESSION = z;
    }

    public void setMTOP_VERSION(String str) {
        this.MTOP_VERSION = str;
    }

    public void setProductId(String str) {
        this.productId = str;
    }

    public void setREQUEST_METHOD(String str) {
        this.REQUEST_METHOD = str;
    }
}
