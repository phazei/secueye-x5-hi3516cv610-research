package com.alibaba.sdk.android.oauth.alipay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.auth.third.core.MemberSDK;
import com.alibaba.sdk.android.oauth.AppCredential;
import com.alibaba.sdk.android.oauth.BindByOauthTask;
import com.alibaba.sdk.android.oauth.LoginByOauthRequest;
import com.alibaba.sdk.android.oauth.LoginByOauthTask;
import com.alibaba.sdk.android.oauth.OauthServiceProvider;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.FailureCallback;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.alipay.sdk.app.AuthTask;
import com.google.android.gms.auth.api.identity.SaveAccountLinkingTokenRequest;
import com.huawei.hms.framework.common.ContainerUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AlipayOauthServiceProviderImpl implements OauthServiceProvider, EnvironmentChangeListener {
    public static final String TAG = "oa.AlipayOauthServiceProviderImpl";
    public static OABindCallback mBindCallback;
    public static LoginCallback mLoginCallback;

    @Autowired
    private ConfigService configService;
    private boolean mLoginAfterOauth = true;
    private OauthServiceProvider mOauthServiceProvider;

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void authorizeCallback(int i, int i2, Intent intent) {
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void logout(Context context, LogoutCallback logoutCallback) {
    }

    public void init(Context context) {
        this.mOauthServiceProvider = this;
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void oauth(Activity activity2, int i, AppCredential appCredential, LoginCallback loginCallback) {
        if (activity2 == null) {
            CommonUtils.onFailure(loginCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
            return;
        }
        if (TextUtils.isEmpty(ConfigManager.getInstance().getAlipayAppId()) || TextUtils.isEmpty(ConfigManager.getInstance().getAlipayPid()) || TextUtils.isEmpty(ConfigManager.getInstance().getAlipaySignType())) {
            CommonUtils.onFailure(loginCallback, MessageUtils.createMessage(MessageConstants.NOT_PROVIDE_ALIPY_SSO_PID_OR_APPID, new Object[0]));
            return;
        }
        this.mLoginAfterOauth = true;
        mLoginCallback = loginCallback;
        getSignTask(activity2, loginCallback);
    }

    private void getSignTask(final Activity activity2, final FailureCallback failureCallback) {
        final SignRequest signRequest = new SignRequest();
        signRequest.app_id = ConfigManager.getInstance().getAlipayAppId();
        signRequest.pid = ConfigManager.getInstance().getAlipayPid();
        signRequest.sign_type = ConfigManager.getInstance().getAlipaySignType();
        new GetAlipaySignTask(activity2, new GetSignCallback() { // from class: com.alibaba.sdk.android.oauth.alipay.AlipayOauthServiceProviderImpl.1
            @Override // com.alibaba.sdk.android.oauth.alipay.GetSignCallback
            public void onGetSignSuccessed(final String str) {
                ((ExecutorService) OpenAccountSDK.getService(ExecutorService.class)).postTask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.alipay.AlipayOauthServiceProviderImpl.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Map mapAuthV2 = new AuthTask(activity2).authV2(str, true);
                        if (mapAuthV2 != null) {
                            if (ConfigManager.getInstance().isDebugEnabled()) {
                                AliSDKLogger.d(AlipayOauthServiceProviderImpl.TAG, "result=" + mapAuthV2.toString());
                            }
                            String str2 = (String) mapAuthV2.get("result");
                            if (!TextUtils.isEmpty(str2)) {
                                String[] strArrSplit = str2.split("&");
                                String str3 = "";
                                if (strArrSplit != null) {
                                    String str4 = "";
                                    for (String str5 : strArrSplit) {
                                        String[] strArrSplit2 = str5.split(ContainerUtils.KEY_VALUE_DELIMITER);
                                        if (strArrSplit2 != null && strArrSplit2.length == 2 && SaveAccountLinkingTokenRequest.TOKEN_TYPE_AUTH_CODE.equals(strArrSplit2[0])) {
                                            str4 = strArrSplit2[1];
                                        }
                                    }
                                    str3 = str4;
                                }
                                if (!TextUtils.isEmpty(str3)) {
                                    LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
                                    loginByOauthRequest.accessToken = str3;
                                    loginByOauthRequest.oauthPlateform = 5;
                                    loginByOauthRequest.oauthAppKey = signRequest.app_id;
                                    HashMap map = new HashMap();
                                    if (AlipayOauthServiceProviderImpl.this.mLoginAfterOauth) {
                                        new LoginByOauthTask(activity2, AlipayOauthServiceProviderImpl.mLoginCallback, map, loginByOauthRequest, null).execute(new Void[0]);
                                        return;
                                    } else {
                                        new BindByOauthTask(activity2, AlipayOauthServiceProviderImpl.mBindCallback, map, loginByOauthRequest, null).execute(new Void[0]);
                                        return;
                                    }
                                }
                                CommonUtils.onFailure(failureCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
                                return;
                            }
                            CommonUtils.onFailure(failureCallback, -1, (String) mapAuthV2.get("memo"));
                        }
                    }
                });
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                CommonUtils.onFailure(failureCallback, i, str);
            }
        }, signRequest).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void bind(Activity activity2, int i, AppCredential appCredential, OABindCallback oABindCallback) {
        if (activity2 == null) {
            CommonUtils.onFailure(oABindCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
            return;
        }
        if (TextUtils.isEmpty(ConfigManager.getInstance().getAlipayAppId()) || TextUtils.isEmpty(ConfigManager.getInstance().getAlipayPid()) || TextUtils.isEmpty(ConfigManager.getInstance().getAlipaySignType())) {
            CommonUtils.onFailure(oABindCallback, MessageUtils.createMessage(MessageConstants.NOT_PROVIDE_ALIPY_SSO_PID_OR_APPID, new Object[0]));
            return;
        }
        this.mLoginAfterOauth = false;
        mBindCallback = oABindCallback;
        getSignTask(activity2, oABindCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        initEnvironment(environment2);
    }

    private void initEnvironment(Environment environment) {
        switch (environment) {
            case TEST:
                MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.TEST);
                break;
            case ONLINE:
                MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.ONLINE);
                break;
            case PRE:
                MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.PRE);
                break;
            case SANDBOX:
                MemberSDK.setEnvironment(com.ali.auth.third.core.config.Environment.SANDBOX);
                break;
        }
        com.ali.auth.third.core.config.ConfigManager.APP_KEY_INDEX = this.configService.getAppKeyIndex();
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void cleanUp() {
        mLoginCallback = null;
        mBindCallback = null;
    }
}
