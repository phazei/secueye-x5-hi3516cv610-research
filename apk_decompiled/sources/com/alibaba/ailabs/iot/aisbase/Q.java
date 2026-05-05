package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandSendListener;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.plugin.basic.BasicProxy;

/* JADX INFO: compiled from: BasicProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class Q implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ICommandSendListener f2512a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BasicProxy f2513b;

    public Q(BasicProxy basicProxy, ICommandSendListener iCommandSendListener) {
        this.f2513b = basicProxy;
        this.f2512a = iCommandSendListener;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        if (this.f2512a == null) {
            return;
        }
        if (this.f2513b.f2624c.getConnectionState() != LayerState.CONNECTED) {
            this.f2512a.onFailure(0, "");
        } else {
            this.f2512a.onFailure(1, "");
        }
    }
}
