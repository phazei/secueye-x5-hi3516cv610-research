package com.taobao.accs.utl;

import com.taobao.accs.utl.i;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class g implements i.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private c[] f6456a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f6457b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String[] f6458c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long[] f6459d;
    private int e;

    /* JADX INFO: compiled from: Taobao */
    interface c {
        boolean a(String str);
    }

    /* JADX INFO: compiled from: Taobao */
    private static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final g f6461a = new g();

        private b() {
        }
    }

    public static g a() {
        return b.f6461a;
    }

    private g() {
        this.f6457b = 5;
        this.f6458c = new String[5];
        this.f6459d = new long[5];
        this.e = 0;
        for (int i = 0; i < 5; i++) {
            this.f6458c[i] = null;
            this.f6459d[i] = 0;
        }
        this.f6456a = new c[]{new a("send msg time out"), new a("errorCode::"), new a("errorId::"), new a("TNET_JNI_ERR_LOAD_SO_FAIL")};
    }

    @Override // com.taobao.accs.utl.i.a
    public void a(String str) {
        try {
            if (a(str, this.f6456a)) {
                b(str);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void b(String str) {
        int i = this.e % 5;
        this.f6458c[i] = str;
        this.e = i + 1;
    }

    private boolean a(String str, c[] cVarArr) {
        for (c cVar : cVarArr) {
            if (cVar.a(str)) {
                return true;
            }
        }
        return false;
    }

    public String b() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis() / 1000);
        sb.append(" ");
        try {
            int i = ((this.e - 1) % 5) + 5;
            for (int i2 = 0; i2 < 5; i2++) {
                int i3 = (i - i2) % 5;
                if (this.f6458c[i3] == null) {
                    break;
                }
                sb.append(this.f6459d[i3]);
                sb.append(" ");
                sb.append(this.f6458c[i3]);
                sb.append(" ");
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }

    /* JADX INFO: compiled from: Taobao */
    static class a implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f6460a;

        public a(String str) {
            this.f6460a = str;
        }

        @Override // com.taobao.accs.utl.g.c
        public boolean a(String str) {
            return str != null && str.contains(this.f6460a);
        }
    }
}
