package com.alibaba.sdk.android.openaccount.util;

import android.util.Log;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.rpc.RpcService;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.pluto.Pluto;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class RpcUtils {
    public static Result<JSONObject> invokeWithRiskControlInfo(String str, Map<String, Object> map, String str2) {
        try {
            RpcResponse rpcResponsePureInvokeWithRiskControlInfo = pureInvokeWithRiskControlInfo(str, map, str2);
            return Result.result(rpcResponsePureInvokeWithRiskControlInfo.code, rpcResponsePureInvokeWithRiskControlInfo.message, rpcResponsePureInvokeWithRiskControlInfo.data);
        } catch (Exception e) {
            Log.e(OpenAccountConstants.LOG_TAG, "json error", e);
            Message messageCreateMessage = MessageUtils.createMessage(18, new Object[0]);
            return Result.result(messageCreateMessage.code, messageCreateMessage.message);
        }
    }

    public static RpcResponse pureInvokeWithRiskControlInfo(String str, Map<String, Object> map, String str2) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = str2;
        map.put("riskControlInfo", OpenAccountRiskControlContext.buildRiskContext());
        HashMap map2 = new HashMap();
        map2.put(str, map);
        rpcRequest.params = map2;
        return ((RpcService) Pluto.DEFAULT_INSTANCE.getBean(RpcService.class)).invoke(rpcRequest);
    }

    public static RpcResponse pureInvokeWithoutRiskControlInfo(String str, Map<String, Object> map, String str2) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = str2;
        HashMap map2 = new HashMap();
        map2.put(str, map);
        rpcRequest.params = map2;
        return ((RpcService) Pluto.DEFAULT_INSTANCE.getBean(RpcService.class)).invoke(rpcRequest);
    }

    public static RpcResponse pureInvokeWithRiskControlInfo(RpcRequest rpcRequest, String str) {
        ((Map) rpcRequest.params.get(str)).put("riskControlInfo", OpenAccountRiskControlContext.buildRiskContext());
        return ((RpcService) Pluto.DEFAULT_INSTANCE.getBean(RpcService.class)).invoke(rpcRequest);
    }

    public static String toRawRpcJsonString(RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("code", rpcResponse.code);
            jSONObject.put("message", rpcResponse.message);
            jSONObject.put("traceId", rpcResponse.traceId);
            jSONObject.put("subCode", rpcResponse.subCode);
            if (rpcResponse.data != null) {
                jSONObject.put("data", rpcResponse.data);
            } else if (rpcResponse.arrayData != null) {
                jSONObject.put("data", rpcResponse.arrayData);
            }
            return jSONObject.toString();
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to create rpc raw string", e);
            return null;
        }
    }
}
