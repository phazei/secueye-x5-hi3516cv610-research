package a.a.a.a.a.a.a.a;

/* JADX INFO: compiled from: InexpensiveControlRsp.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public byte[] f1110a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public byte f1111b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public byte f1112c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f1113d;
    public int e;
    public byte[] f;

    public a(byte[] bArr) {
        this.f1110a = bArr;
    }

    public static a a(byte[] bArr) {
        if (bArr == 0) {
            a.a.a.a.b.m.a.b("InexpensiveControlRsp", "data is null");
            return null;
        }
        if (bArr.length < 6) {
            a.a.a.a.b.m.a.b("InexpensiveControlRsp", "data length illegal. " + bArr.length);
            return null;
        }
        a aVar = new a(bArr);
        aVar.f1111b = bArr[1];
        aVar.f1112c = bArr[2];
        aVar.f1113d = (bArr[3] >> 4) & 15;
        aVar.e = bArr[3] & 15;
        if (aVar.e <= aVar.f1113d) {
            int i = bArr[4];
            if (i > 0) {
                aVar.f = new byte[i];
                System.arraycopy(bArr, 5, aVar.f, 0, i);
            }
            return aVar;
        }
        a.a.a.a.b.m.a.b("InexpensiveControlRsp", "package number illegal: totalNumber = " + aVar.f1113d + ", curIndex = " + aVar.e);
        return null;
    }

    public byte[] b() {
        return this.f;
    }

    public byte c() {
        return this.f1112c;
    }

    public byte d() {
        return this.f1111b;
    }

    public int e() {
        return this.f1113d;
    }

    public int a() {
        return this.e;
    }
}
