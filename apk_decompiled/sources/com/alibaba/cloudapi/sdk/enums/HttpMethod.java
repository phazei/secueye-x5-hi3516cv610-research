package com.alibaba.cloudapi.sdk.enums;

import anet.channel.request.Request;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;

/* JADX INFO: loaded from: classes.dex */
public enum HttpMethod {
    GET("GET", HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    POST_FORM("POST", HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    POST_BODY("POST", HttpConstant.CLOUDAPI_CONTENT_TYPE_STREAM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PUT_FORM(Request.Method.PUT, HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PUT_BODY(Request.Method.PUT, HttpConstant.CLOUDAPI_CONTENT_TYPE_STREAM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PATCH_FORM("PATCH", HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    PATCH_BODY("PATCH", HttpConstant.CLOUDAPI_CONTENT_TYPE_STREAM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    DELETE("DELETE", HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON),
    HEAD(Request.Method.HEAD, HttpConstant.CLOUDAPI_CONTENT_TYPE_FORM, HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON);

    private String acceptContentType;
    private String requestContentType;
    private String value;

    HttpMethod(String str, String str2, String str3) {
        this.value = str;
        this.requestContentType = str2;
        this.acceptContentType = str3;
    }

    public String getValue() {
        return this.value;
    }

    public String getRequestContentType() {
        return this.requestContentType;
    }

    public String getAcceptContentType() {
        return this.acceptContentType;
    }
}
