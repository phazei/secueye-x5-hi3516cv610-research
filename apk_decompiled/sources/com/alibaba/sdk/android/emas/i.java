package com.alibaba.sdk.android.emas;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: ForBackgroundCallback.java */
/* JADX INFO: loaded from: classes.dex */
class i implements Application.ActivityLifecycleCallbacks {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private List<a> f2891c;
    private int e = 0;

    /* JADX INFO: renamed from: c, reason: collision with other field name */
    private boolean f14c = false;

    /* JADX INFO: compiled from: ForBackgroundCallback.java */
    public interface a {
        void c();

        void d();
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity2, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity2) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity2) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity2) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity2, Bundle bundle) {
    }

    i() {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity2) {
        this.e++;
        if (this.f14c) {
            return;
        }
        this.f14c = true;
        List<a> list = this.f2891c;
        if (list != null) {
            Iterator<a> it = list.iterator();
            while (it.hasNext()) {
                it.next().c();
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity2) {
        this.e--;
        if (this.e == 0) {
            this.f14c = false;
            List<a> list = this.f2891c;
            if (list != null) {
                Iterator<a> it = list.iterator();
                while (it.hasNext()) {
                    it.next().d();
                }
            }
        }
    }

    public void a(a aVar) {
        if (this.f2891c == null) {
            this.f2891c = new ArrayList();
        }
        this.f2891c.add(aVar);
    }
}
