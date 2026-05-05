package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.tg.utils.LogUtils;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import datasource.NetworkCallback;
import datasource.implemention.FeiyanAuthManager;

/* JADX INFO: compiled from: FeiyanAuthManager.java */
/* JADX INFO: loaded from: classes.dex */
public class Wa extends ApiCallBack {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ NetworkCallback f2540a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ FeiyanAuthManager f2541b;

    public Wa(FeiyanAuthManager feiyanAuthManager, NetworkCallback networkCallback) {
        this.f2541b = feiyanAuthManager;
        this.f2540a = networkCallback;
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onFail(int i, String str) {
        LogUtils.i(FeiyanAuthManager.f7861a, "getAuthRandomIdForBLEDevice: onFail code " + i + ", msg " + str);
        NetworkCallback networkCallback = this.f2540a;
        if (networkCallback != null) {
            networkCallback.onFailure(i + "", str);
        }
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onSuccess(Object obj) {
        LogUtils.i(FeiyanAuthManager.f7861a, "getAuthRandomIdForBLEDevice: onSuccess--- ");
        NetworkCallback networkCallback = this.f2540a;
        if (networkCallback != null) {
            if (obj instanceof String) {
                networkCallback.onSuccess((String) obj);
            } else {
                onFail(-1, "resp is not string");
            }
        }
    }
}
