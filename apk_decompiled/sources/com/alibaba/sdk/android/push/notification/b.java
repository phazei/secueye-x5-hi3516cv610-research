package com.alibaba.sdk.android.push.notification;

import android.os.Build;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static AmsLogger f3155a = AmsLogger.getLogger("MPS:CPushNotification");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static ArrayList<Integer> f3156b = new ArrayList<>();
    private boolean A;
    private String B;
    private String C;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Map<String, String> f3157c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f3158d;
    private String e;
    private String f;
    private String g;
    private String h;
    private int i = 0;
    private String j;
    private String k;
    private String l;
    private String m;
    private String n;
    private boolean o;
    private int p;
    private int q;
    private int r;
    private int s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;

    static {
        int i = Build.VERSION.SDK_INT;
        f3156b.add(-2);
        f3156b.add(0);
        f3156b.add(1);
        f3156b.add(-1);
        f3156b.add(2);
    }

    public b() {
        int i = Build.VERSION.SDK_INT;
        this.q = 0;
        this.r = 0;
        this.s = 1;
        this.t = 0;
        this.u = 3;
        this.v = 0;
        this.w = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.A = false;
    }

    public String a() {
        return this.g;
    }

    public void a(int i) {
        this.p = i;
    }

    public void a(String str) {
        this.g = str;
    }

    public void a(Map<String, String> map) {
        this.f3157c = map;
    }

    public void a(boolean z) {
        this.A = z;
    }

    public String b() {
        return this.f3158d;
    }

    public void b(int i) {
        this.r = i;
    }

    public void b(String str) {
        this.f3158d = str;
    }

    public String c() {
        return this.e;
    }

    public void c(int i) {
        this.s = i;
    }

    public void c(String str) {
        this.e = str;
    }

    public int d() {
        return this.p;
    }

    public void d(int i) {
        this.t = i;
    }

    public void d(String str) {
        this.f = str;
    }

    public void e(int i) {
        this.u = i;
    }

    public void e(String str) {
        try {
            if (f3156b.contains(Integer.valueOf(Integer.parseInt(str)))) {
                this.q = Integer.parseInt(str);
            }
        } catch (NumberFormatException e) {
            f3155a.e("formar error:数字格式错误", e);
        }
    }

    public boolean e() {
        return this.o;
    }

    public int f() {
        return this.r;
    }

    public void f(int i) {
        this.v = i;
    }

    public void f(String str) {
        this.n = str;
    }

    public int g() {
        return this.s;
    }

    public void g(int i) {
        this.w = i;
    }

    public void g(String str) {
        this.h = str;
    }

    public int h() {
        return this.t;
    }

    public void h(int i) {
        this.x = i;
    }

    public void h(String str) {
        this.j = str;
    }

    public int i() {
        return this.u;
    }

    public void i(int i) {
        this.y = i;
    }

    public void i(String str) {
        this.k = str;
    }

    public int j() {
        return this.v;
    }

    public void j(int i) {
        this.z = i;
    }

    public void j(String str) {
        this.l = str;
    }

    public int k() {
        return this.w;
    }

    public void k(int i) {
        this.i = i;
    }

    public void k(String str) {
        this.m = str;
    }

    public int l() {
        return this.x;
    }

    public void l(String str) {
        this.B = str;
    }

    public int m() {
        return this.y;
    }

    public void m(String str) {
        this.C = str;
    }

    public int n() {
        return this.z;
    }

    public boolean o() {
        return this.A;
    }

    public int p() {
        return this.q;
    }

    public String q() {
        return this.n;
    }

    public String r() {
        return this.h;
    }

    public int s() {
        return this.i;
    }

    public String t() {
        return this.j;
    }

    public String u() {
        return this.k;
    }

    public String v() {
        return this.l;
    }

    public String w() {
        return this.m;
    }

    public String x() {
        return this.B;
    }

    public String y() {
        return this.C;
    }
}
