package com.aliyun.alink.linksdk.tmp.data.discovery;

import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsDiscoveryConfig;
import com.aliyun.alink.linksdk.tmp.data.cloud.CloudLcaRequestParams;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DiscoveryConfig {
    public CloudLcaRequestParams cloudLcaRequestParams;
    public DiscoveryParams discoveryParams;

    public static class DiscoveryParams {
        public static final String BREEZE_PLUGIN_ID = "com.aliyun.iot.breeze.lpbs";
        public static final String ICA_PLUGIN_ID = "iot_ica";
        public DiscoveryStrategy discoveryStrategy;
        public List<String> lpbsPluginIdList;
    }

    public enum DiscoveryStrategy {
        LOW_LATENCY(1),
        LOW_ENERGY(2);

        protected int value;

        public int value() {
            return this.value;
        }

        DiscoveryStrategy(int i) {
            this.value = i;
        }
    }

    public AlcsDiscoveryConfig translateToALCSDiscoveryConfig() {
        if (this.discoveryParams == null) {
            return null;
        }
        AlcsDiscoveryConfig alcsDiscoveryConfig = new AlcsDiscoveryConfig();
        alcsDiscoveryConfig.mPluginIdList = this.discoveryParams.lpbsPluginIdList;
        if (this.discoveryParams.discoveryStrategy == null) {
            this.discoveryParams.discoveryStrategy = DiscoveryStrategy.LOW_ENERGY;
        }
        alcsDiscoveryConfig.mDiscoveryStrategy = this.discoveryParams.discoveryStrategy.value();
        return alcsDiscoveryConfig;
    }
}
