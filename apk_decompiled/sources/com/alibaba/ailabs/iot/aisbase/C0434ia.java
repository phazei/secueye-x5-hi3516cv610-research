package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import datasource.implemention.data.DeviceVersionInfo;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ia, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0434ia implements IActionListener<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f2586a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2587b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2588c;

    public C0434ia(OTAPluginProxy oTAPluginProxy, String str, IActionListener iActionListener) {
        this.f2588c = oTAPluginProxy;
        this.f2586a = str;
        this.f2587b = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(String str) {
        this.f2588c.w = str;
        this.f2588c.x = this.f2586a;
        this.f2588c.b(str, this.f2586a, (IActionListener<DeviceVersionInfo>) this.f2587b);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        IActionListener iActionListener = this.f2587b;
        if (iActionListener != null) {
            iActionListener.onFailure(i, str);
        }
    }
}
