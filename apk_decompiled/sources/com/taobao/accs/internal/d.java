package com.taobao.accs.internal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.facebook.internal.NativeProtocol;
import com.heytap.mcssdk.constant.IntentConstant;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.base.IBaseService;
import com.taobao.accs.common.Constants;
import com.taobao.accs.net.w;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.accs.utl.Utils;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class d implements IBaseService {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static ConcurrentHashMap<String, com.taobao.accs.net.b> f6340a = new ConcurrentHashMap<>(2);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f6341b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Service f6342c;

    public abstract int a(Intent intent);

    @Override // com.taobao.accs.base.IBaseService
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // com.taobao.accs.base.IBaseService
    public boolean onUnbind(Intent intent) {
        return false;
    }

    public d(Service service) {
        this.f6342c = null;
        this.f6342c = service;
        this.f6341b = service.getApplicationContext();
    }

    @Override // com.taobao.accs.base.IBaseService
    public void onCreate() {
        ALog.i("ElectionServiceImpl", "onCreate,", "sdkVersion", Integer.valueOf(Constants.SDK_VERSION_CODE));
    }

    @Override // com.taobao.accs.base.IBaseService
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            return 2;
        }
        String action = intent.getAction();
        ALog.i("ElectionServiceImpl", "onStartCommand begin", NativeProtocol.WEB_DIALOG_ACTION, action);
        if (TextUtils.equals(action, Constants.ACTION_START_SERVICE)) {
            b(intent);
        } else if (TextUtils.isEmpty(action)) {
            a(true);
        } else {
            a(false);
        }
        return a(intent);
    }

    @Override // com.taobao.accs.base.IBaseService
    public void onDestroy() {
        ALog.e("ElectionServiceImpl", "Service onDestroy", new Object[0]);
        this.f6341b = null;
        this.f6342c = null;
    }

    private void b(Intent intent) {
        try {
            String stringExtra = intent.getStringExtra(Constants.KEY_PACKAGE_NAME);
            String stringExtra2 = intent.getStringExtra("appKey");
            String stringExtra3 = intent.getStringExtra(Constants.KEY_TTID);
            String stringExtra4 = intent.getStringExtra("app_sercet");
            String stringExtra5 = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
            int intExtra = intent.getIntExtra("mode", 0);
            ALog.i("ElectionServiceImpl", "handleStartCommand", Constants.KEY_CONFIG_TAG, stringExtra5, "appkey", stringExtra2, IntentConstant.APP_SECRET, stringExtra4, Constants.KEY_TTID, stringExtra3, "pkg", stringExtra);
            if (TextUtils.isEmpty(stringExtra) || TextUtils.isEmpty(stringExtra2) || !stringExtra.equals(this.f6341b.getPackageName())) {
                return;
            }
            Utils.setMode(this.f6341b, intExtra);
            com.taobao.accs.net.b bVarA = a(this.f6341b, stringExtra5, false);
            if (bVarA != null) {
                bVarA.f6363a = stringExtra3;
            } else {
                ALog.e("ElectionServiceImpl", "handleStartCommand start action, no connection", Constants.KEY_CONFIG_TAG, stringExtra5);
            }
            UtilityImpl.e(this.f6341b, stringExtra2);
        } catch (Throwable th) {
            ALog.e("ElectionServiceImpl", "handleStartCommand", th, new Object[0]);
        }
    }

    private void a(boolean z) {
        for (String str : AccsClientConfig.tags()) {
            try {
                if (!AccsClientConfig.getConfigByTag(str).getDisableChannel()) {
                    a(this.f6341b, str, z);
                }
            } catch (Throwable th) {
                ALog.w("ElectionServiceImpl", "tryStartAllConnections " + str, th, new Object[0]);
            }
        }
    }

    protected static com.taobao.accs.net.b a(Context context, String str, boolean z) {
        com.taobao.accs.net.b bVar = null;
        try {
            if (TextUtils.isEmpty(str)) {
                ALog.w("ElectionServiceImpl", "getConnection configTag null or env invalid", "conns.size", Integer.valueOf(f6340a.size()));
                if (f6340a.size() > 0) {
                    return f6340a.elements().nextElement();
                }
                return null;
            }
            ALog.i("ElectionServiceImpl", "getConnection", Constants.KEY_CONFIG_TAG, str, "start", Boolean.valueOf(z));
            AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
            if (configByTag != null && configByTag.getDisableChannel()) {
                ALog.e("ElectionServiceImpl", "getConnection channel disabled!", Constants.KEY_CONFIG_TAG, str);
                return null;
            }
            int mode = Utils.getMode(context);
            String str2 = str + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + mode;
            com.taobao.accs.net.b bVar2 = f6340a.get(str2);
            if (bVar2 != null) {
                return bVar2;
            }
            try {
                AccsClientConfig.mEnv = mode;
                w wVar = new w(context, 0, str);
                if (z) {
                    wVar.a();
                }
                if (f6340a.size() < 10) {
                    f6340a.put(str2, wVar);
                    return wVar;
                }
                ALog.e("ElectionServiceImpl", "getConnection fail as exist too many conns!!!", new Object[0]);
                return wVar;
            } catch (Throwable th) {
                th = th;
                bVar = bVar2;
                ALog.e("ElectionServiceImpl", "getConnection", th, new Object[0]);
                return bVar;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
