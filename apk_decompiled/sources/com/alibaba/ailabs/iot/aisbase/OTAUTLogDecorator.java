package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;

/* JADX INFO: loaded from: classes.dex */
public class OTAUTLogDecorator implements IOTAPlugin.IOTAActionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IOTAPlugin.IOTAActionListener f2504a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BluetoothDeviceWrapper f2505b;

    public OTAUTLogDecorator(IOTAPlugin.IOTAActionListener iOTAActionListener, BluetoothDeviceWrapper bluetoothDeviceWrapper) {
        this.f2504a = iOTAActionListener;
        this.f2505b = bluetoothDeviceWrapper;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onFailed(int i, String str) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2504a;
        if (iOTAActionListener != null) {
            iOTAActionListener.onFailed(i, str);
        }
        BluetoothDeviceWrapper bluetoothDeviceWrapper = this.f2505b;
        if (bluetoothDeviceWrapper != null) {
            UTLogUtils.updateBusInfo("ota", UTLogUtils.buildDeviceInfo(bluetoothDeviceWrapper), UTLogUtils.buildOtaBusInfo("error", 0, i, str));
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onProgress(int i, int i2) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2504a;
        if (iOTAActionListener != null) {
            iOTAActionListener.onProgress(i, i2);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onStateChanged(IOTAPlugin.OTAState oTAState) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2504a;
        if (iOTAActionListener != null) {
            iOTAActionListener.onStateChanged(oTAState);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onSuccess(String str) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2504a;
        if (iOTAActionListener != null) {
            iOTAActionListener.onSuccess(str);
        }
        UTLogUtils.updateBusInfo("ota", UTLogUtils.buildDeviceInfo(this.f2505b), UTLogUtils.buildOtaBusInfo("success", 0, 0, ""));
    }
}
