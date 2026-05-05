package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.ReportProgressUtil;
import com.alibaba.ailabs.tg.utils.LogUtils;
import datasource.NetworkCallback;
import datasource.implemention.data.OtaProgressRespData;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: ReportProgressUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class Da implements NetworkCallback<OtaProgressRespData> {
    @Override // datasource.NetworkCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(OtaProgressRespData otaProgressRespData) {
        LogUtils.d(ReportProgressUtil.f2639a, "Report ota progress successful");
    }

    @Override // datasource.NetworkCallback
    public void onFailure(String str, String str2) {
        LogUtils.e(ReportProgressUtil.f2639a, "Failed to report ota progress");
    }
}
