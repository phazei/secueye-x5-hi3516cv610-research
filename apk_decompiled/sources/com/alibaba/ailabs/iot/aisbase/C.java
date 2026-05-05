package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayerManagerBase;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: compiled from: TransmissionLayerManagerBase.java */
/* JADX INFO: loaded from: classes.dex */
public class C implements BluetoothProfile.ServiceListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BluetoothDevice f2466a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ TransmissionLayerManagerBase f2467b;

    public C(TransmissionLayerManagerBase transmissionLayerManagerBase, BluetoothDevice bluetoothDevice) {
        this.f2467b = transmissionLayerManagerBase;
        this.f2466a = bluetoothDevice;
    }

    @Override // android.bluetooth.BluetoothProfile.ServiceListener
    public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
        BluetoothA2dp bluetoothA2dp = (BluetoothA2dp) bluetoothProfile;
        Class<?> cls = bluetoothA2dp.getClass();
        if (bluetoothA2dp.getConnectionState(this.f2466a) == 2) {
            try {
                try {
                    cls.getMethod("disconnect", BluetoothDevice.class).invoke(bluetoothA2dp, this.f2466a);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            }
        } else {
            this.f2467b.a(bluetoothA2dp.getConnectionState(this.f2466a));
        }
        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(i, bluetoothProfile);
    }

    @Override // android.bluetooth.BluetoothProfile.ServiceListener
    public void onServiceDisconnected(int i) {
    }
}
