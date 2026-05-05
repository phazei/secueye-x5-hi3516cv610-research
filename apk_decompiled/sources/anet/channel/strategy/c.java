package anet.channel.strategy;

import anet.channel.util.HttpConstant;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ConcurrentHashMap<String, String> f1869a = new ConcurrentHashMap<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f1870b = true;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static c f1871a = new c();

        private a() {
        }
    }

    public void a(boolean z) {
        this.f1870b = z;
    }

    public String a(String str) {
        if (!this.f1870b) {
            return null;
        }
        String str2 = this.f1869a.get(str);
        if (str2 != null) {
            return str2;
        }
        this.f1869a.put(str, HttpConstant.HTTPS);
        return HttpConstant.HTTPS;
    }

    public void b(String str) {
        this.f1869a.put(str, HttpConstant.HTTP);
    }
}
