package com.alibaba.sdk.android.push.register;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.impl.XiaoMiMsgParseImpl;
import com.alibaba.sdk.android.push.utils.SysUtils;
import com.taobao.accs.utl.ALog;
import com.xiaomi.mipush.sdk.MiPushClient;

/* JADX INFO: loaded from: classes.dex */
public class MiPushRegister {
    private static final String BLACK_SHARK = "blackshark";
    private static final String REDMI = "redmi";
    private static final String TAG = "MPS:MiPushRegister";
    private static final String XIAOMI = "xiaomi";
    private static final String brand;

    static {
        String str = TextUtils.isEmpty(Build.BRAND) ? Build.MANUFACTURER : Build.BRAND;
        brand = str == null ? "" : str.toLowerCase();
    }

    public static boolean checkDevice() {
        return "xiaomi".equals(brand) || REDMI.equals(brand) || BLACK_SHARK.equals(brand);
    }

    public static boolean register(Context context, String str, String str2, boolean z) {
        try {
        } catch (Throwable th) {
            ALog.e(TAG, "register", th, new Object[0]);
        }
        if (!SysUtils.isTargetProcess(context)) {
            ALog.i(TAG, "register not in target process, return", new Object[0]);
            return false;
        }
        if (!checkDevice() && !z) {
            ALog.i(TAG, "check device fail", new Object[0]);
            return false;
        }
        ALog.i(TAG, "register begin", new Object[0]);
        ThirdPushManager.registerImpl(new XiaoMiMsgParseImpl());
        MiPushClient.registerPush(context, str, str2);
        return true;
    }

    public static boolean register(Context context, String str, String str2) {
        return register(context, str, str2, false);
    }

    public static void unregister(Context context) {
        try {
            MiPushClient.unregisterPush(context);
        } catch (Throwable th) {
            ALog.e(TAG, MiPushClient.COMMAND_UNREGISTER, th, new Object[0]);
        }
    }
}
