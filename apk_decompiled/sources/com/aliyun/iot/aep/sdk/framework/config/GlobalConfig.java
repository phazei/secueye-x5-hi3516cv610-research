package com.aliyun.iot.aep.sdk.framework.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSON;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.language.LanguageManager;
import com.aliyun.iot.aep.sdk.framework.region.CountryManager;
import com.aliyun.iot.aep.sdk.framework.region.RegionInfo;
import com.aliyun.iot.aep.sdk.framework.region.RegionInfo2;
import com.aliyun.iot.aep.sdk.framework.region.RegionManager;
import com.aliyun.iot.aep.sdk.framework.utils.SpUtil;
import com.aliyun.iot.aep.sdk.log.ALog;
import java.io.Serializable;
import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class GlobalConfig {
    public static final String API_ENV_ONLINE = "release";
    public static final String API_ENV_PRE = "pre";
    public static final String API_ENV_TEST = "test";
    public static final String BONE_ENV_PRETEST = "pretest";
    public static final String BONE_ENV_RELEASE = "release";
    public static final String BONE_ENV_TEST = "test";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f4677a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f4678b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f4679c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private IoTSmart.InitConfig f4680d;
    private RegionInfo2 e;
    private IoTSmart.Country f;
    private String g;
    private String h;
    private Boolean i;
    private HashMap<String, String> j;
    private IoTSmart.ICountrySelectCallBack k;

    private boolean a(IoTSmart.Country country, IoTSmart.Country country2) {
        return false;
    }

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @SuppressLint({"StaticFieldLeak"})
        private static final GlobalConfig f4681a = new GlobalConfig();
    }

    private GlobalConfig() {
        this.f4677a = "release";
        this.f4678b = "release";
        this.f4679c = IoTSmart.PRODUCT_SCOPE_PUBLISHED;
        this.i = false;
        this.k = null;
        reStoreConfigs();
        if (this.f4680d == null) {
            this.f4680d = new IoTSmart.InitConfig();
        }
        if (this.e == null) {
            this.e = new RegionInfo2();
        }
        if (this.j == null) {
            this.j = new HashMap<>();
        }
    }

    public static GlobalConfig getInstance() {
        return a.f4681a;
    }

    public IoTSmart.InitConfig getInitConfig() {
        return this.f4680d;
    }

    public void setInitConfig(IoTSmart.InitConfig initConfig) {
        this.f4680d = initConfig;
        storeInitConfig();
    }

    public RegionInfo2 getRegionInfo() {
        return this.e;
    }

    public void setRegionInfo(RegionInfo2 regionInfo2) {
        this.e = regionInfo2;
        storeRegionInfo();
    }

    public IoTSmart.Country getCountry() {
        return this.f;
    }

    public void setCountry(IoTSmart.Country country, IoTSmart.ICountrySetCallBack iCountrySetCallBack) {
        boolean zA = a(this.f, country);
        this.f = country;
        storeCountry();
        if (zA) {
            if (CountryManager.isChina(country)) {
                RegionManager.setRegionChina(null);
            } else {
                RegionManager.setRegionSingapore(null);
            }
        }
        if (iCountrySetCallBack != null) {
            iCountrySetCallBack.onCountrySet(zA);
        }
    }

    public HashMap<String, String> getConfig() {
        return this.j;
    }

    public void setConfig(HashMap<String, String> map) {
        this.j = map;
        storeConfig();
    }

    public void storeConfigs() {
        storeConfig();
        storeCountry();
        storeInitConfig();
        storeRegionInfo();
    }

    public Boolean getEnableSecurityGuard() {
        return this.i;
    }

    public void setEnableSecurityGuard(Boolean bool) {
        this.i = bool;
    }

    public void storeInitConfig() {
        SpUtil.putObject(AApplication.getInstance(), "key_init_config_global", getInitConfig());
        ALog.d("GlobalConfig", "storeInitConfig:" + toString());
    }

    public void storeConfig() {
        SpUtil.putMap(AApplication.getInstance(), "key_config_global", getConfig());
    }

    public void storeRegionInfo() {
        SpUtil.putObject(AApplication.getInstance(), "key_region_info_global", getRegionInfo());
    }

    public void storeCountry() {
        SpUtil.putObject(AApplication.getInstance(), "key_country_global", getCountry());
    }

    public void reStoreConfigs() {
        setInitConfig((IoTSmart.InitConfig) a(AApplication.getInstance(), "key_init_config_global", IoTSmart.InitConfig.class));
        setRegionInfo(a(AApplication.getInstance(), "key_region_info_global"));
        setCountry((IoTSmart.Country) a(AApplication.getInstance(), "key_country_global", IoTSmart.Country.class), null);
        setConfig((HashMap) SpUtil.getMap(AApplication.getInstance(), "key_config_global"));
        setLanguage(SpUtil.getString(AApplication.getInstance(), "key_language_global"));
        setApiEnv(SpUtil.getString(AApplication.getInstance(), "key_api_env_global"));
        setProductScope(SpUtil.getString(AApplication.getInstance(), "key_product_scope_global"));
        ALog.d("GlobalConfig", "reStoreConfigs:" + toString());
    }

    private <T extends Serializable> T a(Context context, String str, Class<T> cls) {
        T t = (T) SpUtil.getObject(AApplication.getInstance(), str, cls);
        if (t != null) {
            return t;
        }
        T t2 = (T) SpUtil.getObject(context, str);
        ALog.d("GlobalConfig", "getObjectOld2:" + JSON.toJSONString(t2));
        return t2;
    }

    private RegionInfo2 a(Context context, String str) {
        RegionInfo regionInfo;
        RegionInfo2 regionInfo2 = (RegionInfo2) SpUtil.getObject(AApplication.getInstance(), str, RegionInfo2.class);
        if (regionInfo2 != null || (regionInfo = (RegionInfo) SpUtil.getObject(AApplication.getInstance(), str)) == null) {
            return regionInfo2;
        }
        RegionInfo2 regionInfo22 = new RegionInfo2();
        regionInfo22.apiGatewayEndpoint = regionInfo.apiGatewayEndpoint;
        regionInfo22.mqttEndpoint = regionInfo.mqttEndpoint;
        regionInfo22.oaApiGatewayEndpoint = regionInfo.oaApiGatewayEndpoint;
        regionInfo22.pushChannelEndpoint = regionInfo.pushChannelEndpoint;
        regionInfo22.regionEnglishName = regionInfo.regionEnglishName;
        regionInfo22.regionId = regionInfo.regionId;
        ALog.d("GlobalConfig", "getObjectOld:" + JSON.toJSONString(regionInfo));
        SpUtil.putObject(AApplication.getInstance(), str, regionInfo22);
        return regionInfo22;
    }

    public void setLanguage(String str) {
        if (TextUtils.equals(this.g, str)) {
            return;
        }
        this.g = str;
        SpUtil.putString(AApplication.getInstance(), "key_language_global", str);
    }

    public String getLanguage() {
        if (TextUtils.isEmpty(this.g)) {
            this.g = SpUtil.getString(AApplication.getInstance(), "key_language_global");
        }
        if (TextUtils.isEmpty(this.g)) {
            setLanguage(LanguageManager.getDefaultLanguage());
        }
        return this.g;
    }

    public String getApiEnv() {
        return this.f4677a;
    }

    public void setApiEnv(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f4677a = str;
        SpUtil.putString(AApplication.getInstance(), "key_api_env_global", str);
    }

    public String getBoneEnv() {
        return this.f4678b;
    }

    public void setBoneEnv(String str) {
        this.f4678b = str;
    }

    public IoTSmart.ICountrySelectCallBack getCountrySelectCallBack() {
        return this.k;
    }

    public void setCountrySelectCallBack(IoTSmart.ICountrySelectCallBack iCountrySelectCallBack) {
        this.k = iCountrySelectCallBack;
    }

    public void setAuthCode(String str) {
        this.h = str;
    }

    public String getProductScope() {
        return this.f4679c;
    }

    public void setProductScope(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f4679c = str;
        SpUtil.putString(AApplication.getInstance(), "key_product_scope_global", str);
    }

    public String getAuthCode() {
        if (!TextUtils.isEmpty(this.h)) {
            return this.h;
        }
        if (TextUtils.isEmpty(this.f4677a) || this.f4680d == null) {
            return "";
        }
        if (!"test".equals(this.f4677a)) {
            return "china_production";
        }
        return "test_" + this.f4680d.getProductEnv();
    }

    public static String getCentralHost() {
        return "test".equals(getInstance().f4677a) ? "api-performance.aliplus.com" : "api.link.aliyun.com";
    }

    public String toString() {
        return "initConfig:" + JSON.toJSONString(this.f4680d) + SdkConstant.CLOUDAPI_LF + "regionInfo:" + JSON.toJSONString(this.e) + SdkConstant.CLOUDAPI_LF + "country:" + JSON.toJSONString(this.f) + SdkConstant.CLOUDAPI_LF + "language:" + this.g + SdkConstant.CLOUDAPI_LF + "apiEnv:" + this.f4677a + SdkConstant.CLOUDAPI_LF + "boneEnv:" + this.f4678b + SdkConstant.CLOUDAPI_LF + "securityIndex(authCode):" + getAuthCode() + SdkConstant.CLOUDAPI_LF + "productScope:" + getProductScope() + SdkConstant.CLOUDAPI_LF + "config:" + JSON.toJSONString(this.j) + SdkConstant.CLOUDAPI_LF;
    }
}
