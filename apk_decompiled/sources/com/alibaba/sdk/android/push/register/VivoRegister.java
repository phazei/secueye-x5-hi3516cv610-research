package com.alibaba.sdk.android.push.register;

import android.content.Context;
import com.alibaba.sdk.android.push.impl.VivoMsgParseImpl;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.alibaba.sdk.android.push.utils.SysUtils;
import com.alibaba.sdk.android.push.utils.ThreadUtil;
import com.taobao.accs.utl.ALog;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public class VivoRegister {
    public static final String TAG = "MPS:vPush";
    private static Context mContext;

    public static void registerAsync(final Context context) {
        ThreadUtil.getExecutor().submit(new Callable<Boolean>() { // from class: com.alibaba.sdk.android.push.register.VivoRegister.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                return Boolean.valueOf(VivoRegister.register(context));
            }
        });
    }

    public static boolean register(final Context context) {
        if (context == null) {
            return false;
        }
        try {
            mContext = context.getApplicationContext();
        } catch (Throwable th) {
            ALog.e(TAG, "register", th, new Object[0]);
        }
        if (!SysUtils.isTargetProcess(context)) {
            ALog.i(TAG, "not in target process, return", new Object[0]);
            return false;
        }
        if (PushClient.getInstance(context).isSupport()) {
            ALog.i(TAG, "register start", new Object[0]);
            VivoMsgParseImpl vivoMsgParseImpl = new VivoMsgParseImpl();
            vivoMsgParseImpl.setContext(mContext);
            ThirdPushManager.registerImpl(vivoMsgParseImpl);
            PushClient.getInstance(context).initialize();
            PushClient.getInstance(context).turnOnPush(new IPushActionListener() { // from class: com.alibaba.sdk.android.push.register.VivoRegister.2
                @Override // com.vivo.push.IPushActionListener
                public void onStateChanged(int i) {
                    ALog.i(VivoRegister.TAG, "oPushRegister turnOnPush", "state", Integer.valueOf(i));
                    String regId = "";
                    if (i == 0 || i == 1) {
                        regId = PushClient.getInstance(context).getRegId();
                        ALog.i(VivoRegister.TAG, "onReceiveRegId regId:" + regId, new Object[0]);
                    } else {
                        ALog.e(VivoRegister.TAG, "onReceiveRegId: " + i, new Object[0]);
                    }
                    ThirdPushManager.reportToken(context, ThirdPushManager.ThirdPushReportKeyword.VIVO.thirdTokenKeyword, regId, "3.0.0.4");
                }
            });
            return true;
        }
        ALog.i(TAG, "this device is not support vPush", new Object[0]);
        return false;
    }

    public static void unregister() {
        ALog.i(TAG, MiPushClient.COMMAND_UNREGISTER, new Object[0]);
        PushClient.getInstance(mContext).turnOffPush(new IPushActionListener() { // from class: com.alibaba.sdk.android.push.register.VivoRegister.3
            @Override // com.vivo.push.IPushActionListener
            public void onStateChanged(int i) {
                ALog.d(VivoRegister.TAG, "turnOffPush state:" + i, new Object[0]);
            }
        });
    }
}
