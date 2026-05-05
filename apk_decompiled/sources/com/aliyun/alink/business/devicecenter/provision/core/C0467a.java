package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.a, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0467a implements TimerUtils.ITimerCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3690a;

    public C0467a(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3690a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
    public void onTimeout() {
        if (this.f3690a.comboDeviceProvisionState == 1) {
            if (this.f3690a.subErrorCode != 0 || !this.f3690a.isIlop() || this.f3690a.mConfigParams.isInSide) {
                BreezeConfigStrategy breezeConfigStrategy = this.f3690a;
                breezeConfigStrategy.provisionFailFromBleNotify(breezeConfigStrategy.subErrorCode, "device connect ap success, but connect cloud failed.");
                return;
            }
            ALog.i(BreezeConfigStrategy.TAG, "provision success from device ble notify connect ap success until timeout.");
            if (TextUtils.isEmpty(this.f3690a.mConfigParams.productKey)) {
                BreezeConfigStrategy breezeConfigStrategy2 = this.f3690a;
                breezeConfigStrategy2.provisionFailFromBleNotify(breezeConfigStrategy2.subErrorCode, "device connect ap success, but no pk info returned.");
                return;
            } else if (TextUtils.isEmpty(this.f3690a.mConfigParams.deviceName)) {
                BreezeConfigStrategy breezeConfigStrategy3 = this.f3690a;
                breezeConfigStrategy3.provisionFailFromBleNotify(breezeConfigStrategy3.subErrorCode, "device connect ap success, but no deviceName info returned.");
                return;
            } else {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.productKey = this.f3690a.mConfigParams.productKey;
                deviceInfo.deviceName = this.f3690a.mConfigParams.deviceName;
                this.f3690a.provisionResultCallback(deviceInfo);
                return;
            }
        }
        if (this.f3690a.comboDeviceProvisionState != 2) {
            if (this.f3690a.comboDeviceProvisionState == -1) {
                BreezeConfigStrategy breezeConfigStrategy4 = this.f3690a;
                breezeConfigStrategy4.provisionFailFromBleNotify(breezeConfigStrategy4.subErrorCode, "device provision fail until timeout.");
                return;
            } else if (this.f3690a.devWiFiMFromFromBleReceivedByteBuffer != null) {
                BreezeConfigStrategy breezeConfigStrategy5 = this.f3690a;
                breezeConfigStrategy5.provisionFailFromBleNotify(breezeConfigStrategy5.subErrorCode == 0 ? this.f3690a.devSubErrorCodeFromBleReceived : this.f3690a.subErrorCode, "device provision fail until timeout, but get ");
                return;
            } else {
                this.f3690a.getBleProvisionTimeoutErrorInfo();
                this.f3690a.provisionResultCallback(null);
                return;
            }
        }
        if (this.f3690a.subErrorCode != 0 || !this.f3690a.isIlop() || this.f3690a.mConfigParams.isInSide) {
            BreezeConfigStrategy breezeConfigStrategy6 = this.f3690a;
            breezeConfigStrategy6.provisionFailFromBleNotify(breezeConfigStrategy6.subErrorCode, "device connect ap success & report token success, check cloud failed.");
            return;
        }
        ALog.i(BreezeConfigStrategy.TAG, "provision success from device ble notify connect ap success until timeout.");
        if (TextUtils.isEmpty(this.f3690a.mConfigParams.productKey)) {
            BreezeConfigStrategy breezeConfigStrategy7 = this.f3690a;
            breezeConfigStrategy7.provisionFailFromBleNotify(breezeConfigStrategy7.subErrorCode, "device connect ap success, but no pk info returned.");
        } else if (TextUtils.isEmpty(this.f3690a.mConfigParams.deviceName)) {
            BreezeConfigStrategy breezeConfigStrategy8 = this.f3690a;
            breezeConfigStrategy8.provisionFailFromBleNotify(breezeConfigStrategy8.subErrorCode, "device connect ap success, but no deviceName info returned.");
        } else {
            DeviceInfo deviceInfo2 = new DeviceInfo();
            deviceInfo2.productKey = this.f3690a.mConfigParams.productKey;
            deviceInfo2.deviceName = this.f3690a.mConfigParams.deviceName;
            this.f3690a.provisionResultCallback(deviceInfo2);
        }
    }
}
