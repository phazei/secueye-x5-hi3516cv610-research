package com.aliyun.alink.business.devicecenter.ut;

/* JADX INFO: loaded from: classes2.dex */
public class UtLinkInfo {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f3752a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f3753b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f3754c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f3755d;
    public String e;

    public UtLinkInfo() {
    }

    public String getConnectionTime() {
        return this.f3755d;
    }

    public String getErrorCode() {
        return this.e;
    }

    public String getLinkType() {
        return this.f3754c;
    }

    public String getProductKey() {
        return this.f3753b;
    }

    public String getUserId() {
        return this.f3752a;
    }

    public void setConnectionTime(String str) {
        this.f3755d = str;
    }

    public void setErrorCode(String str) {
        this.e = str;
    }

    public void setLinkType(String str) {
        this.f3754c = str;
    }

    public void setProductKey(String str) {
        this.f3753b = str;
    }

    public void setUserId(String str) {
        this.f3752a = str;
    }

    public String toString() {
        return "UtLinkInfo{user_id='" + this.f3752a + "', product_key='" + this.f3753b + "', link_type='" + this.f3754c + "', connection_time='" + this.f3755d + "', error_code='" + this.e + "'}";
    }

    public UtLinkInfo(String str, String str2, String str3) {
        this.f3752a = str;
        this.f3753b = str2;
        this.f3754c = str3;
    }

    public UtLinkInfo(String str, String str2, String str3, String str4) {
        this.f3752a = str;
        this.f3755d = str2;
        this.f3753b = str3;
        this.f3754c = str4;
    }
}
