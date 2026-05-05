package com.aliyun.alink.business.devicecenter.api.discovery;

import com.aliyun.alink.business.devicecenter.api.add.DeviceBindResultInfo;

/* JADX INFO: loaded from: classes.dex */
public class GetTokenResult extends DeviceBindResultInfo {
    public String token = null;

    public GetTokenResult() {
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.DeviceBindResultInfo
    public String toString() {
        return "{productKey:" + this.productKey + "deviceName:" + this.deviceName + "token:" + this.token + "bindResult:" + this.bindResult + "iotId:" + this.iotId + "pageRouterUrl:" + this.pageRouterUrl + "categoryKey:" + this.categoryKey + "errorCode:" + this.errorCode + "subErrorCode:" + this.subErrorCode + "localizedMsg:" + this.localizedMsg + "}";
    }

    public GetTokenResult(DeviceBindResultInfo deviceBindResultInfo) {
        this.productKey = deviceBindResultInfo.productKey;
        this.productId = deviceBindResultInfo.productId;
        this.deviceName = deviceBindResultInfo.deviceName;
        this.bindResult = deviceBindResultInfo.bindResult;
        this.iotId = deviceBindResultInfo.iotId;
        this.pageRouterUrl = deviceBindResultInfo.pageRouterUrl;
        this.categoryKey = deviceBindResultInfo.categoryKey;
        this.errorCode = deviceBindResultInfo.errorCode;
        this.subErrorCode = deviceBindResultInfo.subErrorCode;
        this.localizedMsg = deviceBindResultInfo.localizedMsg;
    }
}
