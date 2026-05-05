package com.alibaba.sdk.android.pluto.runtime;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface BeanRegistration {
    void setProperties(Map<String, String> map);

    void unregister();
}
