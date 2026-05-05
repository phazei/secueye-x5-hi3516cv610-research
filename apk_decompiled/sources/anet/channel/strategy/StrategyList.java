package anet.channel.strategy;

import anet.channel.strategy.l;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.util.ALog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class StrategyList implements Serializable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private List<IPConnStrategy> f1855a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<Integer, ConnHistoryItem> f1856b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f1857c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private transient Comparator<IPConnStrategy> f1858d;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    interface Predicate<T> {
        boolean apply(T t);
    }

    public StrategyList() {
        this.f1855a = new ArrayList();
        this.f1856b = new SerialLruCache(40);
        this.f1857c = false;
        this.f1858d = null;
    }

    StrategyList(List<IPConnStrategy> list) {
        this.f1855a = new ArrayList();
        this.f1856b = new SerialLruCache(40);
        this.f1857c = false;
        this.f1858d = null;
        this.f1855a = list;
    }

    public void checkInit() {
        if (this.f1855a == null) {
            this.f1855a = new ArrayList();
        }
        if (this.f1856b == null) {
            this.f1856b = new SerialLruCache(40);
        }
        Iterator<Map.Entry<Integer, ConnHistoryItem>> it = this.f1856b.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue().d()) {
                it.remove();
            }
        }
        for (IPConnStrategy iPConnStrategy : this.f1855a) {
            if (!this.f1856b.containsKey(Integer.valueOf(iPConnStrategy.getUniqueId()))) {
                this.f1856b.put(Integer.valueOf(iPConnStrategy.getUniqueId()), new ConnHistoryItem());
            }
        }
        Collections.sort(this.f1855a, a());
    }

    public String toString() {
        return new ArrayList(this.f1855a).toString();
    }

    public List<IConnStrategy> getStrategyList() {
        if (this.f1855a.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        LinkedList linkedList = null;
        for (IPConnStrategy iPConnStrategy : this.f1855a) {
            ConnHistoryItem connHistoryItem = this.f1856b.get(Integer.valueOf(iPConnStrategy.getUniqueId()));
            if (connHistoryItem != null && connHistoryItem.c()) {
                ALog.i("awcn.StrategyList", "strategy ban!", null, "strategy", iPConnStrategy);
            } else {
                if (linkedList == null) {
                    linkedList = new LinkedList();
                }
                linkedList.add(iPConnStrategy);
            }
        }
        return linkedList == null ? Collections.EMPTY_LIST : linkedList;
    }

    public void update(l.b bVar) {
        Iterator<IPConnStrategy> it = this.f1855a.iterator();
        while (it.hasNext()) {
            it.next().f1843c = true;
        }
        for (int i = 0; i < bVar.h.length; i++) {
            for (int i2 = 0; i2 < bVar.f.length; i2++) {
                a(bVar.f[i2], 1, bVar.h[i]);
            }
            if (bVar.g != null) {
                this.f1857c = true;
                for (int i3 = 0; i3 < bVar.g.length; i3++) {
                    a(bVar.g[i3], 0, bVar.h[i]);
                }
            } else {
                this.f1857c = false;
            }
        }
        if (bVar.i != null) {
            for (int i4 = 0; i4 < bVar.i.length; i4++) {
                l.e eVar = bVar.i[i4];
                a(eVar.f1915a, anet.channel.strategy.utils.c.c(eVar.f1915a) ? -1 : 1, eVar.f1916b);
            }
        }
        ListIterator<IPConnStrategy> listIterator = this.f1855a.listIterator();
        while (listIterator.hasNext()) {
            if (listIterator.next().f1843c) {
                listIterator.remove();
            }
        }
        Collections.sort(this.f1855a, a());
    }

    private void a(String str, int i, l.a aVar) {
        int iA = a(this.f1855a, new j(this, aVar, str, ConnProtocol.valueOf(aVar)));
        if (iA != -1) {
            IPConnStrategy iPConnStrategy = this.f1855a.get(iA);
            iPConnStrategy.cto = aVar.f1903c;
            iPConnStrategy.rto = aVar.f1904d;
            iPConnStrategy.heartbeat = aVar.f;
            iPConnStrategy.f1841a = i;
            iPConnStrategy.f1842b = 0;
            iPConnStrategy.f1843c = false;
            return;
        }
        IPConnStrategy iPConnStrategyA = IPConnStrategy.a(str, aVar);
        if (iPConnStrategyA != null) {
            iPConnStrategyA.f1841a = i;
            iPConnStrategyA.f1842b = 0;
            if (!this.f1856b.containsKey(Integer.valueOf(iPConnStrategyA.getUniqueId()))) {
                this.f1856b.put(Integer.valueOf(iPConnStrategyA.getUniqueId()), new ConnHistoryItem());
            }
            this.f1855a.add(iPConnStrategyA);
        }
    }

    public boolean shouldRefresh() {
        boolean z = true;
        boolean z2 = true;
        for (IPConnStrategy iPConnStrategy : this.f1855a) {
            if (!this.f1856b.get(Integer.valueOf(iPConnStrategy.getUniqueId())).b()) {
                if (iPConnStrategy.f1841a == 0) {
                    z = false;
                    z2 = false;
                } else {
                    z2 = false;
                }
            }
        }
        return (this.f1857c && z) || z2;
    }

    public void notifyConnEvent(IConnStrategy iConnStrategy, ConnEvent connEvent) {
        if (!(iConnStrategy instanceof IPConnStrategy) || this.f1855a.indexOf(iConnStrategy) == -1) {
            return;
        }
        this.f1856b.get(Integer.valueOf(((IPConnStrategy) iConnStrategy).getUniqueId())).a(connEvent.isSuccess);
        Collections.sort(this.f1855a, this.f1858d);
    }

    private Comparator a() {
        if (this.f1858d == null) {
            this.f1858d = new k(this);
        }
        return this.f1858d;
    }

    private static <T> int a(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null) {
            return -1;
        }
        int i = 0;
        Iterator<T> it = collection.iterator();
        while (it.hasNext() && !predicate.apply(it.next())) {
            i++;
        }
        if (i == collection.size()) {
            return -1;
        }
        return i;
    }
}
