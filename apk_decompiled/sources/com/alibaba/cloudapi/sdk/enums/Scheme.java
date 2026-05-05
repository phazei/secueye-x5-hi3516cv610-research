package com.alibaba.cloudapi.sdk.enums;

/* JADX INFO: loaded from: classes.dex */
public enum Scheme {
    HTTP("HTTP://"),
    HTTPS("HTTPS://"),
    WEBSOCKET("WS://");

    String value;

    Scheme(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }
}
