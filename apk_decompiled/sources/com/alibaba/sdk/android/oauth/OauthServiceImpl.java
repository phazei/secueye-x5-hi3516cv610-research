package com.alibaba.sdk.android.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.oauth.callback.OauthQueryCallback;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.OpenAccountLink;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.alibaba.sdk.android.pluto.annotation.BeanProperty;
import com.alibaba.sdk.android.pluto.annotation.Qualifier;
import com.seculink.app.BuildConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OauthServiceImpl implements OauthService {
    private static final String TAG = "oa_Oauth";

    @Autowired(proxyDisabled = true, required = false)
    @Qualifier(filters = {@BeanProperty(key = "provider", value = "ALIPAY")})
    private OauthServiceProvider alipayOauthServiceProvider;

    @Autowired
    private ExecutorService executorService;

    @Autowired(proxyDisabled = true, required = false)
    @Qualifier(filters = {@BeanProperty(key = "provider", value = "FACEBOOK")})
    private OauthServiceProvider facebookOauthServiceProvider;

    @Autowired(proxyDisabled = true, required = false)
    @Qualifier(filters = {@BeanProperty(key = "provider", value = "GOOGLE")})
    private OauthServiceProvider googleOauthServiceProvider;

    @Autowired
    private SessionManagerService sessionManagerService;

    @Autowired(proxyDisabled = true, required = false)
    @Qualifier(filters = {@BeanProperty(key = "provider", value = "TAOBAO2nd")})
    private OauthServiceProvider taobao2ndOauthServiceProvider;

    @Autowired(proxyDisabled = true, required = false)
    @Qualifier(filters = {@BeanProperty(key = "provider", value = "TAOBAO3rd")})
    private OauthServiceProvider taobao3rdOauthServiceProvider;

    @Autowired(proxyDisabled = true, required = false)
    @Qualifier(filters = {@BeanProperty(key = "provider", value = "TWITTER")})
    private OauthServiceProvider twitterOauthServiceProvider;

    @Autowired(proxyDisabled = true, required = false)
    @Qualifier(filters = {@BeanProperty(key = "provider", value = "UMENG")})
    private OauthServiceProvider umengOauthServiceProvider;
    private Map<Integer, AppCredential> plateform2Credential = new HashMap();
    private int currentOauthPlatform = -1;

    public void init(Context context) {
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void oauth(Activity activity2, int i, LoginCallback loginCallback) {
        oauthWhat(activity2, i, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void oaBind(final Activity activity2, final int i, final OABindCallback oABindCallback, final boolean z) {
        if (CommonUtils.sessionFail(oABindCallback)) {
            return;
        }
        queryOauthList(activity2, i, new OauthQueryCallback() { // from class: com.alibaba.sdk.android.oauth.OauthServiceImpl.1
            @Override // com.alibaba.sdk.android.oauth.callback.OauthQueryCallback
            public void onSuccess(List<OpenAccountLink> list) {
                if (!z) {
                    OauthServiceImpl.this.oauthBind(activity2, i, oABindCallback);
                    return;
                }
                if (list == null || list.size() < 1) {
                    OauthServiceImpl.this.oauthBind(activity2, i, oABindCallback);
                    return;
                }
                OABindCallback oABindCallback2 = oABindCallback;
                if (oABindCallback2 != null) {
                    CommonUtils.onFailure(oABindCallback2, MessageUtils.createMessage(i + MessageConstants.OA_ALREADY_BIND_BASE, new Object[0]));
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i2, String str) {
                OABindCallback oABindCallback2 = oABindCallback;
                if (oABindCallback2 != null) {
                    oABindCallback2.onFailure(i2, str);
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void queryOauthList(Context context, int i, OauthQueryCallback oauthQueryCallback) {
        if (CommonUtils.sessionFail(oauthQueryCallback)) {
            return;
        }
        new OauthQueryTask(context, i, oauthQueryCallback).execute(new Void[0]);
    }

    private void oauthWhat(Activity activity2, int i, LoginCallback loginCallback) {
        this.currentOauthPlatform = i;
        if (i == 1) {
            OauthServiceProvider oauthServiceProvider = this.taobao2ndOauthServiceProvider;
            if (oauthServiceProvider != null) {
                oauthServiceProvider.oauth(activity2, i, null, loginCallback);
                return;
            }
            OauthServiceProvider oauthServiceProvider2 = this.taobao3rdOauthServiceProvider;
            if (oauthServiceProvider2 != null) {
                oauthServiceProvider2.oauth(activity2, i, null, loginCallback);
                return;
            }
            Message messageCreateMessage = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, "taobao");
            AliSDKLogger.log(TAG, messageCreateMessage);
            CommonUtils.onFailure(loginCallback, messageCreateMessage);
            return;
        }
        if (i == 5) {
            OauthServiceProvider oauthServiceProvider3 = this.alipayOauthServiceProvider;
            if (oauthServiceProvider3 != null) {
                oauthServiceProvider3.oauth(activity2, i, null, loginCallback);
                return;
            }
            Message messageCreateMessage2 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, "alipay");
            AliSDKLogger.log(TAG, messageCreateMessage2);
            CommonUtils.onFailure(loginCallback, messageCreateMessage2);
            return;
        }
        if (i == 13) {
            OauthServiceProvider oauthServiceProvider4 = this.facebookOauthServiceProvider;
            if (oauthServiceProvider4 != null) {
                oauthServiceProvider4.oauth(activity2, i, null, loginCallback);
                return;
            }
            Message messageCreateMessage3 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, "alipay");
            AliSDKLogger.log(TAG, messageCreateMessage3);
            CommonUtils.onFailure(loginCallback, messageCreateMessage3);
            return;
        }
        if (i == 32) {
            OauthServiceProvider oauthServiceProvider5 = this.googleOauthServiceProvider;
            if (oauthServiceProvider5 != null) {
                oauthServiceProvider5.oauth(activity2, i, null, loginCallback);
                return;
            }
            Message messageCreateMessage4 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, BuildConfig.FLAVOR);
            AliSDKLogger.log(TAG, messageCreateMessage4);
            CommonUtils.onFailure(loginCallback, messageCreateMessage4);
            return;
        }
        if (i == 33) {
            OauthServiceProvider oauthServiceProvider6 = this.twitterOauthServiceProvider;
            if (oauthServiceProvider6 != null) {
                oauthServiceProvider6.oauth(activity2, i, null, loginCallback);
                return;
            }
            Message messageCreateMessage5 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, "twitter");
            AliSDKLogger.log(TAG, messageCreateMessage5);
            CommonUtils.onFailure(loginCallback, messageCreateMessage5);
            return;
        }
        OauthServiceProvider oauthServiceProvider7 = this.umengOauthServiceProvider;
        if (oauthServiceProvider7 == null) {
            Message messageCreateMessage6 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, Integer.valueOf(i));
            AliSDKLogger.log(TAG, messageCreateMessage6);
            CommonUtils.onFailure(loginCallback, messageCreateMessage6);
            return;
        }
        oauthServiceProvider7.oauth(activity2, i, this.plateform2Credential.get(Integer.valueOf(i)), loginCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void oauthBind(Activity activity2, int i, OABindCallback oABindCallback) {
        this.currentOauthPlatform = i;
        if (i == 1) {
            OauthServiceProvider oauthServiceProvider = this.taobao2ndOauthServiceProvider;
            if (oauthServiceProvider != null) {
                oauthServiceProvider.bind(activity2, i, null, oABindCallback);
                return;
            }
            OauthServiceProvider oauthServiceProvider2 = this.taobao3rdOauthServiceProvider;
            if (oauthServiceProvider2 != null) {
                oauthServiceProvider2.bind(activity2, i, null, oABindCallback);
                return;
            }
            Message messageCreateMessage = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, "taobao");
            AliSDKLogger.log(TAG, messageCreateMessage);
            CommonUtils.onFailure(oABindCallback, messageCreateMessage);
            return;
        }
        if (i == 5) {
            OauthServiceProvider oauthServiceProvider3 = this.alipayOauthServiceProvider;
            if (oauthServiceProvider3 != null) {
                oauthServiceProvider3.bind(activity2, i, null, oABindCallback);
                return;
            }
            Message messageCreateMessage2 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, "alipay");
            AliSDKLogger.log(TAG, messageCreateMessage2);
            CommonUtils.onFailure(oABindCallback, messageCreateMessage2);
            return;
        }
        OauthServiceProvider oauthServiceProvider4 = this.umengOauthServiceProvider;
        if (oauthServiceProvider4 == null) {
            Message messageCreateMessage3 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, Integer.valueOf(i));
            AliSDKLogger.log(TAG, messageCreateMessage3);
            CommonUtils.onFailure(oABindCallback, messageCreateMessage3);
            return;
        }
        oauthServiceProvider4.bind(activity2, i, this.plateform2Credential.get(Integer.valueOf(i)), oABindCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void logoutAll(Context context, LogoutCallback logoutCallback) {
        OauthServiceProvider oauthServiceProvider;
        if (ConfigManager.getInstance().isLogoutLoginSDKSwitch() && (oauthServiceProvider = this.taobao2ndOauthServiceProvider) != null) {
            oauthServiceProvider.logout(context, null);
        }
        OauthServiceProvider oauthServiceProvider2 = this.taobao3rdOauthServiceProvider;
        if (oauthServiceProvider2 != null) {
            oauthServiceProvider2.logout(context, null);
        }
        OauthServiceProvider oauthServiceProvider3 = this.alipayOauthServiceProvider;
        if (oauthServiceProvider3 != null) {
            oauthServiceProvider3.logout(context, null);
        }
        OauthServiceProvider oauthServiceProvider4 = this.umengOauthServiceProvider;
        if (oauthServiceProvider4 != null) {
            oauthServiceProvider4.logout(context, null);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void logout(Context context, LogoutCallback logoutCallback) {
        int i = this.currentOauthPlatform;
        if (i == 1) {
            OauthServiceProvider oauthServiceProvider = this.taobao2ndOauthServiceProvider;
            if (oauthServiceProvider != null) {
                oauthServiceProvider.logout(context, null);
                return;
            }
            OauthServiceProvider oauthServiceProvider2 = this.taobao3rdOauthServiceProvider;
            if (oauthServiceProvider2 != null) {
                oauthServiceProvider2.logout(context, logoutCallback);
                return;
            }
            Message messageCreateMessage = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, "taobao");
            AliSDKLogger.log(TAG, messageCreateMessage);
            CommonUtils.onFailure(logoutCallback, messageCreateMessage);
            return;
        }
        OauthServiceProvider oauthServiceProvider3 = this.umengOauthServiceProvider;
        if (oauthServiceProvider3 == null) {
            Message messageCreateMessage2 = MessageUtils.createMessage(MessageConstants.UNINSTALLED_OATUH_PLATFROM, Integer.valueOf(i));
            AliSDKLogger.log(TAG, messageCreateMessage2);
            CommonUtils.onFailure(logoutCallback, messageCreateMessage2);
            return;
        }
        oauthServiceProvider3.logout(context, logoutCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void addAppCredential(int i, String str, String str2) {
        this.plateform2Credential.put(Integer.valueOf(i), new AppCredential(str, str2));
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void authorizeCallback(int i, int i2, Intent intent) {
        OauthServiceProvider oauthServiceProvider = this.taobao3rdOauthServiceProvider;
        if (oauthServiceProvider != null) {
            oauthServiceProvider.authorizeCallback(i, i2, intent);
        }
        OauthServiceProvider oauthServiceProvider2 = this.taobao2ndOauthServiceProvider;
        if (oauthServiceProvider2 != null) {
            oauthServiceProvider2.authorizeCallback(i, i2, intent);
        }
        OauthServiceProvider oauthServiceProvider3 = this.umengOauthServiceProvider;
        if (oauthServiceProvider3 != null) {
            oauthServiceProvider3.authorizeCallback(i, i2, intent);
        }
        OauthServiceProvider oauthServiceProvider4 = this.alipayOauthServiceProvider;
        if (oauthServiceProvider4 != null) {
            oauthServiceProvider4.authorizeCallback(i, i2, intent);
        }
        OauthServiceProvider oauthServiceProvider5 = this.facebookOauthServiceProvider;
        if (oauthServiceProvider5 != null) {
            oauthServiceProvider5.authorizeCallback(i, i2, intent);
        }
        OauthServiceProvider oauthServiceProvider6 = this.googleOauthServiceProvider;
        if (oauthServiceProvider6 != null) {
            oauthServiceProvider6.authorizeCallback(i, i2, intent);
        }
        OauthServiceProvider oauthServiceProvider7 = this.twitterOauthServiceProvider;
        if (oauthServiceProvider7 != null) {
            oauthServiceProvider7.authorizeCallback(i, i2, intent);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.OauthService
    public void cleanUp() {
        OauthServiceProvider oauthServiceProvider = this.taobao2ndOauthServiceProvider;
        if (oauthServiceProvider != null) {
            oauthServiceProvider.cleanUp();
        }
        OauthServiceProvider oauthServiceProvider2 = this.taobao3rdOauthServiceProvider;
        if (oauthServiceProvider2 != null) {
            oauthServiceProvider2.cleanUp();
        }
        OauthServiceProvider oauthServiceProvider3 = this.alipayOauthServiceProvider;
        if (oauthServiceProvider3 != null) {
            oauthServiceProvider3.cleanUp();
        }
        OauthServiceProvider oauthServiceProvider4 = this.umengOauthServiceProvider;
        if (oauthServiceProvider4 != null) {
            oauthServiceProvider4.cleanUp();
        }
        OauthServiceProvider oauthServiceProvider5 = this.googleOauthServiceProvider;
        if (oauthServiceProvider5 != null) {
            oauthServiceProvider5.cleanUp();
        }
        OauthServiceProvider oauthServiceProvider6 = this.twitterOauthServiceProvider;
        if (oauthServiceProvider6 != null) {
            oauthServiceProvider6.cleanUp();
        }
    }
}
