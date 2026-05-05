package com.aliyun.alink.business.devicecenter.provision.other;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.other.zero.BatchZeroConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: compiled from: BatchZeroConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class q implements IDeviceInfoNotifyListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BatchZeroConfigStrategy f3746a;

    public q(BatchZeroConfigStrategy batchZeroConfigStrategy) {
        this.f3746a = batchZeroConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onDeviceFound(DeviceInfo deviceInfo) {
        if (deviceInfo == null || this.f3746a.mConfigParams == null) {
            return;
        }
        if (!this.f3746a.waitForResult.get()) {
            ALog.d(BatchZeroConfigStrategy.TAG, "provision finished return.");
            return;
        }
        if (!StringUtils.isEqualString(deviceInfo.productKey, this.f3746a.mConfigParams.productKey)) {
            ALog.i(BatchZeroConfigStrategy.TAG, "onDeviceFound batch Zero otherDeviceInfo=" + deviceInfo);
            return;
        }
        ALog.i(BatchZeroConfigStrategy.TAG, "onDeviceFound batch Zero Provision Success.");
        if (StringUtils.isEqualString(deviceInfo.productKey, this.f3746a.mConfigParams.regProductKey) && StringUtils.isEqualString(deviceInfo.deviceName, this.f3746a.mConfigParams.regDeviceName)) {
            ALog.d(BatchZeroConfigStrategy.TAG, "onDeviceFound batch Zero, find provisioned device, return.");
            return;
        }
        PerformanceLog.trace(BatchZeroConfigStrategy.TAG, "connectap");
        BatchZeroConfigStrategy batchZeroConfigStrategy = this.f3746a;
        batchZeroConfigStrategy.updateCache(deviceInfo, batchZeroConfigStrategy.deviceReportTokenType);
        String str = deviceInfo.productKey + "&&" + deviceInfo.deviceName;
        deviceInfo.regProductKey = this.f3746a.mConfigParams.regProductKey;
        deviceInfo.regDeviceName = this.f3746a.mConfigParams.regDeviceName;
        if (this.f3746a.cacheCallbackMap.containsKey(str) || !this.f3746a.batchDeviceSuccess(deviceInfo.productKey, deviceInfo.deviceName)) {
            ALog.d(BatchZeroConfigStrategy.TAG, "cacheCallbackMap contains " + str);
        } else {
            ALog.d(BatchZeroConfigStrategy.TAG, "cacheCallbackMap not contain " + str);
            this.f3746a.cacheCallbackMap.put(str, true);
            this.f3746a.provisionResultCallback(deviceInfo);
        }
        if (this.f3746a.batchEnrolleeDeviceList == null || this.f3746a.cacheCallbackMap.size() != this.f3746a.batchEnrolleeDeviceList.size()) {
            return;
        }
        this.f3746a.waitForResult.set(false);
        this.f3746a.stopConfig();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onFailure(DCErrorCode dCErrorCode) {
    }
}
