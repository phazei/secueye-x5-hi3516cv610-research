package com.alibaba.sdk.android.openaccount.ui.module;

import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountFaceLoginService;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountFaceLoginServiceImpl;
import com.alibaba.sdk.android.pluto.meta.ModuleInfo;
import com.alibaba.sdk.android.pluto.meta.ModuleInfoBuilder;
import com.alibaba.security.rp.RPSDK;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountFaceModule {
    private static boolean supportRpSDK;
    private static boolean supportWindvane;

    static {
        try {
            Class.forName("android.taobao.windvane.WindVaneSDK");
            supportWindvane = true;
        } catch (Throwable unused) {
        }
        try {
            Class.forName("com.alibaba.security.rp.RPSDK");
            supportRpSDK = true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static ModuleInfo getModuleInfo() {
        ModuleInfoBuilder moduleInfoBuilder = new ModuleInfoBuilder("openAccountFaceModule");
        if (supportRpSDK && supportWindvane) {
            switch (ConfigManager.getInstance().getEnvironment()) {
                case ONLINE:
                    RPSDK.initialize(RPSDK.RPSDKEnv.RPSDKEnv_ONLINE, OpenAccountSDK.getAndroidContext());
                    break;
                case PRE:
                    RPSDK.initialize(RPSDK.RPSDKEnv.RPSDKEnv_PRE, OpenAccountSDK.getAndroidContext());
                    break;
                case TEST:
                    RPSDK.initialize(RPSDK.RPSDKEnv.RPSDKEnv_DAILY, OpenAccountSDK.getAndroidContext());
                    break;
            }
            moduleInfoBuilder.addBeanInfo(OpenAccountFaceLoginService.class, OpenAccountFaceLoginServiceImpl.class, "init", (Map<String, String>) null);
        }
        return moduleInfoBuilder.build();
    }
}
