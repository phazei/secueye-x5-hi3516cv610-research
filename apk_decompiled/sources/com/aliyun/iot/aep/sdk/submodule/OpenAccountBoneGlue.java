package com.aliyun.iot.aep.sdk.submodule;

import android.app.Application;
import com.aliyun.alink.sdk.jsbridge.BonePluginRegistry;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.login.plugin.BoneUserAccountPlugin;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class OpenAccountBoneGlue extends SimpleSDKDelegateImp {
    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        if (!SDKManager.isJsBridgeAvailable()) {
            return 0;
        }
        try {
            BonePluginRegistry.register(BoneUserAccountPlugin.API_NAME, BoneUserAccountPlugin.class);
            return 0;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }
}
