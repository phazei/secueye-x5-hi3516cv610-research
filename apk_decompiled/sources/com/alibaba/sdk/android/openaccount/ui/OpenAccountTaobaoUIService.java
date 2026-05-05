package com.alibaba.sdk.android.openaccount.ui;

import android.content.Context;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;

/* JADX INFO: loaded from: classes.dex */
public interface OpenAccountTaobaoUIService {
    void openLogin(Context context, boolean z, LoginCallback loginCallback);

    void showLogin(Context context, boolean z, LoginCallback loginCallback);
}
