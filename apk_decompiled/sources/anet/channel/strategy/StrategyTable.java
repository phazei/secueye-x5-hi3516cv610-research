package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.entity.ConnType;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.l;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class StrategyTable implements Serializable {
    protected static Comparator<StrategyCollection> e = new o();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected String f1859a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected volatile String f1860b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    Map<String, Long> f1861c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected transient boolean f1862d = false;
    private HostLruCache f;
    private volatile transient int g;

    /* JADX INFO: compiled from: Taobao */
    private static class HostLruCache extends SerialLruCache<String, StrategyCollection> {
        public HostLruCache(int i) {
            super(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // anet.channel.strategy.utils.SerialLruCache
        public boolean entryRemoved(Map.Entry<String, StrategyCollection> entry) {
            if (!entry.getValue().f1847d) {
                return true;
            }
            Iterator it = entrySet().iterator();
            while (it.hasNext()) {
                if (!((StrategyCollection) ((Map.Entry) it.next()).getValue()).f1847d) {
                    it.remove();
                    return false;
                }
            }
            return false;
        }
    }

    protected StrategyTable(String str) {
        this.f1859a = str;
        a();
    }

    private void b() {
        if (HttpDispatcher.getInstance().isInitHostsChanged(this.f1859a)) {
            for (String str : HttpDispatcher.getInstance().getInitHosts()) {
                this.f.put(str, new StrategyCollection(str));
            }
        }
    }

    protected void a() {
        if (this.f == null) {
            this.f = new HostLruCache(256);
            b();
        }
        Iterator it = this.f.values().iterator();
        while (it.hasNext()) {
            ((StrategyCollection) it.next()).checkInit();
        }
        ALog.i("awcn.StrategyTable", "strategy map", null, "size", Integer.valueOf(this.f.size()));
        this.g = GlobalAppRuntimeInfo.isTargetProcess() ? 0 : -1;
        if (this.f1861c == null) {
            this.f1861c = new ConcurrentHashMap();
        }
    }

    public List<IConnStrategy> queryByHost(String str) {
        StrategyCollection strategyCollection;
        if (TextUtils.isEmpty(str) || !anet.channel.strategy.utils.c.c(str)) {
            return Collections.EMPTY_LIST;
        }
        c();
        synchronized (this.f) {
            strategyCollection = (StrategyCollection) this.f.get(str);
            if (strategyCollection == null) {
                strategyCollection = new StrategyCollection(str);
                this.f.put(str, strategyCollection);
            }
        }
        if (strategyCollection.f1845b == 0 || (strategyCollection.isExpired() && AmdcRuntimeInfo.getAmdcLimitLevel() == 0)) {
            a(str);
        }
        return strategyCollection.queryStrategyList();
    }

    public String getCnameByHost(String str) {
        StrategyCollection strategyCollection;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        synchronized (this.f) {
            strategyCollection = (StrategyCollection) this.f.get(str);
        }
        if (strategyCollection != null && strategyCollection.isExpired() && AmdcRuntimeInfo.getAmdcLimitLevel() == 0) {
            a(str);
        }
        if (strategyCollection != null) {
            return strategyCollection.f1846c;
        }
        return null;
    }

    public void update(l.d dVar) {
        ALog.i("awcn.StrategyTable", "update strategyTable with httpDns response", this.f1859a, new Object[0]);
        try {
            this.f1860b = dVar.f1911a;
            this.g = dVar.f;
            l.b[] bVarArr = dVar.f1912b;
            if (bVarArr == null) {
                return;
            }
            synchronized (this.f) {
                for (l.b bVar : bVarArr) {
                    if (bVar != null && bVar.f1905a != null) {
                        if (bVar.j) {
                            this.f.remove(bVar.f1905a);
                        } else {
                            StrategyCollection strategyCollection = (StrategyCollection) this.f.get(bVar.f1905a);
                            if (strategyCollection == null) {
                                strategyCollection = new StrategyCollection(bVar.f1905a);
                                this.f.put(bVar.f1905a, strategyCollection);
                            }
                            strategyCollection.update(bVar);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            ALog.e("awcn.StrategyTable", "fail to update strategyTable", this.f1859a, th, new Object[0]);
        }
        this.f1862d = true;
        if (ALog.isPrintLog(1)) {
            StringBuilder sb = new StringBuilder("uniqueId : ");
            sb.append(this.f1859a);
            sb.append("\n-------------------------domains:------------------------------------");
            ALog.d("awcn.StrategyTable", sb.toString(), null, new Object[0]);
            synchronized (this.f) {
                for (Map.Entry entry : this.f.entrySet()) {
                    sb.setLength(0);
                    sb.append((String) entry.getKey());
                    sb.append(" = ");
                    sb.append(((StrategyCollection) entry.getValue()).toString());
                    ALog.d("awcn.StrategyTable", sb.toString(), null, new Object[0]);
                }
            }
        }
    }

    private void a(String str) {
        TreeSet treeSet = new TreeSet();
        treeSet.add(str);
        a(treeSet);
    }

    protected void a(String str, boolean z) {
        StrategyCollection strategyCollection;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        synchronized (this.f) {
            strategyCollection = (StrategyCollection) this.f.get(str);
            if (strategyCollection == null) {
                strategyCollection = new StrategyCollection(str);
                this.f.put(str, strategyCollection);
            }
        }
        if (z || strategyCollection.f1845b == 0 || (strategyCollection.isExpired() && AmdcRuntimeInfo.getAmdcLimitLevel() == 0)) {
            a(str);
        }
    }

    private void a(Set<String> set) {
        if (set == null || set.isEmpty()) {
            return;
        }
        if ((GlobalAppRuntimeInfo.isAppBackground() && AppLifecycle.lastEnterBackgroundTime > 0) || !NetworkStatusHelper.isConnected()) {
            ALog.i("awcn.StrategyTable", "app in background or no network", this.f1859a, new Object[0]);
            return;
        }
        int amdcLimitLevel = AmdcRuntimeInfo.getAmdcLimitLevel();
        if (amdcLimitLevel == 3) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        synchronized (this.f) {
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                StrategyCollection strategyCollection = (StrategyCollection) this.f.get(it.next());
                if (strategyCollection != null) {
                    strategyCollection.f1845b = 30000 + jCurrentTimeMillis;
                }
            }
        }
        if (amdcLimitLevel == 0) {
            b(set);
        }
        HttpDispatcher.getInstance().sendAmdcRequest(set, this.g);
    }

    private void b(Set<String> set) {
        TreeSet<StrategyCollection> treeSet = new TreeSet(e);
        synchronized (this.f) {
            treeSet.addAll(this.f.values());
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        for (StrategyCollection strategyCollection : treeSet) {
            if (!strategyCollection.isExpired() || set.size() >= 40) {
                return;
            }
            strategyCollection.f1845b = 30000 + jCurrentTimeMillis;
            set.add(strategyCollection.f1844a);
        }
    }

    private void c() {
        try {
            if (HttpDispatcher.getInstance().isInitHostsChanged(this.f1859a)) {
                TreeSet treeSet = null;
                synchronized (this.f) {
                    for (String str : HttpDispatcher.getInstance().getInitHosts()) {
                        if (!this.f.containsKey(str)) {
                            this.f.put(str, new StrategyCollection(str));
                            if (treeSet == null) {
                                treeSet = new TreeSet();
                            }
                            treeSet.add(str);
                        }
                    }
                }
                if (treeSet != null) {
                    a(treeSet);
                }
            }
        } catch (Exception e2) {
            ALog.e("awcn.StrategyTable", "checkInitHost failed", this.f1859a, e2, new Object[0]);
        }
    }

    void a(String str, IConnStrategy iConnStrategy, ConnEvent connEvent) {
        StrategyCollection strategyCollection;
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.StrategyTable", "[notifyConnEvent]", null, "Host", str, "IConnStrategy", iConnStrategy, "ConnEvent", connEvent);
        }
        String str2 = iConnStrategy.getProtocol().protocol;
        if (ConnType.HTTP3.equals(str2) || ConnType.HTTP3_PLAIN.equals(str2)) {
            anet.channel.e.a.a(connEvent.isSuccess);
            ALog.e("awcn.StrategyTable", "enable http3", null, "uniqueId", this.f1859a, "enable", Boolean.valueOf(connEvent.isSuccess));
        }
        if (!connEvent.isSuccess && anet.channel.strategy.utils.c.b(iConnStrategy.getIp())) {
            this.f1861c.put(str, Long.valueOf(System.currentTimeMillis()));
            ALog.e("awcn.StrategyTable", "disable ipv6", null, "uniqueId", this.f1859a, "host", str);
        }
        synchronized (this.f) {
            strategyCollection = (StrategyCollection) this.f.get(str);
        }
        if (strategyCollection != null) {
            strategyCollection.notifyConnEvent(iConnStrategy, connEvent);
        }
    }

    boolean a(String str, long j) {
        Long l = this.f1861c.get(str);
        if (l == null) {
            return false;
        }
        if (l.longValue() + j >= System.currentTimeMillis()) {
            return true;
        }
        this.f1861c.remove(str);
        return false;
    }
}
