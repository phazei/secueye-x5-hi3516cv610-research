package com.alibaba.sdk.android.tbrest.rest;

import com.alibaba.sdk.android.tbrest.utils.StringUtils;
import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: compiled from: RestKeyArraySorter.java */
/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static c f3191a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final a f21a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final b f22a;

    private c() {
        this.f22a = new b();
        this.f21a = new a();
    }

    public static synchronized c a() {
        if (f3191a == null) {
            f3191a = new c();
        }
        return f3191a;
    }

    public String[] a(String[] strArr, boolean z) {
        Comparator comparator;
        if (z) {
            comparator = this.f21a;
        } else {
            comparator = this.f22a;
        }
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        Arrays.sort(strArr, comparator);
        return strArr;
    }

    /* JADX INFO: compiled from: RestKeyArraySorter.java */
    private static class b implements Comparator<String> {
        private b() {
        }

        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
                return 0;
            }
            return str.compareTo(str2) * (-1);
        }
    }

    /* JADX INFO: compiled from: RestKeyArraySorter.java */
    private static class a implements Comparator<String> {
        private a() {
        }

        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str2)) {
                return 0;
            }
            return str.compareTo(str2);
        }
    }
}
