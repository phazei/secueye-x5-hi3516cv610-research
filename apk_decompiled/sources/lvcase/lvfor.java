package lvcase;

/* JADX INFO: loaded from: classes4.dex */
public class lvfor extends Exception {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private int f7965lvdo;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f7966lvif;

    public lvfor(int i, String str, String str2) {
        super(str);
        this.f7965lvdo = i;
        this.f7966lvif = str2;
    }

    public lvfor(int i, String str, Throwable th, String str2) {
        super(str, th);
        this.f7965lvdo = i;
        this.f7966lvif = str2;
    }

    public int lvdo() {
        return this.f7965lvdo;
    }
}
