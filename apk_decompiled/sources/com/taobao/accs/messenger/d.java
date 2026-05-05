package com.taobao.accs.messenger;

import android.content.Intent;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f6356a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ Intent f6357b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ c f6358c;

    d(c cVar, String str, Intent intent) {
        this.f6358c = cVar;
        this.f6356a = str;
        this.f6357b = intent;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6358c.b(this.f6356a, this.f6357b);
    }
}
