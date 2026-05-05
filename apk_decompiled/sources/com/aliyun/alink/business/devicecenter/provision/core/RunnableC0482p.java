package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.p, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AlinkBroadcastConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class RunnableC0482p implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IConfigCallback f3711a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AlinkBroadcastConfigStrategy f3712b;

    public RunnableC0482p(AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy, IConfigCallback iConfigCallback) {
        this.f3712b = alinkBroadcastConfigStrategy;
        this.f3711a = iConfigCallback;
    }

    @Override // java.lang.Runnable
    public void run() {
        AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy = this.f3712b;
        alinkBroadcastConfigStrategy.cancelRequest(alinkBroadcastConfigStrategy.retryTransitoryClient);
        AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy2 = this.f3712b;
        alinkBroadcastConfigStrategy2.retryTransitoryClient = ProvisionRepository.getCipher(alinkBroadcastConfigStrategy2.mConfigParams.productKey, this.f3712b.mConfigParams.deviceName, "00000000000000000000000000000000", null, new C0481o(this));
    }
}
