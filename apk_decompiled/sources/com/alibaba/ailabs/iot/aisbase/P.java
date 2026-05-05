package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandSendListener;
import com.alibaba.ailabs.iot.aisbase.plugin.basic.BasicProxy;

/* JADX INFO: compiled from: BasicProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class P implements DataSentCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ICommandSendListener f2508a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BasicProxy f2509b;

    public P(BasicProxy basicProxy, ICommandSendListener iCommandSendListener) {
        this.f2509b = basicProxy;
        this.f2508a = iCommandSendListener;
    }

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        ICommandSendListener iCommandSendListener = this.f2508a;
        if (iCommandSendListener != null) {
            iCommandSendListener.onSent();
        }
    }
}
