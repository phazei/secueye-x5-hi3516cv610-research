package com.aliyun.alink.business.devicecenter.provision.other;

import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftAPConfigStrategy;

/* JADX INFO: compiled from: SoftAPConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SoftAPConfigStrategy f3734a;

    public f(SoftAPConfigStrategy softAPConfigStrategy) {
        this.f3734a = softAPConfigStrategy;
    }

    @Override // java.lang.Runnable
    public void run() {
        SoftAPConfigStrategy softAPConfigStrategy = this.f3734a;
        softAPConfigStrategy.cancelRequest(softAPConfigStrategy.retryTransitoryClient);
        SoftAPConfigStrategy softAPConfigStrategy2 = this.f3734a;
        softAPConfigStrategy2.retryTransitoryClient = ProvisionRepository.getCipher(softAPConfigStrategy2.mConfigParams.productKey, this.f3734a.mConfigParams.deviceName, this.f3734a.mRandom, null, new e(this));
    }
}
