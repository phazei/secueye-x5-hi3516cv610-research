package com.alibaba.sdk.android.push.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public abstract class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected String f3159a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f3160b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected int f3161c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected String f3162d;
    protected String e;
    protected String f;
    protected int g;
    protected String h;
    protected String i;
    protected String j;
    protected String k;
    protected String l;
    protected String m;

    public c() {
        int i = Build.VERSION.SDK_INT;
        this.f3161c = 0;
        this.g = 0;
    }

    public abstract Notification a(Context context, PushData pushData, NotificationConfigure notificationConfigure);

    public String a() {
        return this.f3159a;
    }

    public void a(int i) {
        this.f3161c = i;
    }

    public void a(String str) {
        this.f3159a = str;
    }

    public abstract Notification b(Context context, PushData pushData, NotificationConfigure notificationConfigure);

    public String b() {
        return this.f3160b;
    }

    public void b(int i) {
        this.g = i;
    }

    public void b(String str) {
        this.f3160b = str;
    }

    public int c() {
        return this.f3161c;
    }

    public void c(String str) {
        this.f3162d = str;
    }

    public String d() {
        return this.f3162d;
    }

    public void d(String str) {
        this.f = str;
    }

    public String e() {
        return this.l;
    }

    public void e(String str) {
        this.h = str;
    }

    public String f() {
        return this.m;
    }

    public void f(String str) {
        this.i = str;
    }

    public void g(String str) {
        this.j = str;
    }

    public void h(String str) {
        this.k = str;
    }

    public void i(String str) {
        this.e = str;
    }

    public void j(String str) {
        this.l = str;
    }

    public void k(String str) {
        this.m = str;
    }
}
