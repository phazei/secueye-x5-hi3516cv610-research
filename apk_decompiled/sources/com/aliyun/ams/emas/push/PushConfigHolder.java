package com.aliyun.ams.emas.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.alibaba.sdk.android.logger.ILog;
import com.aliyun.ams.emas.push.notification.CPushMessage;
import com.taobao.accs.utl.AccsLogger;
import java.util.Random;

/* JADX INFO: loaded from: classes2.dex */
public class PushConfigHolder {
    static final int REQUEST_CODE_RANGE = 1000000;
    public static final ILog importantLogger = AccsLogger.getLogger("[MPS]");
    public static String SERVICE_CONTAINER_ACTION = "com.alibaba.sdk.android.push.NOTIFY_ACTION";
    private static Class intentService = null;
    private static CloudPushServiceHelper helper = null;
    private static IReportPushArrive reportPushArrive = null;
    private static int sNotificationIntentRequestCode = 0;
    private static int sNotificationId = 0;
    private static SharedPreferences prefs = null;
    private static Random randomGen = null;

    public static void init(Context context) {
        helper = new CloudPushServiceHelper(context.getApplicationContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static void setReportPushArrive(IReportPushArrive iReportPushArrive) {
        reportPushArrive = iReportPushArrive;
    }

    public static void setMessageIntentService(Class cls) {
        intentService = cls;
    }

    public static Class getCustomMessageIntentService() {
        return intentService;
    }

    public static void setDoNotDisturbMode(boolean z) {
        helper.setDoNotDisturbMode(z);
    }

    public static void setDoNotDisturb(int i, int i2, int i3, int i4, CommonCallback commonCallback) {
        helper.setDoNotDisturb(i, i2, i3, i4, commonCallback);
    }

    public static boolean isInDoNotDisturbTimeWindow() {
        return helper.isInDoNotDisturbTimeWindow();
    }

    public static void reportPushArrive(Context context, String str, int i) {
        IReportPushArrive iReportPushArrive = reportPushArrive;
        if (iReportPushArrive != null) {
            iReportPushArrive.reportPushArrive(context, str, i);
        }
    }

    public static int createNotificationId() {
        if (sNotificationId == 0) {
            if (randomGen == null) {
                randomGen = new Random(System.currentTimeMillis());
            }
            sNotificationId = randomGen.nextInt(REQUEST_CODE_RANGE);
            int i = sNotificationId;
            if (i < 0) {
                sNotificationId = i * (-1);
            }
        }
        int i2 = sNotificationId;
        sNotificationId = i2 + 1;
        return i2;
    }

    public static int getNotificationIntentRequestCode() {
        if (sNotificationIntentRequestCode == 0) {
            if (randomGen == null) {
                randomGen = new Random(System.currentTimeMillis());
            }
            sNotificationIntentRequestCode = randomGen.nextInt(REQUEST_CODE_RANGE);
            int i = sNotificationIntentRequestCode;
            if (i < 0) {
                sNotificationIntentRequestCode = i * (-1);
            }
        }
        int i2 = sNotificationIntentRequestCode;
        sNotificationIntentRequestCode = i2 + 1;
        return i2;
    }

    public static void clickMessage(CPushMessage cPushMessage) {
        helper.clickMessage(cPushMessage);
    }

    public static void dismissMessage(CPushMessage cPushMessage) {
        helper.dismissMessage(cPushMessage);
    }
}
