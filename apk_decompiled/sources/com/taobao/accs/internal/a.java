package com.taobao.accs.internal;

import android.content.Context;
import com.taobao.accs.ACCSClient;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class a implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f6334a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ Context f6335b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ ACCSManagerImpl f6336c;

    a(ACCSManagerImpl aCCSManagerImpl, String str, Context context) {
        this.f6336c = aCCSManagerImpl;
        this.f6334a = str;
        this.f6335b = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            ACCSClient.getAccsClient(this.f6334a).addConnectionListener(new b(this));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
