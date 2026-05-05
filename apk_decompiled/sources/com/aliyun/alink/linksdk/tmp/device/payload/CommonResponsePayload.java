package com.aliyun.alink.linksdk.tmp.device.payload;

/* JADX INFO: loaded from: classes2.dex */
public class CommonResponsePayload<T> {
    protected int code;
    protected T data;
    protected String id;
    protected String msg;

    public T getData() {
        return this.data;
    }

    public void setData(T t) {
        this.data = t;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public boolean payloadSuccess() {
        int i = this.code;
        return i >= 200 && i < 300;
    }
}
