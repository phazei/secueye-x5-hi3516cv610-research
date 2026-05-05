package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ha, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0432ha implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2583a;

    public C0432ha(OTAPluginProxy oTAPluginProxy) {
        this.f2583a = oTAPluginProxy;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onSuccess(Object obj) {
        LogUtils.d(this.f2583a.f2633a, "Get the firmware version successfully: " + obj);
        if (obj instanceof Integer) {
            this.f2583a.K = Utils.adapterToOsUpdateVersion(((Integer) obj).intValue());
        } else if (obj instanceof String) {
            this.f2583a.K = (String) obj;
        }
        LogUtils.d(this.f2583a.f2633a, "OsUpdate version: " + this.f2583a.K);
    }
}
