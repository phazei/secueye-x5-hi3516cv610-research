package com.aliyun.iot.aep.sdk.framework.model;

import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class ILopUserAccountInfo implements Serializable {
    public String email;
    public String phone;
    public String phoneCode;

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getPhoneCode() {
        return this.phoneCode;
    }

    public void setPhoneCode(String str) {
        this.phoneCode = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String toString() {
        return "ILopUserAccountInfo{phone='" + this.phone + "', phoneCode='" + this.phoneCode + "', email='" + this.email + "'}";
    }
}
