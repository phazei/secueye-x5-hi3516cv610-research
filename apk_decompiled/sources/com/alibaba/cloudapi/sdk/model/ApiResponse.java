package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ApiResponse extends ApiHttpMessage {
    int code;
    String contentType;
    Exception ex;
    String message;

    public ApiResponse(int i) {
        this.code = i;
    }

    public ApiResponse(int i, String str, Exception exc) {
        this.code = i;
        this.message = str;
        this.ex = exc;
    }

    public ApiResponse(JSONObject jSONObject) {
        parse(jSONObject);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public Exception getEx() {
        return this.ex;
    }

    public void setEx(Exception exc) {
        this.ex = exc;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String str) {
        this.contentType = str;
    }

    @Override // com.alibaba.cloudapi.sdk.model.ApiHttpMessage
    public void parse(JSONObject jSONObject) {
        super.parse(jSONObject);
        this.code = Integer.parseInt(jSONObject.get("status").toString());
        this.contentType = getFirstHeaderValue(HttpConstant.CLOUDAPI_HTTP_HEADER_CONTENT_TYPE);
        if (getFirstHeaderValue(SdkConstant.CLOUDAPI_X_CA_ERROR_MESSAGE) != null) {
            this.message = getFirstHeaderValue(SdkConstant.CLOUDAPI_X_CA_ERROR_MESSAGE);
        }
    }
}
