package com.alibaba.sdk.android.ams.common.c;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static Map<Class<?>, a<?>> f2832a;

    static {
        HashMap map = new HashMap();
        for (a aVar : d.a(a.class, c.class.getClassLoader())) {
            a aVar2 = (a) map.get(aVar.a());
            if (map.containsKey(aVar.a())) {
                throw new IllegalStateException("Ambiguous providers: " + aVar.getClass().getCanonicalName() + " versus " + aVar2.getClass().getCanonicalName());
            }
            map.put(aVar.a(), aVar);
        }
        f2832a = map;
    }
}
