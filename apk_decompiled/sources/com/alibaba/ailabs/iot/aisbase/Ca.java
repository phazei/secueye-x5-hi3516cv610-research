package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.iot.aisbase.utils.DownloadManagerUtils;

/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class Ca implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DownloadManagerUtils.DownloadTaskDetails f2468a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy.a f2469b;

    public Ca(OTAPluginProxy.a aVar, DownloadManagerUtils.DownloadTaskDetails downloadTaskDetails) {
        this.f2469b = aVar;
        this.f2468a = downloadTaskDetails;
    }

    @Override // java.lang.Runnable
    public void run() {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = OTAPluginProxy.this.A;
        DownloadManagerUtils.DownloadTaskDetails downloadTaskDetails = this.f2468a;
        iFirmwareDownloadListener.onProgress(downloadTaskDetails.totalSize, downloadTaskDetails.downloadedSize);
    }
}
