package com.alibaba.sdk.android.push.utils;

import android.content.Context;
import com.alibaba.sdk.android.logger.ILog;
import com.taobao.accs.utl.AdapterUtilityImpl;

/* JADX INFO: loaded from: classes.dex */
public class SysUtils {
    private static final ILog LOGGER = ThirdPushLogger.getLogger(SysUtils.class);
    private static Boolean isTargetProcess = null;

    @Deprecated
    public static boolean isMainProcess(Context context) {
        return isTargetProcess(context);
    }

    public static boolean isTargetProcess(Context context) {
        Boolean bool = isTargetProcess;
        if (bool != null) {
            return bool.booleanValue();
        }
        try {
            isTargetProcess = Boolean.valueOf(AdapterUtilityImpl.isTargetProcess(context));
            return isTargetProcess.booleanValue();
        } catch (Throwable th) {
            LOGGER.e("isTargetProcess exception!! third push will be disabled", th);
            return false;
        }
    }
}
