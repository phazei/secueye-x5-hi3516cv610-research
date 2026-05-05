package com.taobao.accs.net;

import android.content.Intent;
import com.taobao.accs.common.Constants;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.AdapterUtilityImpl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class v implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ u f6407a;

    v(u uVar) {
        this.f6407a = uVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        String packageName = this.f6407a.f6376a.getPackageName();
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.setAction(Constants.ACTION_COMMAND);
        intent.putExtra("command", 201);
        intent.setClassName(packageName, AdapterUtilityImpl.channelService);
        IntentDispatch.dispatchIntent(this.f6407a.f6376a.getApplicationContext(), intent, AdapterUtilityImpl.channelService);
    }
}
