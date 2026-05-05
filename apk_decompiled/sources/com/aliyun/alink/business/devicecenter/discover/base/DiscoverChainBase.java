package com.aliyun.alink.business.devicecenter.discover.base;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.discover.IDiscoverChain;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class DiscoverChainBase extends AbilityReceiver implements IDiscoverChain {
    public Set<DiscoveryType> discoveryTypes;

    public DiscoverChainBase(Context context) {
        super(context);
        this.discoveryTypes = new HashSet();
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void addDiscoveryType(DiscoveryType discoveryType) {
        this.discoveryTypes.add(discoveryType);
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public boolean isSupport() {
        return true;
    }
}
