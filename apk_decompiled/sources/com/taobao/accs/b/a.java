package com.taobao.accs.b;

import com.taobao.accs.utl.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static a f6283a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ClassLoader f6284b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f6285c = false;

    public static synchronized a a() {
        if (f6283a == null) {
            f6283a = new a();
        }
        return f6283a;
    }

    public synchronized ClassLoader b() {
        if (this.f6284b == null) {
            ALog.d("ACCSClassLoader", "getClassLoader", new Object[0]);
            this.f6284b = a.class.getClassLoader();
        }
        return this.f6284b;
    }
}
