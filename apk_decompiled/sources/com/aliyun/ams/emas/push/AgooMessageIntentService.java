package com.aliyun.ams.emas.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.text.TextUtils;
import com.taobao.accs.messenger.MessengerInnerHandler;
import com.taobao.accs.utl.ALog;
import org.json.JSONException;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AgooMessageIntentService extends Service implements IAgooPushCallback, IAgooPushConfig {
    private static final String TAG = "MPS:AliyunMessageIntentService";
    MessengerInnerHandler innerHandler = new MessengerInnerHandler(TAG, this);
    private final Messenger messenger = new Messenger(this.innerHandler);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.messenger.getBinder();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) throws JSONException {
        onHandleIntent(intent);
        return 2;
    }

    protected void onHandleIntent(Intent intent) throws JSONException {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            ALog.e(TAG, "Action is null, return.", new Object[0]);
            return;
        }
        ALog.d(TAG, "AgooMessageIntentService onHandleIntent action: " + action, new Object[0]);
        if (AgooInnerService.AGOO_RECEIVE_ACTION.equals(action)) {
            AgooPushHandler.handle(getApplicationContext(), intent, this, this);
            return;
        }
        if (AgooMessageReceiver.NOTIFICATION_OPENED_ACTION.equals(action)) {
            AgooPushHandler.handleNotificationClick(getApplicationContext(), intent, this);
        } else if (AgooMessageReceiver.NOTIFICATION_REMOVED_ACTION.equals(action)) {
            AgooPushHandler.handleNotificationDelete(getApplicationContext(), intent, this);
        } else {
            ALog.e(TAG, "Invalid action", new Object[0]);
        }
    }
}
