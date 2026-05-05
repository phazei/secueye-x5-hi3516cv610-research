package com.alibaba.sdk.android.openaccount.model;

/* JADX INFO: loaded from: classes.dex */
public class UserContract {
    String email;
    String hash;
    String hash_key;
    String loginId;
    String mobile;
    String nick;
    String userid;

    public String getLoginId() {
        return this.loginId;
    }

    public void setLoginId(String str) {
        this.loginId = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public UserContract(String str, String str2, String str3, String str4, String str5, String str6) {
        this.userid = str3;
        this.mobile = str2;
        this.nick = str4;
        this.hash = str;
        this.email = str5;
        this.loginId = str6;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String str) {
        this.userid = str;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String str) {
        this.nick = str;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String str) {
        this.hash = str;
    }

    public String getHash_key() {
        return this.hash_key;
    }

    public void setHash_key(String str) {
        this.hash_key = str;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String str) {
        this.mobile = str;
    }
}
