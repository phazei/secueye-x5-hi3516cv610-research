package anet.channel.util;

import java.net.Inet6Address;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f1946a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Inet6Address f1947b;

    public f(Inet6Address inet6Address, int i) {
        this.f1946a = i;
        this.f1947b = inet6Address;
    }

    public String toString() {
        return this.f1947b.getHostAddress() + "/" + this.f1946a;
    }
}
