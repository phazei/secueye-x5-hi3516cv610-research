package com.aliyun.alink.linksdk.tmp.connect;

/* JADX INFO: compiled from: TmpCommonRequest.java */
/* JADX INFO: loaded from: classes2.dex */
public class d<T> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected T f4244a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected Object f4245b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected String f4246c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected String f4247d;
    protected boolean e;

    public boolean a() {
        return false;
    }

    public String d() {
        return null;
    }

    public d(T t) {
        this.f4244a = t;
    }

    public Object b() {
        return this.f4245b;
    }

    public void a(Object obj) {
        this.f4245b = obj;
    }

    public T c() {
        return this.f4244a;
    }

    public void a(String str) {
        this.f4246c = str;
    }

    public void b(String str) {
        this.f4247d = str;
    }

    public String e() {
        return this.f4246c;
    }

    public boolean f() {
        return this.e;
    }

    public void a(boolean z) {
        this.e = z;
    }

    public String g() {
        return this.f4247d;
    }

    public String toString() {
        return "TmpCommonRequest{mWrapperRequest=" + this.f4244a + ", mTag=" + this.f4245b + ", mProductKey='" + this.f4246c + "', mDeviceName='" + this.f4247d + "', mIsSecurity=" + this.e + '}';
    }
}
