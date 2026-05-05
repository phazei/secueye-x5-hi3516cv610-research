package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import datasource.NetworkCallback;
import datasource.implemention.data.GetDeviceUUIDRespData;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ja, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0436ja implements NetworkCallback<GetDeviceUUIDRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2593a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2594b;

    public C0436ja(OTAPluginProxy oTAPluginProxy, IActionListener iActionListener) {
        this.f2594b = oTAPluginProxy;
        this.f2593a = iActionListener;
    }

    @Override // datasource.NetworkCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(GetDeviceUUIDRespData getDeviceUUIDRespData) {
        String model = getDeviceUUIDRespData.getModel();
        IActionListener iActionListener = this.f2593a;
        if (iActionListener != null) {
            iActionListener.onSuccess(model);
        }
    }

    @Override // datasource.NetworkCallback
    public void onFailure(String str, String str2) {
        IActionListener iActionListener = this.f2593a;
        if (iActionListener != null) {
            iActionListener.onFailure(-300, str2);
        }
    }
}
