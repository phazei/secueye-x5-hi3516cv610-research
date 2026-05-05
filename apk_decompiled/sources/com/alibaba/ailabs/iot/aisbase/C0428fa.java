package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.tg.utils.LogUtils;
import datasource.implemention.data.UpdateDeviceVersionRespData;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.fa, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0428fa implements IActionListener<UpdateDeviceVersionRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0430ga f2577a;

    public C0428fa(C0430ga c0430ga) {
        this.f2577a = c0430ga;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(UpdateDeviceVersionRespData updateDeviceVersionRespData) {
        LogUtils.d(this.f2577a.f2581a.f2633a, "Update device version success: " + updateDeviceVersionRespData.getModel());
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        LogUtils.d(this.f2577a.f2581a.f2633a, "Update device version failed, code is: " + i + ", desc: " + str);
    }
}
