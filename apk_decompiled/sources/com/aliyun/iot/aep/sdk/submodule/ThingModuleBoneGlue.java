package com.aliyun.iot.aep.sdk.submodule;

import android.app.Application;
import com.aliyun.alink.linksdk.tmp.extbone.BoneGroup;
import com.aliyun.alink.linksdk.tmp.extbone.BoneSubDeviceService;
import com.aliyun.alink.linksdk.tmp.extbone.BoneThingDiscovery;
import com.aliyun.alink.linksdk.tmp.extbone.BoneThingFactory;
import com.aliyun.alink.sdk.jsbridge.BonePluginRegistry;
import com.aliyun.iot.aep.sdk.bridge.core.BoneServiceFactoryRegistry;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ThingModuleBoneGlue extends SimpleSDKDelegateImp {
    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        if (SDKManager.isTMPAvailable() && SDKManager.isJsBridgeAvailable()) {
            try {
                BoneServiceFactoryRegistry.register(new BoneThingFactory());
                BonePluginRegistry.register("BoneGroup", BoneGroup.class);
                BonePluginRegistry.register("BoneSubDeviceService", BoneSubDeviceService.class);
                BonePluginRegistry.register("BoneThingDiscovery", BoneThingDiscovery.class);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return 0;
    }
}
