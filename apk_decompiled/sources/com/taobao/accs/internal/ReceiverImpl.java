package com.taobao.accs.internal;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.accs.base.IBaseReceiver;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.UtilityImpl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class ReceiverImpl implements IBaseReceiver {
    @Override // com.taobao.accs.base.IBaseReceiver
    public void onReceive(Context context, Intent intent) {
        ALog.d("ReceiverImpl", "ReceiverImpl onReceive begin......", new Object[0]);
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            intent = new Intent();
        } else if (!a(intent)) {
            return;
        }
        if (UtilityImpl.b(context, true)) {
            return;
        }
        try {
            intent.setClassName(context.getPackageName(), AdapterUtilityImpl.channelService);
            IntentDispatch.dispatchIntent(context.getApplicationContext(), intent, AdapterUtilityImpl.channelService);
            if (UtilityImpl.d(context)) {
                String agooCustomServiceName = AdapterGlobalClientInfo.getAgooCustomServiceName(context);
                intent.setClassName(context, agooCustomServiceName);
                IntentDispatch.dispatchIntent(context.getApplicationContext(), intent, agooCustomServiceName);
            }
        } catch (Throwable th) {
            ALog.e("ReceiverImpl", "ReceiverImpl onReceive,exception,e=" + th.getMessage(), new Object[0]);
        }
    }

    private boolean a(Intent intent) {
        String action = intent.getAction();
        return action.equals("android.intent.action.USER_PRESENT") || action.equals("android.intent.action.BOOT_COMPLETED") || action.equals("android.intent.action.PACKAGE_REMOVED") || action.equals("android.net.conn.CONNECTIVITY_CHANGE");
    }
}
