package com.alibaba.sdk.android.openaccount.rpc.model;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import com.taobao.accs.common.Constants;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class RpcRequest {
    public String domain;
    public Map<String, Object> params;
    public String rpcReferer;
    public String target;
    public String version = "1.0";

    public static RpcRequest create(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.target = JSONUtils.optString(jSONObject, Constants.KEY_TARGET);
            String strOptString = JSONUtils.optString(jSONObject, "version");
            if (!TextUtils.isEmpty(strOptString)) {
                rpcRequest.version = strOptString;
            }
            rpcRequest.params = JSONUtils.toMap(jSONObject.optJSONObject("params"));
            return rpcRequest;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return "RpcRequest{target='" + this.target + "', version='" + this.version + "', params=" + this.params + ", domain='" + this.domain + "', rpcReferer='" + this.rpcReferer + "'}";
    }
}
