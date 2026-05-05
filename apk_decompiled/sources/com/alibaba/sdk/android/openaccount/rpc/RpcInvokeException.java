package com.alibaba.sdk.android.openaccount.rpc;

/* JADX INFO: loaded from: classes.dex */
public class RpcInvokeException extends Exception {
    private int errorCode;

    public RpcInvokeException(int i, String str) {
        super(str);
        this.errorCode = i;
    }

    public RpcInvokeException(int i, String str, Throwable th) {
        super(str, th);
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
