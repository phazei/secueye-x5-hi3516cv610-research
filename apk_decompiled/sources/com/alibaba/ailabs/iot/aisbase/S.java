package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.plugin.basic.BasicProxy;

/* JADX INFO: compiled from: BasicProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class S implements DataSentCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BasicProxy f2522a;

    public S(BasicProxy basicProxy) {
        this.f2522a = basicProxy;
    }

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
    }
}
