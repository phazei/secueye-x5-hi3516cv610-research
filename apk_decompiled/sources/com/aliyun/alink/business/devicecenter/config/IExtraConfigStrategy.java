package com.aliyun.alink.business.devicecenter.config;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IExtraConfigStrategy extends IConfigStrategy {
    List<DeviceInfo> getPrepareProvisionDevices();

    int getResetCount();
}
