package com.alibaba.sdk.android.openaccount.model;

import com.alibaba.sdk.android.openaccount.message.Message;

/* JADX INFO: loaded from: classes.dex */
public class Result<T> extends ResultCode {
    public T data;
    public String type;

    public Result() {
    }

    public Result(Message message) {
        super(message);
    }

    public Result(int i, String str, T t) {
        super(i, str);
        this.data = t;
    }

    public static <T> Result<T> result(T t) {
        return result(100, null, t);
    }

    public static <T> Result<T> result(int i, String str, T t) {
        return new Result<>(i, str, t);
    }

    public static <T> Result<T> result(int i, String str) {
        return result(i, str, null);
    }

    public static <T> Result<T> result(Result result) {
        return result(result.code, result.message);
    }

    public static <T> Result<T> result(Message message) {
        return result(message.code, message.message);
    }

    @Override // com.alibaba.sdk.android.openaccount.model.ResultCode
    public boolean isSuccess() {
        return this.code == 100 || this.code == 1;
    }

    public String toString() {
        return "Result [code=" + this.code + ", message=" + this.message + ", data=" + this.data + "]";
    }
}
