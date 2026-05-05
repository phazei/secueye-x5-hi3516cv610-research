package com.taobao.accs.data;

import android.content.Intent;
import android.text.TextUtils;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class i implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Intent f6323a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ MsgDistributeService f6324b;

    i(MsgDistributeService msgDistributeService, Intent intent) {
        this.f6324b = msgDistributeService;
        this.f6323a = intent;
    }

    @Override // java.lang.Runnable
    public void run() {
        ALog.i("MsgDistributeService", "onStartCommand send message", new Object[0]);
        ACCSManager.AccsRequest accsRequest = (ACCSManager.AccsRequest) this.f6323a.getSerializableExtra(Constants.KEY_SEND_REQDATA);
        String stringExtra = this.f6323a.getStringExtra(Constants.KEY_PACKAGE_NAME);
        String stringExtra2 = this.f6323a.getStringExtra("appKey");
        String stringExtra3 = this.f6323a.getStringExtra(Constants.KEY_CONFIG_TAG);
        if (TextUtils.isEmpty(stringExtra3)) {
            stringExtra3 = stringExtra2;
        }
        ACCSManager.getAccsInstance(this.f6324b.getApplicationContext(), stringExtra2, stringExtra3).sendRequest(this.f6324b.getApplicationContext(), accsRequest, stringExtra, true);
    }
}
