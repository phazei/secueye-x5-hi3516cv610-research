package com.aliyun.alink.business.devicecenter.biz.model.request.mtop;

import com.aliyun.alink.business.devicecenter.biz.model.request.TriggerGatewayBatchProvisionRequest;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes.dex */
public class TriggerGatewayBatchProvisionMTopRequest extends TriggerGatewayBatchProvisionRequest implements IMTOPDataObject {
    public String API_NAME = "mtop.alibaba.ai.IotAppService.triggerGatewayBatchProvision";
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
