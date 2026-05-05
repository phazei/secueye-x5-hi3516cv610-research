package anet.channel.strategy;

import anet.channel.strategy.StrategyList;
import anet.channel.strategy.l;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class j implements StrategyList.Predicate<IPConnStrategy> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ l.a f1896a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f1897b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ ConnProtocol f1898c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ StrategyList f1899d;

    j(StrategyList strategyList, l.a aVar, String str, ConnProtocol connProtocol) {
        this.f1899d = strategyList;
        this.f1896a = aVar;
        this.f1897b = str;
        this.f1898c = connProtocol;
    }

    @Override // anet.channel.strategy.StrategyList.Predicate
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public boolean apply(IPConnStrategy iPConnStrategy) {
        return iPConnStrategy.getPort() == this.f1896a.f1901a && iPConnStrategy.getIp().equals(this.f1897b) && iPConnStrategy.protocol.equals(this.f1898c);
    }
}
