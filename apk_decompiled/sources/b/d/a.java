package b.d;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: AccessMessage.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends c {
    public byte[] u;
    public byte[] v;

    public a() {
        this.f2144a = 0;
    }

    @Override // b.d.c
    public void a(HashMap<Integer, byte[]> map) {
        super.a(map);
    }

    @Override // b.d.c
    public void c(HashMap<Integer, byte[]> map) {
        this.f2147d = map;
    }

    public void g(byte[] bArr) {
        this.u = bArr;
    }

    public void h(byte[] bArr) {
        this.v = bArr;
    }

    @Override // b.d.c
    public HashMap<Integer, byte[]> j() {
        return super.j();
    }

    @Override // b.d.c
    public Map<Integer, byte[]> m() {
        return this.f2147d;
    }

    public byte[] u() {
        return this.u;
    }

    public byte[] v() {
        return this.v;
    }
}
