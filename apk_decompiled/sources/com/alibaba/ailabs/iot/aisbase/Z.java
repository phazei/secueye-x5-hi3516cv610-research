package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTADownloadHelper;
import com.alibaba.ailabs.iot.aisbase.utils.DownloadManagerUtils;

/* JADX INFO: compiled from: OTADownloadHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class Z implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DownloadManagerUtils.DownloadTaskDetails f2548a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTADownloadHelper.a f2549b;

    public Z(OTADownloadHelper.a aVar, DownloadManagerUtils.DownloadTaskDetails downloadTaskDetails) {
        this.f2549b = aVar;
        this.f2548a = downloadTaskDetails;
    }

    @Override // java.lang.Runnable
    public void run() {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = OTADownloadHelper.this.k;
        DownloadManagerUtils.DownloadTaskDetails downloadTaskDetails = this.f2548a;
        iFirmwareDownloadListener.onProgress(downloadTaskDetails.totalSize, downloadTaskDetails.downloadedSize);
    }
}
