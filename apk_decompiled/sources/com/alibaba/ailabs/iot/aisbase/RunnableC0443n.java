package com.alibaba.ailabs.iot.aisbase;

import androidx.annotation.RequiresPermission;
import com.alibaba.ailabs.iot.aisbase.C0447p;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.n, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplLollipop.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0443n implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f2607a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0445o f2608b;

    public RunnableC0443n(C0445o c0445o, int i) {
        this.f2608b = c0445o;
        this.f2607a = i;
    }

    @Override // java.lang.Runnable
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void run() {
        if (!this.f2608b.f2612b.g.getUseHardwareCallbackTypesIfSupported() || this.f2608b.f2612b.g.getCallbackType() == 1) {
            this.f2608b.f2612b.a(this.f2607a);
            return;
        }
        this.f2608b.f2612b.g.a();
        C0447p.a aVar = this.f2608b.f2612b;
        C0447p.this.stopScan(aVar.h);
        C0447p.a aVar2 = this.f2608b.f2612b;
        C0447p.this.a(aVar2.f, aVar2.g, aVar2.h, aVar2.i);
    }
}
