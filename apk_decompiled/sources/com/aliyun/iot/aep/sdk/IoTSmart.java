package com.aliyun.iot.aep.sdk;

import com.aliyun.alink.linksdk.securesigner.ISecuritySource;
import com.aliyun.alink.linksdk.securesigner.SecuritySourceContext;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.model.ILopUserAccountInfo;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class IoTSmart {
    public static final String PRODUCT_ENV_DEV = "develop";
    public static final String PRODUCT_ENV_PROD = "production";
    public static final String PRODUCT_SCOPE_ALL = "product_scope_all";
    public static final String PRODUCT_SCOPE_PUBLISHED = "product_scope_published";
    public static final int REGION_ALL = 0;
    public static final int REGION_CHINA_ONLY = 1;

    public static class AppKeyConfig implements Serializable {
        public String appKey;
        public String appSecret;
    }

    public static class Country implements Serializable {
        public String areaName;
        public String code;
        public String domainAbbreviation;
        public String isoCode;
        public String pinyin;
    }

    public interface ICountryListGetCallBack {
        void onFail(String str, int i, String str2);

        void onSucess(List<Country> list);
    }

    public interface ICountrySelectCallBack {
        void onCountrySelect(Country country);
    }

    public interface ICountrySetCallBack {
        void onCountrySet(boolean z);
    }

    public static class PushConfig implements Serializable {
        public String fcmApiKey;
        public String fcmApplicationId;
        public String fcmProjectId;
        public String fcmSendId;
        public String oppoAppKey;
        public String oppoAppSecret;
        public String xiaomiAppId;
        public String xiaomiAppkey;
    }

    public static void init(AApplication aApplication) {
        IoTSmartImpl.getInstance().init(aApplication);
    }

    public static void init(AApplication aApplication, InitConfig initConfig) {
        IoTSmartImpl.getInstance().init(aApplication, initConfig);
    }

    public static void getCountryList(ICountryListGetCallBack iCountryListGetCallBack) {
        IoTSmartImpl.getInstance().getCountryList(iCountryListGetCallBack);
    }

    public static void setCountry(Country country, ICountrySetCallBack iCountrySetCallBack) {
        IoTSmartImpl.getInstance().setCountry(country, iCountrySetCallBack);
    }

    public static void setCountry(String str, ICountrySetCallBack iCountrySetCallBack) {
        IoTSmartImpl.getInstance().setCountry(str, iCountrySetCallBack);
    }

    public static Country getCountry() {
        return IoTSmartImpl.getInstance().getCountry();
    }

    public static void showCountryList(ICountrySelectCallBack iCountrySelectCallBack) {
        IoTSmartImpl.getInstance().showCountryList(iCountrySelectCallBack);
    }

    public static void setDebug(boolean z) {
        IoTSmartImpl.getInstance().setDebug(z);
    }

    public static void setDebugLevel(int i) {
        IoTSmartImpl.getInstance().setDebugLevel(i);
    }

    public static void setLanguage(String str) {
        IoTSmartImpl.getInstance().setLanguage(str);
    }

    public static String getLanguage() {
        return IoTSmartImpl.getInstance().getLanguage();
    }

    public static void setProductScope(String str) {
        IoTSmartImpl.getInstance().setProductScope(str);
    }

    public static void setAuthCode(String str) {
        IoTSmartImpl.getInstance().setAuthCode(str);
    }

    public static String getShortRegionId() {
        return IoTSmartImpl.getInstance().getShortRegionId();
    }

    public static void getRegion(ILopUserAccountInfo iLopUserAccountInfo, IoTCallback ioTCallback) {
        IoTSmartImpl.getInstance().getRegion(iLopUserAccountInfo, ioTCallback);
    }

    public static class InitConfig implements Serializable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f4501a = 1;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f4502b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f4503c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private PushConfig f4504d;
        private AppKeyConfig e;
        private ISecuritySource f;

        public int getRegionType() {
            return this.f4501a;
        }

        public InitConfig setRegionType(int i) {
            this.f4501a = i;
            return this;
        }

        public String getProductEnv() {
            return this.f4502b;
        }

        public InitConfig setProductEnv(String str) {
            this.f4502b = str;
            return this;
        }

        public boolean isDebug() {
            return this.f4503c;
        }

        public InitConfig setDebug(boolean z) {
            this.f4503c = z;
            return this;
        }

        public PushConfig getPushConfig() {
            return this.f4504d;
        }

        public InitConfig setPushConfig(PushConfig pushConfig) {
            this.f4504d = pushConfig;
            return this;
        }

        public InitConfig setAppKeyConfig(AppKeyConfig appKeyConfig) {
            this.e = appKeyConfig;
            SecuritySourceContext.getInstance().setAppKey(appKeyConfig.appKey);
            SecuritySourceContext.getInstance().setAppSecretKey(appKeyConfig.appSecret);
            return this;
        }

        public InitConfig setCustomSecurity(ISecuritySource iSecuritySource) {
            this.f = iSecuritySource;
            SecuritySourceContext.getInstance().setISecuritySource(iSecuritySource);
            return this;
        }

        public ISecuritySource getCustomSecurity() {
            return this.f;
        }

        public AppKeyConfig getAppKeyConfig() {
            return this.e;
        }
    }
}
