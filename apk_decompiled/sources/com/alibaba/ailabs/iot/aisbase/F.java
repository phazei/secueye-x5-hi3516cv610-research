package com.alibaba.ailabs.iot.aisbase;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;
import datasource.NetworkCallback;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class F implements NetworkCallback<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IPlugin f2473a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2474b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ byte[] f2475c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ String f2476d;
    public final /* synthetic */ AuthPluginBusinessProxy e;

    public F(AuthPluginBusinessProxy authPluginBusinessProxy, IPlugin iPlugin, IActionListener iActionListener, byte[] bArr, String str) {
        this.e = authPluginBusinessProxy;
        this.f2473a = iPlugin;
        this.f2474b = iActionListener;
        this.f2475c = bArr;
        this.f2476d = str;
    }

    @Override // datasource.NetworkCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(String str) {
        this.e.cancelGetAuthRandomTimeoutTask();
        LogUtils.d(AuthPluginBusinessProxy.TAG, "Get auth random success: " + str);
        if (str.length() > 0) {
            this.e.deliveryRandomId(this.f2473a, this.f2474b, str);
            return;
        }
        IActionListener iActionListener = this.f2474b;
        if (iActionListener != null) {
            iActionListener.onFailure(-206, "");
        }
    }

    @Override // datasource.NetworkCallback
    public void onFailure(String str, String str2) {
        LogUtils.e(AuthPluginBusinessProxy.TAG, String.format("getAuthRandomId failed, errCode: %s, desc: %s", str, str2));
        UTLogUtils.updateBusInfo("gma_auth", UTLogUtils.buildDeviceInfo(this.f2473a.getBluetoothDeviceWrapper()), UTLogUtils.buildAuthBusInfo("error", this.e.mProductId, this.e.mDeviceAddress, 1, "getAuthRandom failed: " + str2));
        if (this.e.canRetryGetAuthRandom()) {
            if (this.e.mGetAuthRandomTimeoutTask == null) {
                this.e.mGetAuthRandomTimeoutTask = new E(this);
            }
            this.e.mHandler.postDelayed(this.e.mGetAuthRandomTimeoutTask, 3000L);
            return;
        }
        LogUtils.i(AuthPluginBusinessProxy.TAG, "cancel get auth random timeout task");
        this.e.cancelGetAuthRandomTimeoutTask();
        if (this.f2474b != null) {
            if (TextUtils.equals(str, "2064") || TextUtils.equals(str, "28612")) {
                AuthPluginBusinessProxy.isAuthAndBind.set(true);
            }
            this.f2474b.onFailure(-300, str2);
        }
    }
}
