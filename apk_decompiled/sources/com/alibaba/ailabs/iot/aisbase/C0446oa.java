package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import datasource.implemention.data.DeviceVersionInfo;
import java.io.File;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.oa, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0446oa implements OnDownLoadStateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceVersionInfo.DeviceInfoModel f2613a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2614b;

    public C0446oa(OTAPluginProxy oTAPluginProxy, DeviceVersionInfo.DeviceInfoModel deviceInfoModel) {
        this.f2614b = oTAPluginProxy;
        this.f2613a = deviceInfoModel;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener
    public void downLoadStateCallback(String str) {
        this.f2614b.y = -1L;
        this.f2614b.h();
        if (this.f2614b.A != null) {
            File file = new File(str);
            if (file.exists()) {
                if (this.f2613a.getMd5().equalsIgnoreCase(Utils.md5(file))) {
                    this.f2614b.A.onComplete(str);
                } else {
                    this.f2614b.a(-402, "md5 not match");
                }
            }
        }
    }
}
