package com.aliyun.alink.linksdk.cmp.connect.alcs;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryConfig;
import com.aliyun.alink.linksdk.cmp.core.base.DiscoveryConfig;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsDiscoveryConfig extends DiscoveryConfig {
    public int mDiscoveryStrategy;
    public List<String> mPluginIdList;
    public final int DISCOVERY_STRATEGY_LOWLATENCY = 1;
    public final int DISCOVERY_STRATEGY_LOWENERGY = 2;

    PalDiscoveryConfig transform() {
        PalDiscoveryConfig palDiscoveryConfig = new PalDiscoveryConfig();
        palDiscoveryConfig.mDiscoveryStrategy = this.mDiscoveryStrategy;
        palDiscoveryConfig.mPluginIdList = this.mPluginIdList;
        return palDiscoveryConfig;
    }
}
