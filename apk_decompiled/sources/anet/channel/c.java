package anet.channel;

import android.text.TextUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Map<String, Integer> f1677a = new HashMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Map<String, SessionInfo> f1678b = new ConcurrentHashMap();

    c() {
    }

    void a(SessionInfo sessionInfo) {
        if (sessionInfo == null) {
            throw new NullPointerException("info is null");
        }
        if (TextUtils.isEmpty(sessionInfo.host)) {
            throw new IllegalArgumentException("host cannot be null or empty");
        }
        this.f1678b.put(sessionInfo.host, sessionInfo);
    }

    SessionInfo a(String str) {
        return this.f1678b.remove(str);
    }

    SessionInfo b(String str) {
        return this.f1678b.get(str);
    }

    Collection<SessionInfo> a() {
        return this.f1678b.values();
    }

    void a(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("host cannot be null or empty");
        }
        synchronized (this.f1677a) {
            this.f1677a.put(str, Integer.valueOf(i));
        }
    }

    public int c(String str) {
        Integer num;
        synchronized (this.f1677a) {
            num = this.f1677a.get(str);
        }
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }
}
