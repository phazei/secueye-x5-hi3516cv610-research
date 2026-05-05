package com.aliyun.alink.linksdk.alcs.lpbs.bridge;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface IPalBridge {
    void deInitBridge();

    IPalGroupConnect getGroupConnect(PalGroupInfo palGroupInfo);

    IPalAuthRegister getPalAuthRegister();

    IPalConnect getPalConnect(PalDeviceInfo palDeviceInfo);

    IPalDiscovery getPalDiscovery();

    IPalProbe getPalProbe();

    void initBridge(PalInitData palInitData);
}
