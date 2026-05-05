package com.taobao.accs.data;

import android.content.Context;
import android.content.Intent;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
final class h implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Context f6320a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ com.taobao.accs.net.b f6321b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ Intent f6322c;

    h(Context context, com.taobao.accs.net.b bVar, Intent intent) {
        this.f6320a = context;
        this.f6321b = bVar;
        this.f6322c = intent;
    }

    @Override // java.lang.Runnable
    public void run() {
        g.a().b(this.f6320a, this.f6321b, this.f6322c);
    }
}
