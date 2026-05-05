package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;

/* JADX INFO: compiled from: AuthPluginBusinessProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class K implements DataSentCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AuthPluginBusinessProxy f2493a;

    public K(AuthPluginBusinessProxy authPluginBusinessProxy) {
        this.f2493a = authPluginBusinessProxy;
    }

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
    }
}
