package com.alibaba.sdk.android.openaccount;

import android.content.Context;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.callback.OneKeyCallback;
import com.alibaba.sdk.android.openaccount.callback.SSOResultCallback;
import com.alibaba.sdk.android.openaccount.callback.SendSMSCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.session.SessionListener;

/* JADX INFO: loaded from: classes.dex */
public interface OpenAccountService {
    void accessTokenLogin(Context context, String str, int i, LoginCallback loginCallback);

    void addExtParam(String str, String str2);

    void addSessionListener(SessionListener sessionListener);

    void authCodeLogin(Context context, String str, LoginCallback loginCallback);

    String getOpenAccountDeviceId();

    OpenAccountSession getSession();

    void loginWithSMSCode(Context context, String str, String str2, String str3, LoginCallback loginCallback);

    void logout(Context context, LogoutCallback logoutCallback);

    void oneKeyCancel(Context context, String str, OneKeyCallback oneKeyCallback);

    void oneKeyConfirm(Context context, String str, OneKeyCallback oneKeyCallback);

    void removeSessionListeners(SessionListener sessionListener);

    void sendSMSCodeForLogin(String str, String str2, SendSMSCallback sendSMSCallback);

    void ssoTao(Context context, Long l, SSOResultCallback sSOResultCallback);

    void tokenLogin(Context context, String str, LoginCallback loginCallback);
}
