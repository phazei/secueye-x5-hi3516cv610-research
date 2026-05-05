package com.aliyun.alink.business.devicecenter.biz.model;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.http.IRequest;
import java.io.Serializable;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes.dex */
public class GetBindTokenRequest implements IMTOPDataObject, IRequest, Serializable {
    public String bssid;
    public String homeId;
    public String API_NAME = AlinkConstants.HTTP_PATH_MTOP_TOKEN_REQUEST;
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

    public String getBssid() {
        return this.bssid;
    }

    public String getHomeId() {
        return this.homeId;
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

    public void setBssid(String str) {
        this.bssid = str;
    }

    public void setHomeId(String str) {
        this.homeId = str;
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
