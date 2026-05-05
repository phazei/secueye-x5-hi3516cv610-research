package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothAdapter;
import androidx.annotation.RequiresPermission;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.e, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplJB.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0425e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0433i f2572a;

    public RunnableC0425e(C0433i c0433i) {
        this.f2572a = c0433i;
    }

    @Override // java.lang.Runnable
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void run() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || this.f2572a.f <= 0 || this.f2572a.g <= 0) {
            return;
        }
        defaultAdapter.stopLeScan(this.f2572a.j);
        this.f2572a.e.postDelayed(this.f2572a.i, this.f2572a.f);
    }
}
