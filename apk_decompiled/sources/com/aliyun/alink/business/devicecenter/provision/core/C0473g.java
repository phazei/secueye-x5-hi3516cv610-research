package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProtocolVersion;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.g, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0473g implements IBleInterface.IBleScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3698a;

    public C0473g(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3698a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleScanCallback
    public void onBLEDeviceFound(DeviceInfo deviceInfo) {
        if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.productId) || TextUtils.isEmpty(deviceInfo.mac)) {
            return;
        }
        String str = deviceInfo.mac;
        String str2 = deviceInfo.productId;
        if (!TextUtils.isEmpty(this.f3698a.mConfigParams.mac) && !this.f3698a.mConfigParams.mac.equals(str)) {
            ALog.w(BreezeConfigStrategy.TAG, "mac not match, toProvisionMac=" + this.f3698a.mConfigParams.mac + ",foundMacWithColon=" + str);
            return;
        }
        if (!TextUtils.isEmpty(this.f3698a.mConfigParams.productId) && !this.f3698a.mConfigParams.productId.equals(str2)) {
            ALog.w(BreezeConfigStrategy.TAG, "1.0 productId not match, toProvisionPI=" + this.f3698a.mConfigParams.productId + ",foundPI=" + str2);
            return;
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_PI, this.f3698a.mConfigParams.productId);
        ALog.d(BreezeConfigStrategy.TAG, "needBreezeScan=" + this.f3698a.needBreezeScan);
        if (this.f3698a.needBreezeScan.get()) {
            ALog.i(BreezeConfigStrategy.TAG, "onLeScan find match device, breeze state=onLeScanMatch.");
            this.f3698a.mConfigParams.devType = deviceInfo.devType;
            this.f3698a.needBreezeScan.set(false);
            if (ProtocolVersion.NO_PRODUCT.getVersion().equals(this.f3698a.mConfigParams.protocolVersion)) {
                ALog.i(BreezeConfigStrategy.TAG, "No product version. set productId = " + str2);
                this.f3698a.mConfigParams.productId = str2;
            }
            if (TextUtils.isEmpty(this.f3698a.mConfigParams.productId)) {
                ALog.i(BreezeConfigStrategy.TAG, "1.0 product version. mac equal, set productId = " + str2);
                this.f3698a.mConfigParams.productId = str2;
            }
            if (TextUtils.isEmpty(this.f3698a.mConfigParams.productKey)) {
                DeviceInfoUtils.pidReturnToPk(this.f3698a.mConfigParams.productId, new C0472f(this));
            }
            this.f3698a.stopScanNotifyTimer();
            this.f3698a.comboDeviceMac = str;
            ProvisionStatus provisionStatus = ProvisionStatus.BLE_DEVICE_SCAN_SUCCESS;
            provisionStatus.setMessage("scan target ble device success.");
            provisionStatus.addExtraParams(AlinkConstants.KEY_DEV_TYPE, this.f3698a.mConfigParams.devType);
            provisionStatus.addExtraParams(AlinkConstants.KEY_BLE_MAC, this.f3698a.comboDeviceMac);
            provisionStatus.addExtraParams(AlinkConstants.KEY_PRODUCT_ID, this.f3698a.mConfigParams.productId);
            this.f3698a.provisionStatusCallback(provisionStatus);
            DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_SCAN, String.valueOf(System.currentTimeMillis()));
            ALog.i(BreezeConfigStrategy.TAG, "onLeScan breeze state=stopLeScan.");
            this.f3698a.mBleChannelClient.stopScan(this.f3698a.bleScanCallback);
            if (AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(this.f3698a.mConfigParams.devType)) {
                ALog.i(BreezeConfigStrategy.TAG, "wait for user to call continueConfig interface.");
            } else {
                this.f3698a.getCloudToken();
            }
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleScanCallback
    public void onStartScan() {
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleScanCallback
    public void onStopScan() {
    }
}
