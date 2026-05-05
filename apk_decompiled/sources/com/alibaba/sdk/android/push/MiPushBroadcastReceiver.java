package com.alibaba.sdk.android.push;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.alibaba.sdk.android.push.xiaomi.BuildConfig;
import com.taobao.accs.utl.ALog;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiPushBroadcastReceiver extends PushMessageReceiver {
    public static final String TAG = "MPS:MiPushBroadcastReceiver";

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        String command = miPushCommandMessage.getCommand();
        List<String> commandArguments = miPushCommandMessage.getCommandArguments();
        String str = (commandArguments == null || commandArguments.size() <= 0) ? null : commandArguments.get(0);
        if (commandArguments != null && commandArguments.size() > 1) {
            commandArguments.get(1);
        }
        if ("register".equals(command)) {
            String str2 = miPushCommandMessage.getResultCode() == 0 ? str : null;
            ALog.d(TAG, "onReceiveRegisterResult regId:" + str2 + " code: " + miPushCommandMessage.getResultCode() + " msg: " + miPushCommandMessage.getReason(), new Object[0]);
            if (!TextUtils.isEmpty(str2)) {
                ALog.d(TAG, "reportToken", new Object[0]);
                if ("intl".equals(BuildConfig.FLAVOR)) {
                    String appRegion = MiPushClient.getAppRegion(context);
                    ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.XIAOMI.thirdTokenKeyword, str2, "5.6.2_" + appRegion);
                    return;
                }
                ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.XIAOMI.thirdTokenKeyword, str2, BuildConfig.MI_VERSION);
                return;
            }
            ALog.d(TAG, "reportToken fail " + miPushCommandMessage.getResultCode(), new Object[0]);
            ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.XIAOMI.thirdTokenKeyword, "", BuildConfig.MI_VERSION);
        }
    }

    @Override // com.xiaomi.mipush.sdk.PushMessageReceiver
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        try {
            String content = miPushMessage.getContent();
            ALog.d(TAG, "onReceivePassThroughMessage msg:" + content, new Object[0]);
            ThirdPushManager.onPushMsg(context, ThirdPushManager.ThirdPushReportKeyword.XIAOMI.thirdMsgKeyword, content);
        } catch (Throwable th) {
            ALog.e(TAG, "onReceivePassThroughMessage", th, new Object[0]);
        }
    }
}
