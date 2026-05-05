package com.alibaba.sdk.android.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;

/* JADX INFO: loaded from: classes.dex */
public interface OauthServiceProvider {
    void authorizeCallback(int i, int i2, Intent intent);

    void bind(Activity activity2, int i, AppCredential appCredential, OABindCallback oABindCallback);

    void cleanUp();

    void logout(Context context, LogoutCallback logoutCallback);

    void oauth(Activity activity2, int i, AppCredential appCredential, LoginCallback loginCallback);
}
