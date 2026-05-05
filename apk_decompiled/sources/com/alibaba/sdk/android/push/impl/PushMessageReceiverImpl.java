package com.alibaba.sdk.android.push.impl;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.taobao.accs.utl.ALog;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

/* JADX INFO: loaded from: classes.dex */
public class PushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    public static final String TAG = "MPS:vPush:PushMessageReceiverImpl";

    @Override // com.vivo.push.sdk.OpenClientPushMessageReceiver, com.vivo.push.sdk.PushMessageCallback
    public void onReceiveRegId(Context context, String str) {
        if (TextUtils.isEmpty(str) || context == null) {
            return;
        }
        ALog.i(TAG, "onReceiveRegId regId:" + str, new Object[0]);
        ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.VIVO.thirdTokenKeyword, str, "3.0.0.4");
    }
}
