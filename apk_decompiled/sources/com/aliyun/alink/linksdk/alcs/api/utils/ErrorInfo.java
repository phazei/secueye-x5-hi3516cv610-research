package com.aliyun.alink.linksdk.alcs.api.utils;

/* JADX INFO: loaded from: classes2.dex */
public class ErrorInfo {
    protected int mErrorCode;
    protected String mErrorMsg;

    public ErrorInfo(int i, String str) {
        this.mErrorCode = i;
        this.mErrorMsg = str;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorCode(int i) {
        this.mErrorCode = i;
    }

    public String getErrorMsg() {
        return this.mErrorMsg;
    }

    public void setErrorMsg(String str) {
        this.mErrorMsg = str;
    }
}
