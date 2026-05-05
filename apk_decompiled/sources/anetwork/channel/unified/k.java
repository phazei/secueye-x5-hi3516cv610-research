package anetwork.channel.unified;

import android.os.Looper;
import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.interceptor.Interceptor;
import anetwork.channel.interceptor.InterceptorManager;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import config.Constants;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected j f2082a;

    public k(anetwork.channel.entity.g gVar, anetwork.channel.entity.c cVar) {
        cVar.a(gVar.e);
        this.f2082a = new j(gVar, cVar);
    }

    /* JADX INFO: compiled from: Taobao */
    class a implements Interceptor.Chain {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f2084b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private Request f2085c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private Callback f2086d;

        a(int i, Request request, Callback callback) {
            this.f2084b = 0;
            this.f2085c = null;
            this.f2086d = null;
            this.f2084b = i;
            this.f2085c = request;
            this.f2086d = callback;
        }

        @Override // anetwork.channel.interceptor.Interceptor.Chain
        public Request request() {
            return this.f2085c;
        }

        @Override // anetwork.channel.interceptor.Interceptor.Chain
        public Callback callback() {
            return this.f2086d;
        }

        @Override // anetwork.channel.interceptor.Interceptor.Chain
        public Future proceed(Request request, Callback callback) {
            if (k.this.f2082a.f2081d.get()) {
                ALog.i("anet.UnifiedRequestTask", "request canneled or timeout in processing interceptor", request.getSeq(), new Object[0]);
                return null;
            }
            if (this.f2084b < InterceptorManager.getSize()) {
                return InterceptorManager.getInterceptor(this.f2084b).intercept(k.this.new a(this.f2084b + 1, request, callback));
            }
            k.this.f2082a.f2078a.a(request);
            k.this.f2082a.f2079b = callback;
            Cache cache = NetworkConfigCenter.isHttpCacheEnable() ? CacheManager.getCache(k.this.f2082a.f2078a.g(), k.this.f2082a.f2078a.h()) : null;
            k.this.f2082a.e = cache != null ? new anetwork.channel.unified.a(k.this.f2082a, cache) : new e(k.this.f2082a, null, null);
            k.this.f2082a.e.run();
            k.this.c();
            return null;
        }
    }

    public Future a() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.f2082a.f2078a.f2045b.reqServiceTransmissionEnd = jCurrentTimeMillis;
        this.f2082a.f2078a.f2045b.start = jCurrentTimeMillis;
        this.f2082a.f2078a.f2045b.isReqSync = this.f2082a.f2078a.c();
        this.f2082a.f2078a.f2045b.isReqMain = Looper.myLooper() == Looper.getMainLooper();
        try {
            this.f2082a.f2078a.f2045b.netReqStart = Long.valueOf(this.f2082a.f2078a.a(RequestConstant.KEY_REQ_START)).longValue();
        } catch (Exception unused) {
        }
        String strA = this.f2082a.f2078a.a(RequestConstant.KEY_TRACE_ID);
        if (!TextUtils.isEmpty(strA)) {
            this.f2082a.f2078a.f2045b.traceId = strA;
        }
        String strA2 = this.f2082a.f2078a.a(RequestConstant.KEY_REQ_PROCESS);
        this.f2082a.f2078a.f2045b.process = strA2;
        this.f2082a.f2078a.f2045b.pTraceId = this.f2082a.f2078a.a(RequestConstant.KEY_PARENT_TRACE_ID);
        ALog.e("anet.UnifiedRequestTask", "[traceId:" + strA + "]start", this.f2082a.f2080c, "bizId", this.f2082a.f2078a.a().getBizId(), "processFrom", strA2, "url", this.f2082a.f2078a.g());
        if (NetworkConfigCenter.isUrlInDegradeList(this.f2082a.f2078a.f())) {
            b bVar = new b(this.f2082a);
            this.f2082a.e = bVar;
            bVar.f2052a = new anet.channel.request.b(ThreadPoolExecutorFactory.submitBackupTask(new l(this)), this.f2082a.f2078a.a().getSeq());
            c();
            return new d(this);
        }
        ThreadPoolExecutorFactory.submitPriorityTask(new m(this), ThreadPoolExecutorFactory.Priority.HIGH);
        return new d(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        this.f2082a.f = ThreadPoolExecutorFactory.submitScheduledTask(new n(this), this.f2082a.f2078a.b(), TimeUnit.MILLISECONDS);
    }

    void b() {
        if (this.f2082a.f2081d.compareAndSet(false, true)) {
            ALog.e("anet.UnifiedRequestTask", "task cancelled", this.f2082a.f2080c, Constants.URL, this.f2082a.f2078a.f().simpleUrlString());
            RequestStatistic requestStatistic = this.f2082a.f2078a.f2045b;
            if (requestStatistic.isDone.compareAndSet(false, true)) {
                requestStatistic.ret = 2;
                requestStatistic.statusCode = -204;
                requestStatistic.msg = ErrorConstant.getErrMsg(-204);
                requestStatistic.rspEnd = System.currentTimeMillis();
                AppMonitor.getInstance().commitStat(new ExceptionStatistic(-204, null, requestStatistic, null));
                if (requestStatistic.recDataSize > OSSConstants.MIN_PART_SIZE_LIMIT) {
                    anet.channel.monitor.b.a().a(requestStatistic.sendStart, requestStatistic.rspEnd, requestStatistic.recDataSize);
                }
            }
            this.f2082a.b();
            this.f2082a.a();
            this.f2082a.f2079b.onFinish(new DefaultFinishEvent(-204, (String) null, this.f2082a.f2078a.a()));
        }
    }
}
