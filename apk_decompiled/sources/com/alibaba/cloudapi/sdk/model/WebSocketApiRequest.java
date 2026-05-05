package com.alibaba.cloudapi.sdk.model;

import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class WebSocketApiRequest {
    String body;
    Map<String, List<String>> headers;
    String host;
    int isBase64 = 0;
    String method;
    String path;
    Map<String, String> querys;

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String str) {
        this.host = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String str) {
        this.body = str;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, List<String>> map) {
        this.headers = map;
    }

    public Map<String, String> getQuerys() {
        return this.querys;
    }

    public void setQuerys(Map<String, String> map) {
        this.querys = map;
    }

    public int getIsBase64() {
        return this.isBase64;
    }

    public void setIsBase64(int i) {
        this.isBase64 = i;
    }
}
