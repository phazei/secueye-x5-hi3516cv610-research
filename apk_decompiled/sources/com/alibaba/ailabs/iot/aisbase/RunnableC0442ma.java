package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ma, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0442ma implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2606a;

    public RunnableC0442ma(OTAPluginProxy oTAPluginProxy) {
        this.f2606a = oTAPluginProxy;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2606a.b(5, "Command timeout(\nWaiting for response timeout)");
    }
}
