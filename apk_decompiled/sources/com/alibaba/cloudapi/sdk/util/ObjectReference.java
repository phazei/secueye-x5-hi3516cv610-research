package com.alibaba.cloudapi.sdk.util;

/* JADX INFO: loaded from: classes.dex */
public class ObjectReference<T> {
    private T obj;

    public ObjectReference(T t) {
        this.obj = t;
    }

    public ObjectReference() {
    }

    public T getObj() {
        return this.obj;
    }

    public void setObj(T t) {
        this.obj = t;
    }
}
