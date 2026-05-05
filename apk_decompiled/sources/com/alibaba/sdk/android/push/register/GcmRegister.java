package com.alibaba.sdk.android.push.register;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.logger.ILog;
import com.alibaba.sdk.android.push.fcm.BuildConfig;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.alibaba.sdk.android.push.utils.SysUtils;
import com.alibaba.sdk.android.push.utils.ThirdPushLogger;
import com.alibaba.sdk.android.push.utils.ThreadUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.taobao.accs.utl.ALog;

/* JADX INFO: loaded from: classes.dex */
public class GcmRegister {
    public static final String TAG = "MPS:GcmRegister";
    private static final ILog LOGGER = ThirdPushLogger.getLogger("MPS:GcmRegister");
    private static volatile boolean isRegistered = false;

    public static boolean register(final Context context, final String str, final String str2, final String str3, final String str4) {
        if (!SysUtils.isTargetProcess(context)) {
            LOGGER.i("not in target process, return");
            return false;
        }
        ThreadUtil.getExecutor().execute(new Runnable() { // from class: com.alibaba.sdk.android.push.register.GcmRegister.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (GcmRegister.isRegistered) {
                        GcmRegister.LOGGER.w("registered already");
                        return;
                    }
                    boolean unused = GcmRegister.isRegistered = true;
                    FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
                    builder.setGcmSenderId(str);
                    builder.setApplicationId(str2);
                    if (!TextUtils.isEmpty(str3)) {
                        builder.setProjectId(str3);
                    }
                    if (!TextUtils.isEmpty(str4)) {
                        builder.setApiKey(str4);
                    }
                    try {
                        FirebaseApp.initializeApp(context.getApplicationContext(), builder.build());
                    } catch (Throwable th) {
                        GcmRegister.LOGGER.w("register initializeApp", th);
                    }
                    GcmRegister.tryGetTokenAndReport(context);
                } catch (Throwable th2) {
                    ALog.e("MPS:GcmRegister", "register", th2, new Object[0]);
                }
            }
        });
        return true;
    }

    public static void unregister() {
        isRegistered = false;
    }

    public static void tryGetTokenAndReport(final Context context) {
        if (!SysUtils.isTargetProcess(context)) {
            LOGGER.i("not in target process, return");
        } else {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() { // from class: com.alibaba.sdk.android.push.register.GcmRegister.2
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public void onComplete(Task<String> task) {
                    if (!task.isSuccessful()) {
                        GcmRegister.LOGGER.w("Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String result = task.getResult();
                    GcmRegister.LOGGER.w("fcm token is ", result);
                    ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.FCM.thirdTokenKeyword, result, BuildConfig.GCM_VERSION);
                }
            });
        }
    }
}
