package anetwork.channel.unified;

import anet.channel.bytes.ByteArray;
import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import config.Constants;
import io.netty.handler.codec.http.HttpHeaders;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a implements IUnifiedTask {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private j f2049a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Cache f2050b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile boolean f2051c = false;

    public a(j jVar, Cache cache) {
        this.f2049a = null;
        this.f2050b = null;
        this.f2049a = jVar;
        this.f2050b = cache;
    }

    @Override // anet.channel.request.Cancelable
    public void cancel() {
        this.f2051c = true;
        this.f2049a.f2078a.f2045b.ret = 2;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean zEquals;
        Cache.Entry entry;
        if (this.f2051c) {
            return;
        }
        RequestStatistic requestStatistic = this.f2049a.f2078a.f2045b;
        if (this.f2050b != null) {
            String strG = this.f2049a.f2078a.g();
            Request requestA = this.f2049a.f2078a.a();
            String str = requestA.getHeaders().get("Cache-Control");
            boolean zEquals2 = HttpHeaders.Values.NO_STORE.equals(str);
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (zEquals2) {
                this.f2050b.remove(strG);
                zEquals = false;
                entry = null;
            } else {
                zEquals = "no-cache".equals(str);
                Cache.Entry entry2 = this.f2050b.get(strG);
                if (ALog.isPrintLog(2)) {
                    String str2 = this.f2049a.f2080c;
                    Object[] objArr = new Object[8];
                    objArr[0] = "hit";
                    objArr[1] = Boolean.valueOf(entry2 != null);
                    objArr[2] = "cost";
                    objArr[3] = Long.valueOf(requestStatistic.cacheTime);
                    objArr[4] = "length";
                    objArr[5] = Integer.valueOf(entry2 != null ? entry2.data.length : 0);
                    objArr[6] = "key";
                    objArr[7] = strG;
                    ALog.i("anet.CacheTask", "read cache", str2, objArr);
                }
                entry = entry2;
            }
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            requestStatistic.cacheTime = jCurrentTimeMillis2 - jCurrentTimeMillis;
            if (entry != null && !zEquals && entry.isFresh()) {
                if (this.f2049a.f2081d.compareAndSet(false, true)) {
                    this.f2049a.a();
                    requestStatistic.ret = 1;
                    requestStatistic.statusCode = 200;
                    requestStatistic.msg = HttpConstant.SUCCESS;
                    requestStatistic.protocolType = "cache";
                    requestStatistic.rspEnd = jCurrentTimeMillis2;
                    requestStatistic.processTime = jCurrentTimeMillis2 - requestStatistic.start;
                    if (ALog.isPrintLog(2)) {
                        ALog.i("anet.CacheTask", "hit fresh cache", this.f2049a.f2080c, Constants.URL, this.f2049a.f2078a.f().urlString());
                    }
                    this.f2049a.f2079b.onResponseCode(200, entry.responseHeaders);
                    this.f2049a.f2079b.onDataReceiveSize(1, entry.data.length, ByteArray.wrap(entry.data));
                    this.f2049a.f2079b.onFinish(new DefaultFinishEvent(200, HttpConstant.SUCCESS, requestA));
                    return;
                }
                return;
            }
            if (this.f2051c) {
                return;
            }
            e eVar = new e(this.f2049a, zEquals2 ? null : this.f2050b, entry);
            this.f2049a.e = eVar;
            eVar.run();
        }
    }
}
