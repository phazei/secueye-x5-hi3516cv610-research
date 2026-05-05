package lvgoto;

/* JADX INFO: loaded from: classes4.dex */
public class lvint extends lvdo {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private String f8021lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private String f8022lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f8023lvif;

    public lvint(String str, String str2, String str3) {
        lvdo(str.trim());
        lvif(str2.trim());
        lvfor(str3.trim());
    }

    public lvif lvdo() {
        return new lvif(this.f8021lvdo, this.f8023lvif, this.f8022lvfor, Long.MAX_VALUE);
    }

    public void lvdo(String str) {
        this.f8021lvdo = str;
    }

    public void lvfor(String str) {
        this.f8022lvfor = str;
    }

    public void lvif(String str) {
        this.f8023lvif = str;
    }
}
