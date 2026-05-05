package com.aliyun.alink.linksdk.tmp.listener;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;

/* JADX INFO: loaded from: classes2.dex */
public interface IDiscoveryDeviceStateChangeListener {
    void onDiscoveryDeviceStateChange(DeviceBasicData deviceBasicData, TmpEnum.DiscoveryDeviceState discoveryDeviceState);
}
