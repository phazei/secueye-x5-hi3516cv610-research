package com.alibaba.sdk.android.openaccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.callback.OnActivityResultCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.config.LanguageCode;
import com.alibaba.sdk.android.openaccount.config.OpenAccountProvider;
import com.alibaba.sdk.android.openaccount.config.PropertyChangeListener;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.hook.OAApiHook;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.trace.TraceLoggerManager;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.RequestCodeAllocator;
import com.alibaba.sdk.android.openaccount.util.TraceHelper;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.alibaba.sdk.android.pluto.meta.ModuleInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ConfigManager implements ConfigService {
    private static final String CONFIG = "config";
    private static final String PROPERTY_NAME_FILTER_KEY = "_property_name";
    private static final String TAG = "oa_config";
    private OAApiHook apiHook;
    private String bundleName;
    private Class checkCodeFragment;
    private Class confirmFragment;
    private Context context;
    private boolean debugEnabled;

    @Autowired
    private ExecutorService executorService;
    private boolean initialized;
    private String mAlipayAppId;
    private String mAlipayPid;
    private String mAlipaySignType;
    private String mAppKey;
    private String mAppSecret;
    private String mFacebookId;
    private String mGoogleClientId;
    private boolean mIsShowPasswordStrengthHint;
    private OnActivityResultCallback mOnActivityResultCallback;
    private String mTwitterId;
    private String mTwitterSecret;
    private Class mobileFragment;
    private Class mobileRegisterFragment;
    private OpenAccountProvider openAccountProvider;
    private Map<String, String> properties;
    private Class pwdLoginFragment;
    private Class resetPwdFragment;
    private SharedPreferences sp;
    private String userConfigSecurityJpgPostfix;
    private static final ConfigManager INSTANCE = new ConfigManager();
    private static final String[] DEFAULT_SECURITY_GUARD_IMAGE_SUFFIX = {"test", "", "", "test"};
    private int[] appKeyIndexes = {0, 0, 0, 0};
    private Map<String, String> userProperties = new ConcurrentHashMap();
    private Map<String, String> extBizParam = new HashMap(50);
    private Environment env = Environment.ONLINE;
    private boolean useSingleImage = false;
    private String mApiGatewayHost = "sdk.openaccount.aliyun.com";
    private boolean openTaobaoUILogin = false;
    private boolean isOpenMtop = false;
    private boolean isAPIGateway = false;
    private boolean degradeHttps = false;
    private boolean logoutLoginSDKSwitch = false;
    private boolean registerLoginBroadcast = true;
    private Version sdkVersion = new Version(3, 4, 2);
    private boolean dailyNocaptcha = false;
    private boolean debugOKHttp = false;
    private int mMinPasswordLength = 6;
    private int mMaxPasswordLength = 20;
    private List<String> mHostWhiteList = new ArrayList(50);
    int retryTime = 3;
    int socketTimeoutMillis = 5000;
    int connectionTimeoutMills = 5000;
    private boolean supportOfflineLogin = false;

    public boolean isDegradeHttps() {
        return this.degradeHttps;
    }

    public void setDegradeHttps(boolean z) {
        this.degradeHttps = z;
    }

    public int getMinPasswordLength() {
        return this.mMinPasswordLength;
    }

    public void setMinPasswordLength(int i) {
        this.mMinPasswordLength = i;
    }

    public int getMaxPasswordLength() {
        return this.mMaxPasswordLength;
    }

    public void setMaxPasswordLength(int i) {
        this.mMaxPasswordLength = i;
    }

    public boolean isShowPasswordStrengthHint() {
        return this.mIsShowPasswordStrengthHint;
    }

    public void setShowPasswordStrengthHint(boolean z) {
        this.mIsShowPasswordStrengthHint = z;
    }

    public boolean isDebugOKHttp() {
        return this.debugOKHttp;
    }

    public void setDebugOKHttp(boolean z) {
        this.debugOKHttp = z;
    }

    public OAApiHook getApiHook() {
        return this.apiHook;
    }

    public List<String> getHostWhiteList() {
        return this.mHostWhiteList;
    }

    public void setHostWhiteList(List<String> list) {
        this.mHostWhiteList = list;
    }

    public void addHostWhiteList(String str) {
        if (this.mHostWhiteList == null) {
            this.mHostWhiteList = new ArrayList(50);
        }
        if (TextUtils.isEmpty(str) || this.mHostWhiteList.contains(str)) {
            return;
        }
        this.mHostWhiteList.add(str);
    }

    public void setApiHook(OAApiHook oAApiHook) {
        this.apiHook = oAApiHook;
    }

    public int getSocketTimeoutMillis() {
        return this.socketTimeoutMillis;
    }

    public void setSocketTimeoutMillis(int i) {
        this.socketTimeoutMillis = i;
    }

    public int getConnectionTimeoutMills() {
        return this.connectionTimeoutMills;
    }

    public void setConnectionTimeoutMills(int i) {
        this.connectionTimeoutMills = i;
    }

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        if (this.initialized) {
            return;
        }
        this.context = context;
        this.properties = new ConcurrentHashMap();
        for (ModuleInfo moduleInfo : Pluto.DEFAULT_INSTANCE.getModuleInfos()) {
            if (moduleInfo.properties != null) {
                for (Map.Entry<String, String> entry : moduleInfo.properties.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        this.properties.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        overrideProperties(getDynamicConfigs(), false);
        overrideProperties(this.userProperties, false);
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "Initialize plugin system persistent configurations successfully");
        }
        this.initialized = true;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public Map<String, String> getProperties() {
        Map<String, String> map = this.properties;
        if (map == null) {
            map = this.userProperties;
        }
        return Collections.unmodifiableMap(map);
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public int getAppKeyIndex() {
        return this.appKeyIndexes[this.env.ordinal()];
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public boolean isDebugEnabled() {
        return this.debugEnabled;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setDebugEnabled(boolean z) {
        this.debugEnabled = z;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public boolean isUseSingleImage() {
        return this.useSingleImage;
    }

    public void setUseSingleImage(boolean z) {
        this.useSingleImage = z;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setAppKeyIndex(int i, int i2, int i3, int i4) {
        Log.d(TAG, "setAppKeyIndex() called with: test = [" + i + "], online = [" + i2 + "], pre = [" + i3 + "], sandbox = [" + i4 + "]" + Log.getStackTraceString(new Throwable()));
        this.appKeyIndexes = new int[]{i, i2, i3, i4};
        setUseSingleImage(true);
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public int getAppKeyIndex(Environment environment) {
        return this.appKeyIndexes[environment.ordinal()];
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public Environment getEnvironment() {
        return this.env;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setSecGuardImagePostfix(String str) {
        this.userConfigSecurityJpgPostfix = str;
    }

    public void setTtid(String str) {
        TraceHelper.clientTTID = str;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public String getSecurityImagePostfix() {
        String str = this.userConfigSecurityJpgPostfix;
        return str != null ? str : this.useSingleImage ? "" : DEFAULT_SECURITY_GUARD_IMAGE_SUFFIX[this.env.ordinal()];
    }

    public void setEnvironment(final Environment environment) {
        final Environment environment2 = this.env;
        this.env = environment;
        if (!this.initialized || environment2 == this.env) {
            return;
        }
        this.executorService.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ConfigManager.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    for (EnvironmentChangeListener environmentChangeListener : (EnvironmentChangeListener[]) Pluto.DEFAULT_INSTANCE.getBeans(EnvironmentChangeListener.class)) {
                        environmentChangeListener.onEnvironmentChange(environment2, environment);
                    }
                } catch (Exception e) {
                    AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to switch environment from " + environment2 + " to " + environment, e);
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public Version getSDKVersion() {
        return this.sdkVersion;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void registerPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (str == null || propertyChangeListener == null) {
            return;
        }
        Pluto.DEFAULT_INSTANCE.registerBean(PropertyChangeListener.class, propertyChangeListener, Collections.singletonMap(PROPERTY_NAME_FILTER_KEY, str));
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void registerPropertyChangeListener(String[] strArr, PropertyChangeListener propertyChangeListener) {
        for (String str : strArr) {
            Pluto.DEFAULT_INSTANCE.registerBean(PropertyChangeListener.class, propertyChangeListener, Collections.singletonMap(PROPERTY_NAME_FILTER_KEY, str));
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public long getLongProperty(String str, long j) {
        String stringProperty = getStringProperty(str, null);
        if (stringProperty != null) {
            try {
                return Long.parseLong(stringProperty);
            } catch (Exception unused) {
            }
        }
        return j;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public int getIntProperty(String str, int i) {
        String stringProperty = getStringProperty(str, null);
        if (stringProperty != null) {
            try {
                return Integer.parseInt(stringProperty);
            } catch (Exception unused) {
            }
        }
        return i;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public boolean getBooleanProperty(String str, boolean z) {
        String stringProperty = getStringProperty(str, null);
        return stringProperty != null ? Boolean.parseBoolean(stringProperty) : z;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setProperty(String str, String str2) {
        String strPut;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Map<String, String> map = this.properties;
        if (map == null) {
            strPut = this.userProperties.put(str, str2);
        } else {
            strPut = map.put(str, str2);
        }
        if (CommonUtils.isEqual(strPut, str2)) {
            return;
        }
        postPropertyChangeEvent(str, strPut, str2);
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public String getStringProperty(String str, String str2) {
        Map<String, String> map = this.properties;
        if (map == null) {
            map = this.userProperties;
        }
        String str3 = map.get(str);
        return str3 == null ? str2 : str3;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public String[] getStringArrayProperty(String str, String[] strArr) {
        String stringProperty = getStringProperty(str, null);
        return stringProperty != null ? stringProperty.split("[,]") : strArr;
    }

    public void setRequestCodeStartIndex(int i) {
        RequestCodeAllocator.setStartRequestCodeIndex(i);
    }

    private void overrideProperties(Map<String, String> map, boolean z) {
        ArrayList arrayList = z ? new ArrayList(3) : null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                String str = this.properties.get(entry.getKey());
                if (z && !CommonUtils.isEqual(str, entry.getValue())) {
                    addPropertyChangeEvent(arrayList, entry.getKey(), str, entry.getValue());
                }
                this.properties.put(entry.getKey(), entry.getValue());
            }
        }
        if (z) {
            postPropertyChangeEvents(arrayList);
        }
    }

    private void addPropertyChangeEvent(List<InternalPropertyChangeEvent> list, String str, String str2, String str3) {
        InternalPropertyChangeEvent internalPropertyChangeEvent = new InternalPropertyChangeEvent();
        internalPropertyChangeEvent.key = str;
        internalPropertyChangeEvent.newValue = str3;
        internalPropertyChangeEvent.oldValue = str2;
        list.add(internalPropertyChangeEvent);
    }

    private void postPropertyChangeEvents(final List<InternalPropertyChangeEvent> list) {
        ExecutorService executorService;
        if (list.size() == 0 || (executorService = this.executorService) == null) {
            return;
        }
        executorService.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ConfigManager.2
            @Override // java.lang.Runnable
            public void run() {
                PropertyChangeListener[] propertyChangeListenerArr;
                for (InternalPropertyChangeEvent internalPropertyChangeEvent : list) {
                    if (internalPropertyChangeEvent != null && internalPropertyChangeEvent.key != null && (propertyChangeListenerArr = (PropertyChangeListener[]) Pluto.DEFAULT_INSTANCE.getBeans(PropertyChangeListener.class, Collections.singletonMap(ConfigManager.PROPERTY_NAME_FILTER_KEY, internalPropertyChangeEvent.key))) != null && propertyChangeListenerArr.length != 0) {
                        for (PropertyChangeListener propertyChangeListener : propertyChangeListenerArr) {
                            try {
                                propertyChangeListener.propertyChanged(internalPropertyChangeEvent.key, internalPropertyChangeEvent.oldValue, internalPropertyChangeEvent.newValue);
                            } catch (Exception unused) {
                            }
                        }
                    }
                }
            }
        });
    }

    private void postPropertyChangeEvent(final String str, final String str2, final String str3) {
        ExecutorService executorService;
        final PropertyChangeListener[] propertyChangeListenerArr = (PropertyChangeListener[]) Pluto.DEFAULT_INSTANCE.getBeans(PropertyChangeListener.class, Collections.singletonMap(PROPERTY_NAME_FILTER_KEY, str));
        if (propertyChangeListenerArr == null || propertyChangeListenerArr.length == 0 || (executorService = this.executorService) == null) {
            return;
        }
        executorService.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ConfigManager.3
            @Override // java.lang.Runnable
            public void run() {
                for (PropertyChangeListener propertyChangeListener : propertyChangeListenerArr) {
                    try {
                        propertyChangeListener.propertyChanged(str, str2, str3);
                    } catch (Exception unused) {
                    }
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setDynamicProperties(String str) {
        try {
            this.sp.edit().putString("config", str).commit();
        } catch (Throwable unused) {
        }
        overrideProperties(getDynamicConfigs(), true);
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public int getLogLevel() {
        return TraceLoggerManager.INSTANCE.getLogLevel();
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setLogLevel(int i) {
        TraceLoggerManager.INSTANCE.setLogLevel(i);
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setDataProvider(OpenAccountProvider openAccountProvider) {
        this.openAccountProvider = openAccountProvider;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public OpenAccountProvider getDataProvider() {
        return this.openAccountProvider;
    }

    private Map<String, String> getDynamicConfigs() {
        HashMap map = new HashMap();
        try {
            this.sp = this.context.getSharedPreferences(OpenAccountConstants.DYNAMIC_CONFIG_SP, 0);
            String string = this.sp.getString("config", null);
            if (string != null) {
                JSONObject jSONObject = new JSONObject(string);
                Iterator<String> itKeys = jSONObject.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    map.put(next, jSONObject.optString(next));
                }
            }
        } catch (Throwable th) {
            AliSDKLogger.e(TAG, "fail to get dynamic configs", th);
        }
        return map;
    }

    private static class InternalPropertyChangeEvent {
        public String key;
        public String newValue;
        public String oldValue;

        private InternalPropertyChangeEvent() {
        }
    }

    public void putExtBizParam(String str, String str2) {
        Map<String, String> map;
        if (TextUtils.isEmpty(str) || str.length() > 50 || TextUtils.isEmpty(str2) || str2.length() > 200 || (map = this.extBizParam) == null || map.size() >= 50) {
            return;
        }
        this.extBizParam.put(str, str2);
    }

    public Map<String, String> getExtBizMap() {
        return this.extBizParam;
    }

    public Class getMobileFragment() {
        return this.mobileFragment;
    }

    public void setMobileFragment(Class cls) {
        this.mobileFragment = cls;
    }

    public Class getMobileRegisterFragment() {
        return this.mobileRegisterFragment;
    }

    public void setMobileRegisterFragment(Class cls) {
        this.mobileRegisterFragment = cls;
    }

    public Class getCheckCodeFragment() {
        return this.checkCodeFragment;
    }

    public void setCheckCodeFragment(Class cls) {
        this.checkCodeFragment = cls;
    }

    public Class getConfirmFragment() {
        return this.confirmFragment;
    }

    public void setConfirmFragment(Class cls) {
        this.confirmFragment = cls;
    }

    public Class getResetPwdFragment() {
        return this.resetPwdFragment;
    }

    public void setResetPwdFragment(Class cls) {
        this.resetPwdFragment = cls;
    }

    public Class getPwdLoginFragment() {
        return this.pwdLoginFragment;
    }

    public void setPwdLoginFragment(Class cls) {
        this.pwdLoginFragment = cls;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public boolean openTaobaoUILogin() {
        return this.openTaobaoUILogin;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setOpenTaobaoUILogin(boolean z) {
        this.openTaobaoUILogin = z;
    }

    public void setLanguageCode(@LanguageCode.Language String str) {
        OpenAccountConfigs.clientLocal = str;
    }

    public boolean isRegisterLoginBroadcast() {
        return this.registerLoginBroadcast;
    }

    public void setRegisterLoginBroadcast(boolean z) {
        this.registerLoginBroadcast = z;
    }

    public String getTwitterSecret() {
        return this.mTwitterSecret;
    }

    public String getTwitterId() {
        return this.mTwitterId;
    }

    public void setTwitterConfig(String str, String str2) {
        this.mTwitterId = str;
        this.mTwitterSecret = str2;
    }

    public String getGoogleClientId() {
        return this.mGoogleClientId;
    }

    public void setGoogleClientId(String str) {
        this.mGoogleClientId = str;
    }

    public void setAlipayAuthConfig(String str, String str2, String str3) {
        this.mAlipayAppId = str;
        this.mAlipayPid = str2;
        this.mAlipaySignType = str3;
    }

    public String getFacebookId() {
        return this.mFacebookId;
    }

    public void setFacebookId(String str) {
        this.mFacebookId = str;
    }

    public String getAlipayPid() {
        return this.mAlipayPid;
    }

    public String getAlipayAppId() {
        return this.mAlipayAppId;
    }

    public String getAlipaySignType() {
        return this.mAlipaySignType;
    }

    public boolean isDailyNocaptcha() {
        return this.dailyNocaptcha;
    }

    public void setDailyNocaptcha(boolean z) {
        this.dailyNocaptcha = z;
    }

    public String getApiGatewayHost() {
        return this.mApiGatewayHost;
    }

    public void setApiGatewayHost(String str) {
        this.mApiGatewayHost = str;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public OnActivityResultCallback getOnActivityResultCallback() {
        return this.mOnActivityResultCallback;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.ConfigService
    public void setOnActivityResultCallback(OnActivityResultCallback onActivityResultCallback) {
        this.mOnActivityResultCallback = onActivityResultCallback;
    }

    public String getBundleName() {
        return this.bundleName;
    }

    public void setBundleName(String str) {
        this.bundleName = str;
    }

    public boolean isOpenMtop() {
        return this.isOpenMtop;
    }

    public void setOpenMtop(boolean z) {
        this.isOpenMtop = z;
    }

    public boolean isAPIGateway() {
        return this.isAPIGateway;
    }

    public void setAppKey(String str) {
        this.mAppKey = str;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public String getAppSecret() {
        return this.mAppSecret;
    }

    public void setAppSecret(String str) {
        this.mAppSecret = str;
    }

    public void setAPIGateway(boolean z) {
        this.isAPIGateway = z;
    }

    public boolean isLogoutLoginSDKSwitch() {
        return this.logoutLoginSDKSwitch;
    }

    public void setLogoutLoginSDKSwitch(boolean z) {
        this.logoutLoginSDKSwitch = z;
    }

    public boolean isSupportOfflineLogin() {
        return this.supportOfflineLogin;
    }

    public void setSupportOfflineLogin(boolean z) {
        this.supportOfflineLogin = z;
    }
}
