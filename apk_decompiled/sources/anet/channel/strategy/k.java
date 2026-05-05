package anet.channel.strategy;

import java.util.Comparator;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class k implements Comparator<IPConnStrategy> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ StrategyList f1900a;

    k(StrategyList strategyList) {
        this.f1900a = strategyList;
    }

    @Override // java.util.Comparator
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(IPConnStrategy iPConnStrategy, IPConnStrategy iPConnStrategy2) {
        ConnHistoryItem connHistoryItem = (ConnHistoryItem) this.f1900a.f1856b.get(Integer.valueOf(iPConnStrategy.getUniqueId()));
        ConnHistoryItem connHistoryItem2 = (ConnHistoryItem) this.f1900a.f1856b.get(Integer.valueOf(iPConnStrategy2.getUniqueId()));
        int iA = connHistoryItem.a();
        int iA2 = connHistoryItem2.a();
        if (iA != iA2) {
            return iA - iA2;
        }
        if (iPConnStrategy.f1841a != iPConnStrategy2.f1841a) {
            return iPConnStrategy.f1841a - iPConnStrategy2.f1841a;
        }
        return iPConnStrategy.protocol.isHttp - iPConnStrategy2.protocol.isHttp;
    }
}
