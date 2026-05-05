package com.taobao.accs.net;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.taobao.accs.ServiceReceiver;
import com.taobao.accs.internal.AccsJobService;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.OrangeAdapter;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class f {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected static volatile f f6374b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final int[] f6375c = {270, 360, 480};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected Context f6376a;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f6377d;
    private long e;
    private boolean f = false;
    private int[] g = {0, 0, 0};
    private boolean h;

    protected abstract void a(int i);

    protected f(Context context) {
        this.h = true;
        try {
            this.f6376a = context;
            this.f6377d = 0;
            this.e = System.currentTimeMillis();
            this.h = OrangeAdapter.isSmartHb();
        } catch (Throwable th) {
            ALog.e("HeartbeatManager", "HeartbeatManager", th, new Object[0]);
        }
    }

    public static f a(Context context) {
        if (f6374b == null) {
            synchronized (f.class) {
                if (f6374b == null) {
                    if (Build.VERSION.SDK_INT >= 21 && b(context)) {
                        ALog.i("HeartbeatManager", "hb use job", new Object[0]);
                        f6374b = new t(context);
                    } else if (c(context)) {
                        ALog.i("HeartbeatManager", "hb use alarm", new Object[0]);
                        f6374b = new a(context);
                    } else {
                        ALog.i("HeartbeatManager", "hb use thread", new Object[0]);
                        f6374b = new u(context);
                    }
                }
            }
        }
        return f6374b;
    }

    private static boolean b(Context context) {
        return context.getPackageManager().getComponentEnabledSetting(new ComponentName(context.getPackageName(), AccsJobService.class.getName())) == 1;
    }

    private static boolean c(Context context) {
        return context.getPackageManager().getComponentEnabledSetting(new ComponentName(context.getPackageName(), ServiceReceiver.class.getName())) == 1;
    }

    public synchronized void a() {
        try {
            if (this.e < 0) {
                this.e = System.currentTimeMillis();
            }
            int iB = b();
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d("HeartbeatManager", "set " + iB, new Object[0]);
            }
            a(iB);
        } catch (Throwable th) {
            ALog.e("HeartbeatManager", TmpConstant.PROPERTY_IDENTIFIER_SET, th, new Object[0]);
        }
    }

    public int b() {
        int i = this.h ? f6375c[this.f6377d] : 270;
        this.h = OrangeAdapter.isSmartHb();
        return i;
    }

    public void c() {
        this.e = -1L;
        if (this.f) {
            int[] iArr = this.g;
            int i = this.f6377d;
            iArr[i] = iArr[i] + 1;
        }
        int i2 = this.f6377d;
        this.f6377d = i2 > 0 ? i2 - 1 : 0;
        ALog.d("HeartbeatManager", "onNetworkTimeout", new Object[0]);
    }

    public void d() {
        this.e = -1L;
        ALog.d("HeartbeatManager", "onNetworkFail", new Object[0]);
    }

    public void e() {
        ALog.d("HeartbeatManager", "onHeartbeatSucc", new Object[0]);
        if (System.currentTimeMillis() - this.e > 7199000) {
            int i = this.f6377d;
            if (i >= f6375c.length - 1 || this.g[i] > 2) {
                return;
            }
            ALog.d("HeartbeatManager", "upgrade", new Object[0]);
            this.f6377d++;
            this.f = true;
            this.e = System.currentTimeMillis();
            return;
        }
        this.f = false;
        this.g[this.f6377d] = 0;
    }

    public void f() {
        this.f6377d = 0;
        this.e = System.currentTimeMillis();
        ALog.d("HeartbeatManager", "resetLevel", new Object[0]);
    }
}
