package com.aliyun.iot.aep.sdk.credential.IotCredentialManager;

import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;

/* JADX INFO: loaded from: classes2.dex */
public interface ICredentialDataSource {
    void credentialDidCreate(IoTCallback ioTCallback);

    void credentialDidDelete(IoTCallback ioTCallback);

    void credentialDidUpdate(IoTCallback ioTCallback);
}
