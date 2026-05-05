package com.alibaba.sdk.android.push.huawei;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.impl.HuaweiMsgParseImpl;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.alibaba.sdk.android.push.utils.SysUtils;
import com.alibaba.sdk.android.push.utils.ThreadUtil;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.ams.emas.push.notification.AgooMessageNotification;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;
import com.taobao.accs.utl.ALog;
import com.xiaomi.mipush.sdk.Constants;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class HuaWeiRegister {
    private static final String TAG = "MPS:HuaWeiRegister";
    public static boolean isChannelRegister = false;

    public static boolean register(Application application) {
        return registerBundle(application, false);
    }

    public static boolean registerBundle(final Application application, boolean z) {
        try {
            isChannelRegister = z;
        } catch (Throwable th) {
            ALog.e(TAG, "register", th, new Object[0]);
        }
        if (!isChannelRegister && !SysUtils.isTargetProcess(application)) {
            ALog.e(TAG, "register not in target process, return", new Object[0]);
            return false;
        }
        if (checkDevice() && Build.VERSION.SDK_INT >= 17) {
            ThirdPushManager.registerImpl(new HuaweiMsgParseImpl());
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.alibaba.sdk.android.push.huawei.HuaWeiRegister.1
                @Override // java.lang.Runnable
                public void run() {
                    ALog.i(HuaWeiRegister.TAG, "register begin isChannel:" + HuaWeiRegister.isChannelRegister, new Object[0]);
                    HuaWeiRegister.getToken(application.getApplicationContext());
                }
            }, 5000L);
            return true;
        }
        ALog.i(TAG, "register checkDevice false", new Object[0]);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getToken(final Context context) {
        ThreadUtil.getExecutor().execute(new Runnable() { // from class: com.alibaba.sdk.android.push.huawei.HuaWeiRegister.2
            @Override // java.lang.Runnable
            public void run() {
                String token;
                try {
                    String string = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString(Constants.HUAWEI_HMS_CLIENT_APPID);
                    String strReplace = TextUtils.isEmpty(string) ? "" : string.replace("appid=", "");
                    if (TextUtils.isEmpty(strReplace)) {
                        token = HmsInstanceId.getInstance(context).getToken();
                    } else {
                        token = HmsInstanceId.getInstance(context).getToken(strReplace, HmsMessaging.DEFAULT_TOKEN_SCOPE);
                    }
                    ALog.i(HuaWeiRegister.TAG, "onToken", AgooMessageNotification.APP_ID, strReplace, "token", token);
                    if (TextUtils.isEmpty(token)) {
                        return;
                    }
                    try {
                        ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.HUAWEI.thirdTokenKeyword, token, BuildConfig.HW_VERSION);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Throwable th) {
                    if (th instanceof ApiException) {
                        ALog.e(HuaWeiRegister.TAG, "getToken failed. " + ((ApiException) th).getStatusCode(), th, new Object[0]);
                    } else {
                        ALog.e(HuaWeiRegister.TAG, "getToken failed. " + th.getMessage(), th, new Object[0]);
                    }
                    try {
                        ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.HUAWEI.thirdTokenKeyword, "", BuildConfig.HW_VERSION);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    private static boolean checkDevice() {
        boolean zEqualsIgnoreCase = Build.BRAND.equalsIgnoreCase(AgooConstants.MESSAGE_SYSTEM_SOURCE_HUAWEI);
        if (zEqualsIgnoreCase) {
            return zEqualsIgnoreCase;
        }
        return (TextUtils.isEmpty(getProp("ro.build.version.emui")) && TextUtils.isEmpty(getProp("hw_sc.build.platform.version"))) ? false : true;
    }

    private static String getProp(String str) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getDeclaredMethod(TmpConstant.PROPERTY_IDENTIFIER_GET, String.class).invoke(null, str);
        } catch (Throwable unused) {
            return "";
        }
    }
}
