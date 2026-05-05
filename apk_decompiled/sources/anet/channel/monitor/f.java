package anet.channel.monitor;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected long f1791b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final double f1792c = 40.0d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    boolean f1790a = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f1793d = true;

    public int a() {
        return 0;
    }

    public boolean a(double d2) {
        return d2 < 40.0d;
    }

    protected final boolean b() {
        if (!this.f1793d) {
            return false;
        }
        if (System.currentTimeMillis() - this.f1791b <= a() * 1000) {
            return true;
        }
        this.f1793d = false;
        return false;
    }
}
