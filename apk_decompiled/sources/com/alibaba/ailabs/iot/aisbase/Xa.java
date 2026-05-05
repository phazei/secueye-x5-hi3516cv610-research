package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.utils.LogUtils;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import datasource.NetworkCallback;
import datasource.implemention.FeiyanAuthManager;

/* JADX INFO: compiled from: FeiyanAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Xa extends ApiCallBack {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2544a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ FeiyanAuthManager f2545b;

    public Xa(FeiyanAuthManager feiyanAuthManager, NetworkCallback networkCallback) {
        this.f2545b = feiyanAuthManager;
        this.f2544a = networkCallback;
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onFail(int i, String str) {
        LogUtils.i(FeiyanAuthManager.f7861a, "authCipherCheckThenGetKeyForBLEDevice onFail code " + i + ", msg " + str);
        NetworkCallback networkCallback = this.f2544a;
        if (networkCallback != null) {
            networkCallback.onFailure(i + "", str);
        }
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onSuccess(Object obj) {
        LogUtils.i(FeiyanAuthManager.f7861a, "authCipherCheckThenGetKeyForBLEDevice onSuccess");
        if (!(obj instanceof String)) {
            onFail(-1, "resp is not string");
            return;
        }
        NetworkCallback networkCallback = this.f2544a;
        if (networkCallback != null) {
            networkCallback.onSuccess((String) obj);
        } else {
            LogUtils.i(FeiyanAuthManager.f7861a, "authCipherCheckThenGetKeyForBLEDevice callback is null");
        }
    }
}
