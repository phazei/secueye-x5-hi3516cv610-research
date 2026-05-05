package com.aliyun.alink.linksdk.tmp.network;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseException extends Exception {
    protected int code;
    protected String errorMessage;

    public abstract boolean handle(Context context);

    public BaseException() {
    }

    public BaseException(String str) {
        super(str);
    }

    public BaseException(Throwable th) {
        super(th);
    }

    public BaseException(int i, String str) {
        this.code = i;
        this.errorMessage = str;
    }

    public BaseException(String str, Throwable th) {
        super(th);
        this.errorMessage = str;
    }

    public BaseException(int i, String str, Throwable th) {
        super(th);
        this.code = i;
        this.errorMessage = str;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public String getErrorMessage() {
        String str = this.errorMessage;
        return str != null ? str : super.getMessage();
    }

    public void setErrorMessage(String str) {
        this.errorMessage = str;
    }
}
