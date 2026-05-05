package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.StrategyStatObject;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.l;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class StrategyInfoHolder implements NetworkStatusHelper.INetworkStatusChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Map<String, StrategyTable> f1851a = new LruStrategyMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    volatile StrategyConfig f1852b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final a f1853c = new a();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final StrategyTable f1854d = new StrategyTable("Unknown");
    private final Set<String> e = new HashSet();
    private volatile String f = "";

    public static StrategyInfoHolder a() {
        return new StrategyInfoHolder();
    }

    private StrategyInfoHolder() {
        try {
            e();
            g();
        } catch (Throwable th) {
            f();
            throw th;
        }
        f();
    }

    void b() {
        NetworkStatusHelper.removeStatusChangeListener(this);
    }

    private void e() {
        NetworkStatusHelper.addStatusChangeListener(this);
        this.f = a(NetworkStatusHelper.getStatus());
    }

    private void f() {
        Iterator<Map.Entry<String, StrategyTable>> it = this.f1851a.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().a();
        }
        synchronized (this) {
            if (this.f1852b == null) {
                StrategyConfig strategyConfig = new StrategyConfig();
                strategyConfig.b();
                strategyConfig.a(this);
                this.f1852b = strategyConfig;
            }
        }
    }

    private void g() {
        ALog.i("awcn.StrategyInfoHolder", "restore", null, new Object[0]);
        String str = this.f;
        if (!AwcnConfig.isAsyncLoadStrategyEnable()) {
            if (!TextUtils.isEmpty(str)) {
                a(str, true);
            }
            this.f1852b = (StrategyConfig) m.a("StrategyConfig", null);
            if (this.f1852b != null) {
                this.f1852b.b();
                this.f1852b.a(this);
            }
        }
        anet.channel.strategy.utils.a.a(new d(this, str));
    }

    protected void a(String str, boolean z) {
        synchronized (this.e) {
            if (this.e.contains(str)) {
                return;
            }
            this.e.add(str);
            StrategyStatObject strategyStatObject = null;
            if (z) {
                strategyStatObject = new StrategyStatObject(0);
                strategyStatObject.readStrategyFileId = str;
            }
            StrategyTable strategyTable = (StrategyTable) m.a(str, strategyStatObject);
            if (strategyTable != null) {
                strategyTable.a();
                synchronized (this.f1851a) {
                    this.f1851a.put(strategyTable.f1859a, strategyTable);
                }
            }
            synchronized (this.e) {
                this.e.remove(str);
            }
            if (z) {
                strategyStatObject.isSucceed = strategyTable != null ? 1 : 0;
                AppMonitor.getInstance().commitStat(strategyStatObject);
            }
        }
    }

    void c() {
        synchronized (this) {
            for (StrategyTable strategyTable : this.f1851a.values()) {
                if (strategyTable.f1862d) {
                    StrategyStatObject strategyStatObject = new StrategyStatObject(1);
                    strategyStatObject.writeStrategyFileId = strategyTable.f1859a;
                    m.a(strategyTable, strategyTable.f1859a, strategyStatObject);
                    strategyTable.f1862d = false;
                }
            }
            m.a(this.f1852b.a(), "StrategyConfig", null);
        }
    }

    StrategyTable d() {
        StrategyTable strategyTable = this.f1854d;
        String str = this.f;
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.f1851a) {
                strategyTable = this.f1851a.get(str);
                if (strategyTable == null) {
                    strategyTable = new StrategyTable(str);
                    this.f1851a.put(str, strategyTable);
                }
            }
        }
        return strategyTable;
    }

    private String a(NetworkStatusHelper.NetworkStatus networkStatus) {
        if (networkStatus.isWifi()) {
            String strMd5ToHex = StringUtils.md5ToHex(NetworkStatusHelper.getWifiBSSID());
            if (TextUtils.isEmpty(strMd5ToHex)) {
                strMd5ToHex = "";
            }
            return "WIFI$" + strMd5ToHex;
        }
        if (!networkStatus.isMobile()) {
            return "";
        }
        return networkStatus.getType() + "$" + NetworkStatusHelper.getApn();
    }

    void a(l.d dVar) {
        if (dVar.g != 0) {
            AmdcRuntimeInfo.updateAmdcLimit(dVar.g, dVar.h);
        }
        d().update(dVar);
        this.f1852b.a(dVar);
    }

    @Override // anet.channel.status.NetworkStatusHelper.INetworkStatusChangeListener
    public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
        this.f = a(networkStatus);
        String str = this.f;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        synchronized (this.f1851a) {
            if (!this.f1851a.containsKey(str)) {
                anet.channel.strategy.utils.a.a(new e(this, str));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    static class LruStrategyMap extends SerialLruCache<String, StrategyTable> {
        public LruStrategyMap() {
            super(3);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // anet.channel.strategy.utils.SerialLruCache
        public boolean entryRemoved(Map.Entry<String, StrategyTable> entry) {
            anet.channel.strategy.utils.a.a(new f(this, entry));
            return true;
        }
    }
}
