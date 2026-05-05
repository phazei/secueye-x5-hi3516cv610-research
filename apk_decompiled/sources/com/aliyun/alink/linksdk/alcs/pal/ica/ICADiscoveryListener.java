package com.aliyun.alink.linksdk.alcs.pal.ica;

import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface ICADiscoveryListener {
    public static final String PAL_DLCP_RAW = "dlcp-raw";
    public static final String PAL_LINKKIT_ICA = "ali-ica";
    public static final String PAL_LINKKIT_RAW = "linkkit-raw";

    void onDiscoveryDevice(String str, int i, String str2, ICADeviceInfo iCADeviceInfo);

    void onDiscoveryFinish();
}
