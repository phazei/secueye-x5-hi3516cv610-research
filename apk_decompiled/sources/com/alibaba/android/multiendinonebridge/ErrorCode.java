package com.alibaba.android.multiendinonebridge;

/* JADX INFO: loaded from: classes.dex */
public enum ErrorCode {
    ERROR_LOADING_LIBRARY(-2, "internal error, the so library did not load successfully"),
    NOT_INITIALIZED_YET(-3, "Not initialized yet"),
    INVALID_PARAMETERS(-4, "Invalid parameters");

    private final int code;
    private final String description;

    ErrorCode(int i, String str) {
        this.code = i;
        this.description = str;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCode() {
        return this.code;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.code + ": " + this.description;
    }
}
