package com.alibaba.sdk.android.openaccount.task;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.model.LoginResult;
import com.alibaba.sdk.android.openaccount.model.LoginSuccessResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.util.OpenAccountRiskControlContext;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.framework.region.RegionManager;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class LoginWithSMSCodeTask extends TaskWithDialog<Void, Void, Result<LoginResult>> {
    private static final String TAG = "LoginWithSMSCodeTask";
    private final LoginCallback callback;
    protected ExecutorService executorService;
    private final String mobile;
    private final String mobileLocationCode;

    @Autowired
    private SessionManagerService sessionManagerService;
    private final String smsCode;

    public LoginWithSMSCodeTask(Context context, String str, String str2, String str3, LoginCallback loginCallback) {
        super(context);
        this.mobile = str;
        this.mobileLocationCode = str2;
        this.smsCode = str3;
        this.callback = loginCallback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Result<LoginResult> asyncExecute(Void... voidArr) {
        if (ConfigManager.getInstance().getApiHook() != null) {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.target = FirebaseAnalytics.Event.LOGIN;
            HashMap map = new HashMap();
            map.put("loginId", this.mobile);
            HashMap map2 = new HashMap();
            map2.put("loginRequest", map);
            rpcRequest.params = map2;
            RpcResponse rpcResponseOnInterceptRequest = ConfigManager.getInstance().getApiHook().onInterceptRequest(rpcRequest);
            if (rpcResponseOnInterceptRequest != null && rpcResponseOnInterceptRequest.code != 1) {
                LoginCallback loginCallback = this.callback;
                if (loginCallback != null) {
                    loginCallback.onFailure(-1, ResourceUtils.getString(this.context, "onInterceptRequest exception"));
                }
                return null;
            }
        }
        loginWithSMSMethod(this.mobile, this.mobileLocationCode, this.smsCode, RegionManager.getStoredApiAddress());
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
        LoginCallback loginCallback = this.callback;
        if (loginCallback != null) {
            loginCallback.onFailure(-1, th.getMessage());
        }
    }

    private void loginWithSMSMethod(String str, String str2, String str3, String str4) {
        new IoTAPIClientFactory().getClient().send(buildLoginApiRequest(str, str2, str3, str4), new IoTCallback() { // from class: com.alibaba.sdk.android.openaccount.task.LoginWithSMSCodeTask.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                if (LoginWithSMSCodeTask.this.callback != null) {
                    LoginWithSMSCodeTask.this.callback.onFailure(-1, ResourceUtils.getString(LoginWithSMSCodeTask.this.context, "alisdk_openaccount_message_18_message"));
                }
                ALog.d(LoginWithSMSCodeTask.TAG, "onFailure() called with: ioTRequest = [" + ioTRequest + "], e = [" + exc + "]");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ALog.d(LoginWithSMSCodeTask.TAG, "onResponse() called with: ioTRequest = [" + ioTRequest + "], ioTResponse = [" + ioTResponse + "]");
                try {
                    if (ioTResponse.getCode() != 200) {
                        if (LoginWithSMSCodeTask.this.callback != null) {
                            LoginWithSMSCodeTask.this.callback.onFailure(ioTResponse.getCode(), ioTResponse.getMessage());
                            return;
                        }
                        return;
                    }
                    try {
                        JSONObject jSONObject = new JSONObject(ioTResponse.getData().toString());
                        if (jSONObject.has("loginSuccessResult")) {
                            LoginSuccessResult loginSuccessResult = OpenAccountUtils.parseLoginSuccessResult(jSONObject);
                            loginSuccessResult.scenario = 1;
                            if (loginSuccessResult.reTokenCreationTime == null) {
                                loginSuccessResult.reTokenCreationTime = Long.valueOf(System.currentTimeMillis());
                            }
                            if (loginSuccessResult.sidCreationTime == null) {
                                loginSuccessResult.sidCreationTime = Long.valueOf(System.currentTimeMillis());
                            }
                            SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(loginSuccessResult);
                            if (sessionDataCreateSessionDataFromLoginSuccessResult.scenario == null) {
                                sessionDataCreateSessionDataFromLoginSuccessResult.scenario = 1;
                            }
                            if (sessionDataCreateSessionDataFromLoginSuccessResult.refreshTokenCreationTime == null || sessionDataCreateSessionDataFromLoginSuccessResult.refreshTokenCreationTime.longValue() == 0) {
                                sessionDataCreateSessionDataFromLoginSuccessResult.refreshTokenCreationTime = Long.valueOf(System.currentTimeMillis());
                            }
                            LoginWithSMSCodeTask.this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
                            ALog.d(LoginWithSMSCodeTask.TAG, "updateSession: " + sessionDataCreateSessionDataFromLoginSuccessResult.toString());
                        }
                        if (LoginWithSMSCodeTask.this.callback != null) {
                            LoginWithSMSCodeTask.this.callback.onSuccess(LoginWithSMSCodeTask.this.sessionManagerService.getSession());
                        }
                    } catch (JSONException e) {
                        ALog.e(LoginWithSMSCodeTask.TAG, e.toString());
                        if (LoginWithSMSCodeTask.this.callback != null) {
                            LoginWithSMSCodeTask.this.callback.onFailure(-1, e.getMessage());
                        }
                    }
                } catch (Exception e2) {
                    if (LoginWithSMSCodeTask.this.callback != null) {
                        LoginWithSMSCodeTask.this.callback.onFailure(-1, ResourceUtils.getString(LoginWithSMSCodeTask.this.context, "alisdk_openaccount_message_18_message"));
                    }
                    ALog.d(LoginWithSMSCodeTask.TAG, e2.getMessage());
                }
            }
        });
    }

    private IoTRequest buildLoginApiRequest(String str, String str2, String str3, String str4) {
        IoTRequestBuilder scheme = new IoTRequestBuilder().setApiVersion("1.0.0").setPath("/living/account/without/password/login").setHost(str4).setScheme(Scheme.HTTPS);
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        HashMap map3 = new HashMap();
        map3.put(UtilityImpl.NET_TYPE_MOBILE, str);
        map3.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        map3.put("smsCode", str3);
        map2.put("riskControlInfo", OpenAccountRiskControlContext.buildRiskContext());
        map2.put("checkSmsCodeRequest", map3);
        map.put("requestStr", JSON.toJSONString(map2));
        scheme.setParams(map);
        return scheme.build();
    }
}
