package com.aliyun.alink.business.devicecenter.channel.http;

/* JADX INFO: loaded from: classes.dex */
public class DCError {
    public String code;
    public String message;
    public String subCode;
    public Throwable throwable;

    public DCError(String str, String str2) {
        this(str, null, str2, null);
    }

    public String toString() {
        return "DCError{code='" + this.code + "', subCode='" + this.subCode + "', message='" + this.message + "', throwable=" + this.throwable + '}';
    }

    public DCError(String str, String str2, Throwable th) {
        this(str, null, str2, th);
    }

    public DCError(String str, String str2, String str3, Throwable th) {
        this.code = str;
        this.subCode = str2;
        this.message = str3;
        this.throwable = th;
    }
}
