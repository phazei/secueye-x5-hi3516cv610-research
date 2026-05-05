package com.alibaba.sdk.android.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.sdk.android.ams.common.b.b;
import com.alibaba.sdk.android.ams.common.b.c;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.push.report.ReportManager;
import com.aliyun.ams.emas.push.AgooInnerService;
import com.taobao.agoo.TaobaoBaseIntentService;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class AliyunPushIntentService extends TaobaoBaseIntentService {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final AmsLogger f2930a = AmsLogger.getLogger("MPS:AliyunPushIntentService");

    private void a(String str, String str2) {
        ReportManager reportManager = ReportManager.getInstance(getApplicationContext());
        b bVarA = c.a();
        if (reportManager == null || bVarA == null) {
            return;
        }
        reportManager.reportAppTransfer(bVarA.b(), str, str2);
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService, org.android.agoo.control.BaseIntentService
    protected void onError(Context context, String str) {
        f2930a.i("onError:" + str);
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService, org.android.agoo.control.BaseIntentService
    protected void onMessage(Context context, Intent intent) {
        f2930a.d("onMessage");
        com.alibaba.sdk.android.ams.common.a.b.a(context);
        String stringExtra = intent.getStringExtra("body");
        String stringExtra2 = intent.getStringExtra("id");
        String stringExtra3 = intent.getStringExtra("task_id");
        String stringExtra4 = intent.getStringExtra(AgooConstants.MESSAGE_FROM_APPKEY);
        if (!TextUtils.isEmpty(stringExtra4) && !TextUtils.equals(stringExtra4, c.a().a())) {
            a(stringExtra2, stringExtra4);
            f2930a.d("receive msg from other app:" + stringExtra4);
        }
        f2930a.i("onMessage:id:" + stringExtra2 + ", task_id:" + stringExtra3 + ", body:" + stringExtra);
        Intent intent2 = new Intent();
        intent2.putExtras(intent.getExtras());
        intent2.setAction(AgooInnerService.AGOO_RECEIVE_ACTION);
        intent2.setPackage(context.getPackageName());
        try {
            Class<?> clsD = com.alibaba.sdk.android.push.common.a.b.d();
            if (clsD == null) {
                f2930a.d("Send broadcast");
                context.sendBroadcast(intent2);
            } else {
                f2930a.d("Start service:" + clsD.getName());
                intent2.setClass(context, clsD);
                context.startService(intent2);
            }
        } catch (Throwable th) {
            f2930a.e("Send message failed.", th);
        }
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService, org.android.agoo.control.BaseIntentService
    protected void onRegistered(Context context, String str) {
        f2930a.i("onRegistered:" + str);
    }

    @Override // com.taobao.agoo.TaobaoBaseIntentService
    protected void onUnregistered(Context context, String str) {
        f2930a.i("onUnregistered:" + str);
    }
}
