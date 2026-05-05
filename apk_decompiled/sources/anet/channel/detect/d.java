package anet.channel.detect;

import android.content.Context;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.RequestCb;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.entity.ConnType;
import anet.channel.request.Request;
import anet.channel.session.TnetSpdySession;
import anet.channel.session.b;
import anet.channel.statist.HorseRaceStat;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.l;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.android.netutil.PingResponse;
import org.android.netutil.PingTask;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    TreeMap<String, l.c> f1698a = new TreeMap<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private AtomicInteger f1699b = new AtomicInteger(1);

    d() {
    }

    void a() {
        ALog.e("anet.HorseRaceDetector", "network detect thread start", null, new Object[0]);
        while (true) {
            synchronized (this.f1698a) {
                if (!AwcnConfig.isHorseRaceEnable()) {
                    this.f1698a.clear();
                    return;
                }
                Map.Entry<String, l.c> entryPollFirstEntry = this.f1698a.pollFirstEntry();
                if (entryPollFirstEntry == null) {
                    return;
                }
                try {
                    a(entryPollFirstEntry.getValue());
                } catch (Exception e) {
                    ALog.e("anet.HorseRaceDetector", "start hr task failed", null, e, new Object[0]);
                }
            }
        }
    }

    public void b() {
        StrategyCenter.getInstance().registerListener(new e(this));
        AppLifecycle.registerLifecycleListener(new f(this));
    }

    private void a(l.c cVar) {
        if (cVar.f1910b == null || cVar.f1910b.length == 0) {
            return;
        }
        String str = cVar.f1909a;
        for (int i = 0; i < cVar.f1910b.length; i++) {
            l.e eVar = cVar.f1910b[i];
            String str2 = eVar.f1916b.f1902b;
            if (str2.equalsIgnoreCase(HttpConstant.HTTP) || str2.equalsIgnoreCase(HttpConstant.HTTPS)) {
                a(str, eVar);
            } else if (str2.equalsIgnoreCase(ConnType.HTTP2) || str2.equalsIgnoreCase(ConnType.SPDY) || str2.equalsIgnoreCase(ConnType.QUIC)) {
                b(str, eVar);
            } else if (str2.equalsIgnoreCase("tcp")) {
                c(str, eVar);
            }
        }
    }

    private void a(String str, l.e eVar) {
        HttpUrl httpUrl = HttpUrl.parse(eVar.f1916b.f1902b + HttpConstant.SCHEME_SPLIT + str + eVar.f1917c);
        if (httpUrl == null) {
            return;
        }
        ALog.i("anet.HorseRaceDetector", "startShortLinkTask", null, "url", httpUrl);
        Request requestBuild = new Request.Builder().setUrl(httpUrl).addHeader("Connection", "close").setConnectTimeout(eVar.f1916b.f1903c).setReadTimeout(eVar.f1916b.f1904d).setRedirectEnable(false).setSslSocketFactory(new anet.channel.util.j(str)).setSeq("HR" + this.f1699b.getAndIncrement()).build();
        requestBuild.setDnsOptimize(eVar.f1915a, eVar.f1916b.f1901a);
        long jCurrentTimeMillis = System.currentTimeMillis();
        b.a aVarA = anet.channel.session.b.a(requestBuild, (RequestCb) null);
        long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
        HorseRaceStat horseRaceStat = new HorseRaceStat(str, eVar);
        horseRaceStat.connTime = jCurrentTimeMillis2;
        if (aVarA.f1817a <= 0) {
            horseRaceStat.connErrorCode = aVarA.f1817a;
        } else {
            horseRaceStat.connRet = 1;
            horseRaceStat.reqRet = aVarA.f1817a != 200 ? 0 : 1;
            horseRaceStat.reqErrorCode = aVarA.f1817a;
            horseRaceStat.reqTime = horseRaceStat.connTime;
        }
        a(eVar.f1915a, horseRaceStat);
        AppMonitor.getInstance().commitStat(horseRaceStat);
    }

    private void b(String str, l.e eVar) {
        ConnProtocol connProtocolValueOf = ConnProtocol.valueOf(eVar.f1916b);
        ConnType connTypeValueOf = ConnType.valueOf(connProtocolValueOf);
        if (connTypeValueOf == null) {
            return;
        }
        ALog.i("anet.HorseRaceDetector", "startLongLinkTask", null, "host", str, "ip", eVar.f1915a, RtspHeaders.Values.PORT, Integer.valueOf(eVar.f1916b.f1901a), "protocol", connProtocolValueOf);
        String str2 = "HR" + this.f1699b.getAndIncrement();
        Context context = GlobalAppRuntimeInfo.getContext();
        StringBuilder sb = new StringBuilder();
        sb.append(connTypeValueOf.isSSL() ? "https://" : "http://");
        sb.append(str);
        TnetSpdySession tnetSpdySession = new TnetSpdySession(context, new anet.channel.entity.a(sb.toString(), str2, a(connProtocolValueOf, eVar)));
        HorseRaceStat horseRaceStat = new HorseRaceStat(str, eVar);
        long jCurrentTimeMillis = System.currentTimeMillis();
        tnetSpdySession.registerEventcb(257, new h(this, horseRaceStat, jCurrentTimeMillis, str2, eVar, tnetSpdySession));
        tnetSpdySession.connect();
        synchronized (horseRaceStat) {
            try {
                horseRaceStat.wait(eVar.f1916b.f1903c == 0 ? 10000 : eVar.f1916b.f1903c);
                if (horseRaceStat.connTime == 0) {
                    horseRaceStat.connTime = System.currentTimeMillis() - jCurrentTimeMillis;
                }
                a(eVar.f1915a, horseRaceStat);
                AppMonitor.getInstance().commitStat(horseRaceStat);
            } catch (InterruptedException unused) {
            }
        }
        tnetSpdySession.close(false);
    }

    private static IConnStrategy a(ConnProtocol connProtocol, l.e eVar) {
        return new j(eVar, connProtocol);
    }

    private void c(String str, l.e eVar) {
        String str2 = "HR" + this.f1699b.getAndIncrement();
        ALog.i("anet.HorseRaceDetector", "startTcpTask", str2, "ip", eVar.f1915a, RtspHeaders.Values.PORT, Integer.valueOf(eVar.f1916b.f1901a));
        HorseRaceStat horseRaceStat = new HorseRaceStat(str, eVar);
        long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            Socket socket = new Socket(eVar.f1915a, eVar.f1916b.f1901a);
            socket.setSoTimeout(eVar.f1916b.f1903c == 0 ? 10000 : eVar.f1916b.f1903c);
            ALog.i("anet.HorseRaceDetector", "socket connect success", str2, new Object[0]);
            horseRaceStat.connRet = 1;
            horseRaceStat.connTime = System.currentTimeMillis() - jCurrentTimeMillis;
            socket.close();
        } catch (IOException unused) {
            horseRaceStat.connTime = System.currentTimeMillis() - jCurrentTimeMillis;
            horseRaceStat.connErrorCode = -404;
        }
        AppMonitor.getInstance().commitStat(horseRaceStat);
    }

    private void a(String str, HorseRaceStat horseRaceStat) {
        if (AwcnConfig.isPing6Enable() && anet.channel.strategy.utils.c.b(str)) {
            try {
                PingResponse pingResponse = (PingResponse) new PingTask(str, 1000, 3, 0, 0).launch().get();
                if (pingResponse == null) {
                    return;
                }
                horseRaceStat.pingSuccessCount = pingResponse.getSuccessCnt();
                horseRaceStat.pingTimeoutCount = 3 - horseRaceStat.pingSuccessCount;
                horseRaceStat.localIP = pingResponse.getLocalIPStr();
            } catch (Throwable th) {
                ALog.e("anet.HorseRaceDetector", "ping6 task fail.", null, th, new Object[0]);
            }
        }
    }
}
