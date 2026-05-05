package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.entity.ConnType;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import com.taobao.accs.common.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final ConcurrentHashMap<String, List<IPConnStrategy>> f1864a = new ConcurrentHashMap<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final HashMap<String, Object> f1865b = new HashMap<>();

    a() {
    }

    List a(String str) {
        Object obj;
        if (TextUtils.isEmpty(str) || !anet.channel.strategy.utils.c.c(str) || DispatchConstants.getAmdcServerDomain().equalsIgnoreCase(str)) {
            return Collections.EMPTY_LIST;
        }
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.LocalDnsStrategyTable", "try resolve ip with local dns", null, "host", str);
        }
        List arrayList = Collections.EMPTY_LIST;
        if (!this.f1864a.containsKey(str)) {
            synchronized (this.f1865b) {
                if (!this.f1865b.containsKey(str)) {
                    obj = new Object();
                    this.f1865b.put(str, obj);
                    a(str, obj);
                } else {
                    obj = this.f1865b.get(str);
                }
            }
            if (obj != null) {
                try {
                    synchronized (obj) {
                        obj.wait(500L);
                    }
                } catch (InterruptedException unused) {
                }
            }
        }
        List<IPConnStrategy> list = this.f1864a.get(str);
        if (list != null && list != Collections.EMPTY_LIST) {
            arrayList = new ArrayList(list);
        }
        ALog.i("awcn.LocalDnsStrategyTable", "get local strategy", null, "strategyList", list);
        return arrayList;
    }

    void a(String str, ConnProtocol connProtocol) {
        List<IPConnStrategy> list = this.f1864a.get(str);
        if (list == null || list.isEmpty()) {
            return;
        }
        Iterator<IPConnStrategy> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().getProtocol().equals(connProtocol)) {
                return;
            }
        }
        list.add(IPConnStrategy.a(list.get(0).getIp(), !a(connProtocol) ? 80 : Constants.PORT, connProtocol, 0, 0, 1, 45000));
        ALog.i("awcn.LocalDnsStrategyTable", "setProtocolForHost", null, "strategyList", list);
    }

    void a(String str, IConnStrategy iConnStrategy, ConnEvent connEvent) {
        List<IPConnStrategy> list;
        if (connEvent.isSuccess || TextUtils.isEmpty(str) || connEvent.isAccs || (list = this.f1864a.get(str)) == null || list == Collections.EMPTY_LIST) {
            return;
        }
        Iterator<IPConnStrategy> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() == iConnStrategy) {
                it.remove();
            }
        }
        if (list.isEmpty()) {
            this.f1864a.put(str, Collections.EMPTY_LIST);
        }
    }

    private void a(String str, Object obj) {
        anet.channel.strategy.utils.a.a(new b(this, str, obj));
    }

    boolean a(ConnProtocol connProtocol) {
        return connProtocol.protocol.equalsIgnoreCase(HttpConstant.HTTPS) || connProtocol.protocol.equalsIgnoreCase(ConnType.H2S) || !TextUtils.isEmpty(connProtocol.publicKey);
    }
}
