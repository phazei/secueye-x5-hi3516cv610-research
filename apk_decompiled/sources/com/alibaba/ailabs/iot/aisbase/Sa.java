package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.network.Callback;
import datasource.NetworkCallback;
import datasource.implemention.DefaultAuthManager;
import datasource.implemention.data.UpdateDeviceVersionRespData;

/* JADX INFO: compiled from: DefaultAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Sa implements Callback<UpdateDeviceVersionRespData> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2523a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DefaultAuthManager f2524b;

    public Sa(DefaultAuthManager defaultAuthManager, NetworkCallback networkCallback) {
        this.f2524b = defaultAuthManager;
        this.f2523a = networkCallback;
    }

    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(int i, UpdateDeviceVersionRespData updateDeviceVersionRespData) {
        NetworkCallback networkCallback = this.f2523a;
        if (networkCallback != null) {
            networkCallback.onSuccess(updateDeviceVersionRespData);
        }
    }

    public void onFailure(int i, String str, String str2) {
        NetworkCallback networkCallback = this.f2523a;
        if (networkCallback != null) {
            networkCallback.onFailure(str, str2);
        }
    }
}
