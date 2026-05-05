package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.t, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AlinkBroadcastConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class RunnableC0486t implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AlinkBroadcastConfigStrategy f3717a;

    public RunnableC0486t(AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy) {
        this.f3717a = alinkBroadcastConfigStrategy;
    }

    @Override // java.lang.Runnable
    public void run() {
        ALog.d(AlinkBroadcastConfigStrategy.TAG, "startP2PThread run!");
        try {
            if (this.f3717a.provisionHasStopped.get()) {
                ALog.d(AlinkBroadcastConfigStrategy.TAG, "provision has stopped, ignore p2p send");
                return;
            }
            if (!AlinkHelper.isBatchBroadcast(this.f3717a.mConfigParams) && this.f3717a.delayBroadcastTimeAI.get() > 0) {
                Thread.sleep(this.f3717a.delayBroadcastTimeAI.get());
            }
            ALog.d(AlinkBroadcastConfigStrategy.TAG, "start send p2p.");
            if (this.f3717a.provisionHasStopped.get() || this.f3717a.mP2PProvision == null) {
                return;
            }
            this.f3717a.mP2PProvision.a(this.f3717a.mConfigParams);
        } catch (Exception e) {
            ALog.d(AlinkBroadcastConfigStrategy.TAG, "send start p2p send e= " + e);
        }
    }
}
