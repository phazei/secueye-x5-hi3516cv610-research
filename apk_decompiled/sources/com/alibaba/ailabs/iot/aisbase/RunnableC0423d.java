package com.alibaba.ailabs.iot.aisbase;

import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanResult;
import android.os.SystemClock;
import java.util.Iterator;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.d, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerCompat.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0423d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BluetoothLeScannerCompat.a f2566a;

    public RunnableC0423d(BluetoothLeScannerCompat.a aVar) {
        this.f2566a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        long jElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        synchronized (this.f2566a.f1591a) {
            Iterator it = this.f2566a.l.values().iterator();
            while (it.hasNext()) {
                ScanResult scanResult = (ScanResult) it.next();
                if (scanResult.getTimestampNanos() < jElapsedRealtimeNanos - this.f2566a.g.getMatchLostDeviceTimeout()) {
                    it.remove();
                    this.f2566a.i.post(new RunnableC0421c(this, scanResult));
                }
            }
            if (!this.f2566a.l.isEmpty()) {
                this.f2566a.i.postDelayed(this, this.f2566a.g.getMatchLostTaskInterval());
            }
        }
    }
}
