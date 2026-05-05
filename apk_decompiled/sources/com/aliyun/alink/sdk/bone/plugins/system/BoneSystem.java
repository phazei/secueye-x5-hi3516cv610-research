package com.aliyun.alink.sdk.bone.plugins.system;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.sdk.bone.plugins.app.ConvertUtils;
import com.aliyun.alink.sdk.bone.plugins.config.BoneConfig;
import com.aliyun.alink.sdk.bone.plugins.ut.KeyBoardListenerHelper;
import com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.bridge.invoker.SyncBoneInvoker;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneService;
import com.taobao.accs.utl.UtilityImpl;
import com.xiaomi.mipush.sdk.Constants;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONObject;
import sdk.EnvConfigure;

/* JADX INFO: loaded from: classes2.dex */
@BoneService(name = BoneSystem.API_NAME)
public class BoneSystem extends BaseBoneService {
    public static final String API_NAME = "BoneSystem";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f4469a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private BroadcastReceiver f4470b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private BroadcastReceiver f4471c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private KeyBoardListenerHelper f4472d;

    @Override // com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService, com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
    public void onInitialize(Context context) {
        super.onInitialize(context);
        this.f4469a = context;
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService, com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
    public void onDestroy() {
        BroadcastReceiver broadcastReceiver;
        super.onDestroy();
        Context context = this.f4469a;
        if (context != null && (broadcastReceiver = this.f4470b) != null) {
            context.unregisterReceiver(broadcastReceiver);
            this.f4470b = null;
        }
        BroadcastReceiver broadcastReceiver2 = this.f4471c;
        if (broadcastReceiver2 != null) {
            this.f4469a.unregisterReceiver(broadcastReceiver2);
            this.f4471c = null;
        }
        this.f4469a = null;
    }

    @BoneMethod
    public void getSystemInfo(BoneCallback boneCallback) {
        Locale locale = Locale.getDefault();
        String str = locale.getLanguage() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + locale.getCountry();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("phone", Build.BOARD + " " + Build.MODEL);
            jSONObject.put("platform", "Android");
            jSONObject.put("systemVersion", String.valueOf(Build.VERSION.SDK_INT));
            jSONObject.put(EnvConfigure.KEY_LANGUAGE, str);
            jSONObject.put("serverEnv", BoneConfig.get("serverEnv"));
            jSONObject.put("pluginEnv", BoneConfig.get("pluginEnv"));
            boneCallback.success(jSONObject);
        } catch (Exception e) {
            boneCallback.failed("608", e.getMessage(), "");
        }
    }

    @BoneMethod
    public void getContainerInfo(BoneCallback boneCallback) {
        Application application = (Application) this.f4469a.getApplicationContext();
        String packageName = application.getPackageName();
        PackageManager packageManager = application.getPackageManager();
        try {
            String string = packageManager.getApplicationLabel(this.f4469a.getApplicationInfo()).toString();
            String str = packageManager.getPackageInfo(packageName, 0).versionName;
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(DispatchConstants.APP_NAME, string);
            jSONObject.put("appVersion", str);
            jSONObject.put("runtime", "bone-aep-rn");
            jSONObject.put("apiLevel", 6);
            boneCallback.success(jSONObject);
        } catch (Exception e) {
            boneCallback.failed("608", e.getMessage(), "");
        }
    }

