package com.aliyun.alink.business.devicecenter.activate;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.http.mtop.data.BindIotDeviceResult;

/* JADX INFO: loaded from: classes.dex */
public interface IActivateRtosDeviceCallback {
    void onFailed(DCErrorCode dCErrorCode);

    void onSuccess(BindIotDeviceResult bindIotDeviceResult);
}
