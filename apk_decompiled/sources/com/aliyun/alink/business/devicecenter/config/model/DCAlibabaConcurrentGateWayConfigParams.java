package com.aliyun.alink.business.devicecenter.config.model;

import android.util.Log;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DCAlibabaConcurrentGateWayConfigParams extends DCConfigParams {
    public static final int MAX_BATCH_COUNT = 201;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f3568a;
    public int deviceCount = 0;
    public List<UnprovisionedGateMeshDevice> mDeviceInfoList;

    public void addDevice(DeviceInfo deviceInfo) {
        UnprovisionedGateMeshDevice unprovisionedGateMeshDevice;
        Log.d("DCConfigParams", "addDevice() called with: deviceInfo = [" + deviceInfo + "]");
        this.deviceCount = this.deviceCount + 1;
        if (this.mDeviceInfoList == null) {
            this.mDeviceInfoList = new ArrayList();
        }
        if (this.mDeviceInfoList.size() == 0) {
            unprovisionedGateMeshDevice = new UnprovisionedGateMeshDevice();
            this.mDeviceInfoList.add(unprovisionedGateMeshDevice);
        } else {
            unprovisionedGateMeshDevice = this.mDeviceInfoList.get(r0.size() - 1);
        }
        if (unprovisionedGateMeshDevice.addDeviceInfo(deviceInfo)) {
            return;
        }
        UnprovisionedGateMeshDevice unprovisionedGateMeshDevice2 = new UnprovisionedGateMeshDevice();
        unprovisionedGateMeshDevice2.addDeviceInfo(deviceInfo);
        this.mDeviceInfoList.add(unprovisionedGateMeshDevice2);
    }

    public List<DeviceInfo> getAllDevice() {
        List<UnprovisionedGateMeshDevice> list = this.mDeviceInfoList;
        if (list == null || list.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (UnprovisionedGateMeshDevice unprovisionedGateMeshDevice : this.mDeviceInfoList) {
            if (unprovisionedGateMeshDevice != null && unprovisionedGateMeshDevice.getDeviceInfo() != null && unprovisionedGateMeshDevice.getDeviceInfo().size() > 0) {
                arrayList.addAll(unprovisionedGateMeshDevice.getDeviceInfo());
            }
        }
        return arrayList;
    }

    public String getGatewayIotId() {
        return this.f3568a;
    }

    public void setGatewayIotId(String str) {
        Log.d("DCConfigParams", "setGatewayIotId() called with: gatewayIotId = [" + str + "]");
        this.f3568a = str;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.model.DCConfigParams
    public String toString() {
        return "DCAlibabaConcurrentGateWayConfigParams{mDeviceInfoList=" + this.mDeviceInfoList + '}';
    }

    public void addDevice(List<DeviceInfo> list, int i) {
        if (list == null) {
            return;
        }
        Log.d("DCConfigParams", "addDevice: " + list.size() + "serialExecuteIndex=" + i);
        if (i < this.mDeviceInfoList.size() - 1) {
            Iterator<DeviceInfo> it = list.iterator();
            while (it.hasNext()) {
                addDevice(it.next());
            }
        } else {
            this.mDeviceInfoList.add(new UnprovisionedGateMeshDevice());
            Iterator<DeviceInfo> it2 = list.iterator();
            while (it2.hasNext()) {
                addDevice(it2.next());
            }
        }
    }
}
