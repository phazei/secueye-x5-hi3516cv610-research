package anet.channel.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.thread.ThreadPoolExecutorFactory;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.concurrent.CopyOnWriteArraySet;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class AppLifecycle {
    private static final String TAG = "awcn.AppLifeCycle";
    private static CopyOnWriteArraySet<AppLifecycleListener> listeners = new CopyOnWriteArraySet<>();
    public static volatile long lastEnterBackgroundTime = 0;
    public static volatile boolean isGoingForeground = false;
    private static Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() { // from class: anet.channel.util.AppLifecycle.2
        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity2, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity2) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity2, Bundle bundle) {
            if (GlobalAppRuntimeInfo.isAppBackground()) {
                AppLifecycle.isGoingForeground = true;
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity2) {
            if (GlobalAppRuntimeInfo.isAppBackground()) {
                AppLifecycle.isGoingForeground = true;
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity2) {
            AppLifecycle.onForeground();
        }
    };
    private static ComponentCallbacks2 mComponentCallbacks2 = new ComponentCallbacks2() { // from class: anet.channel.util.AppLifecycle.3
        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }

        @Override // android.content.ComponentCallbacks2
        public void onTrimMemory(int i) {
            ALog.i(AppLifecycle.TAG, "onTrimMemory", null, FirebaseAnalytics.Param.LEVEL, Integer.valueOf(i));
            if (i == 20) {
                AppLifecycle.onBackground();
            }
        }
    };

    /* JADX INFO: compiled from: Taobao */
    public interface AppLifecycleListener {
        void background();

        void forground();
    }

    private AppLifecycle() {
    }

    public static void initialize() {
        if (Build.VERSION.SDK_INT < 14 || !AwcnConfig.isAppLifeCycleListenerEnable()) {
            return;
        }
        ((Application) GlobalAppRuntimeInfo.getContext().getApplicationContext()).registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        GlobalAppRuntimeInfo.getContext().registerComponentCallbacks(mComponentCallbacks2);
    }

    public static void registerLifecycleListener(AppLifecycleListener appLifecycleListener) {
        if (appLifecycleListener != null) {
            listeners.add(appLifecycleListener);
        }
    }

    public static void unregisterLifecycleListener(AppLifecycleListener appLifecycleListener) {
        listeners.remove(appLifecycleListener);
    }

    public static void onForeground() {
        if (GlobalAppRuntimeInfo.isAppBackground()) {
            GlobalAppRuntimeInfo.setBackground(false);
            isGoingForeground = false;
            notifyListener(true);
        }
    }

    public static void onBackground() {
        if (GlobalAppRuntimeInfo.isAppBackground()) {
            return;
        }
        GlobalAppRuntimeInfo.setBackground(true);
        lastEnterBackgroundTime = System.currentTimeMillis();
        notifyListener(false);
    }

    private static void notifyListener(final boolean z) {
        ALog.i(TAG, "notifyListener", null, DownloadService.KEY_FOREGROUND, Boolean.valueOf(z));
        ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() { // from class: anet.channel.util.AppLifecycle.1
            @Override // java.lang.Runnable
            public void run() {
                for (AppLifecycleListener appLifecycleListener : AppLifecycle.listeners) {
                    try {
                        if (z) {
                            appLifecycleListener.forground();
                        } else {
                            appLifecycleListener.background();
                        }
                    } catch (Exception e) {
                        ALog.e(AppLifecycle.TAG, "notifyListener exception.", null, e, new Object[0]);
                    }
                }
            }
        });
    }
}
