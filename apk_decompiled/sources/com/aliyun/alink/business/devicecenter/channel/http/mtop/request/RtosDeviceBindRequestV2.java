package com.aliyun.alink.business.devicecenter.channel.http.mtop.request;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes.dex */
public class RtosDeviceBindRequestV2 extends BaseApiRequest {
    public ModelV2 deviceBindRequest;
    public String API_NAME = "mtop.alibaba.ai.iot.bindDevice";
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String authInfo = null;

    public static class ModelV2 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f3522a = null;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f3523b = null;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public Param f3524c = null;

        public String getNetworkType() {
            return this.f3523b;
        }

        public Param getParams() {
            return this.f3524c;
        }

        public String getProductKey() {
            return this.f3522a;
        }

        public void setNetworkType(String str) {
            this.f3523b = str;
        }

        public void setParams(Param param) {
            this.f3524c = param;
        }

        public void setProductKey(String str) {
            this.f3522a = str;
        }
    }

    public static class Param {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f3525a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f3526b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public String f3527c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public String f3528d;
        public String e;

        public String getClientId() {
            return this.f3528d;
        }

        public String getCode() {
            return this.e;
        }

        public String getDeviceName() {
            return this.f3525a;
        }

        public String getProductKey() {
            return this.f3526b;
        }

        public String getToken() {
            return this.f3527c;
        }

        public void setClientId(String str) {
            this.f3528d = str;
        }

        public void setCode(String str) {
            this.e = str;
        }

        public void setDeviceName(String str) {
            this.f3525a = str;
        }

        public void setProductKey(String str) {
            this.f3526b = str;
        }

        public void setToken(String str) {
            this.f3527c = str;
        }
    }

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public ModelV2 getDeviceBindRequest() {
        return this.deviceBindRequest;
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

    public void setDeviceBindRequest(ModelV2 modelV2) {
        this.deviceBindRequest = modelV2;
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
