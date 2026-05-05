package com.aliyun.alink.business.devicecenter.cache;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.aliyun.alink.business.devicecenter.api.add.ICacheModel;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public class EnrolleeTgBleDeviceCacheModel implements ICacheModel {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public BluetoothDeviceWrapper f3411a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BluetoothDeviceSubtype f3412b;

    public EnrolleeTgBleDeviceCacheModel(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
        this.f3411a = bluetoothDeviceWrapper;
        this.f3412b = bluetoothDeviceSubtype;
    }

    public static String getMacAddressWithColon(BluetoothDeviceWrapper bluetoothDeviceWrapper) {
        if (bluetoothDeviceWrapper == null) {
            return null;
        }
        if (bluetoothDeviceWrapper.getAddress() != null) {
            return bluetoothDeviceWrapper.getAddress().toLowerCase();
        }
        if (bluetoothDeviceWrapper.getAisManufactureDataADV() == null) {
            return null;
        }
        String strBytesToHexString = StringUtils.bytesToHexString(bluetoothDeviceWrapper.getAisManufactureDataADV().getMacAddress());
        if (TextUtils.isEmpty(strBytesToHexString)) {
            return null;
        }
        return AlinkHelper.getMacFromSimpleMac(strBytesToHexString.toLowerCase());
    }

    public BluetoothDeviceWrapper getBluetoothDeviceWrapper() {
        return this.f3411a;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public String getKey() {
        return getMacAddressWithColon(this.f3411a);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        return !TextUtils.isEmpty(getMacAddressWithColon(this.f3411a));
    }
}
