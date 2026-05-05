package com.aliyun.alink.linksdk.cmp.connect.apigw;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ApiGatewayRequest extends ARequest {
    public String apiVersion;
    public String authType;
    public Method method;
    public Map<String, Object> params;
    public Scheme scheme;
    public String host = null;
    public String path = null;
    public String traceId = "";
    public String alinkIdForTracker = "";

    public ApiGatewayRequest() {
        this.scheme = null;
        this.method = null;
        this.apiVersion = null;
        this.authType = null;
        this.params = null;
        this.scheme = null;
        this.method = Method.POST;
        this.authType = AlinkConstants.KEY_IOT_AUTH;
        this.params = new HashMap();
        this.apiVersion = "1.0";
    }

    public static ApiGatewayRequest build(String str, String str2, Map<String, Object> map) {
        ApiGatewayRequest apiGatewayRequest = new ApiGatewayRequest();
        apiGatewayRequest.path = str;
        if (!TextUtils.isEmpty(str2)) {
            apiGatewayRequest.apiVersion = str2;
        }
        if (map != null) {
            apiGatewayRequest.params.putAll(map);
        }
        return apiGatewayRequest;
    }

    public void addParams(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (this.params == null) {
            this.params = new HashMap();
        }
        this.params.put(str, obj);
    }

    public IoTRequest toIotRequest() {
        Map<String, Object> map = this.params;
        if (map != null && map.containsKey(TmpConstant.KEY_IOT_PERFORMANCE_ID)) {
            this.traceId = this.params.get(TmpConstant.KEY_IOT_PERFORMANCE_ID).toString();
            this.params.remove(TmpConstant.KEY_IOT_PERFORMANCE_ID);
        }
        IoTRequestBuilder ioTRequestBuilder = new IoTRequestBuilder();
        ioTRequestBuilder.setPath(this.path).setAuthType(this.authType).setApiVersion(this.apiVersion).setParams(this.params);
        Scheme scheme = this.scheme;
        if (scheme != null) {
            ioTRequestBuilder.setScheme(scheme);
        }
        if (!TextUtils.isEmpty(this.host)) {
            ioTRequestBuilder.setHost(this.host);
        } else if (!TextUtils.isEmpty(ApiGatewayConnect.CONFIGE_HOST)) {
            ioTRequestBuilder.setHost(ApiGatewayConnect.CONFIGE_HOST);
        }
        IoTRequest ioTRequestBuild = ioTRequestBuilder.build();
        if (ioTRequestBuild != null) {
            this.alinkIdForTracker = ioTRequestBuild.getId();
        }
        return ioTRequestBuild;
    }
}
