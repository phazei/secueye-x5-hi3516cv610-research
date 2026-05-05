package com.aliyun.alink.apiclient;

import com.aliyun.alink.apiclient.constants.Constants;
import com.aliyun.alink.apiclient.utils.ParameterHelper;

/* JADX INFO: loaded from: classes.dex */
public class CommonPoPRequest extends CommonRequest {
    private String format = "JSON";
    private String regionId = Constants.IOT_REGION_ID_DEFAULT;
    private String signatureMethod = "HMAC-SHA1";
    private String signatureNonce = ParameterHelper.getUniqueNonce();
    private String signatureVersion = "1.0";
    private String timestamp = ParameterHelper.getISO8601Time(null);
    private String version = Constants.IOT_VERSION_DEFAULT;

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String str) {
        this.format = str;
    }

    public String getRegionId() {
        return this.regionId;
    }

    public void setRegionId(String str) {
        this.regionId = str;
    }

    public String getSignatureMethod() {
        return this.signatureMethod;
    }

    public void setSignatureMethod(String str) {
        this.signatureMethod = str;
    }

    public String getSignatureNonce() {
        return this.signatureNonce;
    }

    public void setSignatureNonce(String str) {
        this.signatureNonce = str;
    }

    public String getSignatureVersion() {
        return this.signatureVersion;
    }

    public void setSignatureVersion(String str) {
        this.signatureVersion = str;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String str) {
        this.timestamp = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }
}
