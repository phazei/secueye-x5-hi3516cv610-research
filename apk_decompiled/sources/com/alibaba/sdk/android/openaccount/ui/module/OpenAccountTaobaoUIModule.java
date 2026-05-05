package com.alibaba.sdk.android.openaccount.ui.module;

import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountTaobaoUIService;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountTaobaoUIServiceImpl;
import com.alibaba.sdk.android.pluto.meta.ModuleInfo;
import com.alibaba.sdk.android.pluto.meta.ModuleInfoBuilder;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountTaobaoUIModule {
    public static final String TAG = "oa.OpenAccountTaobaoUIModule";
    private static boolean secondPartTaobaoSdkAvailable;

    static {
        try {
            Class.forName("com.taobao.login4android.login.LoginController");
            Class.forName("com.ali.user.mobile.filter.PreLoginFilter");
            secondPartTaobaoSdkAvailable = true;
        } catch (ClassNotFoundException unused) {
        }
    }

    public static ModuleInfo getModuleInfo() {
        ModuleInfoBuilder moduleInfoBuilder = new ModuleInfoBuilder("openAccountWithTaobaoUI");
        if (secondPartTaobaoSdkAvailable) {
            moduleInfoBuilder.addBeanInfo(OpenAccountTaobaoUIService.class, OpenAccountTaobaoUIServiceImpl.class, "init", (Map<String, String>) null);
        } else {
            AliSDKLogger.w(TAG, "Taobao login4android SDK is not available, login with Taobao 2nd UI Service Provider is disabled");
        }
        return moduleInfoBuilder.build();
    }
}
