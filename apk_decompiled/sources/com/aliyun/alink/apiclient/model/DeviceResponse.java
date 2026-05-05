package com.aliyun.alink.apiclient.model;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class DeviceResponse<T> implements Serializable {
    private String code = null;
    private String message = null;
    private T info = null;

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public T getInfo() {
        return this.info;
    }

    public void setInfo(T t) {
        this.info = t;
    }
}
