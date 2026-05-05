package com.aliyun.alink.linksdk.alcs.api;

import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface ICAProbeListener {
    public static final int ALCS_SEND_OK = 0;
    public static final int ALCS_SEND_RSPERROR = 2;
    public static final int ALCS_SEND_TIMEOUT = 1;

    void onComplete(ICADeviceInfo iCADeviceInfo, int i);
}
