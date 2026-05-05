package com.aliyun.alink.linksdk.tmp.service;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.IDiscoveryFilter;
import com.aliyun.alink.linksdk.tmp.data.cloud.CloudLcaRequestParams;
import com.aliyun.alink.linksdk.tmp.device.panel.data.BoneDisFilter;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tools.ALog;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class DevDiscoveryService {
    private static final String TAG = "[Tmp]DevDiscoveryService";

    public static void startDiscoveryWithFilter(JSONObject jSONObject, IDevListener iDevListener) {
        final BoneDisFilter boneDisFilter;
        ALog.d(TAG, "startDiscoveryWithFilter params:" + jSONObject + " listener:" + iDevListener);
        if (iDevListener == null) {
            ALog.e(TAG, "startDiscoveryWithFilter params listener null");
            return;
        }
        if (jSONObject == null) {
            boneDisFilter = null;
        } else {
            try {
                boneDisFilter = (BoneDisFilter) com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(jSONObject), BoneDisFilter.class);
            } catch (Exception e) {
                ALog.e(TAG, "startDiscoveryWithFilter error:" + e.toString());
                return;
            }
        }
        CloudLcaRequestParams cloudLcaRequestParams = new CloudLcaRequestParams();
        cloudLcaRequestParams.groupId = boneDisFilter.groupId;
        cloudLcaRequestParams.gatewayIotId = boneDisFilter.gatewayIotId;
        DeviceManager.getInstance().discoverDevices((Object) null, false, 5000L, (Object) cloudLcaRequestParams, new IDiscoveryFilter() { // from class: com.aliyun.alink.linksdk.tmp.service.DevDiscoveryService.1
            @Override // com.aliyun.alink.linksdk.tmp.api.IDiscoveryFilter
            public boolean doFilter(DeviceBasicData deviceBasicData) {
                BoneDisFilter boneDisFilter2 = boneDisFilter;
                if (boneDisFilter2 == null) {
                    ALog.d(DevDiscoveryService.TAG, "boneDisFilter null return true");
                    return true;
                }
                return boneDisFilter2.doFilter(deviceBasicData);
            }
        }, iDevListener);
    }

    public static void stopDiscovery(JSONObject jSONObject, IDevListener iDevListener) {
        ALog.d(TAG, "stopDiscovery params:" + jSONObject + " listener:" + iDevListener);
        if (iDevListener == null) {
            ALog.e(TAG, "stopDiscovery params listener null");
        } else {
            DeviceManager.getInstance().stopDiscoverDevices();
        }
    }
}
