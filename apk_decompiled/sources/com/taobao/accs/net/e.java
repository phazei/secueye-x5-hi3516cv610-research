package com.taobao.accs.net;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.UtilityImpl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Context f6372a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ b f6373b;

    e(b bVar, Context context) {
        this.f6373b = bVar;
        this.f6372a = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (UtilityImpl.l(this.f6372a)) {
            ALog.d(this.f6373b.d(), "startChannelService", new Object[0]);
            Intent intent = new Intent(Constants.ACTION_START_SERVICE);
            intent.putExtra("appKey", this.f6373b.i());
            intent.putExtra(Constants.KEY_TTID, this.f6373b.f6363a);
            intent.putExtra(Constants.KEY_PACKAGE_NAME, this.f6372a.getPackageName());
            intent.putExtra("app_sercet", this.f6373b.i.getAppSecret());
            intent.putExtra("mode", AccsClientConfig.mEnv);
            intent.putExtra(Constants.KEY_CONFIG_TAG, this.f6373b.m);
            intent.setClassName(this.f6372a.getPackageName(), AdapterUtilityImpl.channelService);
            IntentDispatch.dispatchIntent(this.f6372a, intent, AdapterUtilityImpl.channelService);
            Intent intent2 = new Intent();
            intent2.setAction("org.agoo.android.intent.action.REPORT");
            intent2.setPackage(this.f6372a.getPackageName());
            String agooCustomServiceName = AdapterGlobalClientInfo.getAgooCustomServiceName(this.f6372a);
            if (TextUtils.isEmpty(agooCustomServiceName)) {
                return;
            }
            intent2.setClassName(this.f6372a.getPackageName(), agooCustomServiceName);
            IntentDispatch.dispatchIntent(this.f6372a, intent2, agooCustomServiceName);
        }
    }
}
