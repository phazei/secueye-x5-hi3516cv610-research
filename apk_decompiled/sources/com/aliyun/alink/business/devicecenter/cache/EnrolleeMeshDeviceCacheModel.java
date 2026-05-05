package com.aliyun.alink.business.devicecenter.cache;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.aliyun.alink.business.devicecenter.api.add.ICacheModel;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;

/* JADX INFO: loaded from: classes.dex */
public class EnrolleeMeshDeviceCacheModel implements ICacheModel {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public UnprovisionedBluetoothMeshDevice f3410a;

    public EnrolleeMeshDeviceCacheModel(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        this.f3410a = null;
        this.f3410a = unprovisionedBluetoothMeshDevice;
    }

    public static EnrolleeMeshDeviceCacheModel getEnrolleeMeshDeviceICacheModel(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        if (unprovisionedBluetoothMeshDevice == null) {
            return null;
        }
        return new EnrolleeMeshDeviceCacheModel(unprovisionedBluetoothMeshDevice);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public String getKey() {
        UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = this.f3410a;
        if (unprovisionedBluetoothMeshDevice != null) {
            return AlinkHelper.getMacFromSimpleMac(unprovisionedBluetoothMeshDevice.getAddress());
        }
        return null;
    }

    public UnprovisionedBluetoothMeshDevice getMeshDevice() {
        return this.f3410a;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = this.f3410a;
        return (unprovisionedBluetoothMeshDevice == null || TextUtils.isEmpty(unprovisionedBluetoothMeshDevice.getAddress())) ? false : true;
    }
}
