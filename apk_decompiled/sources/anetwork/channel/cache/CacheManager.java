package anetwork.channel.cache;

import anet.channel.util.ALog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class CacheManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static List<a> f1990a = new ArrayList();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final ReentrantReadWriteLock f1991b = new ReentrantReadWriteLock();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final ReentrantReadWriteLock.ReadLock f1992c = f1991b.readLock();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final ReentrantReadWriteLock.WriteLock f1993d = f1991b.writeLock();

    /* JADX INFO: compiled from: Taobao */
    private static class a implements Comparable<a> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Cache f1994a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final CachePrediction f1995b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final int f1996c;

        a(Cache cache, CachePrediction cachePrediction, int i) {
            this.f1994a = cache;
            this.f1995b = cachePrediction;
            this.f1996c = i;
        }

        @Override // java.lang.Comparable
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compareTo(a aVar) {
            return this.f1996c - aVar.f1996c;
        }
    }

    public static void addCache(Cache cache, CachePrediction cachePrediction, int i) {
        try {
            if (cache == null) {
                throw new IllegalArgumentException("cache is null");
            }
            if (cachePrediction == null) {
                throw new IllegalArgumentException("prediction is null");
            }
            f1993d.lock();
            f1990a.add(new a(cache, cachePrediction, i));
            Collections.sort(f1990a);
        } finally {
            f1993d.unlock();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001b, code lost:
    
        r0.remove();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void removeCache(anetwork.channel.cache.Cache r2) {
        /*
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = anetwork.channel.cache.CacheManager.f1993d     // Catch: java.lang.Throwable -> L24
            r0.lock()     // Catch: java.lang.Throwable -> L24
            java.util.List<anetwork.channel.cache.CacheManager$a> r0 = anetwork.channel.cache.CacheManager.f1990a     // Catch: java.lang.Throwable -> L24
            java.util.ListIterator r0 = r0.listIterator()     // Catch: java.lang.Throwable -> L24
        Lb:
            boolean r1 = r0.hasNext()     // Catch: java.lang.Throwable -> L24
            if (r1 == 0) goto L1e
            java.lang.Object r1 = r0.next()     // Catch: java.lang.Throwable -> L24
            anetwork.channel.cache.CacheManager$a r1 = (anetwork.channel.cache.CacheManager.a) r1     // Catch: java.lang.Throwable -> L24
            anetwork.channel.cache.Cache r1 = r1.f1994a     // Catch: java.lang.Throwable -> L24
            if (r1 != r2) goto Lb
            r0.remove()     // Catch: java.lang.Throwable -> L24
        L1e:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r2 = anetwork.channel.cache.CacheManager.f1993d
            r2.unlock()
            return
        L24:
            r2 = move-exception
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = anetwork.channel.cache.CacheManager.f1993d
            r0.unlock()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.cache.CacheManager.removeCache(anetwork.channel.cache.Cache):void");
    }

    public static Cache getCache(String str, Map<String, String> map) {
        try {
            f1992c.lock();
            for (a aVar : f1990a) {
                if (aVar.f1995b.handleCache(str, map)) {
                    return aVar.f1994a;
                }
            }
            return null;
        } finally {
            f1992c.unlock();
        }
    }

    public static void clearAllCache() {
        ALog.w("anet.CacheManager", "clearAllCache", null, new Object[0]);
        Iterator<a> it = f1990a.iterator();
        while (it.hasNext()) {
            try {
                it.next().f1994a.clear();
            } catch (Exception unused) {
            }
        }
    }
}
