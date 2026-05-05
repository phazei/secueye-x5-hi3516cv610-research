package com.alibaba.cloudapi.sdk.model;

import com.alibaba.cloudapi.sdk.enums.HttpConnectionModel;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.enums.WebSocketApiType;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ApiRequest extends ApiHttpMessage {
    private Date currentDate;
    private String host;
    private HttpMethod method;
    private String path;
    private Scheme scheme;
    private String signatureMethod;
    private String url;
    private HttpConnectionModel httpConnectionMode = HttpConnectionModel.SINGER_CONNECTION;
    private WebSocketApiType webSocketApiType = WebSocketApiType.COMMON;
    private Map<String, String> pathParams = new HashMap();
    private Map<String, String> querys = new HashMap();
    private Map<String, String> formParams = new HashMap();
    private boolean isBase64BodyViaWebsocket = false;

    public ApiRequest(HttpMethod httpMethod, String str) {
        this.method = httpMethod;
        this.path = str;
    }

    public ApiRequest(HttpMethod httpMethod, String str, byte[] bArr) {
        this.method = httpMethod;
        this.path = str;
        this.body = bArr;
    }

    public ApiRequest(JSONObject jSONObject) {
        parse(jSONObject);
    }

    public Scheme getScheme() {
        return this.scheme;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getHost() {
        return this.host;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getPathParams() {
        return this.pathParams;
    }

    public Map<String, String> getQuerys() {
        return this.querys;
    }

    public Map<String, String> getFormParams() {
        return this.formParams;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void addParam(String str, String str2, ParamPosition paramPosition, boolean z) {
        Map<String, String> map;
        if (str2 == null) {
            if (z) {
                throw new SdkException(String.format("param %s is not nullable, please check your codes", str));
            }
            return;
        }
        switch (paramPosition) {
            case HEAD:
                addHeader(str, str2);
                return;
            case PATH:
                map = this.pathParams;
                break;
            case QUERY:
                map = this.querys;
                break;
            case BODY:
                map = this.formParams;
                break;
            default:
                throw new SdkException("unknown param position: " + paramPosition);
        }
        if (str2 instanceof String) {
            map.put(str, str2);
        } else {
            map.put(str, str2.toString());
        }
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public void setMethod(HttpMethod httpMethod) {
        this.method = httpMethod;
    }

    public void setHost(String str) {
        this.host = str;
    }

    public void setPathParams(Map<String, String> map) {
        this.pathParams = map;
    }

    public void setQuerys(Map<String, String> map) {
        this.querys = map;
    }

    public void setFormParams(Map<String, String> map) {
        this.formParams = map;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public boolean isBase64BodyViaWebsocket() {
        return this.isBase64BodyViaWebsocket;
    }

    public void setBase64BodyViaWebsocket(boolean z) {
        this.isBase64BodyViaWebsocket = z;
    }

    public String getSignatureMethod() {
        return this.signatureMethod;
    }

    public void setSignatureMethod(String str) {
        this.signatureMethod = str;
    }

    public HttpConnectionModel getHttpConnectionMode() {
        return this.httpConnectionMode;
    }

    public void setHttpConnectionMode(HttpConnectionModel httpConnectionModel) {
        this.httpConnectionMode = httpConnectionModel;
    }

    @Override // com.alibaba.cloudapi.sdk.model.ApiHttpMessage
    public void parse(JSONObject jSONObject) {
        super.parse(jSONObject);
    }

    public WebSocketApiType getWebSocketApiType() {
        return this.webSocketApiType;
    }

    public void setWebSocketApiType(WebSocketApiType webSocketApiType) {
        this.webSocketApiType = webSocketApiType;
    }

    public Date getCurrentDate() {
        return this.currentDate;
    }

    public void setCurrentDate(Date date) {
        this.currentDate = date;
    }

    public ApiRequest duplicate() {
        ApiRequest apiRequest = new ApiRequest(this.method, this.path, this.body);
        apiRequest.scheme = this.scheme;
        String str = this.host;
        if (str != null) {
            apiRequest.host = new String(str);
        }
        String str2 = this.url;
        if (str2 != null) {
            apiRequest.url = new String(str2);
        }
        apiRequest.pathParams = new HashMap();
        apiRequest.pathParams.putAll(this.pathParams);
        apiRequest.headers = new HashMap();
        apiRequest.headers.putAll(this.headers);
        apiRequest.querys = new HashMap();
        apiRequest.querys.putAll(this.querys);
        apiRequest.formParams = new HashMap();
        apiRequest.formParams.putAll(this.formParams);
        String str3 = this.signatureMethod;
        if (str3 != null) {
            apiRequest.signatureMethod = new String(str3);
        }
        apiRequest.webSocketApiType = this.webSocketApiType;
        apiRequest.httpConnectionMode = this.httpConnectionMode;
        apiRequest.isBase64BodyViaWebsocket = this.isBase64BodyViaWebsocket;
        return apiRequest;
    }
}
