package com.alibaba.sdk.android.oauth;

import com.alibaba.sdk.android.oauth.alipay.AlipayOauthServiceProviderImpl;
import com.alibaba.sdk.android.oauth.facebook.FacebookOauthServiceProviderImpl;
import com.alibaba.sdk.android.oauth.google.GoogleOauthServiceProviderImpl;
import com.alibaba.sdk.android.oauth.taobao.Taobao2ndOauthServiceProviderImpl;
import com.alibaba.sdk.android.oauth.taobao.Taobao3rdOauthServiceProviderImpl;
import com.alibaba.sdk.android.oauth.umeng.UmengServiceWrapper;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.pluto.meta.ModuleInfo;
import com.alibaba.sdk.android.pluto.meta.ModuleInfoBuilder;
import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OauthModule {
    private static final String TAG = "oa_Oauth";
    private static boolean alipaySdkAvailable;
    private static boolean facebookAvailable;
    private static boolean googleAvailable;
    private static boolean secondPartTaobaoSdkAvailable;
    private static boolean thirdPartMemberSdkAvaiable;
    private static boolean twitterAvailable;
    private static boolean umengAvailable;

    public static boolean isUmengAvailable() {
        return umengAvailable;
    }

    public static boolean isThirdPartMemberSdkAvaiable() {
        return thirdPartMemberSdkAvaiable;
    }

    public static boolean isSecondPartTaobaoSdkAvailable() {
        return secondPartTaobaoSdkAvailable;
    }

    public static boolean isAlipaySdkAvailable() {
        return alipaySdkAvailable;
    }

    public static boolean isFacebookAvailable() {
        return facebookAvailable;
    }

    static {
        try {
            Class.forName("com.umeng.socialize.UMShareAPI");
            umengAvailable = true;
        } catch (ClassNotFoundException unused) {
        }
        try {
            Class.forName("com.ali.auth.third.core.MemberSDK");
            thirdPartMemberSdkAvaiable = true;
        } catch (ClassNotFoundException unused2) {
        }
        try {
            Class.forName("com.taobao.login4android.login.LoginController");
            secondPartTaobaoSdkAvailable = true;
        } catch (ClassNotFoundException unused3) {
        }
        try {
            Class.forName("com.alipay.sdk.app.AuthTask");
            alipaySdkAvailable = true;
        } catch (ClassNotFoundException unused4) {
        }
        try {
            Class.forName("com.facebook.CallbackManager");
            facebookAvailable = true;
        } catch (ClassNotFoundException unused5) {
        }
        try {
            Class.forName("com.google.android.gms.auth.api.Auth");
            googleAvailable = true;
        } catch (ClassNotFoundException unused6) {
        }
        try {
            Class.forName("com.twitter.sdk.android.core.TwitterApiClient");
            twitterAvailable = true;
        } catch (ClassNotFoundException unused7) {
        }
    }

    public static ModuleInfo getModuleInfo() {
        ModuleInfoBuilder moduleInfoBuilder = new ModuleInfoBuilder("oauth");
        if (umengAvailable) {
            moduleInfoBuilder.addBeanInfo(OauthServiceProvider.class, UmengServiceWrapper.class, "init", Collections.singletonMap("provider", "UMENG"));
        } else {
            AliSDKLogger.w(TAG, "Umeng is not available, Umeng Oauth Service Provider is disabled");
        }
        if (thirdPartMemberSdkAvaiable) {
            moduleInfoBuilder.addBeanInfo(new Class[]{OauthServiceProvider.class, EnvironmentChangeListener.class}, Taobao3rdOauthServiceProviderImpl.class, "init", Collections.singletonMap("provider", "TAOBAO3rd"));
        } else {
            AliSDKLogger.w(TAG, "Taobao MemberSDK is not available, Taobao 3rd Oauth Service Provider is disabled");
        }
        if (secondPartTaobaoSdkAvailable) {
            moduleInfoBuilder.addBeanInfo(new Class[]{OauthServiceProvider.class, EnvironmentChangeListener.class}, Taobao2ndOauthServiceProviderImpl.class, "init", Collections.singletonMap("provider", "TAOBAO2nd"));
        } else {
            AliSDKLogger.w(TAG, "Taobao login4android SDK is not available, Taobao 2nd Oauth Service Provider is disabled");
        }
        if (alipaySdkAvailable) {
            moduleInfoBuilder.addBeanInfo(new Class[]{OauthServiceProvider.class, EnvironmentChangeListener.class}, AlipayOauthServiceProviderImpl.class, "init", Collections.singletonMap("provider", "ALIPAY"));
        } else {
            AliSDKLogger.w(TAG, "Alipay sdk is not available, Alipay Oauth Service Provider is disabled");
        }
        if (facebookAvailable) {
            moduleInfoBuilder.addBeanInfo(new Class[]{OauthServiceProvider.class, Environment.class}, FacebookOauthServiceProviderImpl.class, "init", Collections.singletonMap("provider", "FACEBOOK"));
        } else {
            AliSDKLogger.w(TAG, "Facebook sdk is not available, Facebook Oauth Service Provider is disabled");
        }
        if (googleAvailable) {
            moduleInfoBuilder.addBeanInfo(new Class[]{OauthServiceProvider.class, Environment.class}, GoogleOauthServiceProviderImpl.class, "init", Collections.singletonMap("provider", "GOOGLE"));
        } else {
            AliSDKLogger.w(TAG, "Google sdk is not available, Google Oauth Service Provider is disabled");
        }
        if (umengAvailable || thirdPartMemberSdkAvaiable || secondPartTaobaoSdkAvailable || alipaySdkAvailable || facebookAvailable || googleAvailable || twitterAvailable) {
            moduleInfoBuilder.addBeanInfo(OauthService.class, OauthServiceImpl.class, "init", (Map<String, String>) null);
        }
        return moduleInfoBuilder.build();
    }
}
