package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.network.Callback;
import datasource.NetworkCallback;
import datasource.implemention.DefaultAuthManager;
import datasource.implemention.data.DeviceVersionInfo;

/* JADX INFO: compiled from: DefaultAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Ra implements Callback<DeviceVersionInfo> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2516a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DefaultAuthManager f2517b;

    public Ra(DefaultAuthManager defaultAuthManager, NetworkCallback networkCallback) {
        this.f2517b = defaultAuthManager;
        this.f2516a = networkCallback;
    }

    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(int i, DeviceVersionInfo deviceVersionInfo) {
        NetworkCallback networkCallback = this.f2516a;
        if (networkCallback != null) {
            networkCallback.onSuccess(deviceVersionInfo);
        }
    }

    public void onFailure(int i, String str, String str2) {
        NetworkCallback networkCallback = this.f2516a;
        if (networkCallback != null) {
            networkCallback.onFailure(str, str2);
        }
    }
}
