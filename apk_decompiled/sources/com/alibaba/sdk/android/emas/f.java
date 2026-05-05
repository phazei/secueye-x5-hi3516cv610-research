package com.alibaba.sdk.android.emas;

import java.util.List;

/* JADX INFO: compiled from: EmasLog.java */
/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final List<g> f2888b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final d f2889c;

    /* JADX INFO: renamed from: c, reason: collision with other field name */
    private final String f13c;

    public f(List<g> list) {
        this(list, d.MEM_CACHE, null);
    }

    public f(List<g> list, d dVar, String str) {
        this.f2888b = list;
        this.f2889c = dVar;
        this.f13c = str;
    }

    public d a() {
        return this.f2889c;
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    public List<g> m17a() {
        return this.f2888b;
    }

    public String getLocation() {
        if (this.f2889c == d.DISK_CACHE) {
            return this.f13c;
        }
        return null;
    }
}
