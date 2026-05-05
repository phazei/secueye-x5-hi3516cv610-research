package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.network.Callback;
import com.alibaba.ailabs.tg.utils.LogUtils;
import datasource.NetworkCallback;
import datasource.implemention.DefaultAuthManager;
import datasource.implemention.data.AuthRandomIdRespData;

/* JADX INFO: compiled from: DefaultAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Ta implements Callback<AuthRandomIdRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2527a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DefaultAuthManager f2528b;

    public Ta(DefaultAuthManager defaultAuthManager, NetworkCallback networkCallback) {
        this.f2528b = defaultAuthManager;
        this.f2527a = networkCallback;
    }

    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(int i, AuthRandomIdRespData authRandomIdRespData) {
        LogUtils.d("AuthPluginBusinessProxy", "getAuthRandomId success: " + authRandomIdRespData);
        NetworkCallback networkCallback = this.f2527a;
        if (networkCallback != null) {
            networkCallback.onSuccess(authRandomIdRespData.getModel());
        }
    }

    public void onFailure(int i, String str, String str2) {
        NetworkCallback networkCallback = this.f2527a;
        if (networkCallback != null) {
            networkCallback.onFailure(str, str2);
        }
    }
}
