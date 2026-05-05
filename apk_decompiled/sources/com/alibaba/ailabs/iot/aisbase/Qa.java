package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.network.Callback;
import datasource.NetworkCallback;
import datasource.implemention.DefaultAuthManager;
import datasource.implemention.data.GetDeviceUUIDRespData;

/* JADX INFO: compiled from: DefaultAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Qa implements Callback<GetDeviceUUIDRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2514a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DefaultAuthManager f2515b;

    public Qa(DefaultAuthManager defaultAuthManager, NetworkCallback networkCallback) {
        this.f2515b = defaultAuthManager;
        this.f2514a = networkCallback;
    }

    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(int i, GetDeviceUUIDRespData getDeviceUUIDRespData) {
        NetworkCallback networkCallback = this.f2514a;
        if (networkCallback != null) {
            networkCallback.onSuccess(getDeviceUUIDRespData);
        }
    }

    public void onFailure(int i, String str, String str2) {
        NetworkCallback networkCallback = this.f2514a;
        if (networkCallback != null) {
            networkCallback.onFailure(str, str2);
        }
    }
}
