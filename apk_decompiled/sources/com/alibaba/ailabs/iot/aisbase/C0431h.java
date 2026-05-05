package com.alibaba.ailabs.iot.aisbase;

import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.SystemClock;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.h, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplJB.java */
/* JADX INFO: loaded from: classes.dex */
public class C0431h implements BluetoothAdapter.LeScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0433i f2582a;

    public C0431h(C0433i c0433i) {
        this.f2582a = c0433i;
    }

    @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        ScanResult scanResult = new ScanResult(bluetoothDevice, ScanRecord.parseFromBytes(bArr), i, SystemClock.elapsedRealtimeNanos());
        synchronized (this.f2582a.f2584c) {
            for (BluetoothLeScannerCompat.a aVar : this.f2582a.f2584c.values()) {
                aVar.i.post(new RunnableC0429g(this, aVar, scanResult));
            }
        }
    }
}
