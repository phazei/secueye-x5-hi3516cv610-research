package com.aliyun.iot.aep.sdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.share.DeviceShareManager;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAP;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestPayload;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.base.ApiDataCallBack;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.language.LanguageManager;
import com.aliyun.iot.aep.sdk.framework.log.APITracker;
import com.aliyun.iot.aep.sdk.framework.model.ILopUserAccountInfo;
import com.aliyun.iot.aep.sdk.framework.model.SaltBean;
import com.aliyun.iot.aep.sdk.framework.region.CountryManager;
import com.aliyun.iot.aep.sdk.framework.region.RegionManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.framework.utils.MD5Utils;
import com.aliyun.iot.aep.sdk.framework.utils.MailHelper;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.page.CountryListActivity;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class IoTSmartImpl {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f4505a = false;

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @SuppressLint({"StaticFieldLeak"})
        private static final IoTSmartImpl f4515a = new IoTSmartImpl();
    }

    public static IoTSmartImpl getInstance() {
        return a.f4515a;
    }

    public void init(AApplication aApplication) {
        init(aApplication, new IoTSmart.InitConfig());
    }

    public void init(AApplication aApplication, IoTSmart.InitConfig initConfig) {
        if (initConfig == null) {
            ALog.e("IoTSmartImpl", "wrong init with no initConfig");
            return;
        }
        if (this.f4505a) {
            ALog.e("IoTSmartImpl", "has been initialized");
            return;
        }
        setDebug(initConfig.isDebug());
        GlobalConfig.getInstance().setInitConfig(initConfig);
        if (initConfig.getAppKeyConfig() == null && initConfig.getCustomSecurity() == null && !GlobalConfig.getInstance().getEnableSecurityGuard().booleanValue()) {
            throw new RuntimeException("Please configure appSecret or customSecurity");
        }
        if (1 == initConfig.getRegionType()) {
            setCountry(CountryManager.COUNTRY_CHINA_ABBR, (IoTSmart.ICountrySetCallBack) null);
        }
        if (TextUtils.isEmpty(GlobalConfig.getInstance().getAuthCode())) {
            ALog.d("IoTSmartImpl", "无法加载安全图片，请检查初始化参数");
            return;
        }
        if (SDKManager.isOAAvailable()) {
            RegionManager.registerOAApiHook();
        }
        SDKManager.init(aApplication);
        setDebug(initConfig.isDebug());
        this.f4505a = true;
    }

    public void getCountryList(final IoTSmart.ICountryListGetCallBack iCountryListGetCallBack) {
        CountryManager.queryCountryList(new ApiDataCallBack<List<IoTSmart.Country>>() { // from class: com.aliyun.iot.aep.sdk.IoTSmartImpl.1
            @Override // com.aliyun.iot.aep.sdk.framework.base.DataCallBack
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onSuccess(final List<IoTSmart.Country> list) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.IoTSmartImpl.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        iCountryListGetCallBack.onSucess(list);
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.framework.base.DataCallBack
            public void onFail(final int i, final String str) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.IoTSmartImpl.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        iCountryListGetCallBack.onFail("server", i, str);
                    }
                });
            }
        });
    }

    public void getRegion(ILopUserAccountInfo iLopUserAccountInfo, IoTCallback ioTCallback) {
        a(iLopUserAccountInfo, ioTCallback, null);
    }

    private void a(ILopUserAccountInfo iLopUserAccountInfo, final IoTCallback ioTCallback, SaltBean saltBean) {
        new IoTAPIClientFactory().getClient().send(a(iLopUserAccountInfo, saltBean), new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.IoTSmartImpl.2
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onFailure(ioTRequest, exc);
                    ALog.d("IoTSmartImpl", "get region info on failure  exception:" + exc.toString());
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                IoTCallback ioTCallback2 = ioTCallback;
                if (ioTCallback2 != null) {
                    ioTCallback2.onResponse(ioTRequest, ioTResponse);
                }
            }
        });
    }

    private IoTRequest a(ILopUserAccountInfo iLopUserAccountInfo, SaltBean saltBean) {
        IoTRequestBuilder scheme = new IoTRequestBuilder().setApiVersion("1.0.2").setPath("living/account/region/get").setHost(a()).setScheme(Scheme.HTTPS);
        HashMap map = new HashMap();
        map.put("countryCode", CountryManager.COUNTRY_CHINA_ABBR);
        if (saltBean == null) {
            if (MailHelper.isEmail(iLopUserAccountInfo.email)) {
                map.put("email", iLopUserAccountInfo.email);
                map.put("phoneLocationCode", "");
                map.put("type", DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
            } else {
                map.put("phone", iLopUserAccountInfo.phone);
                map.put("phoneLocationCode", iLopUserAccountInfo.phoneCode);
                map.put("type", "PHONE");
            }
        } else if (MailHelper.isEmail(iLopUserAccountInfo.email)) {
            map.put("email", a(iLopUserAccountInfo.email, saltBean));
            map.put("phoneLocationCode", "");
            map.put("type", DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
        } else {
            map.put("phone", a(iLopUserAccountInfo.phone, saltBean));
            map.put("phoneLocationCode", iLopUserAccountInfo.phoneCode);
            map.put("type", "PHONE");
        }
        scheme.setParams(map);
        return scheme.build();
    }

    private String a(String str, SaltBean saltBean) {
        String salt = saltBean.getSalt();
        String md5 = MD5Utils.parseMd5(str + MD5Utils.parseMd5(str) + salt);
        StringBuilder sb = new StringBuilder();
        sb.append("md5 encryption succeeded:");
        sb.append(md5);
        ALog.d("IoTSmartImpl", sb.toString());
        return md5;
    }

    private String a() {
        return "test".equalsIgnoreCase(GlobalConfig.getInstance().getApiEnv()) ? "api-performance.aliplus.com" : "api.link.aliyun.com";
    }

    public void setCountry(IoTSmart.Country country, IoTSmart.ICountrySetCallBack iCountrySetCallBack) {
        if (1 == GlobalConfig.getInstance().getInitConfig().getRegionType() && !CountryManager.COUNTRY_CHINA_ABBR.equals(country.domainAbbreviation)) {
            ALog.e("IoTSmartImpl", "can NOT set other country than 'CN' for REGION_CHINA_ONLY");
        } else {
            GlobalConfig.getInstance().setCountry(country, iCountrySetCallBack);
        }
    }

    public void setCountry(String str, IoTSmart.ICountrySetCallBack iCountrySetCallBack) {
        IoTSmart.Country country = CountryManager.getCountry(str);
        if (country == null) {
            if (iCountrySetCallBack != null) {
                iCountrySetCallBack.onCountrySet(false);
            }
            ALog.e("IoTSmartImpl", "setCountry error: Wrong domainAbbreviation");
            return;
        }
        getInstance().setCountry(country, iCountrySetCallBack);
    }

    public IoTSmart.Country getCountry() {
        return GlobalConfig.getInstance().getCountry();
    }

    public void showCountryList(IoTSmart.ICountrySelectCallBack iCountrySelectCallBack) {
        GlobalConfig.getInstance().setCountrySelectCallBack(iCountrySelectCallBack);
        Intent intent = new Intent(AApplication.getInstance(), (Class<?>) CountryListActivity.class);
        intent.addFlags(268435456);
        AApplication.getInstance().startActivity(intent);
    }

    public void setDebug(boolean z) {
        setDebugLevel(z ? 2 : 6);
        b();
    }

    public void setDebugLevel(int i) {
        int i2 = i - 2;
        AlcsCoAP.setLogLevelEx(i2);
        com.aliyun.alink.linksdk.tools.ALog.setLevel((byte) i2);
        ALog.setLevel((byte) (i - 1));
    }

    public void setLanguage(String str) {
        LanguageManager.setLanguage(str);
        CountryManager.updateCountry();
    }

    public String getLanguage() {
        return GlobalConfig.getInstance().getLanguage();
    }

    public void setProductScope(String str) {
        GlobalConfig.getInstance().setProductScope(str);
    }

    public String getShortRegionId() {
        return RegionManager.getStoredShortRegionId();
    }

    public void setAuthCode(String str) {
        GlobalConfig.getInstance().setAuthCode(str);
    }

    private void b() {
        try {
            IoTAPIClientImpl ioTAPIClientImpl = IoTAPIClientImpl.getInstance();
            if (ioTAPIClientImpl == null) {
                return;
            }
            ioTAPIClientImpl.registerTracker(new APITracker());
        } catch (Exception unused) {
        }
    }

    public void doIntercept(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload) {
        if (ioTRequest == null || ioTRequestPayload == null || !IoTSmart.PRODUCT_SCOPE_ALL.equals(GlobalConfig.getInstance().getProductScope())) {
            return;
        }
        for (String str : new String[]{AlinkConstants.PROVISION_DEVICE_FILTER, AlinkConstants.HTTP_PATH_CLOUD_ENROLLEE_DISCOVER, "/thing/productInfo/getByAppKey"}) {
            if (str.equals(ioTRequest.getPath())) {
                ALog.d("IoTSmartImpl", "doIntercept : dev");
                ioTRequest.getParams().put("productStatusEnv", "dev");
                ioTRequestPayload.getParams().put("productStatusEnv", "dev");
                return;
            }
        }
    }
}
