package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.data.ica.ICADiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgrConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalAuthRegister;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalGroupConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupInfo;
import com.aliyun.alink.linksdk.alcs.pal.ica.ICAAlcsNative;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: ICAAlcsBridge.java */
/* JADX INFO: loaded from: classes2.dex */
public class c implements IPalBridge {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4050a = "[AlcsLPBS]ICAAlcsBridge";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private PalInitData f4052c;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<String, ICADiscoveryDeviceInfo> f4051b = new HashMap();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private e f4053d = new e(this);
    private g e = new g();

    public c(PluginMgrConfig pluginMgrConfig) {
    }

    public void a(String str, ICADiscoveryDeviceInfo iCADiscoveryDeviceInfo) {
        this.f4051b.put(str, iCADiscoveryDeviceInfo);
    }

    public ICADiscoveryDeviceInfo a(String str) {
        return this.f4051b.get(str);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public void initBridge(PalInitData palInitData) {
        this.f4052c = palInitData;
        ALog.d(f4050a, " initBridge initData:" + palInitData);
        ICAAlcsNative.initPal(m.a(palInitData));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public void deInitBridge() {
        ALog.d(f4050a, " deInitBridge");
        ICAAlcsNative.deInitPal();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalDiscovery getPalDiscovery() {
        return this.f4053d;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalConnect getPalConnect(PalDeviceInfo palDeviceInfo) {
        return new d(this, palDeviceInfo);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalAuthRegister getPalAuthRegister() {
        return this.e;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalProbe getPalProbe() {
        return new f(this);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalBridge
    public IPalGroupConnect getGroupConnect(PalGroupInfo palGroupInfo) {
        return new com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.a.a(this, palGroupInfo);
    }
}
