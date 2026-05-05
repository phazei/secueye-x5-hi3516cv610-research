package com.http.helper;

/* JADX INFO: loaded from: classes3.dex */
public class HttpFailCode extends Throwable {
    int code;

    public HttpFailCode(int i) {
        this.code = -1;
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }
}
