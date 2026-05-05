package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;

/* JADX INFO: renamed from: a.a.a.a.b.i.d, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0338d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1417a;

    public RunnableC0338d(FastProvisionManager fastProvisionManager) {
        this.f1417a = fastProvisionManager;
    }

    @Override // java.lang.Runnable
    public void run() {
        a.a.a.a.b.m.a.a(FastProvisionManager.TAG, "provision success, delay stop scan");
        this.f1417a.stopScan();
    }
}
