package com.alibaba.sdk.android.push.huawei;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.taobao.accs.utl.ALog;

/* JADX INFO: loaded from: classes.dex */
public class HuaweiPushMessageService extends HmsMessageService {
    private static final String TAG = "MPS:HuaWeiPushMessageService";

    @Override // com.huawei.hms.push.HmsMessageService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            String data = remoteMessage.getData();
            ALog.i(TAG, "onPushMsg", "content", data);
            ThirdPushManager.onPushMsg(this, ThirdPushManager.ThirdPushReportKeyword.HUAWEI.thirdMsgKeyword, data);
        } catch (Throwable th) {
            ALog.e(TAG, "onPushMsg", th, new Object[0]);
        }
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onMessageSent(String str) {
        super.onMessageSent(str);
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onSendError(String str, Exception exc) {
        super.onSendError(str, exc);
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onNewToken(String str) {
        super.onNewToken(str);
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            ALog.i(TAG, "onToken", "token", str);
            ThirdPushManager.reportToken(this, ThirdPushManager.ThirdPushReportKeyword.HUAWEI.thirdTokenKeyword, str, BuildConfig.HW_VERSION);
        } catch (Throwable th) {
            ALog.e(TAG, "onToken", th, new Object[0]);
        }
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onNewToken(String str, Bundle bundle) {
        super.onNewToken(str, bundle);
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            ALog.i(TAG, "onToken", "token", str);
            ThirdPushManager.reportToken(this, ThirdPushManager.ThirdPushReportKeyword.HUAWEI.thirdTokenKeyword, str, BuildConfig.HW_VERSION);
        } catch (Throwable th) {
            ALog.e(TAG, "onToken", th, new Object[0]);
        }
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onTokenError(Exception exc) {
        super.onTokenError(exc);
        ALog.e(TAG, "onTokenError", exc, new Object[0]);
    }

    @Override // com.huawei.hms.push.HmsMessageService
    public void onTokenError(Exception exc, Bundle bundle) {
        super.onTokenError(exc, bundle);
        ALog.e(TAG, "onTokenError", exc, new Object[0]);
    }

    @Override // com.huawei.hms.push.HmsMessageService, android.app.Service
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override // com.huawei.hms.push.HmsMessageService, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    @Override // com.huawei.hms.push.HmsMessageService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }
}
