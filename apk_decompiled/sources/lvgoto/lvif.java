package lvgoto;

/* JADX INFO: loaded from: classes4.dex */
public class lvif {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private String f8017lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private String f8018lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f8019lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private long f8020lvint;

    public lvif(String str, String str2, String str3, long j) {
        lvif(str);
        lvfor(str2);
        lvdo(str3);
        lvdo(j);
    }

    public String lvdo() {
        return this.f8018lvfor;
    }

    public void lvdo(long j) {
        this.f8020lvint = j;
    }

    public void lvdo(String str) {
        this.f8018lvfor = str;
    }

    public String lvfor() {
        return this.f8019lvif;
    }

    public void lvfor(String str) {
        this.f8019lvif = str;
    }

    public String lvif() {
        return this.f8017lvdo;
    }

    public void lvif(String str) {
        this.f8017lvdo = str;
    }

    public String toString() {
        return "FederationToken [tempAk=" + this.f8017lvdo + ", tempSk=" + this.f8019lvif + ", securityToken=" + this.f8018lvfor + ", expiration=" + this.f8020lvint + "]";
    }
}
