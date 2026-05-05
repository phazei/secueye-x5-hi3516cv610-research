package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import com.alibaba.ailabs.iot.aisbase.channel.TransmissionLayerManagerBase;
import com.taobao.accs.utl.BaseMonitor;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: compiled from: TransmissionLayerManagerBase.java */
/* JADX INFO: loaded from: classes.dex */
public class B implements BluetoothProfile.ServiceListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BluetoothDevice f2463a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ TransmissionLayerManagerBase f2464b;

    public B(TransmissionLayerManagerBase transmissionLayerManagerBase, BluetoothDevice bluetoothDevice) {
        this.f2464b = transmissionLayerManagerBase;
        this.f2463a = bluetoothDevice;
    }

    @Override // android.bluetooth.BluetoothProfile.ServiceListener
    public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
        BluetoothA2dp bluetoothA2dp = (BluetoothA2dp) bluetoothProfile;
        Class<?> cls = bluetoothA2dp.getClass();
        int connectionState = bluetoothA2dp.getConnectionState(this.f2463a);
        if (connectionState != 2) {
            try {
                try {
                    cls.getMethod(BaseMonitor.ALARM_POINT_CONNECT, BluetoothDevice.class).invoke(bluetoothA2dp, this.f2463a);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            }
        } else {
            this.f2464b.a(connectionState);
        }
        BluetoothDevice bluetoothDeviceA = this.f2464b.a(bluetoothA2dp);
        if (bluetoothDeviceA == null || !bluetoothDeviceA.getAddress().equalsIgnoreCase(this.f2463a.getAddress())) {
            this.f2464b.a(bluetoothA2dp, this.f2463a);
        }
        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(i, bluetoothProfile);
    }

    @Override // android.bluetooth.BluetoothProfile.ServiceListener
    public void onServiceDisconnected(int i) {
    }
}
