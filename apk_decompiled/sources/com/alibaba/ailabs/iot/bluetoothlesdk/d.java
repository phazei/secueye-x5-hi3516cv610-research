package com.alibaba.ailabs.iot.bluetoothlesdk;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.aisbase.utils.ut.UTUtil;

/* JADX INFO: compiled from: UTLogDecorator.java */
/* JADX INFO: loaded from: classes.dex */
public class d<T> implements IActionListener<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static boolean f2748a = true;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private IActionListener<T> f2749b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private BluetoothDeviceWrapper f2750c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f2751d;

    public d(IActionListener<T> iActionListener, BluetoothDeviceWrapper bluetoothDeviceWrapper, String str) {
        this.f2749b = iActionListener;
        this.f2750c = bluetoothDeviceWrapper;
        this.f2751d = str;
        a(str, this.f2750c, "start", 0, "");
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onSuccess(T t) {
        IActionListener<T> iActionListener = this.f2749b;
        if (iActionListener != null) {
            iActionListener.onSuccess(t);
        }
        a(this.f2751d, this.f2750c, "success", 0, "");
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        IActionListener<T> iActionListener = this.f2749b;
        if (iActionListener != null) {
            iActionListener.onFailure(i, str);
        }
        a(this.f2751d, this.f2750c, "error", i, str);
    }

    private void a(String str, BluetoothDeviceWrapper bluetoothDeviceWrapper, String str2, int i, String str3) {
        if (f2748a) {
            UTUtil.updateBusInfo(str, bluetoothDeviceWrapper, str2, i, str3);
        }
    }
}
