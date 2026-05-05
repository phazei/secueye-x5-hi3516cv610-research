package com.taobao.accs.utl;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class UtilityImpl {
    public static final String NET_TYPE_MOBILE = "mobile";
    public static final String NET_TYPE_UNKNOWN = "unknown";
    public static final String NET_TYPE_WIFI = "wifi";
    public static final int TNET_FILE_NUM = 5;
    public static final int TNET_FILE_SIZE = 5242880;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final byte[] f6450a = new byte[0];

    public static String a() {
        return TmpConstant.GROUP_ROLE_UNKNOWN;
    }

    public static String a(Context context) {
        String string = context.getSharedPreferences(Constants.SP_FILE_NAME, 4).getString(Constants.KEY_PROXY_HOST, null);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        String strC = c();
        if (TextUtils.isEmpty(strC)) {
            return null;
        }
        return strC;
    }

    public static int b(Context context) {
        int i = context.getSharedPreferences(Constants.SP_FILE_NAME, 4).getInt(Constants.KEY_PROXY_PORT, -1);
        if (i > 0) {
            return i;
        }
        if (a(context) == null) {
            return -1;
        }
        try {
            return d();
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public static boolean c(Context context) {
        String str;
        int i;
        synchronized (f6450a) {
            PackageInfo packageInfo = GlobalClientInfo.getInstance(context).getPackageInfo();
            int i2 = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getInt(Constants.KEY_APP_VERSION_CODE, -1);
            String string = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getString(Constants.KEY_APP_VERSION_NAME, "");
            if (packageInfo != null) {
                i = packageInfo.versionCode;
                str = packageInfo.versionName;
            } else {
                str = null;
                i = 0;
            }
            if (i2 == i && string.equals(str)) {
                return false;
            }
            n(context);
            ALog.i("UtilityImpl", "appVersionChanged", "oldV", Integer.valueOf(i2), "nowV", Integer.valueOf(i), "oldN", string, "nowN", str);
            return true;
        }
    }

    private static void n(Context context) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
            editorEdit.putInt(Constants.KEY_APP_VERSION_CODE, GlobalClientInfo.getInstance(context).getPackageInfo().versionCode);
            editorEdit.putString(Constants.KEY_APP_VERSION_NAME, GlobalClientInfo.getInstance(context).getPackageInfo().versionName);
            editorEdit.apply();
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "saveAppVersion", th, new Object[0]);
        }
    }

    public static boolean d(Context context) {
        String agooCustomServiceName = AdapterGlobalClientInfo.getAgooCustomServiceName(context);
        if (TextUtils.isEmpty(agooCustomServiceName)) {
            return false;
        }
        ComponentName componentName = new ComponentName(context, agooCustomServiceName);
        PackageManager packageManager = context.getPackageManager();
        if (!componentName.getPackageName().equals("!")) {
            return packageManager.getServiceInfo(componentName, 128).enabled;
        }
        ALog.e("UtilityImpl", "getAgooServiceEnabled,exception,comptName.getPackageName()=" + componentName.getPackageName(), new Object[0]);
        return false;
    }

    public static void a(Context context, String str) {
        ComponentName componentName = new ComponentName(context, str);
        ALog.d("UtilityImpl", "enableComponent", "comptName", componentName);
        try {
            context.getPackageManager().setComponentEnabledSetting(componentName, 1, 1);
        } catch (Throwable th) {
            ALog.w("UtilityImpl", "enableComponent", th, new Object[0]);
        }
    }

    public static boolean b(Context context, String str) {
        ComponentName componentName = new ComponentName(context, str);
        PackageManager packageManager = context.getPackageManager();
        try {
            ALog.d("UtilityImpl", "disableComponent,comptName=" + componentName.toString(), new Object[0]);
            if (packageManager.getComponentEnabledSetting(componentName) != 2) {
                packageManager.setComponentEnabledSetting(componentName, 2, 1);
                return true;
            }
        } catch (Throwable unused) {
        }
        return false;
    }

    public static void enableService(Context context) {
        a(context, AdapterUtilityImpl.channelService);
    }

    public static void disableService(Context context) {
        try {
            b(context, AdapterUtilityImpl.channelService);
        } catch (Throwable unused) {
        }
    }

    public static String e(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo == null ? "unknown" : activeNetworkInfo.getType() == 1 ? "wifi" : activeNetworkInfo.getType() == 0 ? NET_TYPE_MOBILE : "unknown";
        } catch (Throwable th) {
            th.printStackTrace();
            return "unknown";
        }
    }

    public static String f(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo == null ? "unknown" : activeNetworkInfo.getType() == 1 ? "wifi" : activeNetworkInfo.getType() == 0 ? NET_TYPE_MOBILE : "unknown";
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "getNetworkTypeExt", th, new Object[0]);
            return null;
        }
    }

    public static String a(long j) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Long.valueOf(j));
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "formatDay", th, new Object[0]);
            return "";
        }
    }

    private static boolean d(String str) {
        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
        return (configByTag == null ? 0 : configByTag.getSecurity()) == 2;
    }

    public static String a(Context context, String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str)) {
            ALog.e("UtilityImpl", "getAppsign appkey null", new Object[0]);
            return null;
        }
        try {
            if (com.aliyun.alink.linksdk.securesigner.util.Utils.hasSecurityGuardDep() && !d(str4)) {
                SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(context);
                if (securityGuardManager != null) {
                    ALog.d("UtilityImpl", "getAppsign SecurityGuardManager not null!", new Object[0]);
                    ISecureSignatureComponent secureSignatureComp = securityGuardManager.getSecureSignatureComp();
                    SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
                    securityGuardParamContext.appKey = str;
                    securityGuardParamContext.paramMap.put("INPUT", str3 + str);
                    securityGuardParamContext.requestType = 3;
                    AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str4);
                    ALog.d("UtilityImpl", "getAppsign getCached config = " + configByTag, new Object[0]);
                    String strSignRequest = secureSignatureComp.signRequest(securityGuardParamContext, configByTag != null ? configByTag.getAuthCode() : null);
                    ALog.d("UtilityImpl", "getAppsign sgpc = " + securityGuardParamContext + ", result = " + strSignRequest, new Object[0]);
                    return strSignRequest;
                }
                ALog.d("UtilityImpl", "getAppsign SecurityGuardManager is null", new Object[0]);
                return null;
            }
            if (!TextUtils.isEmpty(str2)) {
                String strSign = new SecurityImpl().sign(str + str3, "HmacSHA256");
                Log.d("UtilityImpl", "getAppsign result = " + strSign);
                return strSign;
            }
            ALog.e("UtilityImpl", "getAppsign secret null", new Object[0]);
            return null;
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "getAppsign", th, new Object[0]);
            return null;
        }
    }

    public static byte[] a(String str) {
        return d(str) ? null : null;
    }

    public static int a(Context context, String str, String str2, String str3, byte[] bArr) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || context == null || bArr == null) {
            return -1;
        }
        try {
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "SecurityGuardPutSslTicket2", th, new Object[0]);
        }
        return d(str) ? -1 : -1;
    }

    public static byte[] a(Context context, String str, String str2, String str3) {
        if (context == null || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str2)) {
            ALog.i("UtilityImpl", "get sslticket input null", new Object[0]);
            return null;
        }
        try {
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "SecurityGuardGetSslTicket2", th, new Object[0]);
        }
        return d(str) ? null : null;
    }

    public static boolean g(Context context) {
        if (context == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = GlobalClientInfo.getInstance(context).getConnectivityManager().getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDeviceId(Context context) {
        return AdapterUtilityImpl.getDeviceId(context);
    }

    public static boolean c(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            ALog.e("UtilityImpl", "package not exist", "pkg", str);
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static boolean utdidChanged(String str, Context context) {
        boolean z;
        try {
            try {
            } catch (Throwable th) {
                while (true) {
                    th = th;
                }
            }
        } catch (Throwable th2) {
            th = th2;
            str = null;
        }
        synchronized (f6450a) {
            try {
                String deviceId = getDeviceId(context);
                z = !context.getSharedPreferences(str, 0).getString("utdid", deviceId).equals(deviceId);
                return z;
            } catch (Throwable th3) {
                th = th3;
                str = null;
                try {
                    throw th;
                } catch (Throwable th4) {
                    th = th4;
                    ALog.e("UtilityImpl", "saveUtdid", th, new Object[0]);
                    z = str;
                    return z;
                }
            }
        }
    }

    public static void saveUtdid(String str, Context context) {
        JSONArray jSONArray;
        try {
            synchronized (f6450a) {
                String deviceId = getDeviceId(context);
                SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                String string = sharedPreferences.getString(Constants.SP_KEY_UTDID_LIST, null);
                if (string == null || !string.contains(deviceId)) {
                    if (string == null) {
                        jSONArray = new JSONArray();
                    } else {
                        jSONArray = new JSONArray(string);
                    }
                    jSONArray.put(deviceId);
                    editorEdit.putString(Constants.SP_KEY_UTDID_LIST, jSONArray.toString());
                }
                editorEdit.putString("utdid", deviceId);
                editorEdit.apply();
            }
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "saveUtdid", th, new Object[0]);
        }
    }

    public static List<String> getUtdids(String str, Context context) {
        ArrayList arrayList;
        try {
            synchronized (f6450a) {
                String string = context.getSharedPreferences(str, 0).getString(Constants.SP_KEY_UTDID_LIST, null);
                arrayList = new ArrayList();
                if (string != null) {
                    JSONArray jSONArray = new JSONArray(string);
                    for (int i = 0; i < jSONArray.length(); i++) {
                        arrayList.add(jSONArray.getString(i));
                    }
                }
                arrayList.add(getDeviceId(context));
            }
            return arrayList;
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "getUtdidList", th, new Object[0]);
            return null;
        }
    }

    public static void hitUtdid(String str, Context context, String str2) {
        try {
            synchronized (f6450a) {
                SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(str2);
                editorEdit.putString(Constants.SP_KEY_UTDID_LIST, jSONArray.toString());
                editorEdit.apply();
            }
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "hitUtdid", th, new Object[0]);
        }
    }

    public static String getUtdid(String str, Context context) {
        String string;
        try {
            synchronized (f6450a) {
                string = context.getSharedPreferences(str, 0).getString("utdid", getDeviceId(context));
            }
            return string;
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "getUtdid", th, new Object[0]);
            return "";
        }
    }

    public static void a(Context context, String str, long j) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_CHANNEL_FILE_NAME, 0).edit();
            editorEdit.putLong(str, j);
            editorEdit.apply();
            ALog.d("UtilityImpl", "setServiceTime:" + j, new Object[0]);
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "setServiceTime:", th, new Object[0]);
        }
    }

    public static long h(Context context) {
        long j;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_CHANNEL_FILE_NAME, 0);
            long j2 = sharedPreferences.getLong(Constants.SP_KEY_SERVICE_START, 0L);
            j = j2 > 0 ? sharedPreferences.getLong(Constants.SP_KEY_SERVICE_END, 0L) - j2 : 0L;
            try {
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                editorEdit.putLong(Constants.SP_KEY_SERVICE_START, 0L);
                editorEdit.putLong(Constants.SP_KEY_SERVICE_END, 0L);
                editorEdit.apply();
            } catch (Throwable th) {
                th = th;
                ALog.e("UtilityImpl", "getServiceAliveTime:", th, new Object[0]);
            }
        } catch (Throwable th2) {
            th = th2;
            j = 0;
        }
        return j;
    }

    public static String i(Context context) {
        try {
            return GlobalClientInfo.getInstance(context).getPackageInfo().versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int b(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return str.getBytes("utf-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String a(Throwable th) {
        return AdapterUtilityImpl.getStackMsg(th);
    }

    public static String j(Context context) {
        try {
            return context.getSharedPreferences(Constants.SP_COOKIE_FILE_NAME, 4).getString(Constants.SP_KEY_COOKIE_SEC, null);
        } catch (Exception e) {
            ALog.e("UtilityImpl", "reStoreCookie fail", e, new Object[0]);
            return null;
        }
    }

    public static long b() {
        return AdapterUtilityImpl.getUsableSpace();
    }

    public static String c() {
        if (Build.VERSION.SDK_INT < 11) {
            return Proxy.getDefaultHost();
        }
        return System.getProperty("http.proxyHost");
    }

    public static int d() {
        if (Build.VERSION.SDK_INT < 11) {
            return Proxy.getDefaultPort();
        }
        try {
            return Integer.parseInt(System.getProperty("http.proxyPort"));
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public static String e() {
        String str = c() + ":" + d();
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d("UtilityImpl", "getProxy:" + str, new Object[0]);
        }
        return str;
    }

    public static String k(Context context) {
        return AdapterUtilityImpl.isNotificationEnabled(context);
    }

    public static String d(Context context, String str) {
        try {
            File externalFilesDir = context.getExternalFilesDir("emastnetlogs");
            if (externalFilesDir == null || !externalFilesDir.exists() || !externalFilesDir.canWrite()) {
                externalFilesDir = context.getDir("emaslogs", 0);
            }
            ALog.d("UtilityImpl", "getTnetLogFilePath :" + externalFilesDir, new Object[0]);
            return externalFilesDir + "/" + str.toLowerCase();
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "getTnetLogFilePath", th, new Object[0]);
            return null;
        }
    }

    public static String a(int i) {
        try {
            return String.valueOf(i);
        } catch (Exception e) {
            ALog.e("UtilityImpl", "int2String", e, new Object[0]);
            return null;
        }
    }

    public static void e(Context context, String str) {
        try {
            synchronized (f6450a) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_FILE_NAME, 0);
                String string = sharedPreferences.getString("appkey", "");
                if (!TextUtils.isEmpty(str) && !string.equals(str) && !string.contains(str)) {
                    if (!TextUtils.isEmpty(string)) {
                        str = string + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + str;
                    }
                    SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                    editorEdit.putString("appkey", str);
                    editorEdit.apply();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void clearSharePreferences(Context context) {
        try {
            synchronized (f6450a) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_FILE_NAME, 0);
                String string = sharedPreferences.getString("appkey", null);
                String string2 = sharedPreferences.getString("app_sercet", null);
                String string3 = sharedPreferences.getString(Constants.KEY_PROXY_HOST, null);
                int i = sharedPreferences.getInt(Constants.KEY_PROXY_PORT, -1);
                int i2 = sharedPreferences.getInt("version", -1);
                SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                editorEdit.clear();
                if (!TextUtils.isEmpty(string)) {
                    editorEdit.putString("appkey", string);
                }
                if (!TextUtils.isEmpty(string2)) {
                    editorEdit.putString("app_sercet", string2);
                }
                if (!TextUtils.isEmpty(string3)) {
                    editorEdit.putString(Constants.KEY_PROXY_HOST, string3);
                }
                if (i > 0) {
                    editorEdit.putInt(Constants.KEY_PROXY_PORT, i);
                }
                if (i2 > 0) {
                    editorEdit.putInt("version", i2);
                }
                editorEdit.apply();
            }
        } catch (Throwable th) {
            ALog.e("UtilityImpl", "clearSharePreferences", th, new Object[0]);
        }
    }

    public static String f() {
        String str;
        Class<?>[] clsArr = {String.class};
        Object[] objArr = {"ro.build.version.emui"};
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            str = (String) cls.getDeclaredMethod(TmpConstant.PROPERTY_IDENTIFIER_GET, clsArr).invoke(cls, objArr);
            ALog.d("UtilityImpl", "getEmuiVersion", "result", str);
        } catch (Exception e) {
            ALog.e("UtilityImpl", "getEmuiVersion", e, new Object[0]);
        }
        return !TextUtils.isEmpty(str) ? str : "";
    }

    public static final Map<String, String> a(Map<String, List<String>> map) {
        HashMap map2 = new HashMap();
        try {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                if (!TextUtils.isEmpty(key)) {
                    String strA = a(entry.getValue());
                    if (!TextUtils.isEmpty(strA)) {
                        if (!key.startsWith(":")) {
                            key = key.toLowerCase(Locale.US);
                        }
                        map2.put(key, strA);
                    }
                }
            }
        } catch (Throwable unused) {
        }
        return map2;
    }

    public static final String a(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            stringBuffer.append(list.get(i));
            if (i < size - 1) {
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }

    public static String c(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    public static boolean l(Context context) {
        try {
            return context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getBoolean(Constants.SP_KEY_ENABLE_CHANNEL_PROCESS, true);
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void a(Context context, boolean z) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
            editorEdit.putBoolean(Constants.SP_KEY_ENABLE_CHANNEL_PROCESS, z);
            editorEdit.apply();
        } catch (Throwable unused) {
        }
    }

    public static boolean b(Context context, boolean z) {
        if (l(context)) {
            return false;
        }
        ALog.d("UtilityImpl", "channel process is disabled, kill it", new Object[0]);
        if (!z) {
            return true;
        }
        Process.killProcess(Process.myPid());
        return true;
    }

    public static void saveChannelInitClass(Context context, String str) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_FILE_NAME, 0);
            String string = sharedPreferences.getString(Constants.SP_KEY_CHANNEL_INIT, null);
            ArrayList arrayList = new ArrayList();
            if (string != null) {
                JSONArray jSONArray = new JSONArray(string);
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(jSONArray.getString(i));
                }
            }
            if (arrayList.contains(str)) {
                return;
            }
            arrayList.add(str);
            JSONArray jSONArray2 = new JSONArray();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                jSONArray2.put(arrayList.get(i2));
            }
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putString(Constants.SP_KEY_CHANNEL_INIT, jSONArray2.toString());
            editorEdit.commit();
        } catch (Throwable unused) {
        }
    }

    public static void a(Context context, List<String> list) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_FILE_NAME, 0);
            String string = sharedPreferences.getString(Constants.SP_KEY_CHANNEL_INIT, null);
            ArrayList arrayList = new ArrayList();
            if (string != null) {
                JSONArray jSONArray = new JSONArray(string);
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(jSONArray.getString(i));
                }
            }
            if (arrayList.removeAll(list)) {
                JSONArray jSONArray2 = new JSONArray();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    jSONArray2.put(arrayList.get(i2));
                }
                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                editorEdit.putString(Constants.SP_KEY_CHANNEL_INIT, jSONArray2.toString());
                editorEdit.commit();
            }
        } catch (Throwable unused) {
        }
    }

    public static List<String> m(Context context) {
        try {
            String string = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getString(Constants.SP_KEY_CHANNEL_INIT, null);
            if (string != null) {
                JSONArray jSONArray = new JSONArray(string);
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    arrayList.add(jSONArray.getString(i));
                }
                return arrayList;
            }
        } catch (Throwable unused) {
        }
        return null;
    }
}
