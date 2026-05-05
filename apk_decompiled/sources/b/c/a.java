package b.c;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.LinkedHashMap;
import java.util.Map;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: NetworkMetricTable.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2129a = "" + a.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static volatile a f2130b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Map<String, b> f2131c = new LinkedHashMap();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public C0177a f2132d = new C0177a();

    /* JADX INFO: renamed from: b.c.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: NetworkMetricTable.java */
    public class C0177a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public SparseIntArray f2133a = new SparseIntArray();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public Map<String, SparseArray<C0178a>> f2134b = new LinkedHashMap();

        /* JADX INFO: renamed from: b.c.a$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: NetworkMetricTable.java */
        class C0178a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public int f2136a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public byte[] f2137b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public int f2138c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            public boolean f2139d = false;
            public long e = System.currentTimeMillis();

            public C0178a(int i, byte[] bArr) {
                this.f2136a = i;
                this.f2137b = bArr;
                this.f2138c = C0177a.this.f2133a.get(i);
            }

            public void a() {
                this.f2139d = true;
            }
        }

        public C0177a() {
            this.f2133a.put(33282, 33284);
            this.f2133a.put(33350, -32187);
            this.f2133a.put(33438, -32187);
            this.f2133a.put(32795, -32737);
            this.f2133a.put(32796, -32737);
        }

        public void a(byte[] bArr, byte[] bArr2, int i, byte[] bArr3) {
            if (this.f2133a.get(i) == 0) {
                return;
            }
            String strB = a.this.b(bArr, bArr2);
            if (TextUtils.isEmpty(strB)) {
                return;
            }
            SparseArray<C0178a> sparseArray = this.f2134b.get(strB);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                this.f2134b.put(strB, sparseArray);
            }
            C0178a c0178a = new C0178a(i, bArr3);
            sparseArray.put(a.this.a(c0178a.f2138c, bArr3), c0178a);
            a.a.a.a.b.m.a.a(a.f2129a, String.format("Measure on message sent, to: %s, opcode: %s", MeshParserUtils.bytesToHex(bArr2, true), MeshParserUtils.bytesToHex(MeshParserUtils.getOpCodes(i), true)));
        }

        public void a(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2, int i3) {
            int iA;
            C0178a c0178a;
            String strB = a.this.b(bArr, bArr2);
            if (TextUtils.isEmpty(strB)) {
                return;
            }
            b bVarA = a.this.a(bArr, bArr2);
            bVarA.c(i3);
            bVarA.a(i2);
            SparseArray<C0178a> sparseArray = this.f2134b.get(strB);
            if (sparseArray == null || (c0178a = sparseArray.get((iA = a.this.a(i, bArr3)))) == null) {
                return;
            }
            c0178a.a();
            bVarA.b((int) (System.currentTimeMillis() - c0178a.e));
            sparseArray.remove(iA);
            a.a.a.a.b.m.a.c(a.f2129a, String.format("Mesh node metric update, node: %s, retransmissionTimeout: %d", MeshParserUtils.bytesToHex(bArr2, true), Integer.valueOf(bVarA.a())));
        }
    }

    /* JADX INFO: compiled from: NetworkMetricTable.java */
    private static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f2140a = 0;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public int f2141b = 0;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public int f2142c = 10;
        public int e = 10000;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public int f2143d = 10000;
        public int f = 10000;

        public void a(int i) {
            this.f2140a = i;
        }

        public void b(int i) {
            this.f2143d = i;
            this.e = (int) ((((double) this.e) * 0.8d) + (((double) i) * 0.2d));
            this.f = (int) Math.min(15000.0d, Math.max(500.0d, ((double) this.e) * 1.3d));
        }

        public void c(int i) {
            this.f2141b = i;
        }

        public int a() {
            return this.f;
        }
    }

    public final int a(int i, byte[] bArr) {
        return i;
    }

    public C0177a c() {
        return this.f2132d;
    }

    public static a b() {
        if (f2130b == null) {
            synchronized (a.class) {
                if (f2130b == null) {
                    f2130b = new a();
                }
            }
        }
        return f2130b;
    }

    public int c(byte[] bArr, byte[] bArr2) {
        return a(bArr, bArr2).a();
    }

    public final b a(byte[] bArr, byte[] bArr2) {
        String strB = b(bArr, bArr2);
        b bVar = this.f2131c.get(strB);
        if (bVar != null) {
            return bVar;
        }
        b bVar2 = new b();
        this.f2131c.put(strB, bVar2);
        return bVar2;
    }

    public final String b(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return null;
        }
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return MeshParserUtils.bytesToHex(bArr3, false);
    }
}
