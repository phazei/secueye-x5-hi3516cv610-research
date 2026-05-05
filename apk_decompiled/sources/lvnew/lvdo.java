package lvnew;

/* JADX INFO: loaded from: classes4.dex */
public class lvdo {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    int f8045lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    int f8046lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    int f8047lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    int f8048lvint;

    public lvdo() {
    }

    public lvdo(int i, int i2, int i3, int i4) {
        this.f8045lvdo = i;
        this.f8047lvif = i2;
        this.f8046lvfor = i3;
        this.f8048lvint = i4;
    }

    public int lvdo() {
        return this.f8048lvint;
    }

    public boolean lvdo(lvdo lvdoVar) {
        return this.f8045lvdo == lvdoVar.lvfor() && this.f8047lvif == lvdoVar.lvint() && this.f8046lvfor == lvdoVar.lvif() && this.f8048lvint == lvdoVar.lvdo();
    }

    public int lvfor() {
        return this.f8045lvdo;
    }

    public int lvif() {
        return this.f8046lvfor;
    }

    public int lvint() {
        return this.f8047lvif;
    }
}
