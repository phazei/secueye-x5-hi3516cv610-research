package com.alibaba.sdk.android.oauth.taobao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.InitResultCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.login.LoginService;
import com.ali.auth.third.login.callback.LogoutCallback;
import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.sdk.android.oauth.AppCredential;
import com.alibaba.sdk.android.oauth.BindByOauthTask;
import com.alibaba.sdk.android.oauth.LoginByOauthRequest;
import com.alibaba.sdk.android.oauth.LoginByOauthTask;
import com.alibaba.sdk.android.oauth.OauthServiceProvider;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class Taobao3rdOauthServiceProviderImpl implements OauthServiceProvider, EnvironmentChangeListener {
    private static final String TAG = "oa_TaobaoOauth";

    @Autowired
    private ConfigService configService;
    private OauthServiceProvider oauthServiceProvider;

    @Autowired
    private SecurityGuardService securityGuardService;

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void cleanUp() {
    }

    public void init(Context context) {
        this.oauthServiceProvider = this;
        if (this.configService.getBooleanProperty("disable3rdMemberSdkInit", false)) {
            return;
        }
        ConfigManager.POSTFIX_OF_SECURITY_JPG_USER_SET = this.configService.getSecurityImagePostfix();
        if (this.configService.isDebugEnabled()) {
            MemberSDK.turnOnDebug();
        }
        initEnvironment(this.configService.getEnvironment());
        MemberSDK.init(context, new InitResultCallback() { // from class: com.alibaba.sdk.android.oauth.taobao.Taobao3rdOauthServiceProviderImpl.1
            public void onSuccess() {
                AliSDKLogger.i(Taobao3rdOauthServiceProviderImpl.TAG, "MemberSDK initialized successfully");
            }

            public void onFailure(int i, String str) {
                AliSDKLogger.e(Taobao3rdOauthServiceProviderImpl.TAG, "MemberSDK initialized failed code = " + i + " message = " + str);
            }
        });
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
        ConfigManager.APP_KEY_INDEX = this.configService.getAppKeyIndex();
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void oauth(final Activity activity2, int i, AppCredential appCredential, final LoginCallback loginCallback) {
        final LoginService loginService = (LoginService) MemberSDK.getService(LoginService.class);
        loginService.logout(activity2, new LogoutCallback() { // from class: com.alibaba.sdk.android.oauth.taobao.Taobao3rdOauthServiceProviderImpl.2
            public void onSuccess() {
                Taobao3rdOauthServiceProviderImpl.this.oauthTaobao(activity2, loginCallback, loginService);
            }

            public void onFailure(int i2, String str) {
                Taobao3rdOauthServiceProviderImpl.this.oauthTaobao(activity2, loginCallback, loginService);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void oauthTaobao(final Activity activity2, final LoginCallback loginCallback, LoginService loginService) {
        loginService.auth(activity2, new com.ali.auth.third.core.callback.LoginCallback() { // from class: com.alibaba.sdk.android.oauth.taobao.Taobao3rdOauthServiceProviderImpl.3
            public void onSuccess(Session session) {
                LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
                loginByOauthRequest.accessToken = session.openSid;
                loginByOauthRequest.openId = session.openId;
                loginByOauthRequest.oauthAppKey = Taobao3rdOauthServiceProviderImpl.this.securityGuardService.getAppKey();
                loginByOauthRequest.oauthPlateform = 1;
                loginByOauthRequest.tokenType = "havana-sid";
                HashMap map = new HashMap();
                map.put("avatarUrl", session.avatarUrl);
                map.put("nick", session.nick);
                map.put("openId", session.openId);
                new LoginByOauthTask(activity2, loginCallback, map, loginByOauthRequest, Taobao3rdOauthServiceProviderImpl.this.oauthServiceProvider).execute(new Void[0]);
            }

            public void onFailure(int i, String str) {
                AliSDKLogger.e(Taobao3rdOauthServiceProviderImpl.TAG, "Taobao login failed with code " + i + " message " + str);
            }
        });
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void bind(final Activity activity2, int i, AppCredential appCredential, final OABindCallback oABindCallback) {
        ((LoginService) MemberSDK.getService(LoginService.class)).auth(activity2, new com.ali.auth.third.core.callback.LoginCallback() { // from class: com.alibaba.sdk.android.oauth.taobao.Taobao3rdOauthServiceProviderImpl.4
            public void onSuccess(Session session) {
                LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
                loginByOauthRequest.accessToken = session.openSid;
                loginByOauthRequest.openId = session.openId;
                loginByOauthRequest.oauthAppKey = Taobao3rdOauthServiceProviderImpl.this.securityGuardService.getAppKey();
                loginByOauthRequest.oauthPlateform = 1;
                loginByOauthRequest.tokenType = "havana-sid";
                HashMap map = new HashMap();
                map.put("avatarUrl", session.avatarUrl);
                map.put("nick", session.nick);
                map.put("openId", session.openId);
                new BindByOauthTask(activity2, oABindCallback, map, loginByOauthRequest, Taobao3rdOauthServiceProviderImpl.this.oauthServiceProvider).execute(new Void[0]);
            }

            public void onFailure(int i2, String str) {
                AliSDKLogger.e(Taobao3rdOauthServiceProviderImpl.TAG, "Taobao login failed with code " + i2 + " message " + str);
            }
        });
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void authorizeCallback(int i, int i2, Intent intent) {
        CallbackContext.onActivityResult(i, i2, intent);
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void logout(Context context, com.alibaba.sdk.android.openaccount.callback.LogoutCallback logoutCallback) {
        ((LoginService) MemberSDK.getService(LoginService.class)).logout(new LogoutCallback() { // from class: com.alibaba.sdk.android.oauth.taobao.Taobao3rdOauthServiceProviderImpl.5
            public void onFailure(int i, String str) {
            }

            public void onSuccess() {
            }
        });
    }
}
