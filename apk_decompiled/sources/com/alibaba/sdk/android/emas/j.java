package com.alibaba.sdk.android.emas;

import android.content.Context;
import android.os.Looper;
import com.alibaba.sdk.android.tbrest.SendService;
import com.alibaba.sdk.android.tbrest.request.BizRequest;
import com.alibaba.sdk.android.tbrest.request.UrlWrapper;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: SendManager.java */
/* JADX INFO: loaded from: classes.dex */
public class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final ThreadPoolExecutor f2892a = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS, new LinkedBlockingQueue(10), new a());

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private PreSendHandler f15a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final EmasSender f2893b;
    private final e mDiskCacheManager;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f2894d = false;
    private int f = 0;
    private final SendService mSendService = new SendService();

    public j(EmasSender emasSender, e eVar) {
        this.f2893b = emasSender;
        this.mDiskCacheManager = eVar;
    }

    public void init(Context context, String str, String str2, String str3, String str4, String str5) {
        this.mSendService.init(context.getApplicationContext(), str, str2, str3, str4, str5);
    }

    public void setHost(String str) {
        this.mSendService.changeHost(str);
    }

    public void a(String str) {
        this.mSendService.appSecret = str;
    }

    public void openHttp(boolean z) {
        this.mSendService.openHttp = Boolean.valueOf(z);
    }

    public void a(PreSendHandler preSendHandler) {
        this.f15a = preSendHandler;
    }

    public void setUserNick(String str) {
        this.mSendService.userNick = str;
    }

    public void a(boolean z) {
        this.f2894d = z;
    }

    public void a(int i) {
        if (i >= 10) {
            this.f = 9;
        } else {
            this.f = i;
        }
    }

    public void b(g gVar) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(gVar);
        a(arrayList);
    }

    public void a(List<g> list) {
        c(new f(list));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(f fVar) {
        f2892a.execute(new b(fVar, this.f2894d, this.f));
    }

    void e() {
        if (this.mDiskCacheManager != null && f2892a.getQueue().isEmpty()) {
            new Thread(new Runnable() { // from class: com.alibaba.sdk.android.emas.j.1
                @Override // java.lang.Runnable
                public void run() {
                    f fVar;
                    j.this.mDiskCacheManager.clear();
                    if (j.f2892a.getQueue().size() > j.this.f || (fVar = j.this.mDiskCacheManager.get()) == null) {
                        return;
                    }
                    j.this.c(fVar);
                }
            }).start();
        }
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    public SendService m22a() {
        return this.mSendService;
    }

    /* JADX INFO: compiled from: SendManager.java */
    private class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final f f2896a;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final boolean f2897d;
        private final int f;

        public b(f fVar, boolean z, int i) {
            this.f2896a = fVar;
            this.f2897d = z;
            this.f = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            List<g> listM17a;
            if (this.f2896a.a() == d.DISK_CACHE) {
                LogUtil.d("SendManager send disk log, location:" + this.f2896a.getLocation());
            }
            if (j.this.f15a != null) {
                listM17a = j.this.f15a.onHandlePreSend(this.f2896a.m17a(), this.f2896a.a());
            } else {
                listM17a = this.f2896a.m17a();
            }
            if (listM17a != null && !listM17a.isEmpty()) {
                byte[] packRequest = null;
                try {
                    packRequest = BizRequest.getPackRequest(j.this.mSendService.context, j.this.mSendService, a(listM17a));
                } catch (Exception e) {
                    LogUtil.w("SendManager pack request failed", e);
                    if (j.this.mDiskCacheManager != null) {
                        j.this.mDiskCacheManager.b(this.f2896a);
                    }
                }
                if (packRequest != null) {
                    if (!UrlWrapper.sendRequest(j.this.mSendService, j.this.mSendService.host, packRequest).isSuccess()) {
                        if (j.this.mDiskCacheManager != null) {
                            LogUtil.d("SendManager request failed. put into cache.");
                            j.this.mDiskCacheManager.add(this.f2896a);
                            return;
                        } else {
                            LogUtil.d("SendManager request failed. do nothing.");
                            return;
                        }
                    }
                    d(this.f2896a);
                    return;
                }
                LogUtil.d("SendManager pack requst is null.");
                d(this.f2896a);
                return;
            }
            d(this.f2896a);
        }

        private void d(f fVar) {
            if (j.this.mDiskCacheManager == null) {
                return;
            }
            if (fVar.a() == d.DISK_CACHE) {
                j.this.mDiskCacheManager.remove(fVar);
            }
            if (a()) {
                LogUtil.d("SendManager trying send disk cache.");
                f fVar2 = j.this.mDiskCacheManager.get();
                if (fVar2 != null) {
                    LogUtil.d("SendManager sending disk cache.");
                    j.this.c(fVar2);
                    return;
                } else {
                    LogUtil.d("SendManager disk cache is empty.");
                    return;
                }
            }
            LogUtil.d("SendManager finish send. background: " + j.this.f2893b.isBackground() + ", queue size: " + j.f2892a.getQueue().size() + ", limit: " + this.f);
        }

        private boolean a() {
            return (this.f2897d || !j.this.f2893b.isBackground()) && j.f2892a.getQueue().size() <= this.f;
        }

        private boolean b() {
            return this.f2896a.a() == d.DISK_CACHE;
        }

        /* JADX INFO: renamed from: b, reason: collision with other method in class */
        public f m23b() {
            return this.f2896a;
        }

        public void f() {
            if (j.this.mDiskCacheManager != null) {
                if (!b()) {
                    LogUtil.d("SendManager send queue fill, write into disk cache.");
                    Runnable runnable = new Runnable() { // from class: com.alibaba.sdk.android.emas.j.b.1
                        @Override // java.lang.Runnable
                        public void run() {
                            j.this.mDiskCacheManager.add(b.this.m23b());
                        }
                    };
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        new Thread(runnable).start();
                        return;
                    } else {
                        runnable.run();
                        return;
                    }
                }
                LogUtil.d("SendManager send queue fill, already in disk cache. do nothing.");
                return;
            }
            LogUtil.d("SendManager send queue fill, disk cache not open, discard.");
        }

        private Map<String, String> a(List<g> list) {
            HashMap map = new HashMap();
            for (g gVar : list) {
                StringBuilder sb = (StringBuilder) map.get(gVar.i);
                if (sb == null) {
                    map.put(gVar.i, new StringBuilder(gVar.h));
                } else {
                    sb.append((char) 1);
                    sb.append(gVar.h);
                }
            }
            HashMap map2 = new HashMap();
            for (Map.Entry entry : map.entrySet()) {
                map2.put(entry.getKey(), ((StringBuilder) entry.getValue()).toString());
            }
            return map2;
        }
    }

    /* JADX INFO: compiled from: SendManager.java */
    private static class a implements RejectedExecutionHandler {
        private a() {
        }

        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            if (runnable instanceof b) {
                ((b) runnable).f();
            }
        }
    }
}
