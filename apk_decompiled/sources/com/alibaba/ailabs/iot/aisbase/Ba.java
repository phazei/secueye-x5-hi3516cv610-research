package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class Ba implements DataSentCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2465a;

    public Ba(OTAPluginProxy oTAPluginProxy) {
        this.f2465a = oTAPluginProxy;
    }

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        LogUtils.d(this.f2465a.f2633a, "Send request verify firmware success");
        this.f2465a.a(IOTAPlugin.OTAState.WAIT_VERIFY_RESPONSE);
    }
}
