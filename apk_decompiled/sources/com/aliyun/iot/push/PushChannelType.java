package com.aliyun.iot.push;

/* JADX INFO: loaded from: classes2.dex */
public enum PushChannelType {
    IOT_MAINLAND_CLOUD_PUSH("mainland cloud push"),
    IOT_OVERSEAS_CLOUD_PUSH(" overseas cloud push");


    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f4906a;

    PushChannelType(String str) {
        this.f4906a = null;
        this.f4906a = str;
    }

    public String getName() {
        return this.f4906a;
    }
}
