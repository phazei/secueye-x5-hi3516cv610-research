package com.alibaba.ailabs.iot.aisbase.scanner;

import android.content.Context;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
public class DirectionalLeScanCallback<T extends BluetoothDeviceWrapper> implements ILeScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2657a = "DirectionalLeScanCallback";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f2658b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String[] f2659c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public IActionListener<T[]> f2660d;
    public T[] e;
    public boolean f = false;
    public Class<T> g;

    public DirectionalLeScanCallback(Context context, String[] strArr, IActionListener<T[]> iActionListener, Class<T> cls) {
        this.g = cls;
        this.f2658b = context;
        this.f2659c = strArr;
        this.f2660d = iActionListener;
        this.e = (T[]) ((BluetoothDeviceWrapper[]) Array.newInstance((Class<?>) cls, strArr.length));
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
        if (this.g.isInstance(bluetoothDeviceWrapper)) {
            String address = bluetoothDeviceWrapper.getAddress();
            Object[] objArr = this.f2659c;
            int length = objArr.length;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                if (address.equals(objArr[i2])) {
                    ((T[]) this.e)[i3] = bluetoothDeviceWrapper;
                    break;
                } else {
                    i3++;
                    i2++;
                }
            }
            while (true) {
                T[] tArr = this.e;
                if (i >= tArr.length || tArr[i] == null) {
                    break;
                } else {
                    i++;
                }
            }
            if (i >= this.e.length) {
                this.f = true;
                BLEScannerProxy.getInstance().stopDirectionalScan();
                if (this.f2660d != null) {
                    LogUtils.d(f2657a, "Directional scanning completed");
                    this.f2660d.onSuccess(this.e);
                }
            }
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStartScan() {
        LogUtils.d(f2657a, "Start directional scanning");
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
    public void onStopScan() {
        if (this.f || this.f2660d == null) {
            return;
        }
        this.f = true;
        LogUtils.d(f2657a, "Directional scanning completed");
        this.f2660d.onSuccess(this.e);
    }
}
