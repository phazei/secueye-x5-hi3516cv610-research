package com.alibaba.sdk.android.oauth.helper;

import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.config.OpenAccountProvider;
import com.taobao.login4android.constants.LoginEnvType;
import com.taobao.login4android.login.DefaultTaobaoAppProvider;

/* JADX INFO: loaded from: classes.dex */
public class LoginUtil {
    public static LoginEnvType getEnv(Environment environment) {
        switch (environment) {
            case TEST:
                return LoginEnvType.DEV;
            case PRE:
                return LoginEnvType.PRE;
            case SANDBOX:
                return LoginEnvType.DEV;
            default:
                return LoginEnvType.ONLINE;
        }
    }

    public static DefaultTaobaoAppProvider convert(OpenAccountProvider openAccountProvider) {
        DefaultTaobaoAppProvider defaultTaobaoAppProvider = new DefaultTaobaoAppProvider();
        if (openAccountProvider != null) {
            defaultTaobaoAppProvider.setContext(openAccountProvider.getContext());
            defaultTaobaoAppProvider.setAppkey(openAccountProvider.getAppKey());
            defaultTaobaoAppProvider.setDeviceId(openAccountProvider.getDeviceId());
            defaultTaobaoAppProvider.setProductVersion(openAccountProvider.getProductVersion());
            defaultTaobaoAppProvider.setTTID(openAccountProvider.getTTID());
            defaultTaobaoAppProvider.setTID(openAccountProvider.getTID());
            defaultTaobaoAppProvider.setThreadPoolExecutor(openAccountProvider.getThreadPool());
            defaultTaobaoAppProvider.setForceShowPwdInAlert(openAccountProvider.showPWDInAlert());
            defaultTaobaoAppProvider.setNeedWindVaneInit(openAccountProvider.isNeedWindVaneInit());
            defaultTaobaoAppProvider.setSite(openAccountProvider.getSite());
            defaultTaobaoAppProvider.setIsTaobaoApp(openAccountProvider.isTaobaoApp());
            defaultTaobaoAppProvider.setUseSeparateThreadPool(openAccountProvider.isUseSeparateThreadPool());
            defaultTaobaoAppProvider.setAlipaySsoDesKey(openAccountProvider.getAlipaySsoDesKey());
        }
        return defaultTaobaoAppProvider;
    }
}
