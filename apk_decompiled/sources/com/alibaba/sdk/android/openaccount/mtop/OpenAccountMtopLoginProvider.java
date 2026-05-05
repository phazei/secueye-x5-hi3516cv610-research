package com.alibaba.sdk.android.openaccount.mtop;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.OpenAccountSessionService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.taobao.tao.remotebusiness.login.IRemoteLogin;
import com.taobao.tao.remotebusiness.login.LoginContext;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
import com.taobao.tao.remotebusiness.login.onLoginListener;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountMtopLoginProvider implements IRemoteLogin {
    public boolean isLogining() {
        return false;
    }

    public OpenAccountMtopLoginProvider() {
        RemoteLogin.setLoginImpl(this);
    }

    public void login(final onLoginListener onloginlistener, boolean z) {
        OpenAccountSessionService openAccountSessionService = (OpenAccountSessionService) OpenAccountSDK.getService(OpenAccountSessionService.class);
        OpenAccountService openAccountService = (OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class);
        if (openAccountSessionService == null || openAccountService == null) {
            return;
        }
        if ((openAccountService.getSession().isLogin() && openAccountSessionService.refreshSession(true).code == 100) || OpenAccountConfigs.mtopLoginService == null) {
            return;
        }
        OpenAccountConfigs.mtopLoginService.login(OpenAccountSDK.getAndroidContext(), new LoginCallback() { // from class: com.alibaba.sdk.android.openaccount.mtop.OpenAccountMtopLoginProvider.1
            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                if (i == 100) {
                    onloginlistener.onLoginCancel();
                } else {
                    onloginlistener.onLoginFail();
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
            public void onSuccess(OpenAccountSession openAccountSession) {
                onloginlistener.onLoginSuccess();
            }
        });
    }

    public boolean isSessionValid() {
        OpenAccountSession session;
        OpenAccountService openAccountService = (OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class);
        return (openAccountService == null || (session = openAccountService.getSession()) == null || !session.isLogin()) ? false : true;
    }

    public LoginContext getLoginContext() {
        OpenAccountService openAccountService = (OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class);
        OpenAccountSessionService openAccountSessionService = (OpenAccountSessionService) OpenAccountSDK.getService(OpenAccountSessionService.class);
        if (openAccountService == null || openAccountSessionService == null) {
            return null;
        }
        OpenAccountSession session = openAccountService.getSession();
        LoginContext loginContext = new LoginContext();
        Result<String> sessionId = openAccountSessionService.getSessionId();
        if (sessionId == null || TextUtils.isEmpty(sessionId.data)) {
            return null;
        }
        loginContext.sid = sessionId.data;
        if (session != null && session.isLogin()) {
            loginContext.nickname = session.getUser().nick;
            loginContext.userId = session.getUserId();
        }
        return loginContext;
    }
}
