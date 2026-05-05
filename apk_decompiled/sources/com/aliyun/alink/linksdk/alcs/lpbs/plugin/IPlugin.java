package com.aliyun.alink.linksdk.alcs.lpbs.plugin;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge;

/* JADX INFO: loaded from: classes2.dex */
public interface IPlugin {
    IPalBridge getPalBridge();

    String getPluginId();

    boolean startPlugin(String str, PluginConfig pluginConfig);

    boolean stopPlugin(String str);
}
