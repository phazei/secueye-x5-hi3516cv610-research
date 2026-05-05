package com.taobao.accs.client;

import com.taobao.accs.utl.UtilityImpl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ GlobalClientInfo f6295a;

    c(GlobalClientInfo globalClientInfo) {
        this.f6295a = globalClientInfo;
    }

    @Override // java.lang.Runnable
    public void run() {
        GlobalClientInfo.f6290b = UtilityImpl.j(GlobalClientInfo.f6289a);
    }
}
