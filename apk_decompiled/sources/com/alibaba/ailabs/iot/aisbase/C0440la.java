package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import datasource.NetworkCallback;
import datasource.implemention.data.UpdateDeviceVersionRespData;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.la, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0440la implements NetworkCallback<UpdateDeviceVersionRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2602a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2603b;

    public C0440la(OTAPluginProxy oTAPluginProxy, IActionListener iActionListener) {
        this.f2603b = oTAPluginProxy;
        this.f2602a = iActionListener;
    }

    @Override // datasource.NetworkCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(UpdateDeviceVersionRespData updateDeviceVersionRespData) {
        IActionListener iActionListener = this.f2602a;
        if (iActionListener != null) {
            iActionListener.onSuccess(updateDeviceVersionRespData);
        }
    }

    @Override // datasource.NetworkCallback
    public void onFailure(String str, String str2) {
        IActionListener iActionListener = this.f2602a;
        if (iActionListener != null) {
            iActionListener.onFailure(-300, str2);
        }
    }
}
