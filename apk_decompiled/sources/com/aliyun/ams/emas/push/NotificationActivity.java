package com.aliyun.ams.emas.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.aliyun.ams.emas.push.notification.CPushServiceListener;
import com.taobao.accs.utl.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationActivity extends Activity {
    public static final String TAG = "MPS:NotificationActivity";
    CPushServiceListener listener = new CPushServiceListener();

    @Override // android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            ALog.d(TAG, " onCreate begin...action=" + action, new Object[0]);
            if (TextUtils.equals(action, PushConfigHolder.SERVICE_CONTAINER_ACTION)) {
                this.listener.onStartCommandFrom(intent, getApplicationContext(), 1);
            }
        }
        finish();
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String action = intent.getAction();
            ALog.d(TAG, " onNewIntent begin...action=" + action, new Object[0]);
            if (TextUtils.equals(action, PushConfigHolder.SERVICE_CONTAINER_ACTION)) {
                this.listener.onStartCommandFrom(intent, getApplicationContext(), 1);
            }
        }
        finish();
    }
}
