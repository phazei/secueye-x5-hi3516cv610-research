package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTADownloadHelper;
import datasource.implemention.data.DeviceVersionInfo;
import java.io.File;

/* JADX INFO: compiled from: OTADownloadHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class X implements OnDownLoadStateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceVersionInfo f2542a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTADownloadHelper f2543b;

    public X(OTADownloadHelper oTADownloadHelper, DeviceVersionInfo deviceVersionInfo) {
        this.f2543b = oTADownloadHelper;
        this.f2542a = deviceVersionInfo;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener
    public void downLoadStateCallback(String str) {
        this.f2543b.i = -1L;
        this.f2543b.b();
        if (this.f2543b.k != null) {
            File file = new File(str);
            if (file.exists()) {
                if (this.f2542a.getModel().getMd5().equalsIgnoreCase(Utils.md5(file))) {
                    this.f2543b.k.onComplete(str);
                } else {
                    this.f2543b.a(-402, "md5 not match");
                }
            }
        }
    }
}
