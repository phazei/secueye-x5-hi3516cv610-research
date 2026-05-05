package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.basic.BasicProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: BasicProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class U implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f2529a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2530b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ BasicProxy f2531c;

    public U(BasicProxy basicProxy, int i, IActionListener iActionListener) {
        this.f2531c = basicProxy;
        this.f2529a = i;
        this.f2530b = iActionListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        LogUtils.w(BasicProxy.f2622a, "Timeout for key: " + this.f2529a);
        this.f2530b.onFailure(-5, "Command timeout");
        this.f2531c.e.remove(this.f2529a);
    }
}
