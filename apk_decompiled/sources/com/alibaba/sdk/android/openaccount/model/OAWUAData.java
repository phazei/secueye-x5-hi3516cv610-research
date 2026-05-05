package com.alibaba.sdk.android.openaccount.model;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class OAWUAData implements Serializable {
    public String appKey;
    public String t;
    public String wua;

    public OAWUAData(String str, String str2, String str3) {
        this.appKey = str;
        this.t = str2;
        this.wua = str3;
    }

    public OAWUAData() {
    }
}
