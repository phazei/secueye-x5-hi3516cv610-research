package com.alibaba.sdk.android.openaccount.ui;

import android.content.Context;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface OpenAccountFaceLoginService {
    void showFaceLogin(Context context, LoginCallback loginCallback);

    void showFaceLogin(Context context, Map<String, String> map, LoginCallback loginCallback);

    void showFaceReg(Context context, LoginCallback loginCallback);

    void showFaceReg(Context context, Map<String, String> map, LoginCallback loginCallback);
}
