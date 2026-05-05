package anet.channel.security;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile ISecurityFactory f1812a;

    public static ISecurityFactory a() {
        if (f1812a == null) {
            f1812a = new d();
        }
        return f1812a;
    }
}
