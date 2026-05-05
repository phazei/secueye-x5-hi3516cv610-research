package com.aliyun.alink.business.devicecenter.config.model;

import android.text.TextUtils;
import android.util.Log;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class UnprovisionedGateMeshDevice {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public List<DeviceInfo> f3569a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Map<String, Boolean> f3570b = new HashMap();

    public boolean addDeviceInfo(DeviceInfo deviceInfo) {
        if (this.f3569a == null) {
            this.f3569a = new ArrayList();
        }
        if (this.f3569a.size() + 1 > 201) {
            return false;
        }
        this.f3569a.add(deviceInfo);
        return true;
    }

    public void addProvisionResult(String str, boolean z) {
        Log.d("UnprovisionedGateMesh", "addProvisionResult() called with: iotId = [" + str + "], success = [" + z + "]");
        this.f3570b.put(str, Boolean.valueOf(z));
    }

    public boolean containerDevice(String str) {
        List<DeviceInfo> list;
        if (!TextUtils.isEmpty(str) && (list = this.f3569a) != null && list.size() > 0) {
            Iterator<DeviceInfo> it = this.f3569a.iterator();
            while (it.hasNext()) {
                if (str.equals(it.next().iotId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean endProvision() {
        List<DeviceInfo> list = this.f3569a;
        return list == null || list.size() <= 0 || this.f3570b.size() == this.f3569a.size();
    }

    public List<DeviceInfo> getDeviceInfo() {
        return this.f3569a;
    }

    public DeviceInfo getDeviceInfoFromIotId(String str) {
        List<DeviceInfo> list;
        if (!TextUtils.isEmpty(str) && (list = this.f3569a) != null && list.size() > 0) {
            for (DeviceInfo deviceInfo : this.f3569a) {
                if (str.equals(deviceInfo.iotId)) {
                    return deviceInfo;
                }
            }
        }
        return null;
    }

    public List<DeviceInfo> getUnProvisionDeviceList() {
        List<DeviceInfo> list = this.f3569a;
        if (list == null || list.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (DeviceInfo deviceInfo : this.f3569a) {
            if (!this.f3570b.containsKey(deviceInfo.iotId)) {
                arrayList.add(deviceInfo);
            }
        }
        return arrayList;
    }

    public void setDeviceInfo(List<DeviceInfo> list) {
        this.f3569a = list;
    }

    public DeviceInfo getDeviceInfo(String str) {
        List<DeviceInfo> list;
        if (!TextUtils.isEmpty(str) && (list = this.f3569a) != null && list.size() > 0) {
            for (DeviceInfo deviceInfo : this.f3569a) {
                if (str.equals(deviceInfo.deviceId)) {
                    return deviceInfo;
                }
            }
        }
        return null;
    }
}
