package com.alibaba.sdk.android.openaccount.impl;

import android.content.Context;
import com.alibaba.sdk.android.oauth.LoginByOauthRequest;
import com.alibaba.sdk.android.oauth.LoginByOauthTask;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.callback.OneKeyCallback;
import com.alibaba.sdk.android.openaccount.callback.SSOResultCallback;
import com.alibaba.sdk.android.openaccount.callback.SendSMSCallback;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.session.SessionListener;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.LoginWithSMSCodeTask;
import com.alibaba.sdk.android.openaccount.task.LogoutTask;
import com.alibaba.sdk.android.openaccount.task.OneKeyCancelTask;
import com.alibaba.sdk.android.openaccount.task.OneKeyConfirmTask;
import com.alibaba.sdk.android.openaccount.task.SSOTaoProvider;
import com.alibaba.sdk.android.openaccount.task.SendSMSForLoginTask;
import com.alibaba.sdk.android.openaccount.task.TokenLoginTask;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountServiceImpl implements OpenAccountService {
    public static SSOResultCallback _ssoResultCallback;

    @Autowired
    private SessionManagerService sessionManagerService;

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void logout(Context context, LogoutCallback logoutCallback) {
        new LogoutTask(context, logoutCallback).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void tokenLogin(Context context, String str, LoginCallback loginCallback) {
        new TokenLoginTask(context, str, loginCallback).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void authCodeLogin(Context context, String str, LoginCallback loginCallback) {
        LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
        loginByOauthRequest.oauthAppKey = ((SecurityGuardService) Pluto.DEFAULT_INSTANCE.getBean(SecurityGuardService.class)).getAppKey();
        loginByOauthRequest.authCode = str;
        loginByOauthRequest.oauthPlateform = 23;
        new LoginByOauthTask(context, loginCallback, new HashMap(), loginByOauthRequest, null).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void accessTokenLogin(Context context, String str, int i, LoginCallback loginCallback) {
        LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
        loginByOauthRequest.oauthAppKey = ((SecurityGuardService) Pluto.DEFAULT_INSTANCE.getBean(SecurityGuardService.class)).getAppKey();
        loginByOauthRequest.accessToken = str;
        loginByOauthRequest.oauthPlateform = i;
        new LoginByOauthTask(context, loginCallback, new HashMap(), loginByOauthRequest, null).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public OpenAccountSession getSession() {
        return this.sessionManagerService.getSession();
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void addSessionListener(SessionListener sessionListener) {
        this.sessionManagerService.registerSessionListener(sessionListener);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void removeSessionListeners(SessionListener sessionListener) {
        this.sessionManagerService.unRegisterSessionListener(sessionListener);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void ssoTao(Context context, Long l, SSOResultCallback sSOResultCallback) {
        _ssoResultCallback = sSOResultCallback;
        if (CommonUtils.sessionFail(sSOResultCallback)) {
            return;
        }
        new SSOTaoProvider(context, l, sSOResultCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void oneKeyCancel(Context context, String str, OneKeyCallback oneKeyCallback) {
        if (CommonUtils.sessionFail(oneKeyCallback)) {
            return;
        }
        new OneKeyCancelTask(context, str, oneKeyCallback).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void oneKeyConfirm(Context context, String str, OneKeyCallback oneKeyCallback) {
        if (CommonUtils.sessionFail(oneKeyCallback)) {
            return;
        }
        new OneKeyConfirmTask(context, str, oneKeyCallback).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public String getOpenAccountDeviceId() {
        return DeviceManager.INSTANCE.getSdkDeviceId();
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void addExtParam(String str, String str2) {
        ConfigManager.getInstance().putExtBizParam(str, str2);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void sendSMSCodeForLogin(String str, String str2, SendSMSCallback sendSMSCallback) {
        SendSMSForLoginTask.SendSMSForLogin(str, str2, sendSMSCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountService
    public void loginWithSMSCode(Context context, String str, String str2, String str3, LoginCallback loginCallback) {
        new LoginWithSMSCodeTask(context, str, str2, str3, loginCallback).execute(new Void[0]);
    }
}
