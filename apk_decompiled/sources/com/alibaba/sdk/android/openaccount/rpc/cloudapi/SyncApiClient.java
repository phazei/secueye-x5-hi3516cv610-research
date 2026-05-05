package com.alibaba.sdk.android.openaccount.rpc.cloudapi;

import com.alibaba.cloudapi.sdk.client.HttpApiClient;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.enums.HttpConnectionModel;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SyncApiClient extends HttpApiClient {
    public static SyncApiClient build(HttpClientBuilderParams httpClientBuilderParams) {
        if (httpClientBuilderParams == null) {
            throw new SdkException("buildParam must not be null");
        }
        return new SyncApiClient(httpClientBuilderParams);
    }

    private SyncApiClient(HttpClientBuilderParams httpClientBuilderParams) {
        super.init(httpClientBuilderParams);
    }

    public ApiResponse httpPostSync(String str, String str2, String str3, Map<String, String> map, Map<String, String> map2, Map<String, String> map3, byte[] bArr, Map<String, String> map4) {
        try {
            return sendSyncRequest(buildHttpRequest(str, str2, str3, map, map2, map3, bArr, "application/x-www-form-urlencoded; charset=UTF-8", "application/json; charset=UTF-8", map4));
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private ApiRequest buildHttpRequest(String str, String str2, String str3, Map<String, String> map, Map<String, String> map2, Map<String, String> map3, byte[] bArr, String str4, String str5, Map<String, String> map4) {
        ApiRequest apiRequest = new ApiRequest(HttpMethod.POST_FORM, str3);
        apiRequest.setHost(str2);
        apiRequest.setPathParams(map);
        if (ConfigManager.getInstance().isDegradeHttps()) {
            apiRequest.setScheme(Scheme.HTTP);
        } else {
            apiRequest.setScheme(Scheme.HTTPS);
        }
        apiRequest.setFormParams(map3);
        apiRequest.setSignatureMethod("HmacSHA1");
        if (map4 == null) {
            map4 = new HashMap<>();
        }
        for (String str6 : map4.keySet()) {
            String str7 = map4.get(str6);
            if (str7 != null && str7.length() > 0) {
                apiRequest.addHeader(str6, new String(str7.getBytes(SdkConstant.CLOUDAPI_ENCODING), SdkConstant.CLOUDAPI_HEADER_ENCODING));
            }
        }
        apiRequest.setHttpConnectionMode(HttpConnectionModel.MULTIPLE_CONNECTION);
        return apiRequest;
    }
}
