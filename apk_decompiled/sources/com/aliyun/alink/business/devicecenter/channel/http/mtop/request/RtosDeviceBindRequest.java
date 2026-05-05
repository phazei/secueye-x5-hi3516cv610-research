package com.aliyun.alink.business.devicecenter.channel.http.mtop.request;

import com.aliyun.alink.business.devicecenter.channel.http.IRequest;
import java.io.Serializable;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes.dex */
public class RtosDeviceBindRequest implements IMTOPDataObject, IRequest, Serializable {
    public Model deviceBindRequest;
    public String API_NAME = "mtop.alibaba.ai.iot.bindDevice";
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String authInfo = null;

    public static class Model implements Serializable {
        public String productKey = null;
        public String networkType = null;
        public Param params = null;

        public String getNetworkType() {
            return this.networkType;
        }

        public Param getParams() {
            return this.params;
        }

        public String getProductKey() {
            return this.productKey;
        }

        public void setNetworkType(String str) {
            this.networkType = str;
        }

        public void setParams(Param param) {
            this.params = param;
        }

        public void setProductKey(String str) {
            this.productKey = str;
        }
    }

    public static class Param implements Serializable {
        public String clientId;
        public String code;
        public String deviceName;
        public String productKey;
        public String token;

        public String getClientId() {
            return this.clientId;
        }

        public String getCode() {
            return this.code;
        }

        public String getDeviceName() {
            return this.deviceName;
        }

        public String getProductKey() {
            return this.productKey;
        }

        public String getToken() {
            return this.token;
        }

        public void setClientId(String str) {
            this.clientId = str;
        }

        public void setCode(String str) {
            this.code = str;
        }

        public void setDeviceName(String str) {
            this.deviceName = str;
        }

        public void setProductKey(String str) {
            this.productKey = str;
        }

        public void setToken(String str) {
            this.token = str;
        }
    }

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public Model getDeviceBindRequest() {
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

    public void setDeviceBindRequest(Model model) {
        this.deviceBindRequest = model;
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
