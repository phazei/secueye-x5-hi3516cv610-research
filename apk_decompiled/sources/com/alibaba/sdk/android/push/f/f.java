package com.alibaba.sdk.android.push.f;

import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ConcurrentHashMap<Integer, a> f3099a = new ConcurrentHashMap<>();

    class a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f3101b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private String f3102c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private long f3103d;

        public a(String str, String str2, long j) {
            this.f3101b = str;
            this.f3102c = str2;
            this.f3103d = j;
        }

        public String a() {
            return this.f3102c;
        }

        public long b() {
            return this.f3103d;
        }
    }

    private boolean a(long j, long j2) {
        return j2 - j >= 5000;
    }

    public a a(int i) {
        a aVar;
        ConcurrentHashMap<Integer, a> concurrentHashMap = this.f3099a;
        if (concurrentHashMap == null || (aVar = concurrentHashMap.get(Integer.valueOf(i))) == null || a(aVar.b(), System.currentTimeMillis())) {
            return null;
        }
        return aVar;
    }

    public void a(int i, String str) {
        a aVar;
        int i2;
        if (this.f3099a == null) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        switch (i) {
            case 1:
                i2 = 1;
                aVar = new a(String.valueOf(1), str, jCurrentTimeMillis);
                break;
            case 2:
                i2 = 2;
                aVar = new a(String.valueOf(2), str, jCurrentTimeMillis);
                break;
            case 3:
                i2 = 3;
                aVar = new a(String.valueOf(3), str, jCurrentTimeMillis);
                break;
            case 4:
                i2 = 4;
                aVar = new a(String.valueOf(4), str, jCurrentTimeMillis);
                break;
            default:
                return;
        }
        this.f3099a.put(Integer.valueOf(i2), aVar);
    }
}
