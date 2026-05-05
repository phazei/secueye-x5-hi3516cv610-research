package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTADownloadHelper;

/* JADX INFO: compiled from: OTADownloadHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class W implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTADownloadHelper f2539a;

    public W(OTADownloadHelper oTADownloadHelper) {
        this.f2539a = oTADownloadHelper;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2539a.b(5, "OTA activity timed out");
    }
}
