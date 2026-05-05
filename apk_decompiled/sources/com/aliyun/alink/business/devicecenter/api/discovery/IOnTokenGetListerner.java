package com.aliyun.alink.business.devicecenter.api.discovery;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;

/* JADX INFO: loaded from: classes.dex */
public interface IOnTokenGetListerner {
    void onFail(DCErrorCode dCErrorCode);

    void onSuccess(GetTokenResult getTokenResult);
}
