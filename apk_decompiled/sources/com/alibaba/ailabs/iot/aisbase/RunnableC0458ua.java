package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ua, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0458ua implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2680a;

    public RunnableC0458ua(OTAPluginProxy oTAPluginProxy) {
        this.f2680a = oTAPluginProxy;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2680a.b(5, "OTA activity timed out");
    }
}
