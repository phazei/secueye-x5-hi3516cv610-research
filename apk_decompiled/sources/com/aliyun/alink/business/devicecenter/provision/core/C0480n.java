package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.n, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AlinkBroadcastConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0480n implements IDeviceInfoNotifyListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AlinkBroadcastConfigStrategy f3709a;

    public C0480n(AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy) {
        this.f3709a = alinkBroadcastConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onDeviceFound(DeviceInfo deviceInfo) {
        if (deviceInfo == null || this.f3709a.mConfigParams == null) {
            return;
        }
        if (!this.f3709a.waitForResult.get()) {
            ALog.d(AlinkBroadcastConfigStrategy.TAG, "provision finished return.");
            return;
        }
        if (!StringUtils.isEqualString(deviceInfo.productKey, this.f3709a.mConfigParams.productKey) && (!TextUtils.isEmpty(this.f3709a.mConfigParams.productKey) || TextUtils.isEmpty(this.f3709a.mConfigParams.productEncryptKey))) {
            ALog.i(AlinkBroadcastConfigStrategy.TAG, "onDeviceFound BroadCast otherDeviceInfo=" + deviceInfo);
            return;
        }
        if (!AlinkHelper.isBatchBroadcast(this.f3709a.mConfigParams) && !TextUtils.isEmpty(this.f3709a.mConfigParams.id) && !TextUtils.isEmpty(deviceInfo.mac) && !this.f3709a.mConfigParams.id.equals(AlinkHelper.getHalfMacFromMac(deviceInfo.mac))) {
            ALog.i(AlinkBroadcastConfigStrategy.TAG, "deviceId not equal to device mac. return. deviceId=" + this.f3709a.mConfigParams.id + ", mac=" + deviceInfo.mac);
            return;
        }
        if (!AlinkHelper.isBatchBroadcast(this.f3709a.mConfigParams) && !TextUtils.isEmpty(this.f3709a.mConfigParams.deviceName) && !TextUtils.isEmpty(deviceInfo.deviceName) && !this.f3709a.mConfigParams.deviceName.equals(deviceInfo.deviceName)) {
            ALog.i(AlinkBroadcastConfigStrategy.TAG, "not same device. return. deviceName=" + this.f3709a.mConfigParams.deviceName + ", FDeviceName=" + deviceInfo.deviceName);
            return;
        }
        this.f3709a.mConfigParams.deviceName = deviceInfo.deviceName;
        ALog.i(AlinkBroadcastConfigStrategy.TAG, "onDeviceFound BroadCast Provision Success.");
        PerformanceLog.trace(AlinkBroadcastConfigStrategy.TAG, "connectap");
        this.f3709a.updateCache(deviceInfo, DeviceReportTokenType.APP_TOKEN);
        this.f3709a.provisionResultCallback(deviceInfo);
        if (AlinkHelper.isBatchBroadcast(this.f3709a.mConfigParams)) {
            return;
        }
        this.f3709a.waitForResult.set(false);
        this.f3709a.stopBackupCheck();
        this.f3709a.stopConfig();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onFailure(DCErrorCode dCErrorCode) {
    }
}
