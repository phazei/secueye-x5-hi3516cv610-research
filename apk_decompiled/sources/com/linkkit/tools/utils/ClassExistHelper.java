package com.linkkit.tools.utils;

import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes3.dex */
public class ClassExistHelper {
    private ConcurrentHashMap<String, Boolean> classExistHashMap;

    public synchronized boolean hasClass(String str) {
        if (this.classExistHashMap == null) {
            return false;
        }
        if (!this.classExistHashMap.containsKey(str)) {
            boolean zHasClass = ReflectUtils.hasClass(str);
            if (zHasClass) {
                this.classExistHashMap.put(str, true);
            } else {
                this.classExistHashMap.put(str, false);
            }
            return zHasClass;
        }
        return this.classExistHashMap.get(str).booleanValue();
    }

    private ClassExistHelper() {
        this.classExistHashMap = null;
        this.classExistHashMap = new ConcurrentHashMap<>();
    }

    static class SingletonHolder {
        private static final ClassExistHelper INSTANCE = new ClassExistHelper();

        private SingletonHolder() {
        }
    }

    public static ClassExistHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
