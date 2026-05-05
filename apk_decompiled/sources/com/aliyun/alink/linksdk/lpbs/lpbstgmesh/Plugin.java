package com.aliyun.alink.linksdk.lpbs.lpbstgmesh;

import android.os.Handler;
import android.os.Looper;
import com.alibaba.ailabs.iot.mesh.AuthInfoListener;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.PluginConfig;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class Plugin implements IPlugin {
    private static final String TAG = Utils.TAG + "Plugin";
    private Bridge mBridge;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private PluginConfig mPluginConfig;

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public String getPluginId() {
        return Utils.LPBS_TGMESH_PLUGINID;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public boolean startPlugin(String str, PluginConfig pluginConfig) {
        ALog.d(TAG, "startPlugin s:" + str + " pluginConfig:" + pluginConfig);
        this.mPluginConfig = pluginConfig;
        this.mBridge = new Bridge(this.mPluginConfig);
        if (!TgMeshManager.getInstance().isInitialized()) {
            TgMeshManager.getInstance().init(pluginConfig.context, new AuthInfoListener() { // from class: com.aliyun.alink.linksdk.lpbs.lpbstgmesh.Plugin.1
                @Override // com.alibaba.ailabs.iot.mesh.AuthInfoListener
                public String getAuthInfo() {
                    return "";
                }
            });
        }
        LogUtils.enable(true);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public boolean stopPlugin(String str) {
        ALog.d(TAG, "stopPlugin s:" + str);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public IPalBridge getPalBridge() {
        ALog.d(TAG, "getPalBridge");
        return this.mBridge;
    }
}
