package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;
import datasource.NetworkCallback;
import datasource.implemention.data.DeviceVersionInfo;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ka, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0438ka implements NetworkCallback<DeviceVersionInfo> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2598a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2599b;

    public C0438ka(OTAPluginProxy oTAPluginProxy, IActionListener iActionListener) {
        this.f2599b = oTAPluginProxy;
        this.f2598a = iActionListener;
    }

    @Override // datasource.NetworkCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(DeviceVersionInfo deviceVersionInfo) {
        LogUtils.d(this.f2599b.f2633a, "Successful got ota info: " + deviceVersionInfo);
        this.f2599b.C = deviceVersionInfo;
        IActionListener iActionListener = this.f2598a;
        if (iActionListener != null) {
            iActionListener.onSuccess(deviceVersionInfo);
        }
    }

    @Override // datasource.NetworkCallback
    public void onFailure(String str, String str2) {
        LogUtils.e(this.f2599b.f2633a, "Failed to query OTA info: " + str + "(" + str2 + ")");
        IActionListener iActionListener = this.f2598a;
        if (iActionListener != null) {
            iActionListener.onFailure(-300, str2);
        }
    }
}
