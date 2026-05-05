package com.alibaba.sdk.android.openaccount.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.model.OAWUAData;
import com.alibaba.sdk.android.openaccount.network.ConnectType;
import com.alibaba.sdk.android.openaccount.network.MobileNetworkType;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.pluto.Pluto;
import com.heytap.mcssdk.constant.IntentConstant;
import com.taobao.accs.common.Constants;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountRiskControlContext {
    private static int appVersionCode = -1;
    private static String appVersionName;
    private static volatile Map<String, Object> cachedEnvironmentInfo;
    private static volatile long lastEnvironmentTimeMill;
    private Context context;

    public static void init(Context context) {
    }

    private static int getAppVersionCode() {
        if (appVersionCode < 0) {
            try {
                appVersionCode = OpenAccountSDK.getAndroidContext().getPackageManager().getPackageInfo(OpenAccountSDK.getAndroidContext().getPackageName(), 0).versionCode;
            } catch (Exception e) {
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to getAppVersionCode", e);
            }
        }
        return appVersionCode;
    }

    public static Map<String, Object> buildRiskContext() {
        HashMap map = new HashMap();
        map.putAll(getEnvironmentInfo());
        buildJaqRiskContext(map);
        map.putAll(ConfigManager.getInstance().getExtBizMap());
        map.put(DispatchConstants.SIGNTYPE, TextUtils.isEmpty(ConfigManager.getInstance().getAlipaySignType()) ? "RSA" : ConfigManager.getInstance().getAlipaySignType());
        return map;
    }

    private static Map<String, Object> getEnvironmentInfo() {
        if (cachedEnvironmentInfo != null && System.currentTimeMillis() - lastEnvironmentTimeMill < 10000) {
            return cachedEnvironmentInfo;
        }
        HashMap map = new HashMap();
        Context androidContext = OpenAccountSDK.getAndroidContext();
        map.put("platformName", DispatchConstants.ANDROID);
        map.put(DispatchConstants.PLATFORM_VERSION, Integer.toString(Build.VERSION.SDK_INT));
        map.put("appVersion", Integer.toString(getAppVersionCode()));
        map.put("sdkVersion", OpenAccountSDK.getVersion().toString());
        map.put("utdid", ((UserTrackerService) Pluto.DEFAULT_INSTANCE.getBean(UserTrackerService.class)).getDefaultUserTrackerId());
        map.put("umidToken", ((SecurityGuardService) Pluto.DEFAULT_INSTANCE.getBean(SecurityGuardService.class)).getSecurityToken());
        map.put("deviceId", DeviceManager.INSTANCE.getSdkDeviceId());
        map.put(Constants.KEY_IMEI, DeviceManager.INSTANCE.getImei());
        map.put("brand", Build.BRAND);
        map.put("model", Build.MODEL);
        map.put("appAuthToken", ((SecurityGuardService) Pluto.DEFAULT_INSTANCE.getBean(SecurityGuardService.class)).getSecurityToken());
        map.put("yunOSId", DeviceManager.INSTANCE.getYunOSDeviceId());
        map.put("locale", OpenAccountConfigs.clientLocal != null ? OpenAccountConfigs.clientLocal : getLocale(OpenAccountSDK.getAndroidContext()));
        ConnectType connectType = NetworkUtils.getConnectType(androidContext);
        String str = "unknown";
        if (connectType == ConnectType.CONNECT_TYPE_WIFI) {
            str = "wifi";
        } else if (connectType == ConnectType.CONNECT_TYPE_MOBILE) {
            MobileNetworkType mobileNetworkType = NetworkUtils.getMobileNetworkType(androidContext);
            if (mobileNetworkType == MobileNetworkType.MOBILE_NETWORK_TYPE_2G) {
                str = "2g";
            } else if (mobileNetworkType == MobileNetworkType.MOBILE_NETWORK_TYPE_3G) {
                str = "3g";
            } else if (mobileNetworkType == MobileNetworkType.MOBILE_NETWORK_TYPE_4G) {
                str = "4g";
            }
        }
        map.put("netType", str);
        map.put("routerMac", getBSSID());
        map.put("USE_OA_PWD_ENCRYPT", "true");
        try {
            TelephonyManager telephonyManager = (TelephonyManager) androidContext.getSystemService("phone");
            if (telephonyManager != null && !"true".equals(OpenAccountSDK.getProperty("disableQueryCellLocation"))) {
                CellLocation cellLocation = telephonyManager.getCellLocation();
                if (cellLocation instanceof GsmCellLocation) {
                    map.put("cellID", String.valueOf(((GsmCellLocation) cellLocation).getCid()));
                } else if (cellLocation instanceof CdmaCellLocation) {
                    map.put("cellID", String.valueOf(((CdmaCellLocation) cellLocation).getBaseStationId()));
                }
            }
        } catch (Exception unused) {
        }
        map.put(IntentConstant.APP_ID, androidContext.getPackageName());
        if (appVersionName == null) {
            appVersionName = OpenAccountSDK.getProperty("appVersion");
            if (appVersionName == null) {
                try {
                    appVersionName = androidContext.getPackageManager().getPackageInfo(androidContext.getPackageName(), 0).versionName;
                } catch (Exception unused2) {
                }
            }
        }
        map.put(Constants.KEY_APP_VERSION_NAME, appVersionName);
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            map.put("latitude", String.valueOf(lastKnownLocation.getLatitude()));
            map.put("longitude", String.valueOf(lastKnownLocation.getLongitude()));
            map.put("altitude", String.valueOf(lastKnownLocation.getAltitude()));
        }
        cachedEnvironmentInfo = map;
        lastEnvironmentTimeMill = System.currentTimeMillis();
        return map;
    }

    public static String getLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= 24) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getLanguage() + OpenAccountUIConstants.UNDER_LINE + locale.getCountry();
    }

    private static void buildJaqRiskContext(Map<String, Object> map) {
        if ("true".equals(OpenAccountSDK.getProperty("disableJaqVerification"))) {
            return;
        }
        OAWUAData wua = ((SecurityGuardService) Pluto.DEFAULT_INSTANCE.getBean(SecurityGuardService.class)).getWUA();
        if (wua != null && !TextUtils.isEmpty(wua.wua)) {
            map.put("jaqVerificationToken", wua.wua);
            map.put("jaqVerificationEnabled", "true");
        }
        map.put("USE_H5_NC", "true");
        if (ConfigManager.getInstance().isDailyNocaptcha()) {
            map.put("JAQ_CODE", "abc");
        }
    }

    public static Location getLastKnownLocation() {
        if (OpenAccountSDK.getAndroidContext() == null || ConfigManager.getInstance().getBooleanProperty("disableLocationService", false)) {
            return null;
        }
        try {
            LocationManager locationManager = (LocationManager) OpenAccountSDK.getAndroidContext().getSystemService("location");
            if (locationManager == null) {
                return null;
            }
            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(1);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            if (bestProvider == null) {
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "Unable to find the best provider, requestSingleLocationUpdate failed");
                return null;
            }
            return locationManager.getLastKnownLocation(bestProvider);
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "Unable to find the best provider, ex = " + e.getMessage());
            return null;
        }
    }

    public static String getBSSID() {
        WifiInfo connectionInfo;
        WifiManager wifiManager = (WifiManager) OpenAccountSDK.getAndroidContext().getApplicationContext().getSystemService("wifi");
        return (!wifiManager.isWifiEnabled() || (connectionInfo = wifiManager.getConnectionInfo()) == null) ? "" : connectionInfo.getBSSID();
    }
}
