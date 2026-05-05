package anetwork.channel.unified;

import android.text.TextUtils;
import anet.channel.Config;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.NoAvailStrategyException;
import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.entity.ENV;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.StringUtils;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.http.NetworkSdkSetting;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.util.RequestConstant;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e implements IUnifiedTask {
    public static final int MAX_RSP_BUFFER_LENGTH = 131072;
    public static final String TAG = "anet.NetworkTask";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    j f2059a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Cache f2060b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    Cache.Entry f2061c;
    String e;
    volatile AtomicBoolean h;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    ByteArrayOutputStream f2062d = null;
    volatile Cancelable f = null;
    volatile boolean g = false;
    int i = 0;
    int j = 0;
    boolean k = false;
    boolean l = false;
    a m = null;

    e(j jVar, Cache cache, Cache.Entry entry) {
        this.f2060b = null;
        this.f2061c = null;
        this.e = "other";
        this.h = null;
        this.f2059a = jVar;
        this.h = jVar.f2081d;
        this.f2060b = cache;
        this.f2061c = entry;
        this.e = jVar.f2078a.h().get(HttpConstant.F_REFER);
    }

    @Override // anet.channel.request.Cancelable
    public void cancel() {
        this.g = true;
        if (this.f != null) {
            this.f.cancel();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.g) {
            return;
        }
        RequestStatistic requestStatistic = this.f2059a.f2078a.f2045b;
        requestStatistic.f_refer = this.e;
        if (!NetworkStatusHelper.isConnected()) {
            if (NetworkConfigCenter.isRequestDelayRetryForNoNetwork() && requestStatistic.statusCode != -200) {
                requestStatistic.statusCode = -200;
                ThreadPoolExecutorFactory.submitScheduledTask(new f(this), 1000L, TimeUnit.MILLISECONDS);
                return;
            }
            if (ALog.isPrintLog(2)) {
                ALog.i(TAG, "network unavailable", this.f2059a.f2080c, "NetworkStatus", NetworkStatusHelper.getStatus());
            }
            this.h.set(true);
            this.f2059a.a();
            requestStatistic.isDone.set(true);
            requestStatistic.statusCode = -200;
            requestStatistic.msg = ErrorConstant.getErrMsg(-200);
            requestStatistic.rspEnd = System.currentTimeMillis();
            this.f2059a.f2079b.onFinish(new DefaultFinishEvent(-200, (String) null, this.f2059a.f2078a.a()));
            return;
        }
        if (NetworkConfigCenter.isBgRequestForbidden() && GlobalAppRuntimeInfo.isAppBackground() && AppLifecycle.lastEnterBackgroundTime > 0 && !AppLifecycle.isGoingForeground && System.currentTimeMillis() - AppLifecycle.lastEnterBackgroundTime > NetworkConfigCenter.getBgForbidRequestThreshold() && !NetworkConfigCenter.isUrlInWhiteList(this.f2059a.f2078a.f()) && !NetworkConfigCenter.isBizInWhiteList(this.f2059a.f2078a.a().getBizId()) && !this.f2059a.f2078a.a().isAllowRequestInBg()) {
            this.h.set(true);
            this.f2059a.a();
            if (ALog.isPrintLog(2)) {
                ALog.i(TAG, "request forbidden in background", this.f2059a.f2080c, "url", this.f2059a.f2078a.f());
            }
            requestStatistic.isDone.set(true);
            requestStatistic.statusCode = -205;
            requestStatistic.msg = ErrorConstant.getErrMsg(-205);
            requestStatistic.rspEnd = System.currentTimeMillis();
            this.f2059a.f2079b.onFinish(new DefaultFinishEvent(-205, (String) null, this.f2059a.f2078a.a()));
            ExceptionStatistic exceptionStatistic = new ExceptionStatistic(-205, null, LinkFormat.RESOURCE_TYPE);
            exceptionStatistic.host = this.f2059a.f2078a.f().host();
            exceptionStatistic.url = this.f2059a.f2078a.g();
            AppMonitor.getInstance().commitStat(exceptionStatistic);
            return;
        }
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "exec request", this.f2059a.f2080c, "retryTimes", Integer.valueOf(this.f2059a.f2078a.f2044a));
        }
        if (NetworkConfigCenter.isGetSessionAsyncEnable()) {
            c();
            return;
        }
        try {
            Session sessionB = b();
            if (sessionB == null) {
                return;
            }
            a(sessionB, this.f2059a.f2078a.a());
        } catch (Exception e) {
            ALog.e(TAG, "send request failed.", this.f2059a.f2080c, e, new Object[0]);
        }
    }

    private HttpUrl a(HttpUrl httpUrl) {
        HttpUrl httpUrl2;
        String str = this.f2059a.f2078a.h().get(HttpConstant.X_HOST_CNAME);
        return (TextUtils.isEmpty(str) || (httpUrl2 = HttpUrl.parse(httpUrl.urlString().replaceFirst(httpUrl.host(), str))) == null) ? httpUrl : httpUrl2;
    }

    private SessionCenter a() {
        String strA = this.f2059a.f2078a.a(RequestConstant.APPKEY);
        if (TextUtils.isEmpty(strA)) {
            return SessionCenter.getInstance();
        }
        ENV env = ENV.ONLINE;
        String strA2 = this.f2059a.f2078a.a(RequestConstant.ENVIRONMENT);
        if ("pre".equalsIgnoreCase(strA2)) {
            env = ENV.PREPARE;
        } else if ("test".equalsIgnoreCase(strA2)) {
            env = ENV.TEST;
        }
        if (env != NetworkSdkSetting.CURRENT_ENV) {
            NetworkSdkSetting.CURRENT_ENV = env;
            SessionCenter.switchEnvironment(env);
        }
        Config config2 = Config.getConfig(strA, env);
        if (config2 == null) {
            config2 = new Config.Builder().setAppkey(strA).setEnv(env).setAuthCode(this.f2059a.f2078a.a(RequestConstant.AUTH_CODE)).build();
        }
        return SessionCenter.getInstance(config2);
    }

    private Session b() {
        Session throwsException;
        SessionCenter sessionCenterA = a();
        HttpUrl httpUrlF = this.f2059a.f2078a.f();
        boolean zContainsNonDefaultPort = httpUrlF.containsNonDefaultPort();
        RequestStatistic requestStatistic = this.f2059a.f2078a.f2045b;
        if (this.f2059a.f2078a.f == 1 && NetworkConfigCenter.isSpdyEnabled() && this.f2059a.f2078a.f2044a == 0 && !zContainsNonDefaultPort) {
            HttpUrl httpUrlA = a(httpUrlF);
            try {
                throwsException = sessionCenterA.getThrowsException(httpUrlA, anet.channel.entity.c.f1741a, 0L);
            } catch (NoAvailStrategyException unused) {
                return a(null, sessionCenterA, httpUrlF, zContainsNonDefaultPort);
            } catch (Exception unused2) {
                throwsException = null;
            }
            if (throwsException == null) {
                ThreadPoolExecutorFactory.submitPriorityTask(new g(this, sessionCenterA, httpUrlA, requestStatistic, httpUrlF, zContainsNonDefaultPort), ThreadPoolExecutorFactory.Priority.NORMAL);
                return null;
            }
            ALog.i(TAG, "tryGetSession", this.f2059a.f2080c, RtspHeaders.Names.SESSION, throwsException);
            requestStatistic.spdyRequestSend = true;
            return throwsException;
        }
        return a(null, sessionCenterA, httpUrlF, zContainsNonDefaultPort);
    }

    private void c() {
        SessionCenter sessionCenterA = a();
        HttpUrl httpUrlF = this.f2059a.f2078a.f();
        boolean zContainsNonDefaultPort = httpUrlF.containsNonDefaultPort();
        RequestStatistic requestStatistic = this.f2059a.f2078a.f2045b;
        Request requestA = this.f2059a.f2078a.a();
        if (this.f2059a.f2078a.f == 1 && NetworkConfigCenter.isSpdyEnabled() && this.f2059a.f2078a.f2044a == 0 && !zContainsNonDefaultPort) {
            sessionCenterA.asyncGet(a(httpUrlF), anet.channel.entity.c.f1741a, 3000L, new h(this, requestStatistic, System.currentTimeMillis(), requestA, sessionCenterA, httpUrlF, zContainsNonDefaultPort));
            return;
        }
        a(a(null, sessionCenterA, httpUrlF, zContainsNonDefaultPort), requestA);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Session a(Session session, SessionCenter sessionCenter, HttpUrl httpUrl, boolean z) {
        RequestStatistic requestStatistic = this.f2059a.f2078a.f2045b;
        if (session == null && this.f2059a.f2078a.e() && !z && !NetworkStatusHelper.isProxy()) {
            session = sessionCenter.get(httpUrl, anet.channel.entity.c.f1742b, 0L);
        }
        if (session == null) {
            ALog.i(TAG, "create HttpSession with local DNS", this.f2059a.f2080c, new Object[0]);
            session = new anet.channel.session.d(GlobalAppRuntimeInfo.getContext(), new anet.channel.entity.a(StringUtils.concatString(httpUrl.scheme(), HttpConstant.SCHEME_SPLIT, httpUrl.host()), this.f2059a.f2080c, null));
        }
        if (requestStatistic.spdyRequestSend) {
            requestStatistic.degraded = 1;
        }
        ALog.i(TAG, "tryGetHttpSession", this.f2059a.f2080c, RtspHeaders.Names.SESSION, session);
        return session;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private anet.channel.request.Request a(anet.channel.request.Request r7) {
        /*
            r6 = this;
            anetwork.channel.unified.j r0 = r6.f2059a
            anetwork.channel.entity.g r0 = r0.f2078a
            boolean r0 = r0.i()
            if (r0 == 0) goto L3e
            anetwork.channel.unified.j r0 = r6.f2059a
            anetwork.channel.entity.g r0 = r0.f2078a
            java.lang.String r0 = r0.g()
            java.lang.String r0 = anetwork.channel.cookie.CookieManager.getCookie(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L3e
            anet.channel.request.Request$Builder r1 = r7.newBuilder()
            java.util.Map r2 = r7.getHeaders()
            java.lang.String r3 = "Cookie"
            java.lang.Object r2 = r2.get(r3)
            java.lang.String r2 = (java.lang.String) r2
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L38
            java.lang.String r3 = "; "
            java.lang.String r0 = anet.channel.util.StringUtils.concatString(r2, r3, r0)
        L38:
            java.lang.String r2 = "Cookie"
            r1.addHeader(r2, r0)
            goto L3f
        L3e:
            r1 = 0
        L3f:
            anetwork.channel.cache.Cache$Entry r0 = r6.f2061c
            if (r0 == 0) goto L6f
            if (r1 != 0) goto L49
            anet.channel.request.Request$Builder r1 = r7.newBuilder()
        L49:
            anetwork.channel.cache.Cache$Entry r0 = r6.f2061c
            java.lang.String r0 = r0.etag
            if (r0 == 0) goto L58
            java.lang.String r0 = "If-None-Match"
            anetwork.channel.cache.Cache$Entry r2 = r6.f2061c
            java.lang.String r2 = r2.etag
            r1.addHeader(r0, r2)
        L58:
            anetwork.channel.cache.Cache$Entry r0 = r6.f2061c
            long r2 = r0.lastModified
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L6f
            java.lang.String r0 = "If-Modified-Since"
            anetwork.channel.cache.Cache$Entry r2 = r6.f2061c
            long r2 = r2.lastModified
            java.lang.String r2 = anetwork.channel.cache.a.a(r2)
            r1.addHeader(r0, r2)
        L6f:
            anetwork.channel.unified.j r0 = r6.f2059a
            anetwork.channel.entity.g r0 = r0.f2078a
            int r0 = r0.f2044a
            if (r0 != 0) goto L8e
            java.lang.String r0 = "weex"
            java.lang.String r2 = r6.e
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L8e
            if (r1 != 0) goto L89
            anet.channel.request.Request$Builder r0 = r7.newBuilder()
            r1 = r0
        L89:
            r0 = 3000(0xbb8, float:4.204E-42)
            r1.setReadTimeout(r0)
        L8e:
            if (r1 != 0) goto L91
            goto L95
        L91:
            anet.channel.request.Request r7 = r1.build()
        L95:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.unified.e.a(anet.channel.request.Request):anet.channel.request.Request");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Session session, Request request) {
        if (session == null || this.g) {
            return;
        }
        Request requestA = a(request);
        RequestStatistic requestStatistic = this.f2059a.f2078a.f2045b;
        requestStatistic.reqStart = System.currentTimeMillis();
        this.f = session.request(requestA, new i(this, requestA, requestStatistic));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        int f2063a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        Map<String, List<String>> f2064b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        List<ByteArray> f2065c = new ArrayList();

        a(int i, Map<String, List<String>> map) {
            this.f2063a = i;
            this.f2064b = map;
        }

        void a() {
            Iterator<ByteArray> it = this.f2065c.iterator();
            while (it.hasNext()) {
                it.next().recycle();
            }
        }

        int a(Callback callback, int i) {
            callback.onResponseCode(this.f2063a, this.f2064b);
            Iterator<ByteArray> it = this.f2065c.iterator();
            int i2 = 1;
            while (it.hasNext()) {
                callback.onDataReceiveSize(i2, i, it.next());
                i2++;
            }
            return i2;
        }
    }
}
