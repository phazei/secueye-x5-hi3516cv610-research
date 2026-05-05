package com.aliyun.alink.business.devicecenter.discover.ble;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.ble.BleChannelClient;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.discover.annotation.DeviceDiscovery;
import com.aliyun.alink.business.devicecenter.discover.base.DiscoverChainBase;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
@DeviceDiscovery(discoveryType = {DiscoveryType.BLE_ENROLLEE_DEVICE})
public class BreezeDiscoverChain extends DiscoverChainBase {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public IDeviceDiscoveryListener f3589c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public AtomicBoolean f3590d;
    public Context e;
    public BleChannelClient f;
    public AtomicBoolean g;
    public IBleInterface.IBleScanCallback h;

    public BreezeDiscoverChain(Context context) {
        super(context);
        this.f3589c = null;
        this.f3590d = new AtomicBoolean(false);
        this.e = null;
        this.f = null;
        this.g = new AtomicBoolean(false);
        this.h = new IBleInterface.IBleScanCallback() { // from class: com.aliyun.alink.business.devicecenter.discover.ble.BreezeDiscoverChain.1
            @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleScanCallback
            public void onBLEDeviceFound(DeviceInfo deviceInfo) {
                final DiscoveryType discoveryType;
                if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.devType) || TextUtils.isEmpty(deviceInfo.productId)) {
                    ALog.d("BreezeDiscoverChain", "onBLEDeviceFound invalid device. " + deviceInfo);
                    return;
                }
                if (AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_2.equals(deviceInfo.devType)) {
                    discoveryType = DiscoveryType.BLE_ENROLLEE_DEVICE;
                } else if (AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(deviceInfo.devType)) {
                    discoveryType = DiscoveryType.COMBO_SUBTYPE_0X03_DEVICE;
                } else if (AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_4.equals(deviceInfo.devType)) {
                    discoveryType = DiscoveryType.COMBO_SUBTYPE_0X04_DEVICE;
                } else {
                    if (!AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_8.equals(deviceInfo.devType) && !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_9.equals(deviceInfo.devType) && !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_10.equals(deviceInfo.devType) && !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_11.equals(deviceInfo.devType) && !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_12.equals(deviceInfo.devType)) {
                        ALog.d("BreezeDiscoverChain", "onBLEDeviceFound invalid device subType. " + deviceInfo);
                        return;
                    }
                    discoveryType = DiscoveryType.BLE_ENROLLEE_DEVICE;
                }
                final ArrayList arrayList = new ArrayList();
                arrayList.add(deviceInfo);
                DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.ble.BreezeDiscoverChain.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (BreezeDiscoverChain.this.f3589c != null) {
                            BreezeDiscoverChain.this.f3589c.onDeviceFound(discoveryType, arrayList);
                        }
                    }
                });
            }

            @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleScanCallback
            public void onStartScan() {
            }

            @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleScanCallback
            public void onStopScan() {
            }
        };
        if (context == null) {
            ALog.w("BreezeDiscoverChain", "start ble scan with context = null, return.");
            return;
        }
        this.e = context.getApplicationContext();
        this.f = new BleChannelClient(this.e);
        this.f.init(this.e);
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

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void startDiscover(IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        this.f3589c = iDeviceDiscoveryListener;
        this.f3590d.set(true);
        if (this.g.compareAndSet(false, true)) {
            this.g.set(register("ACTION_SYSTEM_ABILITY_CHANGE"));
        }
        a();
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void stopDiscover() {
        BleChannelClient bleChannelClient = this.f;
        if (bleChannelClient != null) {
            bleChannelClient.stopScan(this.h);
        }
        if (this.g.compareAndSet(true, false)) {
            try {
                unregister();
            } catch (Exception unused) {
            }
        }
    }

    public final void a() {
        if (!PermissionCheckerUtils.isBleAvailable(this.e)) {
            ALog.w("BreezeDiscoverChain", "ble not available, donot start (ble)combo( scan, or it will do nothing for ever.");
            return;
        }
        if (!PermissionCheckerUtils.isLocationPermissionsGranted(this.e)) {
            ALog.w("BreezeDiscoverChain", "Location permission is not granted, donot start (ble)combo( scan, or it will do nothing for ever.");
            return;
        }
        if (!PermissionCheckerUtils.hasBleScanPermission(this.e)) {
            ALog.w("BreezeDiscoverChain", "android 12+ ble scan permission is not granted, donot start (ble)combo( scan, or it will crash.");
            return;
        }
        BleChannelClient bleChannelClient = this.f;
        if (bleChannelClient != null) {
            bleChannelClient.startScan(this.h);
        }
    }
}
