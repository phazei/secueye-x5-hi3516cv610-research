package com.taobao.accs.net;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class y implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ w f6415a;

    y(w wVar) {
        this.f6415a = wVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6415a.q();
        if (this.f6415a.H != null) {
            this.f6415a.H.setCloseReason("shut down");
        }
        synchronized (this.f6415a.t) {
            try {
                this.f6415a.t.notifyAll();
            } catch (Exception unused) {
            }
        }
    }
}
