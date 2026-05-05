package b.d;

import java.util.HashMap;
import java.util.Map;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: Message.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f2144a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public HashMap<Integer, byte[]> f2145b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public HashMap<Integer, byte[]> f2146c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public HashMap<Integer, byte[]> f2147d;
    public int e;
    public int f = 5;
    public byte[] g;
    public byte[] h;
    public byte[] i;
    public byte[] j;
    public int k;
    public int l;
    public int m;
    public int n;
    public byte[] o;
    public int p;
    public byte[] q;
    public boolean r;
    public SecureUtils.K2Output s;
    public String t;

    public void a(byte[] bArr) {
        this.h = bArr;
    }

    public int b() {
        return this.k;
    }

    public abstract void c(HashMap<Integer, byte[]> map);

    public void c(byte[] bArr) {
        this.j = bArr;
    }

    public void d(byte[] bArr) {
        this.o = bArr;
    }

    public int e() {
        return this.f2144a;
    }

    public void f(byte[] bArr) {
        this.g = bArr;
    }

    public void g(int i) {
        this.e = i;
    }

    public void h(int i) {
        this.f = i;
    }

    public byte[] i() {
        return this.j;
    }

    public HashMap<Integer, byte[]> j() {
        return this.f2145b;
    }

    public HashMap<Integer, byte[]> k() {
        return this.f2146c;
    }

    public String l() {
        return this.t;
    }

    public abstract Map<Integer, byte[]> m();

    public int n() {
        return this.n;
    }

    public byte[] o() {
        return this.o;
    }

    public int p() {
        return this.e;
    }

    public byte[] q() {
        return this.i;
    }

    public byte[] r() {
        return this.g;
    }

    public int s() {
        return this.f;
    }

    public boolean t() {
        return this.r;
    }

    public int a() {
        return this.l;
    }

    public void b(int i) {
        this.k = i;
    }

    public int c() {
        return this.m;
    }

    public int d() {
        return this.p;
    }

    public void e(int i) {
        this.f2144a = i;
    }

    public byte[] f() {
        return this.h;
    }

    public byte[] g() {
        return this.q;
    }

    public SecureUtils.K2Output h() {
        return this.s;
    }

    public void a(int i) {
        this.l = i;
    }

    public void b(byte[] bArr) {
        this.q = bArr;
    }

    public void c(int i) {
        this.m = i;
    }

    public void d(int i) {
        this.p = i;
    }

    public void e(byte[] bArr) {
        this.i = bArr;
    }

    public void f(int i) {
        this.n = i;
    }

    public void a(boolean z) {
        this.r = z;
    }

    public void b(HashMap<Integer, byte[]> map) {
        this.f2146c = map;
    }

    public void a(SecureUtils.K2Output k2Output) {
        this.s = k2Output;
    }

    public void a(HashMap<Integer, byte[]> map) {
        this.f2145b = map;
    }

    public void a(String str) {
        this.t = str;
    }
}
