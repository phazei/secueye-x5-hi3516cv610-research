package com.alibaba.ailabs.iot.bluetoothlesdk;

import android.content.Context;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayerManagerBase;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.gattlibrary.channel.GattTransmissionLayer;

/* JADX INFO: compiled from: GattLayerManager.java */
/* JADX INFO: loaded from: classes.dex */
public class c extends TransmissionLayerManagerBase {
    public c(Context context, BluetoothDeviceWrapper bluetoothDeviceWrapper, TransmissionLayer transmissionLayer) {
        super(context, bluetoothDeviceWrapper, transmissionLayer);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayerManagerBase
    public ITransmissionLayer createTransmissionLayer(Context context, TransmissionLayer transmissionLayer) {
        return new GattTransmissionLayer(context);
    }
}
