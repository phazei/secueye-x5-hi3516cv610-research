package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import datasource.implemention.data.DeviceVersionInfo;
import java.io.File;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.na, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0444na implements OnDownLoadStateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceVersionInfo f2609a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2610b;

    public C0444na(OTAPluginProxy oTAPluginProxy, DeviceVersionInfo deviceVersionInfo) {
        this.f2610b = oTAPluginProxy;
        this.f2609a = deviceVersionInfo;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener
    public void downLoadStateCallback(String str) {
        this.f2610b.y = -1L;
        this.f2610b.h();
        if (this.f2610b.A != null) {
            File file = new File(str);
            if (file.exists()) {
                if (this.f2609a.getModel().getMd5().equalsIgnoreCase(Utils.md5(file))) {
                    this.f2610b.A.onComplete(str);
                } else {
                    this.f2610b.a(-402, "md5 not match");
                }
            }
        }
    }
}
