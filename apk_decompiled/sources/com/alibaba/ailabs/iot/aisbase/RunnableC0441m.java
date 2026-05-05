package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.le.ScanResult;
import android.os.SystemClock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.m, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplLollipop.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0441m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ List f2604a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0445o f2605b;

    public RunnableC0441m(C0445o c0445o, List list) {
        this.f2605b = c0445o;
        this.f2604a = list;
    }

    @Override // java.lang.Runnable
    public void run() {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (this.f2605b.f2611a > (jElapsedRealtime - this.f2605b.f2612b.g.getReportDelayMillis()) + 5) {
            return;
        }
        this.f2605b.f2611a = jElapsedRealtime;
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f2604a.iterator();
        while (it.hasNext()) {
            arrayList.add(C0447p.this.a((ScanResult) it.next()));
        }
        this.f2605b.f2612b.a(arrayList);
    }
}
