package com.alibaba.sdk.android.crashdefend;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.sdk.android.crashdefend.a.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile a f2855a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Context f2856b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private b f2858d;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final com.alibaba.sdk.android.crashdefend.a.a f2857c = new com.alibaba.sdk.android.crashdefend.a.a();
    private final Map<String, String> f = new HashMap();
    private final int[] g = new int[5];
    private final List<b> h = new ArrayList();
    private final ExecutorService e = new com.alibaba.sdk.android.crashdefend.b.a().a();

    /* JADX INFO: renamed from: com.alibaba.sdk.android.crashdefend.a$a, reason: collision with other inner class name */
    private class RunnableC0185a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private b f2860b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f2861c;

        RunnableC0185a(b bVar, int i) {
            this.f2860b = bVar;
            this.f2861c = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            do {
                try {
                    Thread.sleep(1000L);
                    this.f2861c--;
                } catch (InterruptedException unused) {
                    return;
                } catch (Exception e) {
                    Log.d("CrashDefend", e.getMessage(), e);
                }
            } while (this.f2861c > 0);
            if (this.f2861c <= 0) {
                a.this.c(this.f2860b);
                com.alibaba.sdk.android.crashdefend.c.a.a(a.this.f2856b, a.this.f2857c, a.this.h);
                return;
            }
            return;
        }
    }

    private a(Context context) {
        this.f2856b = context.getApplicationContext();
        for (int i = 0; i < 5; i++) {
            this.g[i] = (i * 5) + 5;
        }
        this.f.put("sdkId", "crashdefend");
        this.f.put("sdkVersion", "0.0.6");
        try {
            a();
            b();
        } catch (Exception e) {
            Log.d("CrashDefend", e.getMessage(), e);
        }
    }

    public static a a(Context context) {
        if (f2855a == null) {
            synchronized (a.class) {
                if (f2855a == null) {
                    f2855a = new a(context);
                }
            }
        }
        return f2855a;
    }

    private void a() {
        if (!com.alibaba.sdk.android.crashdefend.c.a.b(this.f2856b, this.f2857c, this.h)) {
            this.f2857c.f2862a = 1L;
        } else {
            this.f2857c.f2862a++;
        }
    }

    private boolean a(b bVar) {
        if (bVar.f2866d >= bVar.f2865c) {
            b bVar2 = this.f2858d;
            if (bVar2 == null || !bVar2.f2863a.equals(bVar.f2863a)) {
                return false;
            }
            bVar.f2866d = bVar.f2865c - 1;
        }
        bVar.g = bVar.f;
        return true;
    }

    private boolean a(b bVar, CrashDefendCallback crashDefendCallback) {
        b bVarB;
        String str;
        String str2;
        if (bVar != null && crashDefendCallback != null) {
            try {
                if (TextUtils.isEmpty(bVar.f2864b) || TextUtils.isEmpty(bVar.f2863a) || (bVarB = b(bVar, crashDefendCallback)) == null) {
                    return false;
                }
                boolean zA = a(bVarB);
                bVarB.f2866d++;
                com.alibaba.sdk.android.crashdefend.c.a.a(this.f2856b, this.f2857c, this.h);
                if (zA) {
                    b(bVarB);
                    str = "CrashDefend";
                    str2 = "START:" + bVarB.f2863a + " --- limit:" + bVarB.f2865c + "  count:" + (bVarB.f2866d - 1) + "  restore:" + bVarB.h + "  startSerialNumber:" + bVarB.g + "  registerSerialNumber:" + bVarB.f;
                } else if (bVarB.h >= 5) {
                    crashDefendCallback.onSdkClosed(bVarB.h);
                    str = "CrashDefend";
                    str2 = "CLOSED: " + bVarB.f2863a + " --- restored " + bVarB.h + ", has more than retry limit, so closed it";
                } else {
                    crashDefendCallback.onSdkStop(bVarB.f2865c, bVarB.f2866d - 1, bVarB.h, bVarB.i);
                    str = "CrashDefend";
                    str2 = "STOP:" + bVarB.f2863a + " --- limit:" + bVarB.f2865c + "  count:" + (bVarB.f2866d - 1) + "  restore:" + bVarB.h + "  startSerialNumber:" + bVarB.g + "  registerSerialNumber:" + bVarB.f;
                }
                com.alibaba.sdk.android.crashdefend.c.b.b(str, str2);
                return true;
            } catch (Exception e) {
                Log.d("CrashDefend", e.getMessage(), e);
            }
        }
        return false;
    }

    private synchronized b b(b bVar, CrashDefendCallback crashDefendCallback) {
        b bVar2 = null;
        if (this.h.size() > 0) {
            Iterator<b> it = this.h.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                b next = it.next();
                if (next != null && next.f2863a.equals(bVar.f2863a)) {
                    if (!next.f2864b.equals(bVar.f2864b)) {
                        next.f2864b = bVar.f2864b;
                        next.f2865c = bVar.f2865c;
                        next.e = bVar.e;
                        next.f2866d = 0;
                        next.h = 0;
                        next.i = 0L;
                    }
                    if (next.j) {
                        com.alibaba.sdk.android.crashdefend.c.b.b("CrashDefend", "SDK " + bVar.f2863a + " has been registered");
                        return null;
                    }
                    next.j = true;
                    next.k = crashDefendCallback;
                    next.f = this.f2857c.f2862a;
                    bVar2 = next;
                }
            }
        }
        if (bVar2 == null) {
            bVar2 = (b) bVar.clone();
            bVar2.j = true;
            bVar2.k = crashDefendCallback;
            bVar2.f2866d = 0;
            bVar2.f = this.f2857c.f2862a;
            this.h.add(bVar2);
        }
        return bVar2;
    }

    private void b() {
        String str;
        String str2;
        this.f2858d = null;
        ArrayList arrayList = new ArrayList();
        synchronized (this.h) {
            for (b bVar : this.h) {
                if (bVar.f2866d >= bVar.f2865c) {
                    arrayList.add(bVar);
                }
            }
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                b bVar2 = (b) it.next();
                if (bVar2.h < 5) {
                    long j = this.f2857c.f2862a - ((long) this.g[bVar2.h]);
                    long j2 = (bVar2.g - j) + 1;
                    com.alibaba.sdk.android.crashdefend.c.b.a("CrashDefend", "after restart " + j2 + " times, sdk will be restore");
                    bVar2.i = j2;
                    if (bVar2.g < j) {
                        this.f2858d = bVar2;
                        break;
                    }
                } else {
                    com.alibaba.sdk.android.crashdefend.c.b.b("CrashDefend", "SDK " + bVar2.f2863a + " has been closed");
                }
            }
            if (this.f2858d == null) {
                str = "CrashDefend";
                str2 = "NO SDK restore";
            } else {
                this.f2858d.h++;
                str = "CrashDefend";
                str2 = this.f2858d.f2863a + " will restore --- startSerialNumber:" + this.f2858d.g + "   crashCount:" + this.f2858d.f2866d;
            }
            com.alibaba.sdk.android.crashdefend.c.b.b(str, str2);
        }
    }

    private void b(b bVar) {
        if (bVar == null) {
            return;
        }
        d(bVar);
        if (bVar.k != null) {
            bVar.k.onSdkStart(bVar.f2865c, bVar.f2866d - 1, bVar.h);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(b bVar) {
        if (bVar == null) {
            return;
        }
        bVar.f2866d = 0;
        bVar.h = 0;
    }

    private void d(b bVar) {
        if (bVar == null) {
            return;
        }
        this.e.execute(new RunnableC0185a(bVar, bVar.e));
    }

    public boolean a(String str, String str2, int i, int i2, CrashDefendCallback crashDefendCallback) {
        b bVar = new b();
        bVar.f2863a = str;
        bVar.f2864b = str2;
        bVar.f2865c = i;
        bVar.e = i2;
        return a(bVar, crashDefendCallback);
    }
}
