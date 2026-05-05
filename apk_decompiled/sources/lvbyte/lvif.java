package lvbyte;

/* JADX INFO: loaded from: classes4.dex */
public class lvif {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private int f7955lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private String f7956lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f7957lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private Object f7958lvint;

    public int lvdo() {
        return this.f7955lvdo;
    }

    public void lvdo(int i) {
        this.f7955lvdo = i;
    }

    public void lvdo(Object obj) {
        this.f7958lvint = obj;
    }

    public void lvdo(String str) {
        this.f7956lvfor = str;
    }

    public String lvfor() {
        return this.f7956lvfor;
    }

    public Object lvif() {
        return this.f7958lvint;
    }

    public void lvif(String str) {
        this.f7957lvif = str;
    }

    public String toString() {
        return "APIResponse{code=" + this.f7955lvdo + ", msg='" + this.f7957lvif + "', localizedMsg='" + this.f7956lvfor + "', data=" + this.f7958lvint + '}';
    }
}
