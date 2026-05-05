package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class M implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f2498a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2499b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ AuthPluginBusinessProxy f2500c;

    public M(AuthPluginBusinessProxy authPluginBusinessProxy, int i, IActionListener iActionListener) {
        this.f2500c = authPluginBusinessProxy;
        this.f2498a = i;
        this.f2499b = iActionListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        LogUtils.w(AuthPluginBusinessProxy.TAG, "Timeout for key: " + this.f2498a);
        AISCommand aISCommand = (AISCommand) this.f2500c.mReTransmissionArray.get(this.f2498a);
        if (aISCommand == null) {
            this.f2499b.onFailure(-5, "Command timeout");
        } else {
            this.f2500c.mReTransmissionArray.remove(this.f2498a);
            this.f2500c.reTransmissionCommand(this.f2498a, this.f2499b, aISCommand);
        }
    }
}
