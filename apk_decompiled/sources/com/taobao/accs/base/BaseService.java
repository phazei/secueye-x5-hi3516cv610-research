package com.taobao.accs.base;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.os.Process;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.internal.ServiceImpl;
import com.taobao.accs.messenger.MessengerInnerHandler;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.accs.utl.Utils;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class BaseService extends Service {
    private static final String TAG = "BaseService";
    IBaseService mBaseService = null;
    MessengerInnerHandler innerHandler = new MessengerInnerHandler(TAG, this);
    private final Messenger messenger = new Messenger(this.innerHandler);

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        ACCSClient.setCurrentProcessName(getApplicationContext());
        ThreadPoolExecutorFactory.execute(new Runnable() { // from class: com.taobao.accs.base.BaseService.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    BaseService.this.mBaseService = new ServiceImpl(BaseService.this);
                    BaseService.this.mBaseService.onCreate();
                } catch (Exception e) {
                    ALog.e(BaseService.TAG, "create ServiceImpl error", e.getMessage());
                }
            }
        });
    }

    @Override // android.app.Service
    public int onStartCommand(final Intent intent, final int i, final int i2) {
        ThreadPoolExecutorFactory.execute(new Runnable() { // from class: com.taobao.accs.base.BaseService.2
            @Override // java.lang.Runnable
            public void run() {
                if (BaseService.this.mBaseService != null) {
                    BaseService.this.mBaseService.onStartCommand(intent, i, i2);
                } else {
                    BaseService.this.onCreate();
                    BaseService.this.onStartCommand(intent, i, i2);
                }
            }
        });
        if (UtilityImpl.l(this)) {
            return 1;
        }
        ALog.d(TAG, "channel process is disabled, stop", new Object[0]);
        Process.killProcess(Process.myPid());
        return 1;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        ALog.d(TAG, "onBind", "intent", intent);
        try {
            if (Utils.isTarget26(this)) {
                ALog.i(TAG, "onBind bind service", new Object[0]);
                getApplicationContext().bindService(new Intent(this, getClass()), new ServiceConnection() { // from class: com.taobao.accs.base.BaseService.3
                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(ComponentName componentName) {
                    }
                }, 1);
            }
        } catch (Throwable th) {
            ALog.i(TAG, "onBind bind service with exception", th.toString());
        }
        return this.messenger.getBinder();
    }

    @Override // android.app.Service
    public void onDestroy() {
        ThreadPoolExecutorFactory.execute(new Runnable() { // from class: com.taobao.accs.base.BaseService.4
            @Override // java.lang.Runnable
            public void run() {
                if (BaseService.this.mBaseService != null) {
                    BaseService.this.mBaseService.onDestroy();
                    BaseService.this.mBaseService = null;
                }
            }
        });
        super.onDestroy();
    }
}
