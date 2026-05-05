package com.alibaba.sdk.android.pluto.meta;

import java.util.Arrays;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class BeanInfo {
    public Class<?> implType;
    public String initMethod;
    public Object instance;
    public Map<String, String> properties;
    public Class<?>[] types;

    public String toString() {
        return "BeanInfo{types=" + Arrays.toString(this.types) + ", implType=" + this.implType + ", instance=" + this.instance + ", initMethod='" + this.initMethod + "', properties=" + this.properties + '}';
    }
}
