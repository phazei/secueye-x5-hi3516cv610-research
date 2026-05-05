package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigState;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.e, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0471e implements IDeviceInfoNotifyListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3696a;

    public C0471e(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3696a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onDeviceFound(DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return;
        }
        if (!this.f3696a.waitForResult.get()) {
            ALog.d(BreezeConfigStrategy.TAG, "provision finished return.");
            return;
        }
        if (this.f3696a.mConfigParams == null) {
            ALog.d(BreezeConfigStrategy.TAG, "provision params is null. return.");
            return;
        }
        if (!this.f3696a.hasBleEverConnectedAB.get()) {
            ALog.d(BreezeConfigStrategy.TAG, "provision ble never connected, return.");
            return;
        }
        if ((!TextUtils.isEmpty(this.f3696a.mConfigParams.productKey) && !this.f3696a.mConfigParams.productKey.equals(deviceInfo.productKey)) || (!TextUtils.isEmpty(this.f3696a.mConfigParams.deviceName) && !StringUtils.isEqualString(deviceInfo.deviceName, this.f3696a.mConfigParams.deviceName))) {
            ALog.w(BreezeConfigStrategy.TAG, "onDeviceFound Breeze otherDeviceInfo=" + deviceInfo);
            return;
        }
        ALog.d(BreezeConfigStrategy.TAG, "onDeviceFound Breeze Provision Success.");
        if (TextUtils.isEmpty(deviceInfo.token)) {
            PerformanceLog.trace(BreezeConfigStrategy.TAG, "connectap");
        } else {
            PerformanceLog.trace(BreezeConfigStrategy.TAG, "device_info_notify");
        }
        BreezeConfigStrategy breezeConfigStrategy = this.f3696a;
        breezeConfigStrategy.updateCache(deviceInfo, breezeConfigStrategy.deviceReportTokenType);
        this.f3696a.waitForResult.set(false);
        this.f3696a.stopBackupCheck();
        this.f3696a.updateProvisionState(BreezeConfigState.BLE_SUCCESS);
        this.f3696a.provisionResultCallback(deviceInfo);
        this.f3696a.stopConfig();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onFailure(DCErrorCode dCErrorCode) {
        if (String.valueOf(DCErrorCode.PF_PROVISION_INSIDE_BIND_ERROR).equals(dCErrorCode.code)) {
            this.f3696a.provisionStatusCallback(ProvisionStatus.BLE_DEVICE_CONNECTED_CLOUD);
        } else {
            this.f3696a.provisionErrorInfo = dCErrorCode;
            this.f3696a.provisionResultCallback(null);
            this.f3696a.stopConfig();
        }
    }
}
