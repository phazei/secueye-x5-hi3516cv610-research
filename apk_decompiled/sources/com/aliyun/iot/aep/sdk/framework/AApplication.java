package com.aliyun.iot.aep.sdk.framework;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.multidex.MultiDexApplication;
import com.aliyun.iot.aep.sdk.abus.ABus;
import com.aliyun.iot.aep.sdk.abus.AChannelIDProvider;
import com.aliyun.iot.aep.sdk.abus.IBus;
import com.aliyun.iot.aep.sdk.abus.IChannelIDProvider;
import com.aliyun.iot.aep.sdk.framework.language.LanguageManager;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AApplication extends MultiDexApplication {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AApplication f4666a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private IBus f4667b = new ABus();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private IChannelIDProvider f4668c = new AChannelIDProvider();

    protected void a(Activity activity2) {
    }

    protected void a(Activity activity2, Bundle bundle) {
    }

    protected void b(Activity activity2) {
    }

    protected void c(Activity activity2) {
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new a());
        f4666a = this;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        Resources resources = super.getResources();
        LanguageManager.updateApplicationLanguage(resources);
        return resources;
    }

    public IBus getBus() {
        return this.f4667b;
    }

    public int generateChannelID() {
        return this.f4668c.generateChannelID();
    }

    public static AApplication getInstance() {
        return f4666a;
    }

    static final class a implements Application.ActivityLifecycleCallbacks {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final AApplication f4669a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private volatile int f4670b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private volatile int f4671c;

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity2, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity2) {
        }

        private a(AApplication aApplication) {
            this.f4670b = 0;
            this.f4671c = 0;
            this.f4669a = aApplication;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity2, Bundle bundle) {
            int i = this.f4670b + 1;
            this.f4670b = i;
            if (1 == i) {
                this.f4669a.a(activity2, bundle);
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity2) {
            int i = this.f4671c + 1;
            this.f4671c = i;
            if (1 == i) {
                this.f4669a.b(activity2);
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity2) {
            int i = this.f4671c + 1;
            this.f4671c = i;
            if (1 == i) {
                this.f4669a.c(activity2);
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity2) {
            int i = this.f4670b - 1;
            this.f4670b = i;
            if (i == 0) {
                this.f4669a.a(activity2);
            }
        }
    }
}
