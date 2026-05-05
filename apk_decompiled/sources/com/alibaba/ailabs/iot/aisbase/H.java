package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class H implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IPlugin f2483a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f2484b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2485c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ AuthPluginBusinessProxy f2486d;

    public H(AuthPluginBusinessProxy authPluginBusinessProxy, IPlugin iPlugin, byte[] bArr, IActionListener iActionListener) {
        this.f2486d = authPluginBusinessProxy;
        this.f2483a = iPlugin;
        this.f2484b = bArr;
        this.f2485c = iActionListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f2486d.canRetryAuthAndCheckCipher()) {
            this.f2486d.mAuthAndCheckCipherTimeoutTask = null;
            this.f2486d.authCheckAndGetBleKey(this.f2483a, this.f2484b, this.f2485c);
        }
    }
}
