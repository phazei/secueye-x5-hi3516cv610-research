package anet.channel;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.entity.ConnType;
import anet.channel.entity.EventType;
import anet.channel.session.TnetSpdySession;
import anet.channel.statist.AlarmObject;
import anet.channel.statist.SessionConnStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.taobao.accs.common.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class SessionRequest {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    SessionCenter f1643a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    e f1644b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    SessionInfo f1645c;
    volatile Session e;
    private String i;
    private String j;
    private volatile Future k;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    volatile boolean f1646d = false;
    volatile boolean f = false;
    HashMap<SessionGetCallback, c> g = new HashMap<>();
    SessionConnStat h = null;
    private Object l = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    interface IConnCb {
        void onDisConnect(Session session, long j, int i);

        void onFailed(Session session, long j, int i, int i2);

        void onSuccess(Session session, long j);
    }

    SessionRequest(String str, SessionCenter sessionCenter) {
        this.i = str;
        String str2 = this.i;
        this.j = str2.substring(str2.indexOf(HttpConstant.SCHEME_SPLIT) + 3);
        this.f1643a = sessionCenter;
        this.f1645c = sessionCenter.g.b(this.j);
        this.f1644b = sessionCenter.e;
    }

    protected String a() {
        return this.i;
    }

    void a(boolean z) {
        this.f1646d = z;
        if (z) {
            return;
        }
        if (this.k != null) {
            this.k.cancel(true);
            this.k = null;
        }
        this.e = null;
    }

    /* JADX INFO: compiled from: Taobao */
    private class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        String f1651a;

        b(String str) {
            this.f1651a = null;
            this.f1651a = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SessionRequest.this.f1646d) {
                ALog.e("awcn.SessionRequest", "Connecting timeout!!! reset status!", this.f1651a, new Object[0]);
                SessionRequest.this.h.ret = 2;
                SessionRequest.this.h.totalTime = System.currentTimeMillis() - SessionRequest.this.h.start;
                if (SessionRequest.this.e != null) {
                    SessionRequest.this.e.u = false;
                    SessionRequest.this.e.close();
                    SessionRequest.this.h.syncValueFromSession(SessionRequest.this.e);
                }
                AppMonitor.getInstance().commitStat(SessionRequest.this.h);
                SessionRequest.this.a(false);
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    protected class c implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        SessionGetCallback f1653a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        AtomicBoolean f1654b = new AtomicBoolean(false);

        protected c(SessionGetCallback sessionGetCallback) {
            this.f1653a = null;
            this.f1653a = sessionGetCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f1654b.compareAndSet(false, true)) {
                ALog.e("awcn.SessionRequest", "get session timeout", null, new Object[0]);
                synchronized (SessionRequest.this.g) {
                    SessionRequest.this.g.remove(this.f1653a);
                }
                this.f1653a.onSessionGetFail();
            }
        }
    }

    protected synchronized void a(Context context, int i, String str, SessionGetCallback sessionGetCallback, long j) {
        Session sessionA = this.f1644b.a(this, i);
        if (sessionA != null) {
            ALog.d("awcn.SessionRequest", "Available Session exist!!!", str, new Object[0]);
            if (sessionGetCallback != null) {
                sessionGetCallback.onSessionGetSuccess(sessionA);
            }
            return;
        }
        if (TextUtils.isEmpty(str)) {
            str = anet.channel.util.i.a(null);
        }
        ALog.d("awcn.SessionRequest", "SessionRequest start", str, "host", this.i, "type", Integer.valueOf(i));
        if (this.f1646d) {
            ALog.d("awcn.SessionRequest", "session connecting", str, "host", a());
            if (sessionGetCallback != null) {
                if (b() == i) {
                    c cVar = new c(sessionGetCallback);
                    synchronized (this.g) {
                        this.g.put(sessionGetCallback, cVar);
                    }
                    ThreadPoolExecutorFactory.submitScheduledTask(cVar, j, TimeUnit.MILLISECONDS);
                } else {
                    sessionGetCallback.onSessionGetFail();
                }
            }
            return;
        }
        a(true);
        this.k = ThreadPoolExecutorFactory.submitScheduledTask(new b(str), 45L, TimeUnit.SECONDS);
        this.h = new SessionConnStat();
        this.h.start = System.currentTimeMillis();
        if (!NetworkStatusHelper.isConnected()) {
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.SessionRequest", "network is not available, can't create session", str, "isConnected", Boolean.valueOf(NetworkStatusHelper.isConnected()));
            }
            c();
            throw new RuntimeException("no network");
        }
        List<IConnStrategy> listA = a(i, str);
        if (listA.isEmpty()) {
            ALog.i("awcn.SessionRequest", "no avalible strategy, can't create session", str, "host", this.i, "type", Integer.valueOf(i));
            c();
            throw new NoAvailStrategyException("no avalible strategy");
        }
        List<anet.channel.entity.a> listA2 = a(listA, str);
        try {
            anet.channel.entity.a aVarRemove = listA2.remove(0);
            a(context, aVarRemove, new a(context, listA2, aVarRemove), aVarRemove.h());
            if (sessionGetCallback != null) {
                c cVar2 = new c(sessionGetCallback);
                synchronized (this.g) {
                    this.g.put(sessionGetCallback, cVar2);
                }
                ThreadPoolExecutorFactory.submitScheduledTask(cVar2, j, TimeUnit.MILLISECONDS);
            }
        } catch (Throwable unused) {
            c();
        }
        return;
    }

    protected synchronized void b(Context context, int i, String str, SessionGetCallback sessionGetCallback, long j) {
        Session sessionA = this.f1644b.a(this, i);
        if (sessionA != null) {
            ALog.d("awcn.SessionRequest", "Available Session exist!!!", str, new Object[0]);
            sessionGetCallback.onSessionGetSuccess(sessionA);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            str = anet.channel.util.i.a(null);
        }
        ALog.d("awcn.SessionRequest", "SessionRequest start", str, "host", this.i, "type", Integer.valueOf(i));
        if (this.f1646d) {
            ALog.d("awcn.SessionRequest", "session connecting", str, "host", a());
            if (b() == i) {
                c cVar = new c(sessionGetCallback);
                synchronized (this.g) {
                    this.g.put(sessionGetCallback, cVar);
                }
                ThreadPoolExecutorFactory.submitScheduledTask(cVar, j, TimeUnit.MILLISECONDS);
            } else {
                sessionGetCallback.onSessionGetFail();
            }
            return;
        }
        a(true);
        this.k = ThreadPoolExecutorFactory.submitScheduledTask(new b(str), 45L, TimeUnit.SECONDS);
        this.h = new SessionConnStat();
        this.h.start = System.currentTimeMillis();
        if (!NetworkStatusHelper.isConnected()) {
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.SessionRequest", "network is not available, can't create session", str, "isConnected", Boolean.valueOf(NetworkStatusHelper.isConnected()));
            }
            c();
            throw new RuntimeException("no network");
        }
        List<IConnStrategy> listA = a(i, str);
        if (listA.isEmpty()) {
            ALog.i("awcn.SessionRequest", "no avalible strategy, can't create session", str, "host", this.i, "type", Integer.valueOf(i));
            c();
            throw new NoAvailStrategyException("no avalible strategy");
        }
        List<anet.channel.entity.a> listA2 = a(listA, str);
        try {
            anet.channel.entity.a aVarRemove = listA2.remove(0);
            a(context, aVarRemove, new a(context, listA2, aVarRemove), aVarRemove.h());
            c cVar2 = new c(sessionGetCallback);
            synchronized (this.g) {
                this.g.put(sessionGetCallback, cVar2);
            }
            ThreadPoolExecutorFactory.submitScheduledTask(cVar2, j, TimeUnit.MILLISECONDS);
        } catch (Throwable unused) {
            c();
        }
        return;
    }

    /* JADX INFO: compiled from: Taobao */
    class a implements IConnCb {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        boolean f1647a = false;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private Context f1649c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private List<anet.channel.entity.a> f1650d;
        private anet.channel.entity.a e;

        a(Context context, List<anet.channel.entity.a> list, anet.channel.entity.a aVar) {
            this.f1649c = context;
            this.f1650d = list;
            this.e = aVar;
        }

        @Override // anet.channel.SessionRequest.IConnCb
        public void onFailed(Session session, long j, int i, int i2) {
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.SessionRequest", "Connect failed", this.e.h(), UTConstants.E_SDK_CONNECT_SESSION_ACTION, session, "host", SessionRequest.this.a(), "isHandleFinish", Boolean.valueOf(this.f1647a));
            }
            if (SessionRequest.this.f) {
                SessionRequest.this.f = false;
                return;
            }
            if (this.f1647a) {
                return;
            }
            this.f1647a = true;
            SessionRequest.this.f1644b.b(SessionRequest.this, session);
            if (session.u && NetworkStatusHelper.isConnected() && !this.f1650d.isEmpty()) {
                if (ALog.isPrintLog(1)) {
                    ALog.d("awcn.SessionRequest", "use next connInfo to create session", this.e.h(), "host", SessionRequest.this.a());
                }
                if (this.e.f1735b == this.e.f1736c && (i2 == -2003 || i2 == -2410)) {
                    ListIterator<anet.channel.entity.a> listIterator = this.f1650d.listIterator();
                    while (listIterator.hasNext()) {
                        if (session.getIp().equals(listIterator.next().f1734a.getIp())) {
                            listIterator.remove();
                        }
                    }
                }
                if (anet.channel.strategy.utils.c.b(session.getIp())) {
                    ListIterator<anet.channel.entity.a> listIterator2 = this.f1650d.listIterator();
                    while (listIterator2.hasNext()) {
                        if (anet.channel.strategy.utils.c.b(listIterator2.next().f1734a.getIp())) {
                            listIterator2.remove();
                        }
                    }
                }
                if (!this.f1650d.isEmpty()) {
                    anet.channel.entity.a aVarRemove = this.f1650d.remove(0);
                    SessionRequest sessionRequest = SessionRequest.this;
                    Context context = this.f1649c;
                    sessionRequest.a(context, aVarRemove, sessionRequest.new a(context, this.f1650d, aVarRemove), aVarRemove.h());
                    return;
                }
                SessionRequest.this.c();
                SessionRequest.this.a(session, i, i2);
                synchronized (SessionRequest.this.g) {
                    for (Map.Entry<SessionGetCallback, c> entry : SessionRequest.this.g.entrySet()) {
                        c value = entry.getValue();
                        if (value.f1654b.compareAndSet(false, true)) {
                            ThreadPoolExecutorFactory.removeScheduleTask(value);
                            entry.getKey().onSessionGetFail();
                        }
                    }
                    SessionRequest.this.g.clear();
                }
                return;
            }
            SessionRequest.this.c();
            SessionRequest.this.a(session, i, i2);
            synchronized (SessionRequest.this.g) {
                for (Map.Entry<SessionGetCallback, c> entry2 : SessionRequest.this.g.entrySet()) {
                    c value2 = entry2.getValue();
                    if (value2.f1654b.compareAndSet(false, true)) {
                        ThreadPoolExecutorFactory.removeScheduleTask(value2);
                        entry2.getKey().onSessionGetFail();
                    }
                }
                SessionRequest.this.g.clear();
            }
        }

        @Override // anet.channel.SessionRequest.IConnCb
        public void onSuccess(Session session, long j) {
            ALog.d("awcn.SessionRequest", "Connect Success", this.e.h(), UTConstants.E_SDK_CONNECT_SESSION_ACTION, session, "host", SessionRequest.this.a());
            try {
                if (SessionRequest.this.f) {
                    SessionRequest.this.f = false;
                    session.close(false);
                    return;
                }
                SessionRequest.this.f1644b.a(SessionRequest.this, session);
                SessionRequest.this.a(session);
                synchronized (SessionRequest.this.g) {
                    for (Map.Entry<SessionGetCallback, c> entry : SessionRequest.this.g.entrySet()) {
                        c value = entry.getValue();
                        if (value.f1654b.compareAndSet(false, true)) {
                            ThreadPoolExecutorFactory.removeScheduleTask(value);
                            entry.getKey().onSessionGetSuccess(session);
                        }
                    }
                    SessionRequest.this.g.clear();
                }
            } catch (Exception e) {
                ALog.e("awcn.SessionRequest", "[onSuccess]:", this.e.h(), e, new Object[0]);
            } finally {
                SessionRequest.this.c();
            }
        }

        @Override // anet.channel.SessionRequest.IConnCb
        public void onDisConnect(Session session, long j, int i) {
            boolean zIsAppBackground = GlobalAppRuntimeInfo.isAppBackground();
            ALog.d("awcn.SessionRequest", "Connect Disconnect", this.e.h(), UTConstants.E_SDK_CONNECT_SESSION_ACTION, session, "host", SessionRequest.this.a(), "appIsBg", Boolean.valueOf(zIsAppBackground), "isHandleFinish", Boolean.valueOf(this.f1647a));
            SessionRequest.this.f1644b.b(SessionRequest.this, session);
            if (this.f1647a) {
                return;
            }
            this.f1647a = true;
            if (session.t) {
                if (zIsAppBackground && (SessionRequest.this.f1645c == null || !SessionRequest.this.f1645c.isAccs || AwcnConfig.isAccsSessionCreateForbiddenInBg())) {
                    ALog.e("awcn.SessionRequest", "[onDisConnect]app background, don't Recreate", this.e.h(), UTConstants.E_SDK_CONNECT_SESSION_ACTION, session);
                    return;
                }
                if (!NetworkStatusHelper.isConnected()) {
                    ALog.e("awcn.SessionRequest", "[onDisConnect]no network, don't Recreate", this.e.h(), UTConstants.E_SDK_CONNECT_SESSION_ACTION, session);
                    return;
                }
                try {
                    ALog.d("awcn.SessionRequest", "session disconnected, try to recreate session", this.e.h(), new Object[0]);
                    int accsReconnectionDelayPeriod = 10000;
                    if (SessionRequest.this.f1645c != null && SessionRequest.this.f1645c.isAccs) {
                        accsReconnectionDelayPeriod = AwcnConfig.getAccsReconnectionDelayPeriod();
                    }
                    ThreadPoolExecutorFactory.submitScheduledTask(new i(this, session), (long) (Math.random() * ((double) accsReconnectionDelayPeriod)), TimeUnit.MILLISECONDS);
                } catch (Exception unused) {
                }
            }
        }
    }

    void a(Session session) {
        AlarmObject alarmObject = new AlarmObject();
        alarmObject.module = "networkPrefer";
        alarmObject.modulePoint = "policy";
        alarmObject.arg = this.i;
        alarmObject.isSuccess = true;
        AppMonitor.getInstance().commitAlarm(alarmObject);
        this.h.syncValueFromSession(session);
        SessionConnStat sessionConnStat = this.h;
        sessionConnStat.ret = 1;
        sessionConnStat.totalTime = System.currentTimeMillis() - this.h.start;
        AppMonitor.getInstance().commitStat(this.h);
    }

    void a(Session session, int i, int i2) {
        if (256 != i || i2 == -2613 || i2 == -2601) {
            return;
        }
        AlarmObject alarmObject = new AlarmObject();
        alarmObject.module = "networkPrefer";
        alarmObject.modulePoint = "policy";
        alarmObject.arg = this.i;
        alarmObject.errorCode = String.valueOf(i2);
        alarmObject.isSuccess = false;
        AppMonitor.getInstance().commitAlarm(alarmObject);
        SessionConnStat sessionConnStat = this.h;
        sessionConnStat.ret = 0;
        sessionConnStat.appendErrorTrace(i2);
        this.h.errorCode = String.valueOf(i2);
        this.h.totalTime = System.currentTimeMillis() - this.h.start;
        this.h.syncValueFromSession(session);
        AppMonitor.getInstance().commitStat(this.h);
    }

    private List<IConnStrategy> a(int i, String str) {
        HttpUrl httpUrl;
        List<IConnStrategy> connStrategyListByHost = Collections.EMPTY_LIST;
        try {
            httpUrl = HttpUrl.parse(a());
        } catch (Throwable th) {
            ALog.e("awcn.SessionRequest", "", str, th, new Object[0]);
        }
        if (httpUrl == null) {
            return Collections.EMPTY_LIST;
        }
        connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(httpUrl.host());
        if (!connStrategyListByHost.isEmpty()) {
            boolean zEqualsIgnoreCase = HttpConstant.HTTPS.equalsIgnoreCase(httpUrl.scheme());
            boolean zB = anet.channel.util.c.b();
            ListIterator<IConnStrategy> listIterator = connStrategyListByHost.listIterator();
            while (listIterator.hasNext()) {
                IConnStrategy next = listIterator.next();
                ConnType connTypeValueOf = ConnType.valueOf(next.getProtocol());
                if (connTypeValueOf != null) {
                    if (connTypeValueOf.isSSL() == zEqualsIgnoreCase && (i == anet.channel.entity.c.f1743c || connTypeValueOf.getType() == i)) {
                        if (zB && anet.channel.strategy.utils.c.b(next.getIp())) {
                            listIterator.remove();
                        }
                    }
                    listIterator.remove();
                }
            }
        }
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.SessionRequest", "[getAvailStrategy]", str, "strategies", connStrategyListByHost);
        }
        return connStrategyListByHost;
    }

    private List<anet.channel.entity.a> a(List<IConnStrategy> list, String str) {
        if (list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        while (i < list.size()) {
            IConnStrategy iConnStrategy = list.get(i);
            int retryTimes = iConnStrategy.getRetryTimes();
            int i3 = i2;
            for (int i4 = 0; i4 <= retryTimes; i4++) {
                i3++;
                anet.channel.entity.a aVar = new anet.channel.entity.a(a(), str + OpenAccountUIConstants.UNDER_LINE + i3, iConnStrategy);
                aVar.f1735b = i4;
                aVar.f1736c = retryTimes;
                arrayList.add(aVar);
            }
            i++;
            i2 = i3;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Context context, anet.channel.entity.a aVar, IConnCb iConnCb, String str) {
        ConnType connTypeC = aVar.c();
        if (context != null && !connTypeC.isHttpType()) {
            TnetSpdySession tnetSpdySession = new TnetSpdySession(context, aVar);
            tnetSpdySession.initConfig(this.f1643a.f1640d);
            tnetSpdySession.initSessionInfo(this.f1645c);
            tnetSpdySession.setTnetPublicKey(this.f1643a.g.c(this.j));
            this.e = tnetSpdySession;
        } else {
            this.e = new anet.channel.session.d(context, aVar);
        }
        ALog.i("awcn.SessionRequest", "create connection...", str, "Host", a(), "Type", aVar.c(), "IP", aVar.a(), "Port", Integer.valueOf(aVar.b()), "heartbeat", Integer.valueOf(aVar.g()), UTConstants.E_SDK_CONNECT_SESSION_ACTION, this.e);
        a(this.e, iConnCb, System.currentTimeMillis());
        this.e.connect();
        this.h.retryTimes++;
        this.h.startConnect = System.currentTimeMillis();
        if (this.h.retryTimes == 0) {
            this.h.putExtra("firstIp", aVar.a());
        }
    }

    private void a(Session session, IConnCb iConnCb, long j) {
        if (iConnCb == null) {
            return;
        }
        session.registerEventcb(EventType.ALL, new f(this, iConnCb, j));
        session.registerEventcb(1792, new g(this, session));
    }

    protected void b(boolean z) {
        ALog.d("awcn.SessionRequest", "closeSessions", this.f1643a.f1639c, "host", this.i, "autoCreate", Boolean.valueOf(z));
        if (!z && this.e != null) {
            this.e.u = false;
            this.e.close(false);
        }
        List<Session> listA = this.f1644b.a(this);
        if (listA != null) {
            for (Session session : listA) {
                if (session != null) {
                    session.close(z);
                }
            }
        }
    }

    protected void a(String str) {
        ALog.d("awcn.SessionRequest", "reCreateSession", str, "host", this.i);
        b(true);
    }

    protected void a(long j) throws InterruptedException, TimeoutException {
        ALog.d("awcn.SessionRequest", "[await]", null, "timeoutMs", Long.valueOf(j));
        if (j <= 0) {
            return;
        }
        synchronized (this.l) {
            long jCurrentTimeMillis = System.currentTimeMillis() + j;
            while (this.f1646d) {
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                if (jCurrentTimeMillis2 >= jCurrentTimeMillis) {
                    break;
                } else {
                    this.l.wait(jCurrentTimeMillis - jCurrentTimeMillis2);
                }
            }
            if (this.f1646d) {
                throw new TimeoutException();
            }
        }
    }

    protected int b() {
        Session session = this.e;
        if (session != null) {
            return session.j.getType();
        }
        return -1;
    }

    void c() {
        a(false);
        synchronized (this.l) {
            this.l.notifyAll();
        }
    }

    void a(Session session, int i, String str) {
        if (AwcnConfig.isSendConnectInfoByService()) {
            b(session, i, str);
        }
        c(session, i, str);
    }

    private void b(Session session, int i, String str) {
        SessionInfo sessionInfo;
        Context context = GlobalAppRuntimeInfo.getContext();
        if (context == null || (sessionInfo = this.f1645c) == null || !sessionInfo.isAccs) {
            return;
        }
        ALog.e("awcn.SessionRequest", "sendConnectInfoToAccsByService", null, new Object[0]);
        try {
            Intent intent = new Intent(Constants.ACTION_RECEIVE);
            intent.setPackage(context.getPackageName());
            intent.setClassName(context, "com.taobao.accs.data.MsgDistributeService");
            intent.putExtra("command", 103);
            intent.putExtra("host", session.getHost());
            intent.putExtra(Constants.KEY_CENTER_HOST, true);
            boolean zIsAvailable = session.isAvailable();
            if (!zIsAvailable) {
                intent.putExtra("errorCode", i);
                intent.putExtra(Constants.KEY_ERROR_DETAIL, str);
            }
            intent.putExtra(Constants.KEY_CONNECT_AVAILABLE, zIsAvailable);
            intent.putExtra(Constants.KEY_TYPE_INAPP, true);
            if (Build.VERSION.SDK_INT >= 26) {
                context.bindService(intent, new h(this, intent, context), 1);
            } else {
                context.startService(intent);
            }
        } catch (Throwable th) {
            ALog.e("awcn.SessionRequest", "sendConnectInfoToAccsByService", null, th, new Object[0]);
        }
    }

    private void c(Session session, int i, String str) {
        SessionInfo sessionInfo = this.f1645c;
        if (sessionInfo == null || !sessionInfo.isAccs) {
            return;
        }
        ALog.e("awcn.SessionRequest", "sendConnectInfoToAccsByCallBack", null, new Object[0]);
        Intent intent = new Intent("com.taobao.ACCS_CONNECT_INFO");
        intent.putExtra("command", 103);
        intent.putExtra("host", session.getHost());
        intent.putExtra(Constants.KEY_CENTER_HOST, true);
        boolean zIsAvailable = session.isAvailable();
        if (!zIsAvailable) {
            intent.putExtra("errorCode", i);
            intent.putExtra(Constants.KEY_ERROR_DETAIL, str);
        }
        intent.putExtra(Constants.KEY_CONNECT_AVAILABLE, zIsAvailable);
        intent.putExtra(Constants.KEY_TYPE_INAPP, true);
        this.f1643a.h.notifyListener(intent);
    }
}
