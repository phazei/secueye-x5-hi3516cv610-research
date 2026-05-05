package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.IDetailActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.IAuthPlugin;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import datasource.NetworkCallback;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class J implements NetworkCallback<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2489a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IPlugin f2490b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ AuthPluginBusinessProxy f2491c;

    public J(AuthPluginBusinessProxy authPluginBusinessProxy, IActionListener iActionListener, IPlugin iPlugin) {
        this.f2491c = authPluginBusinessProxy;
        this.f2489a = iActionListener;
        this.f2490b = iPlugin;
    }

    @Override // datasource.NetworkCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(String str) {
        this.f2491c.cancelAuthAndCheckCipherTimeoutTask();
        LogUtils.d(AuthPluginBusinessProxy.TAG, "authCheckAndGetBleKey success: " + str);
        byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(str);
        IActionListener iActionListener = this.f2489a;
        if (iActionListener instanceof IDetailActionListener) {
            ((IDetailActionListener) iActionListener).onState(2, "get auth ble key success", null);
        }
        this.f2491c.sendVerifyResult(this.f2490b, new I(this, bArrHexString2Bytes), true);
    }

    @Override // datasource.NetworkCallback
    public void onFailure(String str, String str2) {
        UTLogUtils.updateBusInfo("gma_auth", UTLogUtils.buildDeviceInfo(this.f2490b.getBluetoothDeviceWrapper()), UTLogUtils.buildAuthBusInfo("error", this.f2491c.mProductId, this.f2491c.mDeviceAddress, 1, "authAndCheckCipher failed: " + str2));
        this.f2491c.cancelAuthAndCheckCipherTimeoutTask();
        LogUtils.e(AuthPluginBusinessProxy.TAG, String.format("authCheckAndGetBleKey failed, errCode: %s, desc: %s", str, str2));
        this.f2491c.mTransmissionLayer.forwardInnerCastEvent(IAuthPlugin.EVENT_AUTH_FAILED);
        IActionListener iActionListener = this.f2489a;
        if (iActionListener != null) {
            iActionListener.onFailure(-300, str2);
        }
    }
}
