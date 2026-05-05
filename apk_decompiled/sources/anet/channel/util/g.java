package anet.channel.util;

import android.util.Base64;
import java.net.InetSocketAddress;
import java.net.Proxy;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static g f1948a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Proxy f1949b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f1950c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f1951d;

    public static g a() {
        return f1948a;
    }

    public Proxy b() {
        return this.f1949b;
    }

    public g(String str, int i, String str2, String str3) {
        this.f1949b = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
        this.f1950c = str2;
        this.f1951d = str3;
    }

    public String c() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.f1950c);
        sb.append(":");
        sb.append(this.f1951d);
        String strEncodeToString = Base64.encodeToString(sb.toString().getBytes(), 0);
        StringBuilder sb2 = new StringBuilder(64);
        sb2.append("Basic ");
        sb2.append(strEncodeToString);
        return sb2.toString();
    }
}
