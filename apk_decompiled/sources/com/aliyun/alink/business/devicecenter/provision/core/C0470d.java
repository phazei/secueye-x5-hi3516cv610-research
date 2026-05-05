package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigState;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.d, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0470d implements TimerUtils.ITimerCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3695a;

    public C0470d(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3695a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
    public void onTimeout() {
        if (this.f3695a.provisionHasStopped.get()) {
            return;
        }
        ALog.d(BreezeConfigStrategy.TAG, "breezeConfigState=" + this.f3695a.breezeConfigState + ", hasNotifiedScanTimeout=" + this.f3695a.hasNotifiedScanTimeout);
        if (this.f3695a.breezeConfigState != BreezeConfigState.BLE_SCANNING || this.f3695a.hasNotifiedScanTimeout.get()) {
            ALog.i(BreezeConfigStrategy.TAG, "startConfig scan->onTimeout breezeConfigState=" + this.f3695a.breezeConfigState);
            return;
        }
        this.f3695a.hasNotifiedScanTimeout.set(true);
        ALog.i(BreezeConfigStrategy.TAG, "startConfig scan->onTimeout scan target device > 10S.");
        ProvisionStatus provisionStatus = ProvisionStatus.BLE_DEVICE_SCAN_NO_RESULT;
        provisionStatus.setMessage("scan target ble device more than 10S, but no result.");
        this.f3695a.provisionStatusCallback(provisionStatus);
    }
}
