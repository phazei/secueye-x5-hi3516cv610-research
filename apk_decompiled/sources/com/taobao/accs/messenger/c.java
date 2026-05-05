package com.taobao.accs.messenger;

import android.content.Intent;
import android.os.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f6353a = c.class.getName() + ".TRY_COUNT";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ScheduledExecutorService f6354b = Executors.newSingleThreadScheduledExecutor();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private a f6355c;

    public c(a aVar) {
        this.f6355c = aVar;
    }

    public void a(String str, Intent intent) {
        b(str, intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str, Intent intent) {
        b bVarA = this.f6355c.a(str);
        if (bVarA == null) {
            this.f6355c.a(str, intent);
            c(str, intent);
            return;
        }
        try {
            bVarA.a(intent);
        } catch (RemoteException unused) {
            this.f6355c.b(str, bVarA);
            this.f6355c.a(str, intent);
            c(str, intent);
        }
    }

    private void c(String str, Intent intent) {
        int intExtra = intent.getIntExtra(f6353a, 0);
        if (intExtra > 10) {
            return;
        }
        intent.putExtra(f6353a, intExtra + 1);
        this.f6354b.schedule(new d(this, str, intent), 1000L, TimeUnit.MILLISECONDS);
    }
}
