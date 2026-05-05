package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTADownloadHelper;
import datasource.implemention.data.DeviceVersionInfo;
import java.io.File;

/* JADX INFO: compiled from: OTADownloadHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class Y implements OnDownLoadStateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceVersionInfo.DeviceInfoModel f2546a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTADownloadHelper f2547b;

    public Y(OTADownloadHelper oTADownloadHelper, DeviceVersionInfo.DeviceInfoModel deviceInfoModel) {
        this.f2547b = oTADownloadHelper;
        this.f2546a = deviceInfoModel;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener
    public void downLoadStateCallback(String str) {
        this.f2547b.i = -1L;
        this.f2547b.b();
        if (this.f2547b.k != null) {
            File file = new File(str);
            if (file.exists()) {
                if (this.f2546a.getMd5().equalsIgnoreCase(Utils.md5(file))) {
                    this.f2547b.k.onComplete(str);
                } else {
                    this.f2547b.a(-402, "md5 not match");
                }
            }
        }
    }
}
