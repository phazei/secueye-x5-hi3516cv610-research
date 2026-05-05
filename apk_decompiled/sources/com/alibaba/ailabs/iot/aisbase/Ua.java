package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.network.Callback;
import datasource.NetworkCallback;
import datasource.implemention.DefaultAuthManager;
import datasource.implemention.data.AuthCheckAndGetBleKeyRespData;

/* JADX INFO: compiled from: DefaultAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Ua implements Callback<AuthCheckAndGetBleKeyRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2532a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DefaultAuthManager f2533b;

    public Ua(DefaultAuthManager defaultAuthManager, NetworkCallback networkCallback) {
        this.f2533b = defaultAuthManager;
        this.f2532a = networkCallback;
    }

    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(int i, AuthCheckAndGetBleKeyRespData authCheckAndGetBleKeyRespData) {
        NetworkCallback networkCallback = this.f2532a;
        if (networkCallback != null) {
            networkCallback.onSuccess(authCheckAndGetBleKeyRespData.getModel());
        }
    }

    public void onFailure(int i, String str, String str2) {
        NetworkCallback networkCallback = this.f2532a;
        if (networkCallback != null) {
            networkCallback.onFailure(str, str2);
        }
    }
}
