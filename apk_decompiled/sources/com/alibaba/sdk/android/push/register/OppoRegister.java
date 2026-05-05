package com.alibaba.sdk.android.push.register;

import android.content.Context;
import android.os.Build;
import com.alibaba.sdk.android.push.impl.OppoMsgParseImpl;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.alibaba.sdk.android.push.utils.SysUtils;
import com.alibaba.sdk.android.push.utils.ThreadUtil;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.taobao.accs.utl.ALog;

/* JADX INFO: loaded from: classes.dex */
public class OppoRegister {
    public static final String TAG = "MPS:OPush";

    public static void registerAsync(final Context context, final String str, final String str2) {
        ThreadUtil.getExecutor().execute(new Runnable() { // from class: com.alibaba.sdk.android.push.register.OppoRegister.1
            @Override // java.lang.Runnable
            public void run() {
                OppoRegister.register(context, str, str2);
            }
        });
    }

    public static boolean register(Context context, String str, String str2) {
        try {
        } catch (Throwable th) {
            ALog.e(TAG, "register error", th, new Object[0]);
        }
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        final Context applicationContext = context.getApplicationContext();
        if (!SysUtils.isTargetProcess(applicationContext)) {
            ALog.i(TAG, "not in target process, return", new Object[0]);
            return false;
        }
        HeytapPushManager.init(applicationContext, (applicationContext.getApplicationInfo().flags & 2) != 0);
        if (HeytapPushManager.isSupportPush(applicationContext)) {
            ThirdPushManager.registerImpl(new OppoMsgParseImpl());
            ALog.i(TAG, "register oppo begin ", new Object[0]);
            HeytapPushManager.register(applicationContext, str, str2, new ICallBackResultService() { // from class: com.alibaba.sdk.android.push.register.OppoRegister.2
                @Override // com.heytap.msp.push.callback.ICallBackResultService
                public void onRegister(int i, String str3) {
                    ALog.i(OppoRegister.TAG, "onRegister code=" + i + " regid=" + str3, new Object[0]);
                    if (i == 0) {
                        if (str3.contains("APP Not prepared") || str3.contains("Invalid App Key") || str3.contains("deviceID is invalid") || str3.contains("Missing App Key")) {
                            ALog.i(OppoRegister.TAG, "OPPO token is invalid", new Object[0]);
                            return;
                        } else {
                            ThirdPushManager.reportToken(applicationContext, ThirdPushManager.ThirdPushReportKeyword.OPPO.thirdTokenKeyword, str3, "3.1.0");
                            return;
                        }
                    }
                    ThirdPushManager.reportToken(applicationContext, ThirdPushManager.ThirdPushReportKeyword.OPPO.thirdTokenKeyword, "", "3.1.0");
                }

                @Override // com.heytap.msp.push.callback.ICallBackResultService
                public void onError(int i, String str3) {
                    ALog.e(OppoRegister.TAG, "onError code=" + i + " msg=" + str3, new Object[0]);
                }

                @Override // com.heytap.msp.push.callback.ICallBackResultService
                public void onUnRegister(int i) {
                    ALog.e(OppoRegister.TAG, "onUnRegister code=" + i, new Object[0]);
                }

                @Override // com.heytap.msp.push.callback.ICallBackResultService
                public void onSetPushTime(int i, String str3) {
                    ALog.i(OppoRegister.TAG, "onSetPushTime code=" + i + " pushTime is " + str3, new Object[0]);
                }

                @Override // com.heytap.msp.push.callback.ICallBackResultService
                public void onGetPushStatus(int i, int i2) {
                    ALog.i(OppoRegister.TAG, "onGetPushStatus code=" + i + " status=" + i2, new Object[0]);
                }

                @Override // com.heytap.msp.push.callback.ICallBackResultService
                public void onGetNotificationStatus(int i, int i2) {
                    ALog.i(OppoRegister.TAG, "onGetNotificationStatus code=" + i + " status=" + i2, new Object[0]);
                }
            });
            return true;
        }
        ALog.i(TAG, "not support oppo push", new Object[0]);
        return false;
    }
}
