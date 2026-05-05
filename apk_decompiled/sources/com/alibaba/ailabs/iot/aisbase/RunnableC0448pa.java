package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.pa, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0448pa implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f2616a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2617b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2618c;

    public RunnableC0448pa(OTAPluginProxy oTAPluginProxy, int i, IActionListener iActionListener) {
        this.f2618c = oTAPluginProxy;
        this.f2616a = i;
        this.f2617b = iActionListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        LogUtils.w(this.f2618c.f2633a, "Timeout for key: " + this.f2616a);
        AISCommand aISCommand = (AISCommand) this.f2618c.J.get(this.f2616a);
        if (aISCommand != null) {
            this.f2618c.J.remove(this.f2616a);
            this.f2618c.a(this.f2616a, this.f2617b, aISCommand);
        } else {
            this.f2617b.onFailure(-5, String.format("Command %d timeout", Integer.valueOf(this.f2616a)));
            this.f2618c.f.remove(this.f2616a);
        }
    }
}
