package com.alibaba.sdk.android.ams.common.c;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static <T> T a(Class<T> cls) {
        a<?> aVar = b.f2832a.get(cls);
        if (aVar != null) {
            return cls.cast(aVar.b());
        }
        throw new IllegalArgumentException("No factory was registered for " + cls.getCanonicalName());
    }
}
