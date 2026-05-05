package com.taobao.accs.internal;

import com.taobao.accs.ConnectionListener;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class b implements ConnectionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f6337a;

    @Override // com.taobao.accs.ConnectionListener
    public void onDisconnect(int i, String str) {
    }

    b(a aVar) {
        this.f6337a = aVar;
    }

    @Override // com.taobao.accs.ConnectionListener
    public void onConnect() {
        if (this.f6337a.f6336c.f6327a.j().isAppUnbinded(this.f6337a.f6335b.getPackageName()) && this.f6337a.f6336c.f6329c) {
            this.f6337a.f6336c.a(this.f6337a.f6335b, this.f6337a.f6336c.f6327a.f6364b, this.f6337a.f6336c.f6327a.f6363a);
        }
    }
}
