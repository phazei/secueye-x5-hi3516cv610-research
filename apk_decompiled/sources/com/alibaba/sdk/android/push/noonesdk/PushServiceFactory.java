package com.alibaba.sdk.android.push.noonesdk;

import android.app.Application;
import android.content.Context;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.PushControlService;
import com.alibaba.sdk.android.push.b.b;
import com.alibaba.sdk.android.push.b.e;
import com.alibaba.sdk.android.push.f.a;
import com.taobao.accs.ACCSClient;

/* JADX INFO: loaded from: classes.dex */
public class PushServiceFactory {
    public static CloudPushService getCloudPushService() {
        return b.a();
    }

    public static PushControlService getPushControlService() {
        return e.a();
    }

    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        if (applicationContext instanceof Application) {
            com.alibaba.sdk.android.ams.common.a.b.a((Application) applicationContext);
        }
        com.alibaba.sdk.android.ams.common.a.b.a(applicationContext);
        b bVarA = b.a();
        bVarA.a(applicationContext);
        bVarA.setPushIntentService(null);
    }

    @Deprecated
    public static void init(Context context, String str, String str2) {
        Context applicationContext = context.getApplicationContext();
        if (applicationContext instanceof Application) {
            com.alibaba.sdk.android.ams.common.a.b.a((Application) applicationContext);
        }
        com.alibaba.sdk.android.ams.common.a.b.a(applicationContext);
        b bVarA = b.a();
        bVarA.a(applicationContext);
        bVarA.setAppkey(str);
        bVarA.setAppSecret(str2);
        bVarA.setPushIntentService(null);
    }

    @Deprecated
    public static void init(Context context, String str, String str2, boolean z) {
        ACCSClient.enableChannelProcess(context, z);
        init(context, str, str2);
    }

    @Deprecated
    public static void init(Context context, boolean z) {
        ACCSClient.enableChannelProcess(context, z);
        init(context);
    }

    public static void init(PushInitConfig pushInitConfig) {
        com.alibaba.sdk.android.ams.common.a.b.a(pushInitConfig.getApplication());
        com.alibaba.sdk.android.ams.common.a.b.a(pushInitConfig.getApplication().getApplicationContext());
        com.alibaba.sdk.android.ams.common.a.b.a(pushInitConfig.isDisableForegroundCheck());
        com.alibaba.sdk.android.ams.common.a.b.a(pushInitConfig.getPushHost());
        com.alibaba.sdk.android.ams.common.a.b.b(pushInitConfig.getAccsAppConnectHost());
        com.alibaba.sdk.android.ams.common.a.b.c(pushInitConfig.getAccsSilentConnectHost());
        com.alibaba.sdk.android.ams.common.a.b.d(pushInitConfig.getAmdcHost());
        b bVarA = b.a();
        bVarA.a(pushInitConfig.getApplication().getApplicationContext());
        bVarA.setPushIntentService(null);
        if (pushInitConfig.getAppKey() != null) {
            bVarA.setAppkey(pushInitConfig.getAppKey());
        }
        if (pushInitConfig.getAppSecret() != null) {
            bVarA.setAppSecret(pushInitConfig.getAppSecret());
        }
        if (pushInitConfig.getLargeIconDownloadListener() != null) {
            bVarA.setLargeIconDownloadListener(pushInitConfig.getLargeIconDownloadListener());
        }
        ACCSClient.enableChannelProcess(pushInitConfig.getApplication(), !pushInitConfig.isDisableChannelProcess());
        ACCSClient.enableChannelProcessHeartbeat(pushInitConfig.getApplication(), !pushInitConfig.isDisableChannelProcessHeartbeat());
        a.a().a(pushInitConfig.isLoopStartChannel(), pushInitConfig.getLoopInterval());
    }
}
