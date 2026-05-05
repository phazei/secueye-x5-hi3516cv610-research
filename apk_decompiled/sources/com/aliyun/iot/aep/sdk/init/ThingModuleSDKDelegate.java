package com.aliyun.iot.aep.sdk.init;

import android.app.Application;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.TmpInitConfig;
import com.aliyun.alink.linksdk.tmp.data.discovery.DiscoveryConfig;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ThingModuleSDKDelegate extends SimpleSDKDelegateImp {
    public static final String DISCOVERY_ON = "discovery_on";
    public static final String DISCOVERY_SETTING_FILENAME = "discovery_setting";

    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(final Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        if (!SDKManager.isTMPAvailable()) {
            return 0;
        }
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: com.aliyun.iot.aep.sdk.init.ThingModuleSDKDelegate.1
            @Override // java.lang.Runnable
            public void run() {
                TmpSdk.init(application, new TmpInitConfig(2));
                ThingModuleSDKDelegate.startSchedule(application);
            }
        });
        return 0;
    }

    public static void startSchedule(Application application) {
        boolean z = application.getSharedPreferences(DISCOVERY_SETTING_FILENAME, 0).getInt(DISCOVERY_ON, 1) >= 1;
        ALog.d("ThingModuleSDKDelegate", "startSchedule isDiscoveryOn:" + z);
        if (z) {
            DiscoveryConfig discoveryConfig = new DiscoveryConfig();
            discoveryConfig.discoveryParams = new DiscoveryConfig.DiscoveryParams();
            discoveryConfig.discoveryParams.discoveryStrategy = DiscoveryConfig.DiscoveryStrategy.LOW_ENERGY;
            TmpSdk.getDeviceManager().discoverDevicesInfinite(false, 40000L, 0L, discoveryConfig, null, null);
        }
    }

    public static void stopSchedule() {
        TmpSdk.getDeviceManager().stopDiscoverDevicesInfinite();
    }
}
