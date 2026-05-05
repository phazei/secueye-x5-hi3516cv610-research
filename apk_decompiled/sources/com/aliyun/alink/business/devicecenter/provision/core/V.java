package com.aliyun.alink.business.devicecenter.provision.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import com.aliyun.alink.business.devicecenter.log.ALog;

/* JADX INFO: compiled from: AlinkP2PProvision.java */
/* JADX INFO: loaded from: classes2.dex */
public class V extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Z f3681a;

    public V(Z z) {
        this.f3681a = z;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) intent.getParcelableExtra("wifiP2pDevice");
            if (wifiP2pDevice == null) {
                return;
            }
            String str = wifiP2pDevice.deviceName;
            String str2 = Z.f3686a;
            StringBuilder sb = new StringBuilder();
            sb.append("registerP2PReceiver(),name=");
            sb.append(str);
            ALog.d(str2, sb.toString());
            if (str == null) {
                return;
            }
            if ((this.f3681a.f3689d != null && this.f3681a.f3689d.startsWith(str)) || (this.f3681a.f3688c != null && this.f3681a.f3688c.startsWith(str))) {
                try {
                    int length = str.getBytes("UTF-8").length;
                    if (this.f3681a.h.get() && this.f3681a.i != 0 && this.f3681a.i != length) {
                        String str3 = Z.f3686a;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("registerP2PReceiver(), less than 32,name=");
                        sb2.append(str);
                        sb2.append("length=");
                        sb2.append(length);
                        sb2.append(" packDataLength=");
                        sb2.append(this.f3681a.i);
                        ALog.w(str3, sb2.toString());
                        this.f3681a.i();
                    }
                } catch (Exception e) {
                    String str4 = Z.f3686a;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("registerP2PReceiver,succ,error");
                    sb3.append(e);
                    ALog.w(str4, sb3.toString());
                }
            }
            if ((this.f3681a.f3689d == null || !str.contains(this.f3681a.f3689d) || str.equals(this.f3681a.f3689d)) && (this.f3681a.f3688c == null || !str.contains(this.f3681a.f3688c) || str.equals(this.f3681a.f3688c))) {
                return;
            }
            ALog.w(Z.f3686a, "name is change fail, unsupport!");
            this.f3681a.i();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
