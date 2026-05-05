package a.a.a.a.a.a.b;

/* JADX INFO: compiled from: CommandData.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1124a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f1125b;

    public a(String str, byte[] bArr) {
        this.f1124a = str;
        try {
            this.f1125b = new String(bArr, "UTF-8");
        } catch (Exception unused) {
            this.f1125b = null;
        }
    }

    public String a() {
        return this.f1125b;
    }

    public String b() {
        return this.f1124a;
    }
}
