package com.aliyun.iotx.linkvisual.media.misc.tracking;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes2.dex */
public class SlsBundle {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private long expireTimeUtc;
    private String logstore;
    private String project;
    private String securityToken;

    public static boolean isExpired(SlsBundle slsBundle) {
        return slsBundle == null || slsBundle.expireTimeUtc - System.currentTimeMillis() < 600000;
    }

    public static boolean isValid(SlsBundle slsBundle) {
        return (isExpired(slsBundle) || TextUtils.isEmpty(slsBundle.accessKeyId) || TextUtils.isEmpty(slsBundle.accessKeySecret) || TextUtils.isEmpty(slsBundle.endpoint) || TextUtils.isEmpty(slsBundle.logstore) || TextUtils.isEmpty(slsBundle.project) || TextUtils.isEmpty(slsBundle.securityToken)) ? false : true;
    }

    public String getAccessKeyId() {
        return this.accessKeyId;
    }

    public String getAccessKeySecret() {
        return this.accessKeySecret;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public long getExpireTimeUtc() {
        return this.expireTimeUtc;
    }

    public String getLogstore() {
        return this.logstore;
    }

    public String getProject() {
        return this.project;
    }

    public String getSecurityToken() {
        return this.securityToken;
    }

    public SlsBundle setAccessKeyId(String str) {
        this.accessKeyId = str;
        return this;
    }

    public SlsBundle setAccessKeySecret(String str) {
        this.accessKeySecret = str;
        return this;
    }

    public SlsBundle setEndpoint(String str) {
        this.endpoint = str;
        return this;
    }

    public SlsBundle setExpireTimeUtc(long j) {
        this.expireTimeUtc = j;
        return this;
    }

    public SlsBundle setLogstore(String str) {
        this.logstore = str;
        return this;
    }

    public SlsBundle setProject(String str) {
        this.project = str;
        return this;
    }

    public SlsBundle setSecurityToken(String str) {
        this.securityToken = str;
        return this;
    }

    public String toString() {
        return "SlsBundle{endpoint='" + this.endpoint + "', project='" + this.project + "', logstore='" + this.logstore + "', accessKeyId='" + this.accessKeyId + "', accessKeySecret='" + this.accessKeySecret + "', securityToken='" + this.securityToken + "', expireTimeUtc=" + this.expireTimeUtc + '}';
    }
}
