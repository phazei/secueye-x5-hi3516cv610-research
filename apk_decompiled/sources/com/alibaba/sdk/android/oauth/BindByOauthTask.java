package com.alibaba.sdk.android.oauth;

import android.content.Context;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.LoginResult;
import com.alibaba.sdk.android.openaccount.model.OpenAccountLink;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class BindByOauthTask extends TaskWithDialog<Void, Void, Void> {

    @Autowired
    private ExecutorService executorService;
    private LoginByOauthRequest loginByOauthRequest;
    private OABindCallback loginCallback;
    private OauthServiceProvider oauthServiceProvider;
    private Map<String, Object> otherInfo;

    @Autowired
    private SessionManagerService sessionManagerService;

    public BindByOauthTask(Context context, OABindCallback oABindCallback, Map map, LoginByOauthRequest loginByOauthRequest, OauthServiceProvider oauthServiceProvider) {
        super(context);
        this.loginCallback = oABindCallback;
        this.loginByOauthRequest = loginByOauthRequest;
        this.otherInfo = map;
        this.oauthServiceProvider = oauthServiceProvider;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Void asyncExecute(Void... voidArr) {
        HashMap map = new HashMap();
        map.put("oauthPlateform", Integer.valueOf(this.loginByOauthRequest.oauthPlateform));
        map.put("accessToken", this.loginByOauthRequest.accessToken);
        map.put("openId", this.loginByOauthRequest.openId);
        map.put("oauthAppKey", this.loginByOauthRequest.oauthAppKey);
        map.put(AlinkConstants.KEY_TOKEN_TYPE, this.loginByOauthRequest.tokenType);
        RpcResponse rpcResponsePureInvokeWithRiskControlInfo = RpcUtils.pureInvokeWithRiskControlInfo("loginByOauthRequest", map, "bindbyoauthtoken");
        final Result<LoginResult> loginResult = OpenAccountUtils.toLoginResult(rpcResponsePureInvokeWithRiskControlInfo);
        final int i = loginResult.code;
        if (!loginResult.isSuccess() || loginResult.data == null || loginResult.data.loginSuccessResult == null) {
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.BindByOauthTask.1
                @Override // java.lang.Runnable
                public void run() {
                    BindByOauthTask.this.loginCallback.onFailure(i, loginResult.message);
                    BindByOauthTask.this.logoutOauthAccount();
                }
            });
            return null;
        }
        try {
            JSONObject jSONObject = rpcResponsePureInvokeWithRiskControlInfo.data;
            final OpenAccountLink openAccountLink = new OpenAccountLink();
            openAccountLink.deviceId = jSONObject.optString("deviceId");
            openAccountLink.nickName = jSONObject.optString("nickName");
            openAccountLink.openAccountId = jSONObject.optString("openAccountId");
            openAccountLink.outerId = jSONObject.optString("outerId");
            openAccountLink.outerPlatform = jSONObject.optString("outerPlatform");
            openAccountLink.type = jSONObject.optString("type");
            openAccountLink.useLogin = Boolean.valueOf(Boolean.parseBoolean(jSONObject.optString("useLogin")));
            openAccountLink.gender = Integer.valueOf(Integer.parseInt(jSONObject.optString("gender")));
            openAccountLink.avatarUrl = jSONObject.optString("avatarUrl");
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.BindByOauthTask.2
                @Override // java.lang.Runnable
                public void run() {
                    BindByOauthTask.this.loginCallback.onSuccess(openAccountLink);
                }
            });
        } catch (Exception unused) {
        }
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
        this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.BindByOauthTask.3
            @Override // java.lang.Runnable
            public void run() {
                BindByOauthTask.this.loginCallback.onFailure(MessageConstants.GENERIC_SYSTEM_ERROR, MessageUtils.getMessageContent(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
                BindByOauthTask.this.logoutOauthAccount();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logoutOauthAccount() {
        OauthServiceProvider oauthServiceProvider = this.oauthServiceProvider;
        if (oauthServiceProvider != null) {
            oauthServiceProvider.logout(this.context, new LogoutCallback() { // from class: com.alibaba.sdk.android.oauth.BindByOauthTask.4
                @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
                public void onFailure(int i, String str) {
                }

                @Override // com.alibaba.sdk.android.openaccount.callback.LogoutCallback
                public void onSuccess() {
                }
            });
        }
    }
}
