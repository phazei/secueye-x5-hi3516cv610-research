package com.aliyun.alink.business.devicecenter.provision.core;

import android.net.wifi.p2p.WifiP2pManager;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;

/* JADX INFO: compiled from: AlinkP2PProvision.java */
/* JADX INFO: loaded from: classes2.dex */
public class W implements WifiP2pManager.ActionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f3682a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Z f3683b;

    public W(Z z, String str) {
        this.f3683b = z;
        this.f3682a = str;
    }

    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
    public void onFailure(int i) {
        ALog.w(Z.f3686a, "changeDeviceName(" + this.f3682a + "),onFailure(), reason = " + i);
        if (i == 0 || i == 1) {
            this.f3683b.i();
        }
    }

    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
    public void onSuccess() {
        ALog.d(Z.f3686a, "changeDeviceName() succ,name = " + this.f3682a + ",prepareName=" + this.f3683b.f3688c + ", configName=" + this.f3683b.f3689d + ", originName=" + this.f3683b.f3687b);
        if (TextUtils.isEmpty(this.f3682a)) {
            return;
        }
        if (!(this.f3683b.h.get() && this.f3682a.equals(this.f3683b.f3689d)) && (this.f3683b.h.get() || !this.f3682a.equals(this.f3683b.f3687b))) {
            return;
        }
        ALog.d(Z.f3686a, "change name succ,expose. ,isProvision=" + this.f3683b.h);
        if (this.f3683b.h.get()) {
            DCUserTrack.addTrackData(AlinkConstants.KEY_BROADCAST_P2P, String.valueOf(System.currentTimeMillis()));
        }
        this.f3683b.a();
    }
}
