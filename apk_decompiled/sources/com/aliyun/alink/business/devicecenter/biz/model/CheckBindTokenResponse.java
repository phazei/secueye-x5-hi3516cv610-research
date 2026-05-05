package com.aliyun.alink.business.devicecenter.biz.model;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class CheckBindTokenResponse implements Serializable {
    public int bindResult;
    public String categoryKey;
    public int code;
    public String deviceName;
    public int insideResult;
    public String iotid;
    public String localizedMsg;
    public String pageRouterUrl;
    public String productKey;
    public String token;

    public int getBindResult() {
        return this.bindResult;
    }

    public String getCategoryKey() {
        return this.categoryKey;
    }

    public int getCode() {
        return this.code;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public int getInsideResult() {
        return this.insideResult;
    }

    public String getIotid() {
        return this.iotid;
    }

    public String getLocalizedMsg() {
        return this.localizedMsg;
    }

    public String getPageRouterUrl() {
        return this.pageRouterUrl;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public String getToken() {
        return this.token;
    }

    public void setBindResult(int i) {
        this.bindResult = i;
    }

    public void setCategoryKey(String str) {
        this.categoryKey = str;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public void setInsideResult(int i) {
        this.insideResult = i;
    }

    public void setIotid(String str) {
        this.iotid = str;
    }

    public void setLocalizedMsg(String str) {
        this.localizedMsg = str;
    }

    public void setPageRouterUrl(String str) {
        this.pageRouterUrl = str;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public String toString() {
        return "Model{productKey='" + this.productKey + "', deviceName='" + this.deviceName + "', token='" + this.token + "', iotid='" + this.iotid + "', bindResult=" + this.bindResult + ", code=" + this.code + ", localizedMsg='" + this.localizedMsg + "', categoryKey='" + this.categoryKey + "', pageRouterUrl='" + this.pageRouterUrl + "'}";
    }
}
