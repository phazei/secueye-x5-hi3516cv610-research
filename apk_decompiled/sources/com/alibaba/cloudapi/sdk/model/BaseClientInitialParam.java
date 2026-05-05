package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.exception.SdkException;

/* JADX INFO: loaded from: classes.dex */
public class BaseClientInitialParam {
    String appKey;
    String appSecret;
    String host;
    Scheme scheme;
    long connectionTimeout = 10000;
    long readTimeout = 10000;
    long writeTimeout = 10000;

    public void check() {
        if (isEmpty(this.appKey) || isEmpty(this.appKey)) {
            throw new SdkException("app key or app secret must be initialed");
        }
        if (isEmpty(this.host) || this.scheme == null) {
            throw new SdkException("host and scheme must be initialed");
        }
    }

    protected boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String str) {
        this.appKey = str;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String str) {
        this.appSecret = str;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String str) {
        this.host = str;
    }

    public Scheme getScheme() {
        return this.scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public long getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(long j) {
        this.connectionTimeout = j;
    }

    public long getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(long j) {
        this.readTimeout = j;
    }

    public long getWriteTimeout() {
        return this.writeTimeout;
    }

    public void setWriteTimeout(long j) {
        this.writeTimeout = j;
    }
}
