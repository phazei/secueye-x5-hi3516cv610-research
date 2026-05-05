package com.alibaba.sdk.android.openaccount.ui.model;

/* JADX INFO: loaded from: classes.dex */
public class CaptchaModel {
    public String csessionid;
    public String nctoken;
    public String sig;

    public CaptchaModel(String str, String str2, String str3) {
        this.sig = str;
        this.csessionid = str2;
        this.nctoken = str3;
    }
}
