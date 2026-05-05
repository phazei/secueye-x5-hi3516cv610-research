package com.alibaba.sdk.android.openaccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.oauth.callback.OauthQueryCallback;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;

/* JADX INFO: loaded from: classes.dex */
public interface OauthService {
    @Deprecated
    void addAppCredential(int i, String str, String str2);

    void authorizeCallback(int i, int i2, Intent intent);

    void cleanUp();

    void logout(Context context, LogoutCallback logoutCallback);

    void logoutAll(Context context, LogoutCallback logoutCallback);

    void oaBind(Activity activity2, int i, OABindCallback oABindCallback, boolean z);

    void oauth(Activity activity2, int i, LoginCallback loginCallback);

    void queryOauthList(Context context, int i, OauthQueryCallback oauthQueryCallback);
}
