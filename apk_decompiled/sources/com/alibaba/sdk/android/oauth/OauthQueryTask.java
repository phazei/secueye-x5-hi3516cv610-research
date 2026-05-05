package com.alibaba.sdk.android.oauth;

import android.content.Context;
import com.alibaba.sdk.android.oauth.callback.OauthQueryCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.model.OpenAccountLink;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class OauthQueryTask extends TaskWithDialog<Void, Void, Void> {

    @Autowired
    private ExecutorService executorService;
    private OauthQueryCallback mCallback;
    private int plateform;

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
    }

    public OauthQueryTask(Context context, int i, OauthQueryCallback oauthQueryCallback) {
        super(context);
        this.mCallback = oauthQueryCallback;
        this.plateform = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Void asyncExecute(Void[] voidArr) {
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("outerPlatform", String.valueOf(this.plateform));
        map2.put("type", 1);
        map.put("accountLinkFilterCondition", map2);
        final RpcResponse rpcResponsePureInvokeWithRiskControlInfo = RpcUtils.pureInvokeWithRiskControlInfo("accountLinkListRequest", map, "queryaccountlinklist");
        if (rpcResponsePureInvokeWithRiskControlInfo == null) {
            return null;
        }
        if (rpcResponsePureInvokeWithRiskControlInfo.data != null) {
            try {
                JSONArray jSONArray = new JSONArray(rpcResponsePureInvokeWithRiskControlInfo.data.optString("openAccountLinkList"));
                final ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                    OpenAccountLink openAccountLink = new OpenAccountLink();
                    openAccountLink.deviceId = jSONObject.optString("deviceId");
                    openAccountLink.nickName = jSONObject.optString("nickName");
                    openAccountLink.openAccountId = jSONObject.optString("openAccountId");
                    openAccountLink.outerId = jSONObject.optString("outerId");
                    openAccountLink.outerPlatform = jSONObject.optString("outerPlatform");
                    openAccountLink.type = jSONObject.optString("type");
                    openAccountLink.useLogin = Boolean.valueOf(Boolean.parseBoolean(jSONObject.optString("useLogin")));
                    openAccountLink.gender = Integer.valueOf(Integer.parseInt(jSONObject.optString("gender")));
                    openAccountLink.avatarUrl = jSONObject.optString("avatarUrl");
                    arrayList.add(openAccountLink);
                }
                if (this.mCallback == null) {
                    return null;
                }
                this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.OauthQueryTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        OauthQueryTask.this.mCallback.onSuccess(arrayList);
                    }
                });
                return null;
            } catch (Exception unused) {
                if (this.mCallback == null) {
                    return null;
                }
                this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.OauthQueryTask.2
                    @Override // java.lang.Runnable
                    public void run() {
                        OauthQueryTask.this.mCallback.onSuccess(null);
                    }
                });
                return null;
            }
        }
        if (this.mCallback == null) {
            return null;
        }
        this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.OauthQueryTask.3
            @Override // java.lang.Runnable
            public void run() {
                OauthQueryTask.this.mCallback.onFailure(rpcResponsePureInvokeWithRiskControlInfo.code, rpcResponsePureInvokeWithRiskControlInfo.message);
            }
        });
        return null;
    }
}
