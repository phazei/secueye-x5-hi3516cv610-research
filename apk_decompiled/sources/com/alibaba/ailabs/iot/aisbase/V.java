package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTADownloadHelper;

/* JADX INFO: compiled from: OTADownloadHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class V implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTADownloadHelper f2536a;

    public V(OTADownloadHelper oTADownloadHelper) {
        this.f2536a = oTADownloadHelper;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2536a.b(5, "Command timeout(\nWaiting for response timeout)");
    }
}
