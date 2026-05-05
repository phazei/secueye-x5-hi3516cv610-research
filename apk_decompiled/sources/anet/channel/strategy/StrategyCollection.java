package anet.channel.strategy;

import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.PolicyVersionStat;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.strategy.l;
import anet.channel.util.ALog;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class StrategyCollection implements Serializable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    String f1844a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    volatile long f1845b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    volatile String f1846c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    boolean f1847d;
    int e;
    private StrategyList f;
    private transient long g;
    private transient boolean h;

    public StrategyCollection() {
        this.f = null;
        this.f1845b = 0L;
        this.f1846c = null;
        this.f1847d = false;
        this.e = 0;
        this.g = 0L;
        this.h = true;
    }

    protected StrategyCollection(String str) {
        this.f = null;
        this.f1845b = 0L;
        this.f1846c = null;
        this.f1847d = false;
        this.e = 0;
        this.g = 0L;
        this.h = true;
        this.f1844a = str;
        this.f1847d = DispatchConstants.isAmdcServerDomain(str);
    }

    public synchronized void checkInit() {
        if (System.currentTimeMillis() - this.f1845b > 172800000) {
            this.f = null;
        } else {
            if (this.f != null) {
                this.f.checkInit();
            }
        }
    }

    public synchronized List<IConnStrategy> queryStrategyList() {
        if (this.f == null) {
            return Collections.EMPTY_LIST;
        }
        if (this.h) {
            this.h = false;
            PolicyVersionStat policyVersionStat = new PolicyVersionStat(this.f1844a, this.e);
            policyVersionStat.reportType = 0;
            AppMonitor.getInstance().commitStat(policyVersionStat);
        }
        return this.f.getStrategyList();
    }

    public synchronized void notifyConnEvent(IConnStrategy iConnStrategy, ConnEvent connEvent) {
        if (this.f != null) {
            this.f.notifyConnEvent(iConnStrategy, connEvent);
            if (!connEvent.isSuccess && this.f.shouldRefresh()) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (jCurrentTimeMillis - this.g > 60000) {
                    StrategyCenter.getInstance().forceRefreshStrategy(this.f1844a);
                    this.g = jCurrentTimeMillis;
                }
            }
        }
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.f1845b;
    }

    public synchronized void update(l.b bVar) {
        this.f1845b = System.currentTimeMillis() + (((long) bVar.f1906b) * 1000);
        if (!bVar.f1905a.equalsIgnoreCase(this.f1844a)) {
            ALog.e("StrategyCollection", "update error!", null, "host", this.f1844a, "dnsInfo.host", bVar.f1905a);
            return;
        }
        if (this.e != bVar.l) {
            this.e = bVar.l;
            PolicyVersionStat policyVersionStat = new PolicyVersionStat(this.f1844a, this.e);
            policyVersionStat.reportType = 1;
            AppMonitor.getInstance().commitStat(policyVersionStat);
        }
        this.f1846c = bVar.f1908d;
        if ((bVar.f != null && bVar.f.length != 0 && bVar.h != null && bVar.h.length != 0) || (bVar.i != null && bVar.i.length != 0)) {
            if (this.f == null) {
                this.f = new StrategyList();
            }
            this.f.update(bVar);
            return;
        }
        this.f = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("\nStrategyList = ");
        sb.append(this.f1845b);
        StrategyList strategyList = this.f;
        if (strategyList != null) {
            sb.append(strategyList.toString());
        } else if (this.f1846c != null) {
            sb.append('[');
            sb.append(this.f1844a);
            sb.append("=>");
            sb.append(this.f1846c);
            sb.append(']');
        } else {
            sb.append("[]");
        }
        return sb.toString();
    }
}
