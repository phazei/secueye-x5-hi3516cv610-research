package com.alibaba.ailabs.iot.aisbase.exception;

/* JADX INFO: loaded from: classes.dex */
public class IncompletePayloadException extends Exception {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f2574a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f2575b;

    public IncompletePayloadException(String str, int i, int i2) {
        super(str);
        this.f2574a = i;
        this.f2575b = i2;
    }

    public int getmCurrentLength() {
        return this.f2575b;
    }

    public int getmRequiredLength() {
        return this.f2574a;
    }

    public void setmCurrentLength(int i) {
        this.f2575b = i;
    }

    public void setmRequiredLength(int i) {
        this.f2574a = i;
    }
}
