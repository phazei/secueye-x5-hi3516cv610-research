package anetwork.channel.interceptor;

import anet.channel.util.ALog;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class InterceptorManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final CopyOnWriteArrayList<Interceptor> f2048a = new CopyOnWriteArrayList<>();

    private InterceptorManager() {
    }

    public static void addInterceptor(Interceptor interceptor) {
        if (f2048a.contains(interceptor)) {
            return;
        }
        f2048a.add(interceptor);
        ALog.i("anet.InterceptorManager", "[addInterceptor]", null, "interceptors", f2048a.toString());
    }

    public static void removeInterceptor(Interceptor interceptor) {
        f2048a.remove(interceptor);
        ALog.i("anet.InterceptorManager", "[remoteInterceptor]", null, "interceptors", f2048a.toString());
    }

    public static Interceptor getInterceptor(int i) {
        return f2048a.get(i);
    }

    public static boolean contains(Interceptor interceptor) {
        return f2048a.contains(interceptor);
    }

    public static int getSize() {
        return f2048a.size();
    }
}
