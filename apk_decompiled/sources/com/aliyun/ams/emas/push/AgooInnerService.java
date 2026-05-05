package com.aliyun.ams.emas.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.TaobaoBaseIntentService;

/* JADX INFO: loaded from: classes2.dex */
public class AgooInnerService extends TaobaoBaseIntentService {
    public static final String AGOO_RECEIVE_ACTION = "com.alibaba.sdk.android.push.RECEIVE";
    private static final String TAG = "AgooInnerService";

    private static final String printBundle(Bundle bundle, int i) {
        StringBuilder sb = new StringBuilder();
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            for (int i2 = 0; i2 < i; i2++) {
                sb.append('\t');
            }
            if (obj instanceof String) {
                sb.append("String\t");
                sb.append(str);
                sb.append('\t');
                sb.append(obj);
                sb.append('\n');
            } else if (obj instanceof Integer) {
                sb.append("int\t");
                sb.append(str);
                sb.append('\t');
                sb.append(obj);
                sb.append('\n');
            } else if (obj instanceof Long) {
                sb.append("long\t");
                sb.append(str);
                sb.append('\t');
                sb.append(obj);
                sb.append('\n');
            } else if (obj instanceof Boolean) {
                sb.append("boolean\t");
                sb.append(str);
                sb.append('\t');
                sb.append(obj);
                sb.append('\n');
            } else if (obj instanceof Bundle) {
                sb.append("Bundle\t");
                sb.append(str);
                sb.append('\t');
                sb.append('\n');
                sb.append(printBundle((Bundle) obj, i + 1));
            } else {
                sb.append("unknown\t");
                sb.append(str);
                sb.append('\t');
                sb.append(obj);
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService, org.android.agoo.control.BaseIntentService
    protected void onMessage(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("body");
        ALog.i(TAG, "onMessage:id:" + intent.getStringExtra("id") + ", task_id:" + intent.getStringExtra("task_id") + ", body:" + stringExtra, new Object[0]);
        ALog.i(TAG, printBundle(intent.getExtras(), 1), new Object[0]);
        Intent intent2 = new Intent();
        intent2.putExtras(intent.getExtras());
        intent2.setAction(AGOO_RECEIVE_ACTION);
        intent2.setPackage(context.getPackageName());
        try {
            Class<?> customMessageIntentService = PushConfigHolder.getCustomMessageIntentService();
            if (customMessageIntentService == null) {
                ALog.d(TAG, "Send broadcast", new Object[0]);
                context.sendBroadcast(intent2, context.getPackageName() + ".AGOO");
            } else {
                ALog.d(TAG, "Start service:" + customMessageIntentService.getName(), new Object[0]);
                intent2.setClass(context, customMessageIntentService);
                IntentDispatch.dispatchIntent(context, intent2, customMessageIntentService.getName());
            }
        } catch (Throwable th) {
            ALog.e(TAG, "Send message failed.", th, new Object[0]);
        }
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService, org.android.agoo.control.BaseIntentService
    protected void onError(Context context, String str) {
        ALog.i(TAG, "onError:" + str, new Object[0]);
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService, org.android.agoo.control.BaseIntentService
    protected void onRegistered(Context context, String str) {
        ALog.i(TAG, "onRegistered:" + str, new Object[0]);
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService
    protected void onUnregistered(Context context, String str) {
        ALog.i(TAG, "onUnregistered:" + str, new Object[0]);
    }
}
