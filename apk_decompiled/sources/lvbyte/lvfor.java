package lvbyte;

/* JADX INFO: loaded from: classes4.dex */
public class lvfor {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private int f7953lvdo;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private int f7954lvif;

    public lvfor(int i) {
        this.f7953lvdo = i;
        this.f7954lvif = i;
    }

    public void lvdo() {
        this.f7954lvif--;
    }

    public void lvfor() {
        this.f7954lvif = this.f7953lvdo;
    }

    public boolean lvif() {
        return this.f7954lvif <= 0;
    }
}
