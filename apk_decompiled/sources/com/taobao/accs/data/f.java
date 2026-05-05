package com.taobao.accs.data;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f6318a;

    f(d dVar) {
        this.f6318a = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f6318a.f6314c != null) {
            this.f6318a.f6314c.a();
        }
    }
}
