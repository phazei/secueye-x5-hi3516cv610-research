package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.OtaActionListener;
import com.alibaba.ailabs.tg.utils.LogUtils;
import datasource.NetworkCallback;
import datasource.implemention.data.OtaProgressRespData;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.y, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OtaActionListener.java */
/* JADX INFO: loaded from: classes.dex */
public class C0465y implements NetworkCallback<OtaProgressRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OtaActionListener f2696a;

    public C0465y(OtaActionListener otaActionListener) {
        this.f2696a = otaActionListener;
    }

    @Override // datasource.NetworkCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(OtaProgressRespData otaProgressRespData) {
        LogUtils.d(OtaActionListener.f2556a, "Report ota progress successful");
    }

    @Override // datasource.NetworkCallback
    public void onFailure(String str, String str2) {
        LogUtils.e(OtaActionListener.f2556a, "Failed to report ota progress");
    }
}
