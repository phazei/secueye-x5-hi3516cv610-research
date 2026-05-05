package com.aliyun.iot.aep.sdk.framework.sdk;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.framework.config.AConfigure;
import com.aliyun.iot.aep.sdk.framework.config.SDKConfig;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.vivo.push.PushClientConstants;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class SDKManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final ArrayList<SDKConfigure> f4730a = new ArrayList<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static String f4731b = "";

    public static class Result {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        boolean f4733a = false;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f4734b = Integer.MAX_VALUE;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        String f4735c = null;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        String f4736d = null;
        String e = null;
    }

    public static ArrayList<SDKConfigure> getSDKConfigures() {
        return (ArrayList) f4730a.clone();
    }

    public static void init(Application application) {
        AConfigure.getInstance().init(application);
        prepareForInitSdk(application);
        init_underUiThread(application);
    }

    public static void prepareForInitSdk(Application application) {
        b(application);
    }

    public static void init_underUiThread(Application application) {
        a(application);
    }

    private static void a(Application application) {
        if (f4730a.isEmpty()) {
            ALog.d("SDKManager", "init sdks: configures is null or empty");
            return;
        }
        f4731b = application.getPackageName();
        String strA = a(application, Process.myPid());
        for (SDKConfigure sDKConfigure : f4730a) {
            if (a(strA, sDKConfigure)) {
                _initSdkDelegates(application, sDKConfigure, _prepareSdkDelegates(sDKConfigure));
            }
        }
        for (SDKConfigure sDKConfigure2 : f4730a) {
            if (a(strA, sDKConfigure2) && sDKConfigure2.submodules != null && !sDKConfigure2.submodules.isEmpty()) {
                for (int i = 0; i < sDKConfigure2.submodules.size(); i++) {
                    SDKConfigure sDKConfigure3 = sDKConfigure2.submodules.get(i);
                    _initSdkDelegates(application, sDKConfigure3, _prepareSdkDelegates(sDKConfigure3));
                }
            }
        }
    }

    private static void b(Application application) {
        if (application == null) {
            return;
        }
        try {
            JSONArray jSONArray = new JSONArray(SDKConfig.sdkConfig);
            if (jSONArray == null) {
                return;
            }
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                ALog.d("SDKManager", jSONObject.toString());
                if (jSONObject != null) {
                    f4730a.add(_parseToSDKConfigure(jSONObject));
                }
            }
        } catch (Exception e) {
            ALog.e("SDKManager", "prepare-configure", e);
            e.printStackTrace();
        }
    }

    @VisibleForTesting(otherwise = 3)
    public static SDKConfigure _parseToSDKConfigure(@NonNull JSONObject jSONObject) {
        ArrayList arrayList;
        String strOptString = jSONObject.optString("name");
        String strOptString2 = jSONObject.optString("version");
        String strOptString3 = jSONObject.optString(TmpConstant.SERVICE_DESC);
        String strOptString4 = jSONObject.optString("doc");
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("classFiles");
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("opt");
        String strOptString5 = jSONObject.optString("process");
        boolean zOptBoolean = jSONObject.optBoolean("needIoTToken");
        JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("submodules");
        if (jSONArrayOptJSONArray2 == null || jSONArrayOptJSONArray2.length() <= 0) {
            arrayList = null;
        } else {
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < jSONArrayOptJSONArray2.length(); i++) {
                try {
                    arrayList2.add(_parseToSDKConfigure(jSONArrayOptJSONArray2.getJSONObject(i)));
                } catch (JSONException e) {
                    ALog.e("SDKManager", "parse SDK configure", e);
                    e.printStackTrace();
                }
            }
            arrayList = arrayList2;
        }
        return new SDKConfigure(strOptString, strOptString2, strOptString3, strOptString4, jSONArrayOptJSONArray, zOptBoolean, jSONObjectOptJSONObject, strOptString5, arrayList);
    }

    @VisibleForTesting
    public static String _prepareSdkDelegateClassName(JSONObject jSONObject) {
        return jSONObject.getString(PushClientConstants.TAG_CLASS_NAME);
    }

    @VisibleForTesting
    public static List<ISDKDelegate> _prepareSdkDelegates(SDKConfigure sDKConfigure) {
        if (sDKConfigure == null) {
            return null;
        }
        if (sDKConfigure.classFiles == null || sDKConfigure.classFiles.length() == 0) {
            ALog.w("SDKManager", "SKIP to prepare " + sDKConfigure.name + ": no class files");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < sDKConfigure.classFiles.length(); i++) {
            try {
                String str_prepareSdkDelegateClassName = _prepareSdkDelegateClassName(sDKConfigure.classFiles.getJSONObject(i));
                if (str_prepareSdkDelegateClassName != null) {
                    try {
                        Class<?> cls = Class.forName(str_prepareSdkDelegateClassName);
                        if (cls != null && ISDKDelegate.class.isAssignableFrom(cls)) {
                            arrayList.add((ISDKDelegate) cls.newInstance());
                        }
                    } catch (Exception e) {
                        ALog.e("SDKManager", "Failed to prepare " + sDKConfigure.name);
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e2) {
                ALog.e("SDKManager", "Failed to prepare " + sDKConfigure.name);
                e2.printStackTrace();
            }
        }
        return arrayList;
    }

    @VisibleForTesting
    public static void _initSdkDelegates(Application application, SDKConfigure sDKConfigure, List<ISDKDelegate> list) {
        if (list == null || list.isEmpty()) {
            ALog.w("SDKManager", "SKIP to init " + sDKConfigure.name + ": no delegates");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ISDKDelegate iSDKDelegate = list.get(i);
            try {
                String string = sDKConfigure.classFiles.getJSONObject(i).getString(PushClientConstants.TAG_CLASS_NAME);
                if (InitResultHolder.isInitialized(string)) {
                    ALog.w("SDKManager", string + " shouldn't be initialized twice");
                } else {
                    int iInit = iSDKDelegate.init(application, sDKConfigure, AConfigure.getInstance().getConfig());
                    Result result = new Result();
                    result.f4733a = true;
                    result.f4734b = iInit;
                    result.f4735c = sDKConfigure.name;
                    result.f4736d = sDKConfigure.version;
                    result.e = sDKConfigure.process;
                    InitResultHolder.updateResult(string, result);
                    ALog.d("SDKManager", "Successfully init: " + string);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ALog.e("SDKManager", "FAILED to init " + sDKConfigure.name + "at delegates[" + i + "]");
            }
        }
    }

    private static String a(Context context, int i) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
        if (activityManager != null && (runningAppProcesses = activityManager.getRunningAppProcesses()) != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == i) {
                    return runningAppProcessInfo.processName;
                }
            }
        }
        return null;
    }

    private static boolean a(String str, SDKConfigure sDKConfigure) {
        if (str == null || str.isEmpty() || WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(sDKConfigure.process)) {
            return true;
        }
        if (TextUtils.isEmpty(sDKConfigure.process)) {
            return str.equals(f4731b);
        }
        for (String str2 : sDKConfigure.process.split(",")) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static class InitResultHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final HashMap<String, Result> f4732a = new HashMap<>();

        public static Map<String, Result> getDelegates() {
            return (Map) f4732a.clone();
        }

        public static boolean isInitialized(String str) {
            Result result = a(str) ? f4732a.get(str) : null;
            return result != null && result.f4733a;
        }

        public static int getInitResultCode(String str) {
            Result result = f4732a.containsKey(str) ? f4732a.get(str) : null;
            if (result != null) {
                return result.f4734b;
            }
            return Integer.MAX_VALUE;
        }

        public static boolean updateResult(String str, Result result) {
            if (str == null || str.length() <= 0 || result == null) {
                return false;
            }
            f4732a.put(str, result);
            return true;
        }

        private static boolean a(String str) {
            return f4732a.containsKey(str);
        }

        public static void dump() {
            ALog.d("SDKManager", "\ndump: --- S ---");
            for (String str : f4732a.keySet()) {
                ALog.d("SDKManager", "[" + str + "]: " + f4732a.get(str).f4733a + ", " + f4732a.get(str).f4734b);
            }
            ALog.d("SDKManager", "dump: --- E --- \n");
        }
    }

    public static boolean isPushAvailable() {
        return a("com.aliyun.iot.push.PushManager");
    }

    public static boolean isTMPAvailable() {
        return a("com.aliyun.alink.linksdk.tmp.TmpSdk");
    }

    public static boolean isOAAvailable() {
        return a("com.aliyun.iot.aep.sdk.login.LoginBusiness");
    }

    public static boolean isRNAvailable() {
        return a("com.aliyun.alink.alirn.RNContainer");
    }

    public static boolean isRNLibAvailable() {
        return a("com.aliyun.alink.page.rn.RNActivity");
    }

    public static boolean isRouterExternalLibAvailable() {
        return a("com.aliyun.iot.aep.routerexternal.RouterBoneService");
    }

    public static boolean isDeviceCenterAvailable() {
        return a("com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr");
    }

    public static boolean isGatewayConnectAvailable() {
        return a("com.aliyun.alink.linksdk.channel.gateway.api.GatewayConnectConfig");
    }

    public static boolean isJsBridgeAvailable() {
        return a("com.aliyun.alink.sdk.jsbridge.methodexport.BaseBonePlugin");
    }

    private static boolean a(String str) {
        try {
            return Class.forName(str) != null;
        } catch (Throwable unused) {
            return false;
        }
    }
}