    @BoneMethod
    public void getNetworkType(BoneCallback boneCallback) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", d(this.f4469a));
            boneCallback.success(jSONObject);
        } catch (Exception e) {
            ALog.e("BoneSystemPlugin", "exception happen");
            boneCallback.failed("608", e.getMessage(), "");
        }
    }

    @BoneMethod
    public void getTimeZone(JSONObject jSONObject, BoneCallback boneCallback) {
        try {
            JSONObject jSONObject2 = new JSONObject();
            TimeZone timeZone = TimeZone.getDefault();
            jSONObject2.putOpt("timeZoneId", timeZone.getID());
            jSONObject2.putOpt("displayName", timeZone.getDisplayName());
            jSONObject2.putOpt("rawOffset", Integer.valueOf(timeZone.getRawOffset()));
            boneCallback.success(jSONObject2);
        } catch (Exception e) {
            ALog.e("BoneSystemPlugin", "exception happen");
            boneCallback.failed("608", e.getMessage(), "");
        }
    }

    @BoneMethod
    public void isMobileDataEnable(BoneCallback boneCallback) {
        Boolean bool;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f4469a.getSystemService("connectivity");
        try {
            Method declaredMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            bool = (Boolean) declaredMethod.invoke(connectivityManager, new Object[0]);
        } catch (Exception e) {
            boneCallback.failed("608", e.getMessage(), "");
            e.printStackTrace();
            bool = null;
        }
        if (bool == null) {
            boneCallback.failed(SyncBoneInvoker.ERROR_SUB_CODE_EXCEPTION, "Runtime Exception", "");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("enable", bool.booleanValue());
            boneCallback.success(jSONObject);
        } catch (Exception e2) {
            ALog.e("BoneSystemPlugin", "exception happen");
            boneCallback.failed("608", e2.getMessage(), "");
        }
    }

    @BoneMethod
    public void isBluetoothEnabled(BoneCallback boneCallback) {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            JSONObject jSONObject = new JSONObject();
            if (defaultAdapter == null) {
                ALog.e("BoneSystemPlugin", "Device does not support Bluetooth");
                jSONObject.put("enable", false);
            } else {
                jSONObject.put("enable", defaultAdapter.isEnabled());
            }
            boneCallback.success(jSONObject);
        } catch (Exception e) {
            ALog.e("BoneSystemPlugin", "exception happen");
            boneCallback.failed("608", e.getMessage(), "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String c(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        if (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable() || !activeNetworkInfo.isConnected() || activeNetworkInfo.getTypeName() == null || !"wifi".equalsIgnoreCase(activeNetworkInfo.getTypeName())) {
            return "";
        }
        String ssid = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getSSID();
        return (TextUtils.isEmpty(ssid) || TextUtils.equals("<unknown ssid>", ssid)) ? "" : ssid.replace("\"", "");
    }

    @BoneMethod
    public void stopListenNetworkStatusChange(BoneCallback boneCallback) {
        BroadcastReceiver broadcastReceiver = this.f4470b;
        if (broadcastReceiver != null) {
            this.f4469a.unregisterReceiver(broadcastReceiver);
            this.f4470b = null;
        }
        boneCallback.success();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String d(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        if (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable() || !activeNetworkInfo.isConnected() || activeNetworkInfo.getTypeName() == null) {
            return "none";
        }
        String typeName = activeNetworkInfo.getTypeName();
        if (UtilityImpl.NET_TYPE_MOBILE.equalsIgnoreCase(typeName)) {
            int subtype = activeNetworkInfo.getSubtype();
            String subtypeName = activeNetworkInfo.getSubtypeName();
            switch (subtype) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    typeName = "2g";
                    break;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    typeName = "3g";
                    break;
                case 13:
                    typeName = "4g";
                    break;
                default:
                    typeName = (!"TD-SCDMA".equalsIgnoreCase(subtypeName) && !"WCDMA".equalsIgnoreCase(subtypeName) && !"CDMA2000".equalsIgnoreCase(subtypeName)) ? UtilityImpl.NET_TYPE_MOBILE : "3g";
                    break;
            }
        }
        return typeName.toLowerCase();
    }

    @BoneMethod
    public void startListenNetworkStatusChange(final JSContext jSContext, BoneCallback boneCallback) {
        if (jSContext == null) {
            boneCallback.failed("608", "FAILED_NO_INITIALIZED", "");
            return;
        }
        if (this.f4470b == null) {
            this.f4470b = new BroadcastReceiver() { // from class: com.aliyun.alink.sdk.bone.plugins.system.BoneSystem.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (jSContext == null) {
                        return;
                    }
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("type", BoneSystem.d(context));
                        jSONObject.put("ssid", BoneSystem.c(context));
                        jSContext.emitter("BoneNetworkStatusChanged", jSONObject);
                    } catch (Exception unused) {
                        ALog.e("BoneSystemPlugin", "exception happen");
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.f4469a.registerReceiver(this.f4470b, intentFilter);
        }
        boneCallback.success();
    }

    @BoneMethod
    public void startListenBluetoothStatusChange(final JSContext jSContext, BoneCallback boneCallback) {
        if (jSContext == null) {
            boneCallback.failed("608", "FAILED_NO_INITIALIZED", "");
            return;
        }
        if (this.f4471c == null) {
            this.f4471c = new BroadcastReceiver() { // from class: com.aliyun.alink.sdk.bone.plugins.system.BoneSystem.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (jSContext == null) {
                        return;
                    }
                    BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                    String strB = (defaultAdapter == null || !defaultAdapter.isEnabled()) ? "OFF" : "ON";
                    if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                        strB = BoneSystem.b(intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0));
                    }
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("state", strB);
                        jSContext.emitter("BoneBluetoothStatusChanged", jSONObject);
                    } catch (Exception unused) {
                        ALog.e("BoneSystemPlugin", "exception happen");
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            this.f4469a.registerReceiver(this.f4471c, intentFilter);
        }
        boneCallback.success();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(int i) {
        switch (i) {
            case 10:
                return "OFF";
            case 11:
                return "TURNING_ON";
            case 12:
                return "ON";
            case 13:
                return "TURNING_OFF";
            default:
                return "unknown (" + i + ")";
        }
    }

    @BoneMethod
    public void stopListenBluetoothStatusChange(BoneCallback boneCallback) {
        Log.d("BoneSystemPlugin", "stopListenBluetoothStatusChange() called with: callback = [" + boneCallback + "]");
        BroadcastReceiver broadcastReceiver = this.f4471c;
        if (broadcastReceiver != null) {
            this.f4469a.unregisterReceiver(broadcastReceiver);
            this.f4471c = null;
        }
        boneCallback.success();
    }

    @BoneMethod
    public void startListenKeyBoardChange(final JSContext jSContext, BoneCallback boneCallback) {
        Log.d("BoneSystemPlugin", "startListenKeyBoardChange() called with: jsContext = [" + jSContext + "], callback = [" + boneCallback + "]");
        try {
            jSContext.getCurrentActivity().runOnUiThread(new Runnable() { // from class: com.aliyun.alink.sdk.bone.plugins.system.BoneSystem.3
                @Override // java.lang.Runnable
                public void run() {
                    BoneSystem.this.f4472d = new KeyBoardListenerHelper(jSContext.getCurrentActivity());
                    BoneSystem.this.f4472d.setOnKeyBoardChangeListener(new KeyBoardListenerHelper.OnKeyBoardChangeListener() { // from class: com.aliyun.alink.sdk.bone.plugins.system.BoneSystem.3.1
                        @Override // com.aliyun.alink.sdk.bone.plugins.ut.KeyBoardListenerHelper.OnKeyBoardChangeListener
                        public void OnKeyBoardChange(boolean z, int i) {
                            Log.d("BoneSystemPlugin", "OnKeyBoardChange() called with: isShow = [" + z + "], keyBoardHeight = [" + i + "]");
                            try {
                                JSONObject jSONObject = new JSONObject();
                                jSONObject.put("isShow", z);
                                jSONObject.put("keyBoardHeight", i);
                                jSContext.emitter("onKeyBoardChange", jSONObject);
                            } catch (Exception unused) {
                            }
                        }
                    });
                }
            });
        } catch (Exception unused) {
        }
        boneCallback.success();
    }

    @BoneMethod
    public void stopListenKeyBoardChange(BoneCallback boneCallback) {
        try {
            this.f4472d.destroy();
            this.f4472d = null;
        } catch (Exception unused) {
        }
        boneCallback.success();
    }

    @BoneMethod
    public void sendBroadcast(JSContext jSContext, String str, JSONObject jSONObject, BoneCallback boneCallback) {
        Activity currentActivity;
        if (jSContext == null || (currentActivity = jSContext.getCurrentActivity()) == null) {
            boneCallback.failed("608", "FAILED_NO_INITIALIZED", "");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            boneCallback.failed("400", "name is required", "");
            return;
        }
        boneCallback.success();
        Intent intent = new Intent(str);
        try {
            intent.putExtras(ConvertUtils.toBundle(jSONObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(currentActivity).sendBroadcast(intent);
    }

    @BoneMethod
    public void setStatusBarStyle(JSContext jSContext, int i, BoneCallback boneCallback) {
        if (jSContext.getCurrentActivity() == null) {
            boneCallback.failed("608", "currentActivity == null", "");
        } else {
            boneCallback.success();
        }
    }
}
