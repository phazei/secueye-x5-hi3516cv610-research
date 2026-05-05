package com.aliyun.alink.business.devicecenter.provision.core;

import android.net.wifi.p2p.WifiP2pManager;
import com.aliyun.alink.business.devicecenter.log.ALog;

/* JADX INFO: compiled from: AlinkP2PProvision.java */
/* JADX INFO: loaded from: classes2.dex */
public class X implements WifiP2pManager.ActionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Z f3684a;

    public X(Z z) {
        this.f3684a = z;
    }

    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
    public void onFailure(int i) {
        ALog.w(Z.f3686a, "exposeData(),discoverPeers fail,reason=" + i);
        if (i == 0 || i == 1) {
            this.f3684a.i();
        }
    }

    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
    public void onSuccess() {
        ALog.d(Z.f3686a, "exposeData(),discoverPeers succ");
    }
}
