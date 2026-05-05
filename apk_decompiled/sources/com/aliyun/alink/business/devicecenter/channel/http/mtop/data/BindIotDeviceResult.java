package com.aliyun.alink.business.devicecenter.channel.http.mtop.data;

/* JADX INFO: loaded from: classes.dex */
public class BindIotDeviceResult {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f3519a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f3520b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f3521c;

    public String getDevId() {
        return this.f3519a;
    }

    public String getDeviceName() {
        return this.f3521c;
    }

    public String getSkillId() {
        return this.f3520b;
    }

    public void setDevId(String str) {
        this.f3519a = str;
    }

    public void setDeviceName(String str) {
        this.f3521c = str;
    }

    public void setSkillId(String str) {
        this.f3520b = str;
    }
}
