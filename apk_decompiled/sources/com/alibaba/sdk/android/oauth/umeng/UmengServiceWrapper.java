package com.alibaba.sdk.android.oauth.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.sdk.android.oauth.AppCredential;
import com.alibaba.sdk.android.oauth.BindByOauthTask;
import com.alibaba.sdk.android.oauth.LoginByOauthRequest;
import com.alibaba.sdk.android.oauth.LoginByOauthTask;
import com.alibaba.sdk.android.oauth.OauthInfoConfig;
import com.alibaba.sdk.android.oauth.OauthServiceProvider;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.ReflectionUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class UmengServiceWrapper implements OauthServiceProvider, EnvironmentChangeListener {
    private static final String TAG = "oa_umeng";

    @Autowired
    private ConfigService configService;
    private UMShareAPI mShareAPI;
    private OauthServiceProvider oauthServiceProvider;

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void cleanUp() {
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void logout(Context context, LogoutCallback logoutCallback) {
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
    }

    public void init(Context context) {
        this.oauthServiceProvider = this;
        try {
            if (!this.configService.getBooleanProperty("disableUmengSinaDetect", false)) {
                try {
                    Class.forName("com.umeng.socialize.handler.UmengSinaHandler");
                    Config.isUmengSina = true;
                } catch (Exception unused) {
                }
            }
            UMShareConfig uMShareConfig = new UMShareConfig();
            uMShareConfig.isNeedAuthOnGetUserInfo(true);
            this.mShareAPI = UMShareAPI.get(context);
            this.mShareAPI.setShareConfig(uMShareConfig);
        } catch (Exception e) {
            e.printStackTrace();
            AliSDKLogger.e(TAG, "umeng oath init failed");
        }
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void authorizeCallback(int i, int i2, Intent intent) {
        this.mShareAPI.onActivityResult(i, i2, intent);
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void oauth(final Activity activity2, final int i, final AppCredential appCredential, final LoginCallback loginCallback) {
        if (appCredential != null) {
            setUmengAppCredential(i, appCredential);
        }
        SHARE_MEDIA share_media = UmengConfigs.plateform2Media.get(Integer.valueOf(i));
        if (share_media == null) {
            Message messageCreateMessage = MessageUtils.createMessage(MessageConstants.UNSUPPORTED_OATUH_PLATFROM, Integer.valueOf(i));
            AliSDKLogger.log(TAG, messageCreateMessage);
            CommonUtils.onFailure(loginCallback, messageCreateMessage);
        } else {
            final OauthInfoConfig oauthInfoConfig = UmengConfigs.plateform2Config.get(share_media);
            this.mShareAPI.doOauthVerify(activity2, share_media, new UMAuthListener() { // from class: com.alibaba.sdk.android.oauth.umeng.UmengServiceWrapper.1
                public void onCancel(SHARE_MEDIA share_media2, int i2) {
                    AliSDKLogger.w(UmengServiceWrapper.TAG, "onCancel: " + share_media2 + " " + i2);
                }

                public void onStart(SHARE_MEDIA share_media2) {
                    AliSDKLogger.w(UmengServiceWrapper.TAG, "onStart: ");
                }

                public void onComplete(SHARE_MEDIA share_media2, int i2, Map<String, String> map) {
                    if (AliSDKLogger.isDebugEnabled()) {
                        AliSDKLogger.d(OpenAccountConstants.LOG_TAG, map.toString());
                    }
                    String str = map.get(oauthInfoConfig.accessTokenKey);
                    if (str == null) {
                        str = map.get(oauthInfoConfig.spareAccessTokenKey);
                    }
                    String string = str != null ? str.toString() : null;
                    String str2 = map.get(oauthInfoConfig.openIdKey);
                    String string2 = str2 != null ? str2.toString() : null;
                    LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
                    loginByOauthRequest.oauthAppKey = getOauthAppKey(share_media2, appCredential);
                    loginByOauthRequest.accessToken = string;
                    loginByOauthRequest.oauthPlateform = i;
                    loginByOauthRequest.openId = string2;
                    new LoginByOauthTask(activity2, loginCallback, map, loginByOauthRequest, UmengServiceWrapper.this.oauthServiceProvider).execute(new Void[0]);
                }

                public void onError(SHARE_MEDIA share_media2, int i2, Throwable th) {
                    AliSDKLogger.e(UmengServiceWrapper.TAG, "onError: " + share_media2, th);
                }

                private String getOauthAppKey(SHARE_MEDIA share_media2, AppCredential appCredential2) {
                    if (appCredential2 != null && appCredential2.appKey != null) {
                        return appCredential2.appKey;
                    }
                    PlatformConfig.CustomPlatform customPlatform = (PlatformConfig.Platform) PlatformConfig.configs.get(share_media2);
                    if (customPlatform == null) {
                        AliSDKLogger.e(UmengServiceWrapper.TAG, "fail to find the appKey for the platform " + share_media2);
                        return null;
                    }
                    String str = (String) ReflectionUtils.getFieldValue(customPlatform.getClass(), oauthInfoConfig.appKeyFieldName, customPlatform);
                    if (!TextUtils.isEmpty(str)) {
                        return str;
                    }
                    if (customPlatform instanceof PlatformConfig.CustomPlatform) {
                        PlatformConfig.CustomPlatform customPlatform2 = customPlatform;
                        if (!TextUtils.isEmpty(customPlatform2.appkey)) {
                            return customPlatform2.appkey;
                        }
                        if (!TextUtils.isEmpty(customPlatform2.appId)) {
                            return customPlatform2.appId;
                        }
                    }
                    AliSDKLogger.e(UmengServiceWrapper.TAG, "fail to detect the appKey for the platform " + share_media2.name());
                    return null;
                }
            });
        }
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void bind(final Activity activity2, final int i, final AppCredential appCredential, final OABindCallback oABindCallback) {
        if (appCredential != null) {
            setUmengAppCredential(i, appCredential);
        }
        SHARE_MEDIA share_media = UmengConfigs.plateform2Media.get(Integer.valueOf(i));
        if (share_media == null) {
            Message messageCreateMessage = MessageUtils.createMessage(MessageConstants.UNSUPPORTED_OATUH_PLATFROM, Integer.valueOf(i));
            AliSDKLogger.log(TAG, messageCreateMessage);
            CommonUtils.onFailure(oABindCallback, messageCreateMessage);
        } else {
            final OauthInfoConfig oauthInfoConfig = UmengConfigs.plateform2Config.get(share_media);
            this.mShareAPI.doOauthVerify(activity2, share_media, new UMAuthListener() { // from class: com.alibaba.sdk.android.oauth.umeng.UmengServiceWrapper.2
                public void onCancel(SHARE_MEDIA share_media2, int i2) {
                    AliSDKLogger.w(UmengServiceWrapper.TAG, "onCancel: " + share_media2 + " " + i2);
                }

                public void onStart(SHARE_MEDIA share_media2) {
                    AliSDKLogger.w(UmengServiceWrapper.TAG, "onStart " + share_media2);
                }

                public void onComplete(SHARE_MEDIA share_media2, int i2, Map<String, String> map) {
                    if (AliSDKLogger.isDebugEnabled()) {
                        AliSDKLogger.d(OpenAccountConstants.LOG_TAG, map.toString());
                    }
                    String str = map.get(oauthInfoConfig.accessTokenKey);
                    if (str == null) {
                        str = map.get(oauthInfoConfig.spareAccessTokenKey);
                    }
                    String string = str != null ? str.toString() : null;
                    String str2 = map.get(oauthInfoConfig.openIdKey);
                    String string2 = str2 != null ? str2.toString() : null;
                    LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
                    loginByOauthRequest.oauthAppKey = getOauthAppKey(share_media2, appCredential);
                    loginByOauthRequest.accessToken = string;
                    loginByOauthRequest.oauthPlateform = i;
                    loginByOauthRequest.openId = string2;
                    new BindByOauthTask(activity2, oABindCallback, map, loginByOauthRequest, UmengServiceWrapper.this.oauthServiceProvider).execute(new Void[0]);
                }

                public void onError(SHARE_MEDIA share_media2, int i2, Throwable th) {
                    AliSDKLogger.e(UmengServiceWrapper.TAG, "onError: " + share_media2, th);
                }

                private String getOauthAppKey(SHARE_MEDIA share_media2, AppCredential appCredential2) {
                    if (appCredential2 != null && appCredential2.appKey != null) {
                        return appCredential2.appKey;
                    }
                    PlatformConfig.CustomPlatform customPlatform = (PlatformConfig.Platform) PlatformConfig.configs.get(share_media2);
                    if (customPlatform == null) {
                        AliSDKLogger.e(UmengServiceWrapper.TAG, "fail to find the appKey for the platform " + share_media2);
                        return null;
                    }
                    String str = (String) ReflectionUtils.getFieldValue(customPlatform.getClass(), oauthInfoConfig.appKeyFieldName, customPlatform);
                    if (!TextUtils.isEmpty(str)) {
                        return str;
                    }
                    if (customPlatform instanceof PlatformConfig.CustomPlatform) {
                        PlatformConfig.CustomPlatform customPlatform2 = customPlatform;
                        if (!TextUtils.isEmpty(customPlatform2.appkey)) {
                            return customPlatform2.appkey;
                        }
                        if (!TextUtils.isEmpty(customPlatform2.appId)) {
                            return customPlatform2.appId;
                        }
                    }
                    AliSDKLogger.e(UmengServiceWrapper.TAG, "fail to detect the appKey for the platform " + share_media2.name());
                    return null;
                }
            });
        }
    }

    public void setUmengAppCredential(int i, AppCredential appCredential) {
        if (i == 4) {
            PlatformConfig.setQQZone(appCredential.appKey, appCredential.appSecret);
        } else if (i == 3) {
            PlatformConfig.setSinaWeibo(appCredential.appKey, appCredential.appSecret, "http://sns.whalecloud.com");
        } else if (i == 2) {
            PlatformConfig.setWeixin(appCredential.appKey, appCredential.appSecret);
        }
    }
}
