package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;

/* JADX INFO: compiled from: BluetoothDeviceWrapper.java */
/* JADX INFO: loaded from: classes.dex */
public class Ka implements IOTAPlugin.IFirmwareDownloadListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IOTAPlugin.IFirmwareDownloadListener f2494a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BluetoothDeviceWrapper f2495b;

    public Ka(BluetoothDeviceWrapper bluetoothDeviceWrapper, IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener) {
        this.f2495b = bluetoothDeviceWrapper;
        this.f2494a = iFirmwareDownloadListener;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
    public void onComplete(String str) {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = this.f2494a;
        if (iFirmwareDownloadListener != null) {
            iFirmwareDownloadListener.onComplete(str);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
    public void onDownloadStart() {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = this.f2494a;
        if (iFirmwareDownloadListener != null) {
            iFirmwareDownloadListener.onDownloadStart();
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
    public void onFailed(int i, String str) {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = this.f2494a;
        if (iFirmwareDownloadListener != null) {
            iFirmwareDownloadListener.onFailed(i, str);
        }
        UTLogUtils.updateBusInfo("ota", UTLogUtils.buildDeviceInfo(this.f2495b), UTLogUtils.buildOtaBusInfo("error", 0, i, str));
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IFirmwareDownloadListener
    public void onProgress(int i, int i2) {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = this.f2494a;
        if (iFirmwareDownloadListener != null) {
            iFirmwareDownloadListener.onProgress(i, i2);
        }
    }
}
