package anet.channel.strategy.dispatch;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.util.ALog;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class a {
    public static final String TAG = "awcn.AmdcThreadPoolExecutor";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Random f1879b = new Random();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Map<String, Object> f1880a;

    a() {
    }

    public void a(Map<String, Object> map) {
        try {
            map.put("Env", GlobalAppRuntimeInfo.getEnv());
            synchronized (this) {
                if (this.f1880a == null) {
                    this.f1880a = map;
                    int iNextInt = f1879b.nextInt(3000) + 2000;
                    ALog.i(TAG, "merge amdc request", null, "delay", Integer.valueOf(iNextInt));
                    anet.channel.strategy.utils.a.a(new RunnableC0173a(), iNextInt);
                } else {
                    Set set = (Set) this.f1880a.get(DispatchConstants.HOSTS);
                    Set set2 = (Set) map.get(DispatchConstants.HOSTS);
                    if (map.get("Env") != this.f1880a.get("Env")) {
                        this.f1880a = map;
                    } else if (set.size() + set2.size() <= 40) {
                        set2.addAll(set);
                        this.f1880a = map;
                    } else {
                        anet.channel.strategy.utils.a.a(new RunnableC0173a(map));
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: renamed from: anet.channel.strategy.dispatch.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: Taobao */
    private class RunnableC0173a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Map<String, Object> f1882b;

        RunnableC0173a(Map<String, Object> map) {
            this.f1882b = map;
        }

        RunnableC0173a() {
        }

        @Override // java.lang.Runnable
        public void run() throws Throwable {
            Map<String, Object> map;
            try {
                Map<String, Object> map2 = this.f1882b;
                if (map2 == null) {
                    synchronized (a.class) {
                        map = a.this.f1880a;
                        a.this.f1880a = null;
                    }
                    map2 = map;
                }
                if (NetworkStatusHelper.isConnected()) {
                    if (GlobalAppRuntimeInfo.getEnv() != map2.get("Env")) {
                        ALog.w(a.TAG, "task's env changed", null, new Object[0]);
                    } else {
                        b.a(d.a(map2));
                    }
                }
            } catch (Exception e) {
                ALog.e(a.TAG, "exec amdc task failed.", null, e, new Object[0]);
            }
        }
    }
}
