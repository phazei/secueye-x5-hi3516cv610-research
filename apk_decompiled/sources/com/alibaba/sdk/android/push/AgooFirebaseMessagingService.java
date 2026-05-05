package com.alibaba.sdk.android.push;

import android.text.TextUtils;
import com.alibaba.sdk.android.push.fcm.BuildConfig;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taobao.accs.utl.ALog;

/* JADX INFO: loaded from: classes.dex */
public class AgooFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "MPS:GcmRegister";

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(String str) {
        super.onNewToken(str);
        ThirdPushManager.reportToken(getApplicationContext(), ThirdPushManager.ThirdPushReportKeyword.FCM.thirdTokenKeyword, str, BuildConfig.GCM_VERSION);
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            ALog.d("MPS:GcmRegister", "onMessageReceived", new Object[0]);
            if (remoteMessage.getData() != null) {
                String str = remoteMessage.getData().get("payload");
                ALog.d("MPS:GcmRegister", "onMessageReceived payload msg:" + str, new Object[0]);
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                ThirdPushManager.onPushMsg(getApplicationContext(), ThirdPushManager.ThirdPushReportKeyword.FCM.thirdMsgKeyword, str);
            }
        } catch (Throwable th) {
            ALog.e("MPS:GcmRegister", "onMessageReceived", th, new Object[0]);
        }
    }
}
