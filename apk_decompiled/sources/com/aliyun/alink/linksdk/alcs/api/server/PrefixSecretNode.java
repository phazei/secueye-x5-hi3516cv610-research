package com.aliyun.alink.linksdk.alcs.api.server;

/* JADX INFO: loaded from: classes2.dex */
public class PrefixSecretNode {
    protected String mPrefix;
    protected String mSecret;

    public PrefixSecretNode(String str, String str2) {
        this.mPrefix = str;
        this.mSecret = str2;
    }

    public String getPrefix() {
        return this.mPrefix;
    }

    public void setPrefix(String str) {
        this.mPrefix = str;
    }

    public String getSecret() {
        return this.mSecret;
    }

    public void setSecret(String str) {
        this.mSecret = str;
    }
}
