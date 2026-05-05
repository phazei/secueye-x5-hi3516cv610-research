package com.aliyun.alink.linksdk.channel.core.base;

/* JADX INFO: loaded from: classes2.dex */
@Deprecated
public class AError {
    public static final int AKErrorInvokeNetError = 4101;
    public static final int AKErrorInvokeServerError = 4102;
    public static final int AKErrorLoginTokenIllegalError = 4001;
    public static final int AKErrorServerBusinessError = 4103;
    public static final int AKErrorSuccess = 0;
    public static final int AKErrorUnknownError = 4201;
    public static final String ERR_DOMAIN_NAME_ALINK = "alinkErrorDomain";
    public int code;
    public String domain = "alinkErrorDomain";
    public String msg;
    public Object originResponseObj;
    public int subCode;
    public String subDomain;
    public String subMsg;

    public int getCode() {
        return this.code;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getOriginResponseObject() {
        return this.originResponseObj;
    }

    public int getSubCode() {
        return this.subCode;
    }

    public String getSubDomain() {
        return this.subDomain;
    }

    public String getSubMsg() {
        return this.subMsg;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public void setDomain(String str) {
        this.domain = str;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public void setOriginResponseObject(Object obj) {
        this.originResponseObj = obj;
    }

    public void setSubCode(int i) {
        this.subCode = i;
    }

    public void setSubDomain(String str) {
        this.subDomain = str;
    }

    public void setSubMsg(String str) {
        this.subMsg = str;
    }

    public String toString() {
        return "AError{domain='" + this.domain + "', code=" + this.code + ", msg='" + this.msg + "', subDomain='" + this.subDomain + "', subCode=" + this.subCode + ", subMsg='" + this.subMsg + "', originResponseObj=" + this.originResponseObj + '}';
    }
}
