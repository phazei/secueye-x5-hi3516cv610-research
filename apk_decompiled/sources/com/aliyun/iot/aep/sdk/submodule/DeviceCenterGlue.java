package com.aliyun.iot.aep.sdk.submodule;

import android.app.Application;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigCenter;
import com.aliyun.alink.business.devicecenter.extbone.BoneAddDeviceBiz;
import com.aliyun.alink.business.devicecenter.extbone.BoneHotspotHelper;
import com.aliyun.alink.business.devicecenter.extbone.BoneLocalDeviceMgr;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.sdk.jsbridge.BonePluginRegistry;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceCenterGlue extends SimpleSDKDelegateImp {
    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        if (!SDKManager.isDeviceCenterAvailable()) {
            return 0;
        }
        try {
            ALog.e("DeviceCenterGlue", "start init devicecenter sdk.");
            ProvisionConfigCenter.getInstance().init(application.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("DeviceCenterGlue", "update your devicecenter dep to 1.9.0+.");
        }
        if (SDKManager.isJsBridgeAvailable()) {
            try {
                BonePluginRegistry.register("BoneAddDeviceBiz", BoneAddDeviceBiz.class);
                BonePluginRegistry.register("BoneLocalDeviceMgr", BoneLocalDeviceMgr.class);
                BonePluginRegistry.register("BoneHotspotHelper", BoneHotspotHelper.class);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return 0;
    }
}
