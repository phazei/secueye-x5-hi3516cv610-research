package com.taobao.accs.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.text.TextUtils;
import com.facebook.internal.NativeProtocol;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.messenger.MessengerInnerHandler;
import com.taobao.accs.utl.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class MsgDistributeService extends Service {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    MessengerInnerHandler f6304a = new MessengerInnerHandler("MsgDistributeService", this);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Messenger f6305b = new Messenger(this.f6304a);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f6305b.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        try {
            ALog.i("MsgDistributeService", "onStartCommand", NativeProtocol.WEB_DIALOG_ACTION, intent.getAction());
            if (!TextUtils.isEmpty(intent.getAction()) && TextUtils.equals(intent.getAction(), Constants.ACTION_SEND)) {
                if (getPackageName().equals(intent.getStringExtra(Constants.KEY_PACKAGE_NAME))) {
                    ThreadPoolExecutorFactory.getScheduledExecutor().execute(new i(this, intent));
                }
            } else {
                ALog.i("MsgDistributeService", "onStartCommand distribute message", new Object[0]);
                g.a(getApplicationContext(), intent);
            }
        } catch (Throwable th) {
            ALog.e("MsgDistributeService", "onStartCommand", th, new Object[0]);
        }
        return 2;
    }
}
