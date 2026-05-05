package com.aliyun.alink.business.devicecenter.provision.other;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.other.zero.AlinkZeroConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: compiled from: AlinkZeroConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class n implements IDeviceInfoNotifyListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AlinkZeroConfigStrategy f3743a;

    public n(AlinkZeroConfigStrategy alinkZeroConfigStrategy) {
        this.f3743a = alinkZeroConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onDeviceFound(DeviceInfo deviceInfo) {
        if (deviceInfo == null || this.f3743a.mConfigParams == null) {
            return;
        }
        if (!this.f3743a.waitForResult.get()) {
            ALog.d(AlinkZeroConfigStrategy.TAG, "provision finished return.");
            return;
        }
        if (!StringUtils.isEqualString(deviceInfo.productKey, this.f3743a.mConfigParams.productKey) || !StringUtils.isEqualString(deviceInfo.deviceName, this.f3743a.mConfigParams.deviceName)) {
            ALog.i(AlinkZeroConfigStrategy.TAG, "onDeviceFound Zero otherDeviceInfo=" + deviceInfo);
            return;
        }
        ALog.i(AlinkZeroConfigStrategy.TAG, "onDeviceFound Zero Provision Success.");
        PerformanceLog.trace(AlinkZeroConfigStrategy.TAG, "connectap");
        this.f3743a.updateCache(deviceInfo, DeviceReportTokenType.APP_TOKEN);
        this.f3743a.waitForResult.set(false);
        this.f3743a.stopBackupCheck();
        this.f3743a.provisionResultCallback(deviceInfo);
        this.f3743a.stopConfig();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onFailure(DCErrorCode dCErrorCode) {
    }
}
