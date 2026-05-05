package com.aliyun.alink.business.devicecenter.biz.model;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.http.IRequest;
import java.io.Serializable;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes.dex */
public class CheckBindTokenRequest implements IMTOPDataObject, IRequest, Serializable {
    public String deviceName;
    public String productKey;
    public String token;
    public String API_NAME = AlinkConstants.HTTP_PATH_MTOP_CHECK_BIND_TOKEN;
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String authInfo = "";

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
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

    public void setDeviceName(String str) {
        this.deviceName = str;
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

    public void setToken(String str) {
        this.token = str;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }
}
