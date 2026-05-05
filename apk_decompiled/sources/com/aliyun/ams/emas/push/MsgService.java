package com.aliyun.ams.emas.push;

import android.content.Intent;
import android.text.TextUtils;
import com.aliyun.ams.emas.push.notification.CPushServiceListener;
import com.taobao.accs.data.MsgDistributeService;
import com.taobao.accs.utl.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class MsgService extends MsgDistributeService {
    public static final String TAG = "MPS:MsgService";
    CPushServiceListener listener = new CPushServiceListener();

    @Override // com.taobao.accs.data.MsgDistributeService, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            ALog.d(TAG, "intent null", new Object[0]);
            return super.onStartCommand(intent, i, i2);
        }
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return super.onStartCommand(intent, i, i2);
        }
        ALog.d(TAG, "MsgService onStartCommand begin...action=" + action, new Object[0]);
        if (TextUtils.equals(action, PushConfigHolder.SERVICE_CONTAINER_ACTION)) {
            this.listener.onStartCommand(intent, getApplicationContext());
            return 2;
        }
        return super.onStartCommand(intent, i, i2);
    }
}
