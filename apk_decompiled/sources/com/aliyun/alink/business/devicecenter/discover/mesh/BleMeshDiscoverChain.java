package com.aliyun.alink.business.devicecenter.discover.mesh;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.alibaba.ailabs.iot.mesh.UnprovisionedMeshDeviceScanStrategy;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.biz.MeshDiscoverCallback;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.cache.EnrolleeMeshDeviceCacheModel;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.discover.annotation.DeviceDiscovery;
import com.aliyun.alink.business.devicecenter.discover.base.DiscoverChainBase;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes.dex */
@DeviceDiscovery(discoveryType = {DiscoveryType.APP_FOUND_BLE_MESH_DEVICE})
public class BleMeshDiscoverChain extends DiscoverChainBase {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Context f3624c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public ILeScanCallback f3625d;
    public int e;
    public AtomicBoolean f;
    public final List<DeviceInfo> g;
    public Future h;
    public HashMap<String, UnprovisionedBluetoothMeshDevice> i;
    public IDeviceDiscoveryListener j;
    public AtomicBoolean k;
    public AtomicBoolean l;
    public AtomicBoolean m;
    public boolean mDiscoveryViaGateway;
    public IDeviceDiscoveryListener n;

    public BleMeshDiscoverChain(Context context) {
        super(context);
        this.f3624c = null;
        this.f3625d = null;
        this.e = 120000;
        this.f = new AtomicBoolean(false);
        this.g = new LinkedList();
        this.h = null;
        this.i = new LinkedHashMap();
        this.k = new AtomicBoolean(false);
        this.l = new AtomicBoolean(false);
        this.m = new AtomicBoolean(false);
        this.n = null;
        this.mDiscoveryViaGateway = false;
        this.f3624c = context;
    }

