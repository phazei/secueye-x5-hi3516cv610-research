package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.IDetailActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class G implements IActionListener<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2478a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IPlugin f2479b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ AuthPluginBusinessProxy f2480c;

    public G(AuthPluginBusinessProxy authPluginBusinessProxy, IActionListener iActionListener, IPlugin iPlugin) {
        this.f2480c = authPluginBusinessProxy;
        this.f2478a = iActionListener;
        this.f2479b = iPlugin;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(byte[] bArr) {
        String lowerCase = ConvertUtils.bytes2HexString(bArr).toLowerCase();
        LogUtils.d(AuthPluginBusinessProxy.TAG, "Delivery random success, received digest: " + lowerCase);
        IActionListener iActionListener = this.f2478a;
        if (iActionListener instanceof IDetailActionListener) {
            ((IDetailActionListener) iActionListener).onState(1, "start get auth ble key", null);
        }
        this.f2480c.authCheckAndGetBleKey(this.f2479b, bArr, this.f2478a);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        LogUtils.e(AuthPluginBusinessProxy.TAG, "Delivery random failed, error code(" + i + "), error message(" + str + ")");
        IActionListener iActionListener = this.f2478a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, str);
        }
    }
}
