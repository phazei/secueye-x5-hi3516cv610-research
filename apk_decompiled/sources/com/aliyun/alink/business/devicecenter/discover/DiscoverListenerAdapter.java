package com.aliyun.alink.business.devicecenter.discover;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.cache.EnrolleeMeshDeviceCacheModel;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DiscoverListenerAdapter implements IDeviceDiscoveryListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IDeviceDiscoveryListener f3580a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Object f3581b = new Object();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ArrayList<DeviceInfo> f3582c;

    public DiscoverListenerAdapter(IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        this.f3580a = null;
        this.f3582c = null;
        this.f3580a = iDeviceDiscoveryListener;
        this.f3582c = new ArrayList<>();
    }

    public void clear() {
        ArrayList<DeviceInfo> arrayList = this.f3582c;
        if (arrayList != null) {
            arrayList.clear();
        }
        if (CacheCenter.getInstance().getDiscoveredMeshDevices() != null) {
            CacheCenter.getInstance().getDiscoveredMeshDevices().clear();
        }
    }

    public void destroy() {
        clear();
        this.f3580a = null;
        this.f3582c = null;
    }

    public ArrayList<DeviceInfo> getLanDevices() {
        return this.f3582c;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener
    public void onDeviceFound(final DiscoveryType discoveryType, List<DeviceInfo> list) {
        final List<DeviceInfo> listA = a(discoveryType, list);
        if (listA == null || listA.size() < 1) {
            return;
        }
        ALog.d("DiscoverListenerAdapter", "onDeviceFound type=" + discoveryType + ", to app deviceInfoList=" + listA);
        ThreadTools.runOnUiThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.DiscoverListenerAdapter.1
            @Override // java.lang.Runnable
            public void run() {
                if (DiscoverListenerAdapter.this.f3580a != null) {
                    DiscoverListenerAdapter.this.f3580a.onDeviceFound(discoveryType, listA);
                }
            }
        });
    }

    public final List<DeviceInfo> a(DiscoveryType discoveryType, List<DeviceInfo> list) {
        if (list == null || list.size() < 1) {
            ALog.d("DiscoverListenerAdapter", "deviceInfoList empty, return.");
            return null;
        }
        if (this.f3582c == null) {
            ALog.w("DiscoverListenerAdapter", "discover stopped, return.");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (discoveryType == DiscoveryType.CLOUD_BLE_MESH_DEVICE || discoveryType == DiscoveryType.APP_FOUND_BLE_MESH_DEVICE || discoveryType == DiscoveryType.APP_FOUND_COMBO_MESH_DEVICE) {
            for (DeviceInfo deviceInfo : list) {
                String meshDeviceUniqueIDByDeviceInfo = DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo);
                if (!CacheCenter.getInstance().getDiscoveredMeshDevices().contains(meshDeviceUniqueIDByDeviceInfo)) {
                    CacheCenter.getInstance().getDiscoveredMeshDevices().add(meshDeviceUniqueIDByDeviceInfo);
                    arrayList.add(deviceInfo);
                    this.f3582c.add(deviceInfo);
                    CacheCenter.getInstance().updateCache(CacheType.BLE_MESH_DISCOVERED_DEVICE, deviceInfo.getExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE));
                }
            }
            return arrayList;
        }
        for (int i = 0; i < list.size(); i++) {
            try {
                DeviceInfo deviceInfo2 = list.get(i);
                if (deviceInfo2 != null) {
                    if (deviceInfo2.isValid() || !TextUtils.isEmpty(String.valueOf(deviceInfo2.getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID)))) {
                        if (this.f3582c == null) {
                            break;
                        }
                        synchronized (this.f3581b) {
                            if (this.f3582c.contains(deviceInfo2)) {
                                int size = this.f3582c.size() - 1;
                                while (true) {
                                    if (size <= -1 || this.f3582c == null) {
                                        break;
                                    }
                                    DeviceInfo deviceInfo3 = this.f3582c.get(size);
                                    if (!deviceInfo2.equals(deviceInfo3)) {
                                        size--;
                                    } else if (!TextUtils.isEmpty(deviceInfo2.token) || !TextUtils.isEmpty(deviceInfo3.token)) {
                                        if (!TextUtils.isEmpty(deviceInfo2.token) && !TextUtils.isEmpty(deviceInfo3.token)) {
                                            deviceInfo3.tag = Long.valueOf(System.currentTimeMillis());
                                        } else if ((TextUtils.isEmpty(deviceInfo2.token) || !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_4.equals(deviceInfo3.devType)) && ((TextUtils.isEmpty(deviceInfo3.token) || !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_4.equals(deviceInfo2.devType)) && ((!TextUtils.isEmpty(deviceInfo2.token) || a(deviceInfo3.tag)) && !deviceInfo2.isSameApWithNoPk(deviceInfo3)))) {
                                            if (!TextUtils.isEmpty(deviceInfo2.token)) {
                                                deviceInfo2.tag = Long.valueOf(System.currentTimeMillis());
                                            }
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("replace callback device ");
                                            sb.append(deviceInfo3);
                                            sb.append(", to ");
                                            sb.append(deviceInfo2);
                                            ALog.d("DiscoverListenerAdapter", sb.toString());
                                            this.f3582c.remove(deviceInfo3);
                                            this.f3582c.add(deviceInfo2);
                                            arrayList.add(deviceInfo2.copy());
                                            if (discoveryType == DiscoveryType.APP_FOUND_BLE_MESH_DEVICE && (deviceInfo2.getExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE) instanceof EnrolleeMeshDeviceCacheModel)) {
                                                CacheCenter.getInstance().updateCache(CacheType.BLE_MESH_DISCOVERED_DEVICE, deviceInfo2.getExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE));
                                            }
                                        }
                                    }
                                }
                            } else {
                                deviceInfo2.tag = Long.valueOf(System.currentTimeMillis());
                                this.f3582c.add(deviceInfo2);
                                arrayList.add(deviceInfo2.copy());
                                if (discoveryType == DiscoveryType.APP_FOUND_BLE_MESH_DEVICE && (deviceInfo2.getExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE) instanceof EnrolleeMeshDeviceCacheModel)) {
                                    CacheCenter.getInstance().updateCache(CacheType.BLE_MESH_DISCOVERED_DEVICE, deviceInfo2.getExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE));
                                }
                            }
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("deviceInfo invalid, continue. i=");
                        sb2.append(i);
                        ALog.w("DiscoverListenerAdapter", sb2.toString());
                    }
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("deviceInfo null, continue. i=");
                    sb3.append(i);
                    ALog.w("DiscoverListenerAdapter", sb3.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public final boolean a(Object obj) {
        if (obj instanceof Long) {
            return System.currentTimeMillis() > ((Long) obj).longValue() + 5000;
        }
        return false;
    }
}
