package com.aliyun.alink.linksdk.cmp.connect.alcs;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsServerConnectConfig extends AConnectConfig {
    private String blackClients;
    private String deviceName;
    private String prefix;
    private String productKey;
    private String secret;

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String str) {
        this.prefix = str;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String str) {
        this.secret = str;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public String getBlackClients() {
        return this.blackClients;
    }

    public void setBlackClients(String str) {
        this.blackClients = str;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig
    public boolean checkVaild() {
        return (TextUtils.isEmpty(this.productKey) || TextUtils.isEmpty(this.deviceName)) ? false : true;
    }

    public boolean isNeedAuthInfo() {
        return TextUtils.isEmpty(this.prefix) || TextUtils.isEmpty(this.secret);
    }
}
