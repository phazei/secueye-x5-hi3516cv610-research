package com.taobao.accs.net;

import android.text.TextUtils;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class q implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ j f6402a;

    q(j jVar) {
        this.f6402a = jVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.f6402a.f6366d == null || TextUtils.isEmpty(this.f6402a.i())) {
                return;
            }
            this.f6402a.t.i("mTryStartServiceRunable bindapp");
            this.f6402a.b(this.f6402a.f6366d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
