package com.taobao.accs.net;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class z implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f6416a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ w f6417b;

    z(w wVar, String str) {
        this.f6417b = wVar;
        this.f6416a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str = this.f6416a;
        if (str != null && str.equals(this.f6417b.N) && this.f6417b.s == 2) {
            this.f6417b.J = false;
            this.f6417b.L = true;
            this.f6417b.q();
            this.f6417b.H.setCloseReason("conn timeout");
        }
    }
}
