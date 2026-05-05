package com.aliyun.alink.business.devicecenter.biz.model.request;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes.dex */
public class QueryProductKeyRequest extends BaseApiRequest {
    public String productId;
    public String REQUEST_METHOD = "POST";
    public String API_HOST = "";
    public String API_PATH = AlinkConstants.PROVISION_DEVICE_PIDTOPK;
    public String API_VERSION = AlinkConstants.PROVISION_DEVICE_PIDTOPK_VERSION;

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String str) {
        this.productId = str;
    }
}
