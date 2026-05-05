package com.alibaba.sdk.android.utils.crashdefend;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/* JADX INFO: compiled from: CrashDefendManager.java */
/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static b f3216b;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f3217a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private com.alibaba.sdk.android.utils.c f36a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private c f38a;

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    private ExecutorService f41b;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private com.alibaba.sdk.android.utils.crashdefend.a f37a = new com.alibaba.sdk.android.utils.crashdefend.a();

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final List<c> f39a = new ArrayList();
    private Map<String, String> e = new HashMap();

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final int[] f40a = new int[5];

    public void d(String str, String str2) {
    }

    public static synchronized b a(Context context, com.alibaba.sdk.android.utils.c cVar) {
        if (f3216b == null) {
            f3216b = new b(context, cVar);
        }
        return f3216b;
    }

    private b(Context context, com.alibaba.sdk.android.utils.c cVar) {
        this.f41b = null;
        this.f3217a = context;
        this.f36a = cVar;
        this.f41b = new f().a();
        for (int i = 0; i < 5; i++) {
            this.f40a[i] = (i * 5) + 5;
        }
        this.e.put("sdkId", "utils");
        this.e.put("sdkVersion", AlinkConstants.PROVISION_DEVICE_PIDTOPK_VERSION);
        try {
            a();
            b();
        } catch (Exception e) {
            Log.d("UtilsSDK", e.getMessage(), e);
        }
    }

    private void a() {
        if (e.m32a(this.f3217a, this.f37a, this.f39a)) {
            this.f37a.f3215a++;
        } else {
            this.f37a.f3215a = 1L;
        }
    }

    private void b() {
        this.f38a = null;
        ArrayList arrayList = new ArrayList();
        synchronized (this.f39a) {
            for (c cVar : this.f39a) {
                if (cVar.crashCount >= cVar.f3220a) {
                    arrayList.add(cVar);
                }
            }
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                c cVar2 = (c) it.next();
                if (cVar2.f3222c < 5) {
                    if (cVar2.f42a < this.f37a.f3215a - ((long) this.f40a[cVar2.f3222c])) {
                        this.f38a = cVar2;
                        break;
                    }
                } else {
                    Log.i("UtilsSDK", "SDK " + cVar2.f44a + " has been closed");
                }
            }
            if (this.f38a == null) {
                Log.i("UtilsSDK", "NO SDK restore");
            } else {
                this.f38a.f3222c++;
                Log.i("UtilsSDK", this.f38a.f44a + " will restore --- startSerialNumber:" + this.f38a.f42a + "   crashCount:" + this.f38a.crashCount);
            }
        }
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    public boolean m30a(c cVar, SDKMessageCallback sDKMessageCallback) {
        c cVarA;
        if (cVar == null || sDKMessageCallback == null) {
            return false;
        }
        try {
            if (TextUtils.isEmpty(cVar.f46b) || TextUtils.isEmpty(cVar.f44a) || (cVarA = a(cVar, sDKMessageCallback)) == null) {
                return false;
            }
            boolean zM29a = m29a(cVarA);
            if (cVarA.crashCount == cVarA.f3220a) {
                a(cVarA.f44a, cVarA.f46b, cVarA.crashCount, cVarA.f3220a);
            }
            cVarA.crashCount++;
            e.a(this.f3217a, this.f37a, this.f39a);
            if (zM29a) {
                a(cVarA);
                Log.i("UtilsSDK", "START:" + cVarA.f44a + " --- limit:" + cVarA.f3220a + "  count:" + (cVarA.crashCount - 1) + "  restore:" + cVarA.f3222c + "  startSerialNumber:" + cVarA.f42a + "  registerSerialNumber:" + cVarA.f45b);
            } else {
                sDKMessageCallback.crashDefendMessage(cVarA.f3220a, cVarA.crashCount - 1);
                Log.i("UtilsSDK", "STOP:" + cVarA.f44a + " --- limit:" + cVarA.f3220a + "  count:" + (cVarA.crashCount - 1) + "  restore:" + cVarA.f3222c + "  startSerialNumber:" + cVarA.f42a + "  registerSerialNumber:" + cVarA.f45b);
            }
            return true;
        } catch (Exception e) {
            Log.d("UtilsSDK", e.getMessage(), e);
            return false;
        }
    }

    private c a(c cVar, SDKMessageCallback sDKMessageCallback) {
        synchronized (this.f39a) {
            c cVar2 = null;
            if (this.f39a != null && this.f39a.size() > 0) {
                Iterator<c> it = this.f39a.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    c next = it.next();
                    if (next != null && next.f44a.equals(cVar.f44a)) {
                        if (!next.f46b.equals(cVar.f46b)) {
                            next.f46b = cVar.f46b;
                            next.f3220a = cVar.f3220a;
                            next.f3221b = cVar.f3221b;
                            next.crashCount = 0;
                            next.f3222c = 0;
                        }
                        if (next.f3223d) {
                            Log.i("UtilsSDK", "SDK " + cVar.f44a + " has been registered");
                            return null;
                        }
                        next.f3223d = true;
                        next.f43a = sDKMessageCallback;
                        next.f45b = this.f37a.f3215a;
                        cVar2 = next;
                    }
                }
            }
            if (cVar2 == null) {
                cVar2 = (c) cVar.clone();
                cVar2.f3223d = true;
                cVar2.f43a = sDKMessageCallback;
                cVar2.crashCount = 0;
                cVar2.f45b = this.f37a.f3215a;
                this.f39a.add(cVar2);
            }
            return cVar2;
        }
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    private boolean m29a(c cVar) {
        if (cVar.crashCount < cVar.f3220a) {
            cVar.f42a = cVar.f45b;
            return true;
        }
        c cVar2 = this.f38a;
        if (cVar2 == null || !cVar2.f44a.equals(cVar.f44a)) {
            return false;
        }
        cVar.crashCount = cVar.f3220a - 1;
        cVar.f42a = cVar.f45b;
        return true;
    }

    private void a(c cVar) {
        if (cVar == null) {
            return;
        }
        d dVar = new d();
        dVar.f3224b = cVar;
        dVar.f3225d = cVar.f3221b;
        a(dVar);
        if (cVar.f43a != null) {
            cVar.f43a.crashDefendMessage(cVar.f3220a, cVar.crashCount - 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(c cVar) {
        if (cVar == null) {
            return;
        }
        if (cVar.f3222c > 0) {
            b(cVar.f44a, cVar.f46b, cVar.f3222c, 5);
        }
        cVar.crashCount = 0;
        cVar.f3222c = 0;
    }

    private void a(d dVar) {
        if (dVar == null || dVar.f3224b == null) {
            return;
        }
        this.f41b.execute(new a(dVar));
    }

    private void a(String str, String str2, int i, int i2) {
        if (this.f36a == null) {
            return;
        }
        HashMap map = new HashMap();
        map.putAll(this.e);
        map.put("crashSdkId", str);
        map.put("crashSdkVer", str2);
        map.put("curCrashCount", String.valueOf(i));
        map.put("crashThreshold", String.valueOf(i2));
        this.f36a.sendCustomHit("utils_biz_crash", 0L, map);
    }

    private void b(String str, String str2, int i, int i2) {
        if (this.f36a == null) {
            return;
        }
        HashMap map = new HashMap();
        map.putAll(this.e);
        map.put("crashSdkId", str);
        map.put("crashSdkVer", str2);
        map.put("recoverCount", String.valueOf(i));
        map.put("recoverThreshold", String.valueOf(i2));
        this.f36a.sendCustomHit("utils_biz_recover", 0L, map);
    }

    /* JADX INFO: compiled from: CrashDefendManager.java */
    private class a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private d f3218a;

        a(d dVar) {
            this.f3218a = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            do {
                try {
                    Thread.sleep(1000L);
                    d dVar = this.f3218a;
                    dVar.f3225d--;
                } catch (InterruptedException unused) {
                    return;
                } catch (Exception e) {
                    Log.d("UtilsSDK", e.getMessage(), e);
                }
            } while (this.f3218a.f3225d > 0);
            if (this.f3218a.f3225d <= 0) {
                b.this.b(this.f3218a.f3224b);
                e.a(b.this.f3217a, b.this.f37a, (List<c>) b.this.f39a);
                return;
            }
            return;
        }
    }
}
