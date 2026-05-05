package com.aliyun.alink.linksdk.tmp.connect.mix;

import com.aliyun.alink.linksdk.cmp.connect.apigw.ApiGatewayRequest;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class MTopAndApiGMixRequest {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4289a = "API_VERSION";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4290b = "API_PATH";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f4291c = "API_SCHEME";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f4292d = "API_AUTH_TYPE";
    private static final String e = "API_HOST";
    private ApiGatewayRequest f;

    public MTopAndApiGMixRequest(ApiGatewayRequest apiGatewayRequest) {
        this.f = apiGatewayRequest;
    }

    public BaseApiRequest a() {
        return new MixApiRequest(this.f);
    }

    public ARequest b() {
        return this.f;
    }

    public static class MixApiRequest extends BaseApiRequest {
        private ApiGatewayRequest mApiGatewayRequest;

        public MixApiRequest(ApiGatewayRequest apiGatewayRequest) {
            this.mApiGatewayRequest = apiGatewayRequest;
        }

        @Override // com.aliyun.alink.linksdk.connectsdk.BaseApiRequest
        public Map<String, Object> objectToMap() {
            if (this.mApiGatewayRequest == null) {
                return null;
            }
            HashMap map = new HashMap();
            map.put(MTopAndApiGMixRequest.f4289a, this.mApiGatewayRequest.apiVersion);
            map.put(MTopAndApiGMixRequest.f4290b, this.mApiGatewayRequest.path);
            map.put(MTopAndApiGMixRequest.f4291c, Scheme.HTTP.equals(this.mApiGatewayRequest.scheme) ? "HTTP" : "HTTPS");
            map.put(MTopAndApiGMixRequest.f4292d, this.mApiGatewayRequest.authType);
            map.put(MTopAndApiGMixRequest.e, this.mApiGatewayRequest.host);
            map.putAll(this.mApiGatewayRequest.params);
            return map;
        }
    }
}
