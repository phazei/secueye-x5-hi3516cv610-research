package com.alibaba.ailabs.iot.aisbase;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.OTAPluginProxy;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.ba, reason: case insensitive filesystem */
/* JADX INFO: compiled from: OTAPluginProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class C0420ba implements DataSentCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ OTAPluginProxy f2552a;

    public C0420ba(OTAPluginProxy oTAPluginProxy) {
        this.f2552a = oTAPluginProxy;
    }

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        LogUtils.v(this.f2552a.f2633a, "Send OTA PDU success, PDU index: " + this.f2552a.o);
        int length = data.getValue() == null ? 0 : data.getValue().length - 4;
        this.f2552a.p += length;
        this.f2552a.o += this.f2552a.D;
        if (!this.f2552a.E) {
            this.f2552a.F = false;
            return;
        }
        if (this.f2552a.o >= this.f2552a.e.size()) {
            this.f2552a.F = false;
        } else if (((AISCommand) this.f2552a.e.get(this.f2552a.o)).getHeader().getFrameSeq() != 0) {
            this.f2552a.f();
        } else {
            LogUtils.d(this.f2552a.f2633a, "next package sequence is 0");
            this.f2552a.F = false;
        }
    }
}
