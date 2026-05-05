package com.aliyun.alink.business.devicecenter.api.add;

import android.content.Context;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IAddDeviceBiz {
    void continueProvision(Map map);

    String getCurrentSsid(Context context);

    AddDeviceState getProcedureState();

    int getWifiRssid(Context context);

    String getWifiType(Context context);

    void setAliProvisionMode(String str);

    void setDevice(DeviceInfo deviceInfo);

    void setExtraInfo(Map map);

    void setProvisionTimeOut(int i);

    void startAddDevice(Context context, IAddDeviceListener iAddDeviceListener);

    void startBindDevice(Context context, IAddDeviceListener iAddDeviceListener);

    void startConcurrentAddDevice(Context context, List<DeviceInfo> list, IConcurrentAddDeviceListener iConcurrentAddDeviceListener);

    void stopAddDevice();

    void stopConcurrentAddDevice();

    void toggleProvision(String str, String str2, int i);
}
