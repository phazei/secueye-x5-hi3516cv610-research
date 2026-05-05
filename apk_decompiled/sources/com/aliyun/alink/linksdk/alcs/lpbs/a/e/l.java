package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener;
import com.aliyun.alink.linksdk.alcs.lpbs.plugin.IPlugin;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: RegisterLayer.java */
/* JADX INFO: loaded from: classes2.dex */
public class l extends e {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f4022c = "[AlcsLPBS]RegisterLayer";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.a.a f4023a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.d.a f4024b;

    public l(com.aliyun.alink.linksdk.alcs.lpbs.a.d.a aVar, com.aliyun.alink.linksdk.alcs.lpbs.a.a.a aVar2, e eVar) {
        super(eVar);
        this.f4023a = aVar2;
        this.f4024b = aVar;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public boolean regAuthProvider(String str, IAuthProvider iAuthProvider) {
        IPlugin pluginByPluginId = PluginMgr.getInstance().getPluginByPluginId(str);
        ALog.d(f4022c, "regAuthProvider pluginId:" + str + " provider:" + iAuthProvider + " plugin:" + pluginByPluginId);
        if (pluginByPluginId == null || pluginByPluginId.getPalBridge().getPalAuthRegister() == null) {
            return true;
        }
        pluginByPluginId.getPalBridge().getPalAuthRegister().setAuthProvider(iAuthProvider);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal
    public void regCloudChannelFactory(ICloudChannelFactory iCloudChannelFactory) {
        ALog.d(f4022c, "regCloudChannelFactory mChannelMgr:" + this.f4023a + " factory:" + iCloudChannelFactory);
        this.f4023a.a(iCloudChannelFactory);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean regDeviceStateListener(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener) {
        ALog.d(f4022c, "regDeviceStateListener mDevStateListenerMgr:" + this.f4024b + " listener:" + palDeviceStateListener);
        return this.f4024b.a(palDeviceInfo, palDeviceStateListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.a.e.e, com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect
    public boolean unregDeviceStateListener(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener) {
        ALog.d(f4022c, "unregDeviceStateListener mDevStateListenerMgr:" + this.f4024b + " listener:" + palDeviceStateListener);
        return this.f4024b.b(palDeviceInfo, palDeviceStateListener);
    }
}
