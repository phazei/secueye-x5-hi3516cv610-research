package com.aliyun.iot.aep.sdk.credential.IotCredentialManager;

import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;

/* JADX INFO: loaded from: classes2.dex */
public interface IoTCredentialManage {
    void asyncRefreshIoTCredential(IoTCredentialListener ioTCredentialListener);

    IoTCredentialData getIoTCredential();

    String getIoTIdentity();

    String getIoTRefreshToken();

    String getIoTToken();

    boolean isIoTRefreshTokenExpired();

    boolean isIoTTokenExpired();
}
