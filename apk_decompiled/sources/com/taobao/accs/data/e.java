package com.taobao.accs.data;

import com.taobao.accs.ut.monitor.TrafficsMonitor;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ TrafficsMonitor.a f6316a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ d f6317b;

    e(d dVar, TrafficsMonitor.a aVar) {
        this.f6317b = dVar;
        this.f6316a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f6317b.f6314c != null) {
            this.f6317b.f6314c.a(this.f6316a);
        }
    }
}
