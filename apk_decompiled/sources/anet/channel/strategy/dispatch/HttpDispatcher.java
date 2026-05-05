package anet.channel.strategy.dispatch;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.util.ALog;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class HttpDispatcher {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private CopyOnWriteArraySet<IDispatchEventListener> f1874a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private anet.channel.strategy.dispatch.a f1875b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile boolean f1876c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Set<String> f1877d;
    private Set<String> e;
    private AtomicBoolean f;

    /* JADX INFO: compiled from: Taobao */
    public interface IDispatchEventListener {
        void onEvent(DispatchEvent dispatchEvent);
    }

    /* JADX INFO: compiled from: Taobao */
    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static HttpDispatcher f1878a = new HttpDispatcher();

        private a() {
        }
    }

    public static HttpDispatcher getInstance() {
        return a.f1878a;
    }

    private HttpDispatcher() {
        this.f1874a = new CopyOnWriteArraySet<>();
        this.f1875b = new anet.channel.strategy.dispatch.a();
        this.f1876c = true;
        this.f1877d = Collections.newSetFromMap(new ConcurrentHashMap());
        this.e = new TreeSet();
        this.f = new AtomicBoolean();
        a();
    }

    public void sendAmdcRequest(Set<String> set, int i) {
        if (!this.f1876c || set == null || set.isEmpty()) {
            ALog.e("awcn.HttpDispatcher", "invalid parameter", null, new Object[0]);
            return;
        }
        if (ALog.isPrintLog(2)) {
            ALog.i("awcn.HttpDispatcher", "sendAmdcRequest", null, DispatchConstants.HOSTS, set.toString());
        }
        HashMap map = new HashMap();
        map.put(DispatchConstants.HOSTS, set);
        map.put(DispatchConstants.CONFIG_VERSION, String.valueOf(i));
        this.f1875b.a(map);
    }

    public void addListener(IDispatchEventListener iDispatchEventListener) {
        this.f1874a.add(iDispatchEventListener);
    }

    public void removeListener(IDispatchEventListener iDispatchEventListener) {
        this.f1874a.remove(iDispatchEventListener);
    }

    void a(DispatchEvent dispatchEvent) {
        Iterator<IDispatchEventListener> it = this.f1874a.iterator();
        while (it.hasNext()) {
            try {
                it.next().onEvent(dispatchEvent);
            } catch (Exception unused) {
            }
        }
    }

    public void setEnable(boolean z) {
        this.f1876c = z;
    }

    public synchronized void addHosts(List<String> list) {
        if (list != null) {
            this.e.addAll(list);
            this.f1877d.clear();
        }
    }

    public static void setInitHosts(List<String> list) {
        if (list != null) {
            DispatchConstants.initHostArray = (String[]) list.toArray(new String[0]);
        }
    }

    public synchronized Set<String> getInitHosts() {
        a();
        return new HashSet(this.e);
    }

    private void a() {
        if (this.f.get() || GlobalAppRuntimeInfo.getContext() == null || !this.f.compareAndSet(false, true)) {
            return;
        }
        this.e.add(DispatchConstants.getAmdcServerDomain());
        if (GlobalAppRuntimeInfo.isTargetProcess()) {
            this.e.addAll(Arrays.asList(DispatchConstants.initHostArray));
        }
    }

    public boolean isInitHostsChanged(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        boolean zContains = this.f1877d.contains(str);
        if (!zContains) {
            this.f1877d.add(str);
        }
        return !zContains;
    }

    public void switchENV() {
        this.f1877d.clear();
        this.e.clear();
        this.f.set(false);
    }
}
