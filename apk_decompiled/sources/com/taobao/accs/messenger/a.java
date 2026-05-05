package com.taobao.accs.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.taobao.accs.utl.ALog;
import java.util.HashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f6346a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final HashMap<String, b> f6347b = new HashMap<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f6348c;

    public a(Context context) {
        this.f6346a = context;
    }

    public b a(String str) {
        b bVar = this.f6347b.get(str);
        if (bVar == null || !bVar.a()) {
            return null;
        }
        return bVar;
    }

    public void a(String str, b bVar) {
        b(str, bVar);
        if (this.f6348c) {
            try {
                this.f6346a.unbindService(bVar);
            } catch (Exception e) {
                ALog.e("ConnectionManager", "disconnect error: " + e.getMessage(), new Object[0]);
            }
        }
    }

    public void a(String str, Intent intent) {
        b bVar = this.f6347b.get(str);
        b bVar2 = null;
        if (bVar == null) {
            bVar2 = bVar;
        } else if (bVar.b()) {
            if (!bVar.c()) {
                return;
            } else {
                a(str, bVar);
            }
        } else {
            this.f6347b.remove(str);
        }
        if (bVar2 == null) {
            b bVar3 = new b(this.f6346a, str, this);
            this.f6347b.put(str, bVar3);
            this.f6348c = this.f6346a.bindService(a(intent), bVar3, 1);
        }
    }

    private static Intent a(Intent intent) {
        Intent intent2 = (Intent) intent.clone();
        intent2.replaceExtras(new Bundle());
        return intent2;
    }

    public void b(String str, b bVar) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.f6347b.remove(str, bVar);
        } else if (this.f6347b.get(str) == bVar) {
            this.f6347b.remove(str);
        }
    }
}
