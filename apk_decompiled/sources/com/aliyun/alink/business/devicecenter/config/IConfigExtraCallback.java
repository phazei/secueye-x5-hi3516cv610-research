package com.aliyun.alink.business.devicecenter.config;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;

/* JADX INFO: loaded from: classes.dex */
public interface IConfigExtraCallback {
    void onError(DCErrorCode dCErrorCode);

    void onSuccess();
}
