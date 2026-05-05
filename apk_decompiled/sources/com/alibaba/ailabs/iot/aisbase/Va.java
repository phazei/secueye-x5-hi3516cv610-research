package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.network.Callback;
import datasource.NetworkCallback;
import datasource.implemention.DefaultAuthManager;
import datasource.implemention.data.OtaProgressRespData;

/* JADX INFO: compiled from: DefaultAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Va implements Callback<OtaProgressRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2537a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DefaultAuthManager f2538b;

    public Va(DefaultAuthManager defaultAuthManager, NetworkCallback networkCallback) {
        this.f2538b = defaultAuthManager;
        this.f2537a = networkCallback;
    }

    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(int i, OtaProgressRespData otaProgressRespData) {
        NetworkCallback networkCallback = this.f2537a;
        if (networkCallback != null) {
            networkCallback.onSuccess(otaProgressRespData);
        }
    }

    public void onFailure(int i, String str, String str2) {
        NetworkCallback networkCallback = this.f2537a;
        if (networkCallback != null) {
            networkCallback.onFailure(str, str2);
        }
    }
}
