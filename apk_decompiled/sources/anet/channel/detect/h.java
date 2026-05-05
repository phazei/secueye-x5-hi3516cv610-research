package anet.channel.detect;

import anet.channel.Session;
import anet.channel.entity.EventCb;
import anet.channel.request.Request;
import anet.channel.session.TnetSpdySession;
import anet.channel.statist.HorseRaceStat;
import anet.channel.strategy.l;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class h implements EventCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ HorseRaceStat f1703a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ long f1704b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ String f1705c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ l.e f1706d;
    final /* synthetic */ TnetSpdySession e;
    final /* synthetic */ d f;

    h(d dVar, HorseRaceStat horseRaceStat, long j, String str, l.e eVar, TnetSpdySession tnetSpdySession) {
        this.f = dVar;
        this.f1703a = horseRaceStat;
        this.f1704b = j;
        this.f1705c = str;
        this.f1706d = eVar;
        this.e = tnetSpdySession;
    }

    @Override // anet.channel.entity.EventCb
    public void onEvent(Session session, int i, anet.channel.entity.b bVar) {
        if (this.f1703a.connTime != 0) {
            return;
        }
        this.f1703a.connTime = System.currentTimeMillis() - this.f1704b;
        if (i == 1) {
            ALog.i("anet.HorseRaceDetector", "tnetSpdySession connect success", this.f1705c, new Object[0]);
            this.f1703a.connRet = 1;
            HttpUrl httpUrl = HttpUrl.parse(session.getHost() + this.f1706d.f1917c);
            if (httpUrl == null) {
                return;
            }
            this.e.request(new Request.Builder().setUrl(httpUrl).setReadTimeout(this.f1706d.f1916b.f1904d).setRedirectEnable(false).setSeq(this.f1705c).build(), new i(this));
            return;
        }
        this.f1703a.connErrorCode = bVar.f1739b;
        synchronized (this.f1703a) {
            this.f1703a.notify();
        }
    }
}
