package com.aliyun.alink.linksdk.tools;

/* JADX INFO: loaded from: classes2.dex */
public class AError {
    public static final int AKErrorInvokeNetError = 4101;
    public static final int AKErrorInvokeServerError = 4102;
    public static final int AKErrorLoginTokenIllegalError = 4001;
    public static final int AKErrorServerBusinessError = 4103;
    public static final int AKErrorSuccess = 0;
    public static final int AKErrorUnknownError = 4201;
    public static final String ERR_DOMAIN_NAME_ALINK = "alinkErrorDomain";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f4435a = "alinkErrorDomain";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f4436b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f4437c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f4438d;
    private int e;
    private String f;
    private Object g;

    public void setDomain(String str) {
        this.f4435a = str;
    }

    public void setCode(int i) {
        this.f4436b = i;
    }

    public void setMsg(String str) {
        this.f4437c = str;
    }

    public void setSubDomain(String str) {
        this.f4438d = str;
    }

    public void setSubCode(int i) {
        this.e = i;
    }

    public void setSubMsg(String str) {
        this.f = str;
    }

    public String getDomain() {
        return this.f4435a;
    }

    public int getCode() {
        return this.f4436b;
    }

    public String getMsg() {
        return this.f4437c;
    }

    public String getSubDomain() {
        return this.f4438d;
    }

    public int getSubCode() {
        return this.e;
    }

    public String getSubMsg() {
        return this.f;
    }

    public Object getOriginResponseObject() {
        return this.g;
    }

    public void setOriginResponseObject(Object obj) {
        this.g = obj;
    }
}
