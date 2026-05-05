package com.aliyun.iotx.linkvisual.media.video.utils.mtopapi;

import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes2.dex */
public class MtopAlibabaAicloudIotAppAliyunPassthroughRequest implements IMTOPDataObject {
    private String API_NAME = "mtop.alibaba.aicloud.iotApp.aliyunPassthrough";
    private String VERSION = "1.0";
    private boolean NEED_ECODE = false;
    private boolean NEED_SESSION = false;
    private String aliyunPassthroughParam = null;
    private String authInfo = null;

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAliyunPassthroughParam() {
        return this.aliyunPassthroughParam;
    }

    public String getAuthInfo() {
        return this.authInfo;
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

    public void setAliyunPassthroughParam(String str) {
        this.aliyunPassthroughParam = str;
    }

    public void setAuthInfo(String str) {
        this.authInfo = str;
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
