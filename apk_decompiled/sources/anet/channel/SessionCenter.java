package anet.channel;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;
import anet.channel.Config;
import anet.channel.detect.n;
import anet.channel.entity.ConnType;
import anet.channel.entity.ENV;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.l;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.StringUtils;
import anet.channel.util.Utils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import tools.LocationUtil;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class SessionCenter {
    public static final String TAG = "awcn.SessionCenter";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static Map<Config, SessionCenter> f1637a = new HashMap();
    private static boolean j = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    String f1639c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    Config f1640d;
    final AccsSessionManager h;
    final e e = new e();
    final LruCache<String, SessionRequest> f = new LruCache<>(32);
    final c g = new c();
    final a i = new a(this, null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Context f1638b = GlobalAppRuntimeInfo.getContext();

    public static synchronized void init(Context context) {
        if (context == null) {
            ALog.e(TAG, "context is null!", null, new Object[0]);
            throw new NullPointerException("init failed. context is null");
        }
        GlobalAppRuntimeInfo.setContext(context.getApplicationContext());
        if (!j) {
            f1637a.put(Config.DEFAULT_CONFIG, new SessionCenter(Config.DEFAULT_CONFIG));
            AppLifecycle.initialize();
            NetworkStatusHelper.startListener(context);
            if (!AwcnConfig.isTbNextLaunch()) {
                StrategyCenter.getInstance().initialize(GlobalAppRuntimeInfo.getContext());
            }
            if (GlobalAppRuntimeInfo.isTargetProcess()) {
                n.a();
                anet.channel.e.a.a();
            }
            j = true;
        }
    }

    @Deprecated
    public static synchronized void init(Context context, String str) {
        init(context, str, GlobalAppRuntimeInfo.getEnv());
    }

    public static synchronized void init(Context context, String str, ENV env) {
        if (context == null) {
            ALog.e(TAG, "context is null!", null, new Object[0]);
            throw new NullPointerException("init failed. context is null");
        }
        Config config2 = Config.getConfig(str, env);
        if (config2 == null) {
            config2 = new Config.Builder().setAppkey(str).setEnv(env).build();
        }
        init(context, config2);
    }

    public static synchronized void init(Context context, Config config2) {
        if (context == null) {
            ALog.e(TAG, "context is null!", null, new Object[0]);
            throw new NullPointerException("init failed. context is null");
        }
        if (config2 == null) {
            ALog.e(TAG, "paramter config is null!", null, new Object[0]);
            throw new NullPointerException("init failed. config is null");
        }
        init(context);
        if (!f1637a.containsKey(config2)) {
            f1637a.put(config2, new SessionCenter(config2));
        }
    }

    private SessionCenter(Config config2) {
        this.f1640d = config2;
        this.f1639c = config2.getAppkey();
        this.i.a();
        this.h = new AccsSessionManager(this);
        if (config2.getAppkey().equals("[default]")) {
            return;
        }
        AmdcRuntimeInfo.setSign(new d(this, config2.getAppkey(), config2.getSecurity()));
    }

    @Deprecated
    public synchronized void switchEnv(ENV env) {
        switchEnvironment(env);
    }

    public static synchronized void switchEnvironment(ENV env) {
        try {
            if (GlobalAppRuntimeInfo.getEnv() != env) {
                ALog.i(TAG, "switch env", null, "old", GlobalAppRuntimeInfo.getEnv(), "new", env);
                GlobalAppRuntimeInfo.setEnv(env);
                StrategyCenter.getInstance().switchEnv();
                SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION).switchAccsServer(env == ENV.TEST ? 0 : 1);
            }
            Iterator<Map.Entry<Config, SessionCenter>> it = f1637a.entrySet().iterator();
            while (it.hasNext()) {
                SessionCenter value = it.next().getValue();
                if (value.f1640d.getEnv() != env) {
                    ALog.i(TAG, "remove instance", value.f1639c, RequestConstant.ENVIRONMENT, value.f1640d.getEnv());
                    value.h.forceCloseSession(false);
                    value.i.b();
                    it.remove();
                }
            }
        } catch (Throwable th) {
            ALog.e(TAG, "switch env error.", null, th, new Object[0]);
        }
    }

    public static synchronized SessionCenter getInstance(String str) {
        Config configByTag;
        configByTag = Config.getConfigByTag(str);
        if (configByTag == null) {
            throw new RuntimeException("tag not exist!");
        }
        return getInstance(configByTag);
    }

    public static synchronized SessionCenter getInstance(Config config2) {
        SessionCenter sessionCenter;
        Context appContext;
        if (config2 == null) {
            throw new NullPointerException("config is null!");
        }
        if (!j && (appContext = Utils.getAppContext()) != null) {
            init(appContext);
        }
        sessionCenter = f1637a.get(config2);
        if (sessionCenter == null) {
            sessionCenter = new SessionCenter(config2);
            f1637a.put(config2, sessionCenter);
        }
        return sessionCenter;
    }

    @Deprecated
    public static synchronized SessionCenter getInstance() {
        Context appContext;
        if (!j && (appContext = Utils.getAppContext()) != null) {
            init(appContext);
        }
        SessionCenter sessionCenter = null;
        for (Map.Entry<Config, SessionCenter> entry : f1637a.entrySet()) {
            SessionCenter value = entry.getValue();
            if (entry.getKey() != Config.DEFAULT_CONFIG) {
                return value;
            }
            sessionCenter = value;
        }
        return sessionCenter;
    }

    public Session getThrowsException(String str, long j2) throws Exception {
        return a(HttpUrl.parse(str), anet.channel.entity.c.f1743c, j2, null);
    }

    @Deprecated
    public Session getThrowsException(String str, ConnType.TypeLevel typeLevel, long j2) throws Exception {
        return a(HttpUrl.parse(str), typeLevel == ConnType.TypeLevel.SPDY ? anet.channel.entity.c.f1741a : anet.channel.entity.c.f1742b, j2, null);
    }

    public Session getThrowsException(HttpUrl httpUrl, int i, long j2) throws Exception {
        return a(httpUrl, i, j2, null);
    }

    @Deprecated
    public Session getThrowsException(HttpUrl httpUrl, ConnType.TypeLevel typeLevel, long j2) throws Exception {
        return a(httpUrl, typeLevel == ConnType.TypeLevel.SPDY ? anet.channel.entity.c.f1741a : anet.channel.entity.c.f1742b, j2, null);
    }

    public Session get(String str, long j2) {
        return get(HttpUrl.parse(str), anet.channel.entity.c.f1743c, j2);
    }

    @Deprecated
    public Session get(String str, ConnType.TypeLevel typeLevel, long j2) {
        return get(HttpUrl.parse(str), typeLevel == ConnType.TypeLevel.SPDY ? anet.channel.entity.c.f1741a : anet.channel.entity.c.f1742b, j2);
    }

    @Deprecated
    public Session get(HttpUrl httpUrl, ConnType.TypeLevel typeLevel, long j2) {
        return get(httpUrl, typeLevel == ConnType.TypeLevel.SPDY ? anet.channel.entity.c.f1741a : anet.channel.entity.c.f1742b, j2);
    }

    public Session get(HttpUrl httpUrl, int i, long j2) {
        try {
            return a(httpUrl, i, j2, null);
        } catch (NoAvailStrategyException e) {
            ALog.i(TAG, "[Get]" + e.getMessage(), this.f1639c, null, "url", httpUrl.urlString());
            return null;
        } catch (ConnectException e2) {
            ALog.e(TAG, "[Get]connect exception", this.f1639c, AlinkConstants.KEY_ERR_MSG, e2.getMessage(), "url", httpUrl.urlString());
            return null;
        } catch (InvalidParameterException e3) {
            ALog.e(TAG, "[Get]param url is invalid", this.f1639c, e3, "url", httpUrl);
            return null;
        } catch (TimeoutException e4) {
            ALog.e(TAG, "[Get]timeout exception", this.f1639c, e4, "url", httpUrl.urlString());
            return null;
        } catch (Exception e5) {
            ALog.e(TAG, "[Get]" + e5.getMessage(), this.f1639c, null, "url", httpUrl.urlString());
            return null;
        }
    }

    public void asyncGet(HttpUrl httpUrl, int i, long j2, SessionGetCallback sessionGetCallback) {
        if (sessionGetCallback == null) {
            throw new NullPointerException("cb is null");
        }
        if (j2 <= 0) {
            throw new InvalidParameterException("timeout must > 0");
        }
        try {
            b(httpUrl, i, j2, sessionGetCallback);
        } catch (Exception unused) {
            sessionGetCallback.onSessionGetFail();
        }
    }

    public void registerSessionInfo(SessionInfo sessionInfo) {
        this.g.a(sessionInfo);
        if (sessionInfo.isKeepAlive) {
            this.h.checkAndStartSession();
        }
    }

    public void unregisterSessionInfo(String str) {
        SessionInfo sessionInfoA = this.g.a(str);
        if (sessionInfoA == null || !sessionInfoA.isKeepAlive) {
            return;
        }
        this.h.checkAndStartSession();
    }

    public void registerAccsSessionListener(ISessionListener iSessionListener) {
        this.h.registerListener(iSessionListener);
    }

    public void unregisterAccsSessionListener(ISessionListener iSessionListener) {
        this.h.unregisterListener(iSessionListener);
    }

    public void registerPublicKey(String str, int i) {
        this.g.a(str, i);
    }

    public static void checkAndStartAccsSession() {
        Iterator<SessionCenter> it = f1637a.values().iterator();
        while (it.hasNext()) {
            it.next().h.checkAndStartSession();
        }
    }

    public void forceRecreateAccsSession() {
        this.h.forceCloseSession(true);
    }

    private SessionRequest a(HttpUrl httpUrl) {
        String cNameByHost = StrategyCenter.getInstance().getCNameByHost(httpUrl.host());
        if (cNameByHost == null) {
            cNameByHost = httpUrl.host();
        }
        String strScheme = httpUrl.scheme();
        if (!httpUrl.isSchemeLocked()) {
            strScheme = StrategyCenter.getInstance().getSchemeByHost(cNameByHost, strScheme);
        }
        return a(StringUtils.concatString(strScheme, HttpConstant.SCHEME_SPLIT, cNameByHost));
    }

    protected Session a(HttpUrl httpUrl, int i, long j2, SessionGetCallback sessionGetCallback) throws Exception {
        SessionInfo sessionInfoB;
        if (!j) {
            ALog.e(TAG, "getInternal not inited!", this.f1639c, new Object[0]);
            throw new IllegalStateException("getInternal not inited");
        }
        if (httpUrl == null) {
            throw new InvalidParameterException("httpUrl is null");
        }
        String str = this.f1639c;
        Object[] objArr = new Object[6];
        objArr[0] = "u";
        objArr[1] = httpUrl.urlString();
        objArr[2] = "sessionType";
        objArr[3] = i == anet.channel.entity.c.f1741a ? "LongLink" : "ShortLink";
        objArr[4] = "timeout";
        objArr[5] = Long.valueOf(j2);
        ALog.d(TAG, "getInternal", str, objArr);
        SessionRequest sessionRequestA = a(httpUrl);
        Session sessionA = this.e.a(sessionRequestA, i);
        if (sessionA != null) {
            ALog.d(TAG, "get internal hit cache session", this.f1639c, UTConstants.E_SDK_CONNECT_SESSION_ACTION, sessionA);
        } else {
            if (this.f1640d == Config.DEFAULT_CONFIG && i != anet.channel.entity.c.f1742b) {
                if (sessionGetCallback == null) {
                    return null;
                }
                sessionGetCallback.onSessionGetFail();
                return null;
            }
            if (GlobalAppRuntimeInfo.isAppBackground() && i == anet.channel.entity.c.f1741a && AwcnConfig.isAccsSessionCreateForbiddenInBg() && (sessionInfoB = this.g.b(httpUrl.host())) != null && sessionInfoB.isAccs) {
                ALog.w(TAG, "app background, forbid to create accs session", this.f1639c, new Object[0]);
                throw new ConnectException("accs session connecting forbidden in background");
            }
            sessionRequestA.a(this.f1638b, i, anet.channel.util.i.a(this.f1639c), sessionGetCallback, j2);
            if (sessionGetCallback == null && j2 > 0 && (i == anet.channel.entity.c.f1743c || sessionRequestA.b() == i)) {
                sessionRequestA.a(j2);
                sessionA = this.e.a(sessionRequestA, i);
                if (sessionA == null) {
                    throw new ConnectException("session connecting failed or timeout");
                }
            }
        }
        return sessionA;
    }

    protected void b(HttpUrl httpUrl, int i, long j2, SessionGetCallback sessionGetCallback) throws Exception {
        SessionInfo sessionInfoB;
        if (!j) {
            ALog.e(TAG, "getInternal not inited!", this.f1639c, new Object[0]);
            throw new IllegalStateException("getInternal not inited");
        }
        if (httpUrl == null) {
            throw new InvalidParameterException("httpUrl is null");
        }
        if (sessionGetCallback == null) {
            throw new InvalidParameterException("sessionGetCallback is null");
        }
        String str = this.f1639c;
        Object[] objArr = new Object[6];
        objArr[0] = "u";
        objArr[1] = httpUrl.urlString();
        objArr[2] = "sessionType";
        objArr[3] = i == anet.channel.entity.c.f1741a ? "LongLink" : "ShortLink";
        objArr[4] = "timeout";
        objArr[5] = Long.valueOf(j2);
        ALog.d(TAG, "getInternal", str, objArr);
        SessionRequest sessionRequestA = a(httpUrl);
        Session sessionA = this.e.a(sessionRequestA, i);
        if (sessionA != null) {
            ALog.d(TAG, "get internal hit cache session", this.f1639c, UTConstants.E_SDK_CONNECT_SESSION_ACTION, sessionA);
            sessionGetCallback.onSessionGetSuccess(sessionA);
            return;
        }
        if (this.f1640d == Config.DEFAULT_CONFIG && i != anet.channel.entity.c.f1742b) {
            sessionGetCallback.onSessionGetFail();
            return;
        }
        if (GlobalAppRuntimeInfo.isAppBackground() && i == anet.channel.entity.c.f1741a && AwcnConfig.isAccsSessionCreateForbiddenInBg() && (sessionInfoB = this.g.b(httpUrl.host())) != null && sessionInfoB.isAccs) {
            ALog.w(TAG, "app background, forbid to create accs session", this.f1639c, new Object[0]);
            throw new ConnectException("accs session connecting forbidden in background");
        }
        sessionRequestA.b(this.f1638b, i, anet.channel.util.i.a(this.f1639c), sessionGetCallback, j2);
    }

    @Deprecated
    public void enterBackground() {
        AppLifecycle.onBackground();
    }

    @Deprecated
    public void enterForeground() {
        AppLifecycle.onForeground();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(l.d dVar) {
        try {
            for (l.b bVar : dVar.f1912b) {
                if (bVar.k) {
                    b(bVar);
                }
                if (bVar.e != null) {
                    a(bVar);
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "checkStrategy failed", this.f1639c, e, new Object[0]);
        }
    }

    private void a(l.b bVar) {
        for (Session session : this.e.a(a(StringUtils.buildKey(bVar.f1907c, bVar.f1905a)))) {
            if (!StringUtils.isStringEqual(session.l, bVar.e)) {
                ALog.i(TAG, "unit change", session.p, "session unit", session.l, "unit", bVar.e);
                session.close(true);
            }
        }
    }

    private void b(l.b bVar) {
        boolean z;
        boolean z2;
        ALog.i(TAG, "find effectNow", this.f1639c, "host", bVar.f1905a);
        l.a[] aVarArr = bVar.h;
        String[] strArr = bVar.f;
        for (Session session : this.e.a(a(StringUtils.buildKey(bVar.f1907c, bVar.f1905a)))) {
            if (!session.getConnType().isHttpType()) {
                int i = 0;
                while (true) {
                    if (i >= strArr.length) {
                        z = false;
                        break;
                    } else {
                        if (session.getIp().equals(strArr[i])) {
                            z = true;
                            break;
                        }
                        i++;
                    }
                }
                if (z) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= aVarArr.length) {
                            z2 = false;
                            break;
                        } else {
                            if (session.getPort() == aVarArr[i2].f1901a && session.getConnType().equals(ConnType.valueOf(ConnProtocol.valueOf(aVarArr[i2])))) {
                                z2 = true;
                                break;
                            }
                            i2++;
                        }
                    }
                    if (!z2) {
                        if (ALog.isPrintLog(2)) {
                            ALog.i(TAG, "aisle not match", session.p, RtspHeaders.Values.PORT, Integer.valueOf(session.getPort()), "connType", session.getConnType(), "aisle", Arrays.toString(aVarArr));
                        }
                        session.close(true);
                    }
                } else {
                    if (ALog.isPrintLog(2)) {
                        ALog.i(TAG, "ip not match", session.p, "session ip", session.getIp(), "ips", Arrays.toString(strArr));
                    }
                    session.close(true);
                }
            }
        }
    }

    protected SessionRequest a(String str) {
        SessionRequest sessionRequest;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        synchronized (this.f) {
            sessionRequest = this.f.get(str);
            if (sessionRequest == null) {
                sessionRequest = new SessionRequest(str, this);
                this.f.put(str, sessionRequest);
            }
        }
        return sessionRequest;
    }

    /* JADX INFO: compiled from: Taobao */
    private class a implements NetworkStatusHelper.INetworkStatusChangeListener, IStrategyListener, AppLifecycle.AppLifecycleListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        boolean f1641a;

        private a() {
            this.f1641a = false;
        }

        /* synthetic */ a(SessionCenter sessionCenter, d dVar) {
            this();
        }

        void a() {
            AppLifecycle.registerLifecycleListener(this);
            NetworkStatusHelper.addStatusChangeListener(this);
            StrategyCenter.getInstance().registerListener(this);
        }

        void b() {
            StrategyCenter.getInstance().unregisterListener(this);
            AppLifecycle.unregisterLifecycleListener(this);
            NetworkStatusHelper.removeStatusChangeListener(this);
        }

        @Override // anet.channel.status.NetworkStatusHelper.INetworkStatusChangeListener
        public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
            ALog.e(SessionCenter.TAG, "onNetworkStatusChanged.", SessionCenter.this.f1639c, "networkStatus", networkStatus);
            List<SessionRequest> listA = SessionCenter.this.e.a();
            if (!listA.isEmpty()) {
                for (SessionRequest sessionRequest : listA) {
                    ALog.d(SessionCenter.TAG, "network change, try recreate session", SessionCenter.this.f1639c, new Object[0]);
                    sessionRequest.a((String) null);
                }
            }
            SessionCenter.this.h.checkAndStartSession();
        }

        @Override // anet.channel.strategy.IStrategyListener
        public void onStrategyUpdated(l.d dVar) {
            SessionCenter.this.a(dVar);
            SessionCenter.this.h.checkAndStartSession();
        }

        @Override // anet.channel.util.AppLifecycle.AppLifecycleListener
        public void forground() {
            ALog.i(SessionCenter.TAG, "[forground]", SessionCenter.this.f1639c, new Object[0]);
            if (SessionCenter.this.f1638b == null || this.f1641a) {
                return;
            }
            this.f1641a = true;
            try {
                if (!SessionCenter.j) {
                    ALog.e(SessionCenter.TAG, "forground not inited!", SessionCenter.this.f1639c, new Object[0]);
                    return;
                }
                try {
                    if (AppLifecycle.lastEnterBackgroundTime != 0 && System.currentTimeMillis() - AppLifecycle.lastEnterBackgroundTime > 60000) {
                        SessionCenter.this.h.forceCloseSession(true);
                    } else {
                        SessionCenter.this.h.checkAndStartSession();
                    }
                } catch (Exception unused) {
                } catch (Throwable th) {
                    this.f1641a = false;
                    throw th;
                }
                this.f1641a = false;
            } catch (Exception unused2) {
            }
        }

        @Override // anet.channel.util.AppLifecycle.AppLifecycleListener
        public void background() {
            ALog.i(SessionCenter.TAG, "[background]", SessionCenter.this.f1639c, new Object[0]);
            if (!SessionCenter.j) {
                ALog.e(SessionCenter.TAG, "background not inited!", SessionCenter.this.f1639c, new Object[0]);
                return;
            }
            try {
                StrategyCenter.getInstance().saveData();
                if (AwcnConfig.isAccsSessionCreateForbiddenInBg() && LocationUtil.MANUFACTURER_OPPO.equalsIgnoreCase(Build.BRAND)) {
                    ALog.i(SessionCenter.TAG, "close session for OPPO", SessionCenter.this.f1639c, new Object[0]);
                    SessionCenter.this.h.forceCloseSession(false);
                }
            } catch (Exception unused) {
            }
        }
    }
}
