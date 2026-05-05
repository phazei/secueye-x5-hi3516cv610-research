package com.aliyun.alink.linksdk.alcs.lpbs.data;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class PalDiscoveryConfig {
    public static final String DISCOVERY_PLUGINID_BREEZE = "com.aliyun.iot.breeze.lpbs";
    public static final String DISCOVERY_PLUGINID_ICA = "iot_ica";
    public static final int DISCOVERY_STRATEGY_LOWENERGY = 2;
    public static final int DISCOVERY_STRATEGY_LOWLATENCY = 1;
    public int mDiscoveryStrategy;
    public List<String> mPluginIdList;
}
