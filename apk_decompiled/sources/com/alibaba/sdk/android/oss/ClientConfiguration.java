package com.alibaba.sdk.android.oss;

import anet.channel.util.HttpConstant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ClientConfiguration {
    private static final int DEFAULT_MAX_RETRIES = 2;
    private String mUserAgentMark;
    private String proxyHost;
    private int proxyPort;
    private int maxConcurrentRequest = 5;
    private int socketTimeout = 15000;
    private int connectionTimeout = 15000;
    private long max_log_size = 5242880;
    private int maxErrorRetry = 2;
    private List<String> customCnameExcludeList = new ArrayList();
    private boolean httpDnsEnable = true;
    private boolean checkCRC64 = false;

    public static ClientConfiguration getDefaultConf() {
        return new ClientConfiguration();
    }

    public int getMaxConcurrentRequest() {
        return this.maxConcurrentRequest;
    }

    public void setMaxConcurrentRequest(int i) {
        this.maxConcurrentRequest = i;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    public void setSocketTimeout(int i) {
        this.socketTimeout = i;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int i) {
        this.connectionTimeout = i;
    }

    public void setMaxLogSize(long j) {
        this.max_log_size = j;
    }

    public long getMaxLogSize() {
        return this.max_log_size;
    }

    public int getMaxErrorRetry() {
        return this.maxErrorRetry;
    }

    public void setMaxErrorRetry(int i) {
        this.maxErrorRetry = i;
    }

    public void setCustomCnameExcludeList(List<String> list) {
        if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("cname exclude list should not be null.");
        }
        this.customCnameExcludeList.clear();
        for (String str : list) {
            if (str.contains(HttpConstant.SCHEME_SPLIT)) {
                this.customCnameExcludeList.add(str.substring(str.indexOf(HttpConstant.SCHEME_SPLIT) + 3));
            } else {
                this.customCnameExcludeList.add(str);
            }
        }
    }

    public List<String> getCustomCnameExcludeList() {
        return Collections.unmodifiableList(this.customCnameExcludeList);
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(String str) {
        this.proxyHost = str;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(int i) {
        this.proxyPort = i;
    }

    public String getCustomUserMark() {
        return this.mUserAgentMark;
    }

    public void setUserAgentMark(String str) {
        this.mUserAgentMark = str;
    }

    public boolean isHttpDnsEnable() {
        return this.httpDnsEnable;
    }

    public void setHttpDnsEnable(boolean z) {
        this.httpDnsEnable = z;
    }

    public boolean isCheckCRC64() {
        return this.checkCRC64;
    }

    public void setCheckCRC64(boolean z) {
        this.checkCRC64 = z;
    }
}
