package com.aliyun.alink.linksdk.alcs.lpbs.plugin;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgrConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.c;

/* JADX INFO: loaded from: classes2.dex */
public class ICAPlugin implements IPlugin {
    private c mBridge;

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public String getPluginId() {
        return "iot_ica";
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public boolean startPlugin(String str, PluginConfig pluginConfig) {
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public boolean stopPlugin(String str) {
        return true;
    }

    public ICAPlugin(PluginMgrConfig pluginMgrConfig) {
        this.mBridge = new c(pluginMgrConfig);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin
    public IPalBridge getPalBridge() {
        return this.mBridge;
    }
}
