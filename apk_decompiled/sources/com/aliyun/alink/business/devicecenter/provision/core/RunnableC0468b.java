package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.utils.NetworkEnvironmentUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.b, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class RunnableC0468b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0469c f3692a;

    public RunnableC0468b(C0469c c0469c) {
        this.f3692a = c0469c;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.f3692a.f3694a.provisionHasStopped.get()) {
                return;
            }
            this.f3692a.f3694a.pingEnvInfo = NetworkEnvironmentUtils.ping();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
