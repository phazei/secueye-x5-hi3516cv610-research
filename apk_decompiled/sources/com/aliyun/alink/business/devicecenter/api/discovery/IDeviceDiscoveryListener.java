package com.aliyun.alink.business.devicecenter.api.discovery;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IDeviceDiscoveryListener {
    void onDeviceFound(DiscoveryType discoveryType, List<DeviceInfo> list);
}
