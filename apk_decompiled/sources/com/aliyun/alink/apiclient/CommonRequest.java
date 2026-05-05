package com.aliyun.alink.apiclient;

import com.aliyun.alink.apiclient.constants.Constants;
import com.aliyun.alink.apiclient.constants.MethodType;
import com.aliyun.alink.apiclient.constants.Schema;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CommonRequest {
    private String domain;
    private MethodType method;
    private String path = null;
    public Map<String, Object> queryParams;
    private Schema schema;

    public CommonRequest() {
        this.domain = null;
        this.method = null;
        this.schema = null;
        this.queryParams = null;
        this.method = MethodType.POST;
        this.schema = Schema.HTTPS;
        this.domain = Constants.IOT_DOMAIN_DEFAULT;
        this.queryParams = new HashMap();
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String str) {
        this.domain = str;
    }

    public MethodType getMethod() {
        return this.method;
    }

    public void setMethod(MethodType methodType) {
        this.method = methodType;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public Schema getSchema() {
        return this.schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Map<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void setQueryParams(Map<String, Object> map) {
        this.queryParams = map;
    }

    public void addQueryParam(String str, Object obj) {
        this.queryParams.put(str, obj);
    }
}
