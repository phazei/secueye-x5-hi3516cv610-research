package sdk;

import activity.OALoginActivity;
import activity.OAMobileCountrySelectorActivity;
import android.util.Log;
import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAP;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter;
import com.facebook.FacebookSdk;
import com.seculink.app.R;
import com.tencent.bugly.crashreport.CrashReport;
import config.AppConfig;
import tools.ImageCache;
import tools.SharePreferenceManager;

/* JADX INFO: loaded from: classes4.dex */
public class SDKInitHelper {
    private static final String TAG = "SDKInitHelper";

    public static void init(AApplication aApplication) {
        if (aApplication == null || AppConfig.isInitSDK) {
            return;
        }
        AppConfig.isInitSDK = true;
        Log.e("广告sdk", "初始化");
        if (AppConfig.isChina) {
            SharePreferenceManager.getInstance().getADSwitch();
        }
        ConfigManager.getInstance().setGoogleClientId(aApplication.getString(R.string.server_client_id));
        String string = aApplication.getString(R.string.facebook_app_id);
        FacebookSdk.setApplicationId(string);
        ConfigManager.getInstance().setFacebookId(string);
        CrashReport.initCrashReport(aApplication, "cfc352468a", false);
        IoTSmart.InitConfig debug = new IoTSmart.InitConfig().setRegionType(AppConfig.isChina ? 1 : 0).setProductEnv(IoTSmart.PRODUCT_ENV_PROD).setDebug(true);
        IoTSmart.AppKeyConfig appKeyConfig = new IoTSmart.AppKeyConfig();
        appKeyConfig.appKey = "26001873";
        appKeyConfig.appSecret = "f4a90ebee699166af95b092dffadcfc3";
        debug.setAppKeyConfig(appKeyConfig);
        IoTSmart.PushConfig pushConfig = new IoTSmart.PushConfig();
        pushConfig.fcmApplicationId = "1:624233934346:android:94c03cbfe8932c080bf9f3";
        pushConfig.fcmSendId = "624233934346";
        pushConfig.fcmProjectId = "secueye-ca610";
        pushConfig.fcmApiKey = "AIzaSyA-_cAIkuzApUl6wNbzI5pRvauy3962puQ";
        pushConfig.xiaomiAppId = "2882303761517988925";
        pushConfig.xiaomiAppkey = "5931798874925";
        pushConfig.oppoAppKey = "59e6c3f3d6024815883c38a1587ef1de";
        pushConfig.oppoAppSecret = "2c04224b50184c7b9ce4205b2b1bf3b4";
        debug.setPushConfig(pushConfig);
        GlobalConfig.getInstance().setApiEnv("release");
        GlobalConfig.getInstance().setBoneEnv("release");
        ALog.d(TAG, "initConfig1:" + JSON.toJSONString(debug));
        IoTSmart.init(aApplication, debug);
        new AlcsCoAP().setLogLevel(2);
        ImageCache.getInstance().init();
        IPCManager.getInstance().init(aApplication, "0.2.1");
        ALog.setLevel((byte) 3);
        OALoginAdapter oALoginAdapter = (OALoginAdapter) LoginBusiness.getLoginAdapter();
        if (oALoginAdapter != null) {
            oALoginAdapter.setDefaultLoginClass(OALoginActivity.class);
        }
        OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.AccountPasswordLoginFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.ChangeMobileFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.ChangeMobileFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.MobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileRegisterFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        OpenAccountUIConfigs.OneStepMobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.OneStepMobileRegisterFlow.mobileCountrySelectorActvityClazz = OAMobileCountrySelectorActivity.class;
        TmpSdk.setTmpAbilityConfigMap(TmpSdk.KEY_ENABLE_LOCAL_SEARCH_GET_IP_ADDRESS, RequestConstant.FALSE);
    }
}
