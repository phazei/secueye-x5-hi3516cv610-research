package com.aliyun.alink.business.devicecenter.provision.other;

import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftAPConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;

/* JADX INFO: compiled from: SoftAPConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class l implements TimerUtils.ITimerCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SoftAPConfigStrategy f3740a;

    public l(SoftAPConfigStrategy softAPConfigStrategy) {
        this.f3740a = softAPConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
    public void onTimeout() {
        if (this.f3740a.provisionHasStopped.get()) {
            return;
        }
        this.f3740a.getSofApProvisionTimeoutErrorInfo();
    }
}
