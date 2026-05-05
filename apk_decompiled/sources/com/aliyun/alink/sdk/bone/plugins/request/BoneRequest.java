package com.aliyun.alink.sdk.bone.plugins.request;

import anet.channel.util.HttpConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.alcs.api.utils.ErrorCode;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTTimeOutCallback;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.bridge.invoker.SyncBoneInvoker;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneService;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.ServiceMode;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
@BoneService(mode = ServiceMode.SINGLE_INSTANCE, name = BoneRequest.API_NAME)
public class BoneRequest extends BaseBoneService {
    public static final String API_NAME = "BoneRequest";

    @BoneMethod
    public void send(String str, String str2, JSONObject jSONObject, JSONObject jSONObject2, final BoneCallback boneCallback) {
        Scheme scheme;
        String strOptString;
        long jOptLong;
        Method method = Method.POST;
        Map<String, Object> map = new HashMap<>();
        if (jSONObject != null) {
            map = (Map) JSON.parseObject(jSONObject.toString(), map.getClass());
        }
        String str3 = null;
        Scheme scheme2 = null;
        if (jSONObject2 != null) {
            String strOptString2 = jSONObject2.optString("method");
            if (TmpConstant.PROPERTY_IDENTIFIER_GET.equalsIgnoreCase(strOptString2)) {
                Method method2 = Method.GET;
            } else if (RequestParameters.SUBRESOURCE_DELETE.equalsIgnoreCase(strOptString2)) {
                Method method3 = Method.DELETE;
            } else if ("put".equalsIgnoreCase(strOptString2)) {
                Method method4 = Method.PUT;
            }
            String strOptString3 = jSONObject2.optString("scheme");
            if ("http://".equalsIgnoreCase(strOptString3) || HttpConstant.HTTP.equalsIgnoreCase(strOptString3)) {
                scheme2 = Scheme.HTTP;
            } else if ("https://".equalsIgnoreCase(strOptString3) || HttpConstant.HTTPS.equalsIgnoreCase(strOptString3)) {
                scheme2 = Scheme.HTTPS;
            }
            String strOptString4 = jSONObject2.optString("host");
            strOptString = jSONObject2.optString("authType");
            jOptLong = jSONObject2.optLong("timeOut");
            scheme = scheme2;
            str3 = strOptString4;
        } else {
            scheme = null;
            strOptString = null;
            jOptLong = 0;
        }
        IoTRequestBuilder params = new IoTRequestBuilder().setHost(str3).setPath(str).setApiVersion(str2).setAuthType(strOptString).setParams(map);
        if (scheme != null) {
            params.setScheme(scheme);
        }
        IoTRequest ioTRequestBuild = params.build();
        IoTCallback ioTTimeOutCallback = new IoTCallback() { // from class: com.aliyun.alink.sdk.bone.plugins.request.BoneRequest.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                boneCallback.failed(SyncBoneInvoker.ERROR_SUB_CODE_EXCEPTION, "client error:" + exc.getMessage(), "运行时异常");
                exc.printStackTrace();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                if (200 != code) {
                    String message = ioTResponse.getMessage();
                    String localizedMsg = ioTResponse.getLocalizedMsg();
                    Object data = ioTResponse.getData();
                    JSONObject jSONObject3 = new JSONObject();
                    try {
                        jSONObject3.put("code", String.valueOf(code));
                        jSONObject3.put("message", message);
                        jSONObject3.put(AlinkConstants.KEY_LOCALIZED_MSG, localizedMsg);
                        jSONObject3.put("data", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boneCallback.failed("608", "server error", "服务端错误", jSONObject3);
                    return;
                }
                Object data2 = ioTResponse.getData();
                JSONObject jSONObject4 = new JSONObject();
                try {
                    jSONObject4.put("code", ErrorCode.UNKNOWN_SUCCESS_CODE);
                    jSONObject4.put("data", data2);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                boneCallback.success(jSONObject4);
            }
        };
        if (jOptLong > 0) {
            ioTTimeOutCallback = new IoTTimeOutCallback(ioTTimeOutCallback, jOptLong);
        }
        new IoTAPIClientFactory().getClient().send(ioTRequestBuild, ioTTimeOutCallback);
    }
}
