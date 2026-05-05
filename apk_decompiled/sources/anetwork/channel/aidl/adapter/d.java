package anetwork.channel.aidl.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Looper;
import anet.channel.util.ALog;
import anetwork.channel.aidl.IRemoteNetworkGetter;
import anetwork.channel.aidl.NetworkService;
import anetwork.channel.config.NetworkConfigCenter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static volatile IRemoteNetworkGetter f1986a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static volatile boolean f1987b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static volatile boolean f1988c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static volatile CountDownLatch f1989d;
    static Handler e = new Handler(Looper.getMainLooper());
    private static ServiceConnection f = new e();

    public static void a(Context context, boolean z) {
        if (f1986a == null && !f1987b) {
            a(context);
            if (f1987b || !z) {
                return;
            }
            try {
                synchronized (d.class) {
                    if (f1986a != null) {
                        return;
                    }
                    if (f1989d == null) {
                        f1989d = new CountDownLatch(1);
                    }
                    ALog.i("anet.RemoteGetter", "[initRemoteGetterAndWait]begin to wait", null, new Object[0]);
                    if (f1989d.await(NetworkConfigCenter.getServiceBindWaitTime(), TimeUnit.SECONDS)) {
                        ALog.i("anet.RemoteGetter", "mServiceBindLock count down to 0", null, new Object[0]);
                    } else {
                        ALog.i("anet.RemoteGetter", "mServiceBindLock wait timeout", null, new Object[0]);
                    }
                }
            } catch (InterruptedException unused) {
                ALog.e("anet.RemoteGetter", "mServiceBindLock wait interrupt", null, new Object[0]);
            }
        }
    }

    public static IRemoteNetworkGetter a() {
        return f1986a;
    }

    private static void a(Context context) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.RemoteGetter", "[asyncBindService] mContext:" + context + " bBindFailed:" + f1987b + " bBinding:" + f1988c, null, new Object[0]);
        }
        if (context == null || f1987b || f1988c) {
            return;
        }
        f1988c = true;
        Intent intent = new Intent(context, (Class<?>) NetworkService.class);
        intent.setAction(IRemoteNetworkGetter.class.getName());
        intent.addCategory("android.intent.category.DEFAULT");
        f1987b = !context.bindService(intent, f, 1);
        if (f1987b) {
            f1988c = false;
            ALog.e("anet.RemoteGetter", "[asyncBindService]ANet_Service start not success. ANet run with local mode!", null, new Object[0]);
        }
        e.postDelayed(new f(), 10000L);
    }
}
