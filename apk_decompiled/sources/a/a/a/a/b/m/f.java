package a.a.a.a.b.m;

/* JADX INFO: compiled from: MessageIdUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static int f1499a;

    public static synchronized byte a() {
        int i = f1499a;
        if (i >= 255) {
            f1499a = 0;
            return (byte) f1499a;
        }
        f1499a = i + 1;
        return (byte) i;
    }
}
