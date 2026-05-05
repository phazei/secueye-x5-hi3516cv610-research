package com.alibaba.sdk.android.openaccount.ui.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.AbsRunnable;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountFaceLoginService;
import com.alibaba.sdk.android.openaccount.ui.ui.face.H5WindVaneContainer;
import com.alibaba.sdk.android.openaccount.ui.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountFaceLoginServiceImpl implements OpenAccountFaceLoginService {
    public static LoginCallback _faceLoginCallback;

    public void init() {
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountFaceLoginService
    public void showFaceReg(Context context, LoginCallback loginCallback) {
        HashMap map = new HashMap();
        map.put("url", getFaceRegUrl());
        showFaceReg(context, map, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountFaceLoginService
    public void showFaceReg(Context context, Map<String, String> map, LoginCallback loginCallback) {
        showFaceLogin(context, map, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountFaceLoginService
    public void showFaceLogin(Context context, LoginCallback loginCallback) {
        HashMap map = new HashMap();
        map.put("url", getFaceLoginUrl());
        showFaceLogin(context, map, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountFaceLoginService
    public void showFaceLogin(final Context context, final Map<String, String> map, LoginCallback loginCallback) {
        _faceLoginCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountFaceLoginServiceImpl.1
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) H5WindVaneContainer.class);
                Map map2 = map;
                if (map2 != null) {
                    for (Map.Entry entry : map2.entrySet()) {
                        intent.putExtra((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                intent.setFlags(67108864);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_FACE_LOGIN);
    }

    private String getFaceRegUrl() {
        return (ConfigManager.getInstance().getEnvironment() == Environment.PRE ? "https://pre-login-openaccount.taobao.com" : "https://login-openaccount.taobao.com") + "/login/h5/faceSetting.htm?appKey=" + ((SecurityGuardService) OpenAccountSDK.getService(SecurityGuardService.class)).getAppKey() + "&userId=" + ((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)).getSession().getUserId();
    }

    private String getFaceLoginUrl() {
        return (ConfigManager.getInstance().getEnvironment() == Environment.PRE ? "https://pre-login-openaccount.taobao.com" : "https://login-openaccount.taobao.com") + "/login/h5/faceLogin.htm?appKey=" + ((SecurityGuardService) OpenAccountSDK.getService(SecurityGuardService.class)).getAppKey();
    }
}
