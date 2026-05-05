package com.aliyun.alink.linksdk.alcs.lpbs.api;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;

/* JADX INFO: loaded from: classes2.dex */
public abstract class IAlcsPal implements IPalConnect, IPalDiscovery, IPalProbe {
    public abstract void deInitAlcs();

    public abstract void initAlcs(PalInitData palInitData);

    public abstract boolean regAuthProvider(String str, IAuthProvider iAuthProvider);

    public abstract void regCloudChannelFactory(ICloudChannelFactory iCloudChannelFactory);
}
