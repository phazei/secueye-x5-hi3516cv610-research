package com.alibaba.ailabs.iot.aisbase;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class E implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ F f2471a;

    public E(F f) {
        this.f2471a = f;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2471a.e.mGetAuthRandomTimeoutTask = null;
        F f = this.f2471a;
        f.e.startAuth(f.f2473a, f.f2475c, f.f2476d, f.f2474b);
    }
}
