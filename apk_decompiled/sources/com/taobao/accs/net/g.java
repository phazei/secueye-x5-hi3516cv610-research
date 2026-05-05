package com.taobao.accs.net;

import anet.channel.entity.ConnType;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.HttpDispatcher;
import com.taobao.accs.utl.ALog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f6378a = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private List<IConnStrategy> f6379b = new ArrayList();

    public g(String str) {
        HttpDispatcher.getInstance().addListener(new h(this));
        a(str);
    }

    public List<IConnStrategy> a(String str) {
        List<IConnStrategy> connStrategyListByHost;
        if ((this.f6378a == 0 || this.f6379b.isEmpty()) && (connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(str)) != null && !connStrategyListByHost.isEmpty()) {
            this.f6379b.clear();
            for (IConnStrategy iConnStrategy : connStrategyListByHost) {
                ConnType connTypeValueOf = ConnType.valueOf(iConnStrategy.getProtocol());
                if (connTypeValueOf.getTypeLevel() == ConnType.TypeLevel.SPDY && connTypeValueOf.isSSL()) {
                    this.f6379b.add(iConnStrategy);
                }
            }
        }
        return this.f6379b;
    }

    public IConnStrategy a() {
        return a(this.f6379b);
    }

    public IConnStrategy a(List<IConnStrategy> list) {
        if (list == null || list.isEmpty()) {
            ALog.d("HttpDnsProvider", "strategys null or 0", new Object[0]);
            return null;
        }
        int i = this.f6378a;
        if (i < 0 || i >= list.size()) {
            this.f6378a = 0;
        }
        return list.get(this.f6378a);
    }

    public void b() {
        this.f6378a++;
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d("HttpDnsProvider", "updateStrategyPos StrategyPos:" + this.f6378a, new Object[0]);
        }
    }

    public int c() {
        return this.f6378a;
    }

    public void b(String str) {
        StrategyCenter.getInstance().forceRefreshStrategy(str);
    }
}
