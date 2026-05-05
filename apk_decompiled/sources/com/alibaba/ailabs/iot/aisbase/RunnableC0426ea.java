package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ea, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0426ea implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2573a;

    public RunnableC0426ea(OTAPluginProxy oTAPluginProxy) {
        this.f2573a = oTAPluginProxy;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2573a.t.connectDevice(this.f2573a.v, new C0424da(this));
    }
}
