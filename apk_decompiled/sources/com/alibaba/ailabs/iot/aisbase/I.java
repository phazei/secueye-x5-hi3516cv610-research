package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.IDetailActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.IAuthPlugin;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class I implements IActionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ byte[] f2487a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ J f2488b;

    public I(J j, byte[] bArr) {
        this.f2488b = j;
        this.f2487a = bArr;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f2488b.f2491c.mTransmissionLayer.forwardInnerCastEvent(IAuthPlugin.EVENT_AUTH_FAILED);
        IActionListener iActionListener = this.f2488b.f2489a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, str);
        } else {
            LogUtils.e(AuthPluginBusinessProxy.TAG, "sendVerifyResult onFailure: listener is null");
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onSuccess(Object obj) {
        IActionListener iActionListener = this.f2488b.f2489a;
        if (iActionListener != null) {
            iActionListener.onSuccess(this.f2487a);
            IActionListener iActionListener2 = this.f2488b.f2489a;
            if (iActionListener2 instanceof IDetailActionListener) {
                ((IDetailActionListener) iActionListener2).onState(3, "auth success", null);
            }
        } else {
            LogUtils.e(AuthPluginBusinessProxy.TAG, "sendVerifyResult onSuccess: listener is null");
        }
        this.f2488b.f2491c.mTransmissionLayer.forwardInnerCastEvent(IAuthPlugin.EVENT_AUTH_SUCCESS);
    }
}
