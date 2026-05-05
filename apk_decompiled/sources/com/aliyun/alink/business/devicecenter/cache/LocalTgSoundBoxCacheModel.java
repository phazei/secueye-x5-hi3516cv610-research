package com.aliyun.alink.business.devicecenter.cache;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.bluetoothlesdk.SmartSpeakerBLEDevice;
import com.aliyun.alink.business.devicecenter.api.add.ICacheModel;

/* JADX INFO: loaded from: classes.dex */
public class LocalTgSoundBoxCacheModel implements ICacheModel {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public SmartSpeakerBLEDevice f3413a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BluetoothDeviceSubtype f3414b;

    public LocalTgSoundBoxCacheModel(SmartSpeakerBLEDevice smartSpeakerBLEDevice, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
        this.f3413a = smartSpeakerBLEDevice;
        this.f3414b = bluetoothDeviceSubtype;
    }

    public BluetoothDeviceSubtype getBluetoothDeviceSubtype() {
        return this.f3414b;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public String getKey() {
        return this.f3413a.getWifiMacAddress().toLowerCase();
    }

    public SmartSpeakerBLEDevice getSpeakerBLEDevice() {
        return this.f3413a;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        return !TextUtils.isEmpty(this.f3413a.getWifiMacAddress());
    }

    public void setBluetoothDeviceSubtype(BluetoothDeviceSubtype bluetoothDeviceSubtype) {
        this.f3414b = bluetoothDeviceSubtype;
    }

    public void setSpeakerBLEDevice(SmartSpeakerBLEDevice smartSpeakerBLEDevice) {
        this.f3413a = smartSpeakerBLEDevice;
    }
}
