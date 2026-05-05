package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalSdk;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: DiscoveryForceStopListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class c implements PalDiscoveryListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3983a = "[AlcsLPBS]DiscoveryForceStopListener";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalDiscoveryListener f3984b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f3986d;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private AtomicInteger f3985c = new AtomicInteger(PluginMgr.getInstance().getPluginCount());
    private a e = new a();

    public c(int i, PalDiscoveryListener palDiscoveryListener) {
        this.f3984b = palDiscoveryListener;
        this.f3986d = i;
        if (AlcsPalSdk.getHandler() != null) {
            AlcsPalSdk.getHandler().postDelayed(this.e, this.f3986d + 1000);
        }
        ALog.d(f3983a, "DiscoveryForceStopListener mFinishedPluginCount:" + this.f3985c.get());
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
    public void onDiscoveryDevice(PalDiscoveryDeviceInfo palDiscoveryDeviceInfo) {
        PalDiscoveryListener palDiscoveryListener = this.f3984b;
        if (palDiscoveryListener != null) {
            palDiscoveryListener.onDiscoveryDevice(palDiscoveryDeviceInfo);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
    public void onDiscoveryFinish() {
        int iDecrementAndGet = this.f3985c.decrementAndGet();
        ALog.d(f3983a, "onDiscoveryFinish count:" + iDecrementAndGet);
        if (iDecrementAndGet == 0) {
            if (this.e != null) {
                AlcsPalSdk.getHandler().removeCallbacks(this.e);
            }
            this.e = null;
            PalDiscoveryListener palDiscoveryListener = this.f3984b;
            if (palDiscoveryListener != null) {
                palDiscoveryListener.onDiscoveryFinish();
            }
        }
    }

    public void a() {
        this.e = null;
        this.f3985c.set(0);
        PalDiscoveryListener palDiscoveryListener = this.f3984b;
        if (palDiscoveryListener != null) {
            palDiscoveryListener.onDiscoveryFinish();
        }
    }

    /* JADX INFO: compiled from: DiscoveryForceStopListener.java */
    public class a implements Runnable {
        public a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            c.this.a();
        }
    }
}
