package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ga, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0430ga implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2581a;

    public C0430ga(OTAPluginProxy oTAPluginProxy) {
        this.f2581a = oTAPluginProxy;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f2581a.b(1, str);
        LogUtils.d(this.f2581a.f2633a, "getFirmwareVersionCommand failed, code is: " + i + ", desc: " + str);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onSuccess(Object obj) {
        this.f2581a.a(IOTAPlugin.OTAState.FINISH);
        String strAdapterToOsUpdateVersion = "";
        if (obj instanceof Integer) {
            strAdapterToOsUpdateVersion = Utils.adapterToOsUpdateVersion(((Integer) obj).intValue());
        } else if (obj instanceof String) {
            strAdapterToOsUpdateVersion = (String) obj;
        }
        if (this.f2581a.f2635c != null) {
            this.f2581a.f2635c.onSuccess(strAdapterToOsUpdateVersion);
        }
        OTAPluginProxy oTAPluginProxy = this.f2581a;
        oTAPluginProxy.updateDeviceVersion(oTAPluginProxy.w, strAdapterToOsUpdateVersion, new C0428fa(this));
    }
}
