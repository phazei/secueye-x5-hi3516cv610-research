package com.aliyun.alink.business.devicecenter.api.discovery;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.add.BatchDiscoveryParams;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface ILocalDeviceMgr {
    void getDeviceToken(Context context, GetTokenParams getTokenParams, IOnTokenGetListerner iOnTokenGetListerner);

    @Deprecated
    void getDeviceToken(Context context, String str, String str2, int i, int i2, IOnDeviceTokenGetListener iOnDeviceTokenGetListener);

    @Deprecated
    void getDeviceToken(Context context, String str, String str2, int i, IOnDeviceTokenGetListener iOnDeviceTokenGetListener);

    @Deprecated
    void getDeviceToken(String str, String str2, int i, IOnDeviceTokenGetListener iOnDeviceTokenGetListener);

    List<DeviceInfo> getLanDevices();

    void startBatchDiscovery(Context context, BatchDiscoveryParams batchDiscoveryParams, IDiscovery iDiscovery);

    void startDiscovery(Context context, IDiscoveryListener iDiscoveryListener);

    void startDiscovery(Context context, EnumSet<DiscoveryType> enumSet, Map<String, Object> map, IDeviceDiscoveryListener iDeviceDiscoveryListener);

    void startDiscoveryWithFilter(Context context, Map<String, Object> map, IDiscoveryListener iDiscoveryListener);

    void stopBatchDiscovery(String str, String str2);

    void stopDiscovery();

    void stopGetDeviceToken();

    void stopGetDeviceToken(String str, String str2);
}
