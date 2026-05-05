package com.aliyun.ams.emas.push.notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import com.alibaba.sdk.android.logger.ILog;
import com.aliyun.ams.emas.push.AgooMessageReceiver;
import com.aliyun.ams.emas.push.PushConfigHolder;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.TaobaoRegister;
import java.util.Iterator;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes2.dex */
public class CPushServiceListener {
    private static final String TAG = "MPS:CPushServiceListener";
    public static final int TYPE_FROM_ACTIVITY = 1;
    public static final int TYPE_FROM_SERVICE = 0;

    public int onStartCommandFrom(Intent intent, Context context, int i) {
        ILog iLog;
        StringBuilder sb;
        ILog iLog2;
        StringBuilder sb2;
        boolean z;
        boolean z2;
        if (intent == null) {
            ALog.w(TAG, "intent is null, return.", new Object[0]);
            return 0;
        }
        String packageName = intent.getComponent().getPackageName();
        String packageName2 = getPackageName(context);
        ALog.i(TAG, "from package name = " + packageName + ", current package name = " + packageName2, new Object[0]);
        if (TextUtils.isEmpty(packageName2) || !packageName2.equals(packageName)) {
            ALog.w(TAG, "package name not equal, return.", new Object[0]);
            return 0;
        }
        Class<?> customMessageIntentService = PushConfigHolder.getCustomMessageIntentService();
        if (AgooConstants.NOTIFICATION_TYPE_OPEN.equals(intent.getStringExtra("action_type"))) {
            Intent intent2 = (Intent) intent.getExtras().get(AgooConstants.KEY_REAL_INTENT);
            intent2.setFlags(335544320);
            String stringExtra = intent2.getStringExtra("msgId");
            String stringExtra2 = intent2.getStringExtra("title");
            String stringExtra3 = intent2.getStringExtra("summary");
            String stringExtra4 = intent.getStringExtra("extData");
            String stringExtra5 = intent.getStringExtra("group");
            int intExtra = intent2.getIntExtra("notificationOpenType", 1);
            int intExtra2 = intent2.getIntExtra("notificationId", 0);
            String stringExtra6 = intent2.getStringExtra("extraMap");
            try {
                try {
                    Intent intent3 = new Intent();
                    intent3.setPackage(context.getPackageName());
                    intent3.setAction(AgooMessageReceiver.NOTIFICATION_OPENED_ACTION);
                    intent3.putExtra("messageId", stringExtra);
                    intent3.putExtra("title", stringExtra2);
                    intent3.putExtra("summary", stringExtra3);
                    intent3.putExtra("extraMap", stringExtra6);
                    intent3.putExtra("notificationOpenType", intExtra);
                    intent3.putExtra("notificationId", intExtra2);
                    if (!TextUtils.isEmpty(stringExtra5)) {
                        intent3.putExtra("group", stringExtra5);
                    }
                    if (Build.VERSION.SDK_INT >= 12) {
                        intent3.setFlags(32);
                    }
                    if (customMessageIntentService == null) {
                        context.sendBroadcast(intent3, context.getPackageName() + ".AGOO");
                    } else {
                        intent3.setClass(context, customMessageIntentService);
                        IntentDispatch.dispatchIntent(context, intent3, customMessageIntentService.getName());
                    }
                    if (i == 0 && "android.intent.action.MAIN".equals(intent2.getAction()) && NotificationUtil.isApplicationForeground(context)) {
                        PushConfigHolder.importantLogger.d("app is in front, action:" + intent2.getAction());
                    } else if (intExtra == 4) {
                        ALog.i(TAG, "open with no action", new Object[0]);
                    } else if (intExtra == 1) {
                        ALog.i(TAG, "open app", new Object[0]);
                        if (Build.VERSION.SDK_INT >= 11 && i == 0) {
                            if (ContextCompat.checkSelfPermission(context, "android.permission.GET_TASKS") == 0 && ContextCompat.checkSelfPermission(context, "android.permission.REORDER_TASKS") == 0) {
                                ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
                                Iterator<ActivityManager.RunningTaskInfo> it = activityManager.getRunningTasks(Integer.MAX_VALUE).iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        z = true;
                                        z2 = true;
                                        break;
                                    }
                                    ActivityManager.RunningTaskInfo next = it.next();
                                    if (next.topActivity.getPackageName().equals(context.getPackageName())) {
                                        ALog.d(TAG, "move task to front", new Object[0]);
                                        try {
                                            activityManager.moveTaskToFront(next.id, 0);
                                            z = false;
                                            z2 = true;
                                            break;
                                        } catch (Throwable th) {
                                            ALog.w(TAG, "move task to front fail", th, new Object[0]);
                                        }
                                    }
                                }
                                if (z2 == z) {
                                    ALog.w(TAG, "do not find corresponing running task, start app with launch activity", new Object[0]);
                                    context.startActivity(intent2);
                                }
                            } else {
                                ALog.d(TAG, "no get tasks and reorder tasks permission, start app with launch activity", new Object[0]);
                                context.startActivity(intent2);
                            }
                        } else {
                            ALog.w(TAG, "sdk version < 11 or start from activity, start app with launch activity", new Object[0]);
                            context.startActivity(intent2);
                        }
                    } else {
                        if (intExtra == 2) {
                            ALog.d(TAG, "open activity", new Object[0]);
                        } else if (intExtra == 3) {
                            ALog.d(TAG, "open url", new Object[0]);
                        }
                        context.startActivity(intent2);
                    }
                    iLog2 = PushConfigHolder.importantLogger;
                    sb2 = new StringBuilder();
                } catch (Throwable th2) {
                    PushConfigHolder.importantLogger.i("Open msg(" + stringExtra + ")");
                    TaobaoRegister.clickMessage(context, stringExtra, stringExtra4);
                    throw th2;
                }
            } catch (Throwable th3) {
                ALog.e(TAG, "startActivity error", th3, new Object[0]);
                iLog2 = PushConfigHolder.importantLogger;
                sb2 = new StringBuilder();
            }
            sb2.append("Open msg(");
            sb2.append(stringExtra);
            sb2.append(")");
            iLog2.i(sb2.toString());
            TaobaoRegister.clickMessage(context, stringExtra, stringExtra4);
            return 0;
        }
        if (AgooConstants.NOTIFICATION_TYPE_DELETE.equals(intent.getStringExtra("action_type"))) {
            String stringExtra7 = intent.getStringExtra("msgId");
            intent.getStringExtra("task_id");
            String stringExtra8 = intent.getStringExtra("extData");
            try {
                try {
                    Intent intent4 = new Intent();
                    intent4.setPackage(context.getPackageName());
                    intent4.setAction(AgooMessageReceiver.NOTIFICATION_REMOVED_ACTION);
                    intent4.putExtra("messageId", stringExtra7);
                    intent4.putExtra("title", intent.getStringExtra("title"));
                    intent4.putExtra("summary", intent.getStringExtra("summary"));
                    intent4.putExtra("extraMap", intent.getStringExtra("extraMap"));
                    intent4.putExtra("notificationOpenType", intent.getIntExtra("notificationOpenType", 1));
                    intent4.putExtra("group", intent.getStringExtra("group"));
                    if (Build.VERSION.SDK_INT >= 12) {
                        intent4.setFlags(32);
                    }
                    if (customMessageIntentService == null) {
                        context.sendBroadcast(intent4, context.getPackageName() + ".AGOO");
                    } else {
                        intent4.setClass(context, customMessageIntentService);
                        IntentDispatch.dispatchIntent(context, intent4, customMessageIntentService.getName());
                    }
                    iLog = PushConfigHolder.importantLogger;
                    sb = new StringBuilder();
                } catch (Throwable th4) {
                    ALog.e(TAG, "send intent failed.", th4, new Object[0]);
                    iLog = PushConfigHolder.importantLogger;
                    sb = new StringBuilder();
                }
                sb.append("Delete msg(");
                sb.append(stringExtra7);
                sb.append(")");
                iLog.i(sb.toString());
                TaobaoRegister.dismissMessage(context, stringExtra7, stringExtra8);
                return 0;
            } catch (Throwable th5) {
                PushConfigHolder.importantLogger.i("Delete msg(" + stringExtra7 + ")");
                TaobaoRegister.dismissMessage(context, stringExtra7, stringExtra8);
                throw th5;
            }
        }
        if (AgooConstants.MESSAGE_TYPE_OPEN.equals(intent.getStringExtra("action_type"))) {
            TaobaoRegister.clickMessage(context, intent.getStringExtra("msgId"), intent.getStringExtra("extData"));
        } else if (AgooConstants.MESSAGE_TYPE_DELETE.equals(intent.getStringExtra("action_type"))) {
            TaobaoRegister.dismissMessage(context, intent.getStringExtra("msgId"), intent.getStringExtra("extData"));
            return 0;
        }
        return 0;
    }

    private String getPackageName(Context context) {
        if (context == null || context.getApplicationContext() == null || TextUtils.isEmpty(context.getApplicationContext().getPackageName())) {
            return null;
        }
        return context.getApplicationContext().getPackageName();
    }

    @SuppressLint({"MissingPermission"})
    public int onStartCommand(Intent intent, Context context) {
        return onStartCommandFrom(intent, context, 0);
    }
}