    public void filterDispatchDevice(int i) {
        ArrayList arrayList;
        final int size;
        if (!this.k.compareAndSet(false, true)) {
            ALog.e("BleMeshDiscoverChain", "Cloud filter, current in filter process, return");
            return;
        }
        if (i > 0) {
            size = this.g.size() > i ? this.g.size() : Math.min(i, this.g.size());
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < i; i2++) {
                DeviceInfo deviceInfo = this.g.get(i2);
                HashMap map = new HashMap();
                map.put(AlinkConstants.KEY_SUB_DEVICE_ID, deviceInfo.deviceId);
                map.put(AlinkConstants.KEY_PRODUCT_ID, deviceInfo.productId);
                String strReplaceAll = deviceInfo.mac.replaceAll(":", "");
                map.put(AlinkConstants.KEY_MAC, strReplaceAll);
                map.put("rssi", Integer.valueOf(deviceInfo.rssi));
                if (deviceInfo.authFlag) {
                    map.put(AlinkConstants.KEY_AUTH_FLAG, true);
                    map.put(AlinkConstants.KEY_AUTH_DEVICE, deviceInfo.authDevice.toLowerCase());
                    map.put(AlinkConstants.KEY_RANDOM, deviceInfo.random.toLowerCase());
                    ALog.d("BleMeshDiscoverChain", "filterDispatchDevice mac = " + strReplaceAll + "; authDevice = " + deviceInfo.authDevice.toLowerCase() + "; random = " + deviceInfo.random.toLowerCase());
                }
                ALog.d("BleMeshDiscoverChain", "filterDispatchDevice rssi = " + deviceInfo.rssi);
                arrayList2.add(map);
            }
            arrayList = arrayList2;
        } else {
            arrayList = null;
            size = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cloud Filter, request size: ");
        sb.append(arrayList != null ? arrayList.size() : 0);
        sb.append(", chain: ");
        sb.append(this);
        ALog.d("BleMeshDiscoverChain", sb.toString());
        ProvisionRepositoryV2.getDiscoveredMeshDevice(null, 1, 200, Boolean.valueOf(this.f.get()), arrayList, new JSONArray(), new MeshDiscoverCallback() { // from class: com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshDiscoverChain.2
            @Override // com.aliyun.alink.business.devicecenter.biz.MeshDiscoverCallback
            public void onFailure(String str) {
                ALog.e("BleMeshDiscoverChain", "Cloud Filter, error: " + str);
                BleMeshDiscoverChain.this.f.set(false);
                BleMeshDiscoverChain.this.k.set(false);
            }

            @Override // com.aliyun.alink.business.devicecenter.biz.MeshDiscoverCallback
            public void onSuccess(JSONArray jSONArray) {
                try {
                    if (size > 0) {
                        BleMeshDiscoverChain.this.g.subList(0, size).clear();
                    }
                    BleMeshDiscoverChain.this.f.set(false);
                } catch (Exception e) {
                    ALog.w("BleMeshDiscoverChain", "getDiscoveredMeshDevice parse exception. " + e);
                }
                if (jSONArray != null && !jSONArray.isEmpty()) {
                    final ArrayList arrayList3 = new ArrayList();
                    final LinkedList linkedList = new LinkedList();
                    final LinkedList linkedList2 = new LinkedList();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Cloud Filter, response size: ");
                    sb2.append(jSONArray.size());
                    sb2.append(", chain: ");
                    sb2.append(BleMeshDiscoverChain.this);
                    ALog.d("BleMeshDiscoverChain", sb2.toString());
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Cloud Filter, mDiscoveredDevicesCache size: ");
                    sb3.append(BleMeshDiscoverChain.this.i.size());
                    ALog.d("BleMeshDiscoverChain", sb3.toString());
                    for (int i3 = 0; i3 < jSONArray.size(); i3++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i3);
                        if (jSONObject != null) {
                            DeviceInfo deviceInfo2 = new DeviceInfo();
                            String string = jSONObject.getString(AlinkConstants.KEY_DISCOVERED_SOURCE);
                            deviceInfo2.deviceName = jSONObject.getString("deviceName");
                            deviceInfo2.productKey = jSONObject.getString("productKey");
                            deviceInfo2.productId = jSONObject.getString(AlinkConstants.KEY_PRODUCT_ID);
                            deviceInfo2.mac = jSONObject.getString(AlinkConstants.KEY_MAC);
                            deviceInfo2.deviceId = jSONObject.getString(AlinkConstants.KEY_SUB_DEVICE_ID);
                            deviceInfo2.linkType = LinkType.ALI_APP_MESH.getName();
                            deviceInfo2.authFlag = jSONObject.getBooleanValue(AlinkConstants.KEY_AUTH_FLAG);
                            deviceInfo2.confirmCloud = jSONObject.getString(AlinkConstants.KEY_CONFIRM_CLOUD);
                            deviceInfo2.subDeviceId = jSONObject.getString(AlinkConstants.KEY_SUB_DEVICE_ID);
                            deviceInfo2.comboMeshFlag = AliMeshUUIDParserUtil.isComboMesh(deviceInfo2.subDeviceId);
                            if (deviceInfo2.authFlag) {
                                deviceInfo2.random = jSONObject.getString(AlinkConstants.KEY_RANDOM);
                                deviceInfo2.authDevice = jSONObject.getString(AlinkConstants.KEY_AUTH_DEVICE);
                            }
                            if (!TextUtils.isEmpty(deviceInfo2.mac) && !deviceInfo2.mac.contains(":")) {
                                deviceInfo2.mac = AlinkHelper.getMacFromSimpleMac(deviceInfo2.mac);
                            }
                            UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = null;
                            if (!TextUtils.isEmpty(deviceInfo2.deviceId) && (unprovisionedBluetoothMeshDevice = (UnprovisionedBluetoothMeshDevice) BleMeshDiscoverChain.this.i.get(deviceInfo2.deviceId.toLowerCase())) != null) {
                                deviceInfo2.setExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE, EnrolleeMeshDeviceCacheModel.getEnrolleeMeshDeviceICacheModel(unprovisionedBluetoothMeshDevice));
                            }
                            if (deviceInfo2.comboMeshFlag) {
                                deviceInfo2.linkType = LinkType.ALI_APP_COMBO_MESH.getName();
                            }
                            if (deviceInfo2.comboMeshFlag) {
                                if (!"meshGw".equalsIgnoreCase(string)) {
                                    linkedList2.add(deviceInfo2);
                                } else if (unprovisionedBluetoothMeshDevice != null) {
                                    linkedList2.add(deviceInfo2);
                                }
                            } else if ("meshGw".equals(string)) {
                                deviceInfo2.iotId = jSONObject.getString("iotId");
                                deviceInfo2.regProductKey = jSONObject.getString(AlinkConstants.KEY_GATEWAY_PRODUCT_KEY);
                                deviceInfo2.regDeviceName = jSONObject.getString(AlinkConstants.KEY_GATEWAY_DEVICE_NAME);
                                deviceInfo2.regIotId = jSONObject.getString("gatewayIotId");
                                deviceInfo2.token = jSONObject.getString("token");
                                deviceInfo2.productName = jSONObject.getString(AlinkConstants.KEY_PRODUCT_NAME);
                                deviceInfo2.image = jSONObject.getString("image");
                                deviceInfo2.netType = jSONObject.getString("netType");
                                deviceInfo2.nodeType = jSONObject.getString(AlinkConstants.KEY_NODE_TYPE);
                                deviceInfo2.categoryKey = jSONObject.getString(AlinkConstants.KEY_CATEGORY_KEY);
                                deviceInfo2.categoryName = jSONObject.getString(AlinkConstants.KEY_CATEGORY_NAME);
                                deviceInfo2.categoryId = jSONObject.getString(AlinkConstants.KEY_CATEGORY_ID);
                                deviceInfo2.linkType = LinkType.ALI_GATEWAY_MESH.getName();
                                linkedList.add(deviceInfo2);
                            } else if (unprovisionedBluetoothMeshDevice == null) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("Miss unprovisioned info: ");
                                sb4.append(deviceInfo2.deviceId);
                                sb4.append(", discard...");
                                ALog.e("BleMeshDiscoverChain", sb4.toString());
                            } else {
                                arrayList3.add(deviceInfo2);
                            }
                        }
                    }
                    DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshDiscoverChain.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (BleMeshDiscoverChain.this.j != null) {
                                if (arrayList3.size() > 0) {
                                    BleMeshDiscoverChain.this.j.onDeviceFound(DiscoveryType.APP_FOUND_BLE_MESH_DEVICE, arrayList3);
                                }
                                if (linkedList.size() > 0) {
                                    BleMeshDiscoverChain.this.j.onDeviceFound(DiscoveryType.CLOUD_BLE_MESH_DEVICE, linkedList);
                                }
                                if (linkedList2.size() > 0) {
                                    BleMeshDiscoverChain.this.j.onDeviceFound(DiscoveryType.APP_FOUND_COMBO_MESH_DEVICE, linkedList2);
                                }
                            }
                        }
                    });
                    BleMeshDiscoverChain.this.k.set(false);
                    return;
                }
                BleMeshDiscoverChain.this.k.set(false);
            }
        });
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.base.AbilityReceiver
    public void onNotify(Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction()) || !"ACTION_SYSTEM_ABILITY_CHANGE".equals(intent.getAction())) {
            return;
        }
        String stringExtra = intent.getStringExtra("bluetooth_state");
        String stringExtra2 = intent.getStringExtra("location_state");
        if ("on".equals(stringExtra)) {
            a();
        }
        if ("on".equals(stringExtra2)) {
            a();
        }
    }

    public void resetFutureTask() {
        ALog.d("BleMeshDiscoverChain", "Reset flush task");
        Future future = this.h;
        if (future != null && !future.isDone()) {
            this.h.cancel(true);
        }
        this.h = null;
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void startDiscover(IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        ALog.d("BleMeshDiscoverChain", "startDiscover() called with: listener = [" + iDeviceDiscoveryListener + "]");
        this.f.set(true);
        CacheCenter.getInstance().getDiscoveredDevices().clear();
        this.j = iDeviceDiscoveryListener;
        if (this.m.compareAndSet(false, true)) {
            this.m.set(register("ACTION_SYSTEM_ABILITY_CHANGE"));
        }
        a();
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void stopDiscover() {
        ALog.d("BleMeshDiscoverChain", "stopDiscover() called with callback=" + this.f3625d + ", hashCode=" + hashCode());
        resetFutureTask();
        this.j = null;
        CacheCenter.getInstance().getDiscoveredDevices().clear();
        try {
            if (this.f3625d != null) {
                BLEScannerProxy.getInstance().stopScan(this.f3625d);
            }
        } catch (Exception e) {
            ALog.w("BleMeshDiscoverChain", "stop scan mesh device failed, mesh sdk exception " + e);
        }
        this.l.set(false);
        if (this.m.compareAndSet(true, false)) {
            try {
                unregister();
            } catch (Exception unused) {
            }
        }
    }

    public final void a() {
        try {
            if (!PermissionCheckerUtils.isBleAvailable(this.f3624c)) {
                ALog.w("BleMeshDiscoverChain", "ble not available, donot start mesh scan, or it will do nothing for ever.");
                return;
            }
            if (!PermissionCheckerUtils.hasBleScanPermission(this.f3624c)) {
                ALog.w("BleMeshDiscoverChain", "android 12+ ble scan permission is not granted, donot start (ble)mesh( scan, or it will crash.");
                return;
            }
            if (this.l.get()) {
                return;
            }
            UnprovisionedMeshDeviceScanStrategy.getInstance().register();
            this.f3625d = new ILeScanCallback() { // from class: com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshDiscoverChain.1
                @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
                public void onAliBLEDeviceFound(BluetoothDeviceWrapper bluetoothDeviceWrapper, BluetoothDeviceSubtype bluetoothDeviceSubtype) {
                    if (bluetoothDeviceWrapper instanceof UnprovisionedBluetoothMeshDevice) {
                        String address = bluetoothDeviceWrapper.getAddress();
                        if (TextUtils.isEmpty(address)) {
                            ALog.w("BleMeshDiscoverChain", "invalid mesh mac address, empty, ignore.");
                            return;
                        }
                        UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = (UnprovisionedBluetoothMeshDevice) bluetoothDeviceWrapper;
                        int sigMeshProductID = unprovisionedBluetoothMeshDevice.getSigMeshProductID();
                        DeviceInfo deviceInfo = new DeviceInfo();
                        deviceInfo.productId = String.valueOf(sigMeshProductID);
                        deviceInfo.mac = AlinkHelper.getMacFromSimpleMac(address);
                        deviceInfo.linkType = LinkType.ALI_APP_MESH.getName();
                        deviceInfo.deviceId = MeshParserUtils.bytesToHex(unprovisionedBluetoothMeshDevice.getUUID(), false).trim().toLowerCase();
                        if (DCEnvHelper.isTgEnv() && !BleMeshDiscoverChain.this.mDiscoveryViaGateway && "0".equals(unprovisionedBluetoothMeshDevice.getAuthFlag())) {
                            deviceInfo.setExtraDeviceInfo(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE, EnrolleeMeshDeviceCacheModel.getEnrolleeMeshDeviceICacheModel(unprovisionedBluetoothMeshDevice));
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(deviceInfo);
                            if (BleMeshDiscoverChain.this.j != null) {
                                BleMeshDiscoverChain.this.j.onDeviceFound(DiscoveryType.APP_FOUND_BLE_MESH_DEVICE, arrayList);
                                return;
                            }
                            return;
                        }
                        byte[] uuid = unprovisionedBluetoothMeshDevice.getUUID();
                        if (uuid != null && uuid.length > 0) {
                            if ("1".equals(unprovisionedBluetoothMeshDevice.getAuthFlag())) {
                                deviceInfo.authFlag = true;
                                deviceInfo.authDevice = unprovisionedBluetoothMeshDevice.getAuthDev();
                                deviceInfo.random = unprovisionedBluetoothMeshDevice.getRandom();
                                deviceInfo.rssi = unprovisionedBluetoothMeshDevice.getMeshScanResult().getRssi();
                            }
                            if (CacheCenter.getInstance().getDiscoveredDevices().contains(deviceInfo.deviceId)) {
                                ALog.d("BleMeshDiscoverChain", "mDiscoveredDevices contains deviceId=" + deviceInfo.deviceId);
                                return;
                            }
                            CacheCenter.getInstance().getDiscoveredDevices().add(deviceInfo.deviceId);
                            ALog.d("BleMeshDiscoverChain", "Put cache: " + bluetoothDeviceWrapper + " for device: " + deviceInfo.deviceId);
                            BleMeshDiscoverChain.this.i.put(deviceInfo.deviceId, unprovisionedBluetoothMeshDevice);
                            CacheCenter.getInstance().updateCache(CacheType.BLE_MESH_DISCOVERED_DEVICE, EnrolleeMeshDeviceCacheModel.getEnrolleeMeshDeviceICacheModel(unprovisionedBluetoothMeshDevice));
                            ALog.i("BleMeshDiscoverChain", "Filter: " + deviceInfo.deviceId);
                            BleMeshDiscoverChain.this.g.add(deviceInfo);
                        }
                        BleMeshDiscoverChain bleMeshDiscoverChain = BleMeshDiscoverChain.this;
                        if (bleMeshDiscoverChain.h == null) {
                            bleMeshDiscoverChain.b();
                        }
                        if (BleMeshDiscoverChain.this.g.size() >= 5) {
                            BleMeshDiscoverChain bleMeshDiscoverChain2 = BleMeshDiscoverChain.this;
                            bleMeshDiscoverChain2.filterDispatchDevice(bleMeshDiscoverChain2.g.size());
                        }
                    }
                }

                @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
                public void onStartScan() {
                    ALog.d("BleMeshDiscoverChain", "onStartScan mesh ");
                }

                @Override // com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback
                public void onStopScan() {
                }
            };
            StringBuilder sb = new StringBuilder();
            sb.append("startLeScan with callback=");
            sb.append(this.f3625d);
            sb.append(", hashCode=");
            sb.append(hashCode());
            ALog.d("BleMeshDiscoverChain", sb.toString());
            BLEScannerProxy.getInstance().startLeScan(this.f3624c, this.e, true, UnprovisionedBluetoothMeshDevice.GENIE_SIGMESH, this.f3625d);
            if (this.mDiscoveryViaGateway) {
                this.l.set(BLEScannerProxy.getInstance().checkIfInScanning());
                b();
            }
        } catch (Exception e) {
            ALog.w("BleMeshDiscoverChain", "scan mesh device failed, mesh sdk exception " + e);
        }
    }

    public final void b() {
        resetFutureTask();
        this.h = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshDiscoverChain.3
            @Override // java.lang.Runnable
            public void run() {
                ALog.d("BleMeshDiscoverChain", "Prepare flush");
                BleMeshDiscoverChain bleMeshDiscoverChain = BleMeshDiscoverChain.this;
                bleMeshDiscoverChain.filterDispatchDevice(bleMeshDiscoverChain.g.size());
            }
        }, 0L, 3L, TimeUnit.SECONDS);
    }
}
