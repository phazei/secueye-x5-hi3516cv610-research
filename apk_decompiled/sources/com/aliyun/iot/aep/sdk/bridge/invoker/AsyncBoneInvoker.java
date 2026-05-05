package com.aliyun.iot.aep.sdk.bridge.invoker;

import android.app.Activity;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.bridge.core.context.ActivityLifeCircleManager;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.context.OnActivityResultManager;
import com.aliyun.iot.aep.sdk.bridge.core.context.OnNewIntentManager;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallMode;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class AsyncBoneInvoker implements BoneInvoker {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    ExecutorService f4613b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    BoneInvoker f4614c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    BlockingQueue f4612a = new LinkedBlockingQueue();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile boolean f4615d = false;

    public AsyncBoneInvoker(BoneInvoker boneInvoker) {
        if (boneInvoker == null) {
            throw new IllegalArgumentException("invoker can not be null");
        }
        this.f4614c = boneInvoker;
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        this.f4613b = new ThreadPoolExecutor(iAvailableProcessors, (iAvailableProcessors * 2) + 1, 8L, TimeUnit.SECONDS, this.f4612a);
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.invoker.BoneInvoker
    public void invoke(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback, BoneCallback boneCallback2) {
        if (this.f4615d) {
            return;
        }
        if (jSContext == null) {
            throw new IllegalArgumentException("jsContext can not be null");
        }
        if (jSContext.getCurrentActivity() == null) {
            ALog.d("AsyncBoneInvoker", "ignore call after destroy");
            return;
        }
        if (TextUtils.isEmpty(jSContext.getCurrentUrl())) {
            throw new IllegalArgumentException("jsContext.getCurrentUrl can not be empty");
        }
        if (boneCall == null) {
            throw new IllegalArgumentException("call can not be null");
        }
        if (TextUtils.isEmpty(boneCall.serviceId)) {
            throw new IllegalArgumentException("call.serviceId can not be empty");
        }
        if (TextUtils.isEmpty(boneCall.methodName)) {
            throw new IllegalArgumentException("call.methodName can not be empty");
        }
        if (BoneCallMode.ASYNC != boneCall.mode) {
            throw new IllegalArgumentException("only support async call");
        }
        if (boneCallback2 == null) {
            throw new IllegalArgumentException("asyncCallback can not be null");
        }
        ExecutorService executorService = this.f4613b;
        if (executorService == null || executorService.isShutdown()) {
            throw new IllegalStateException("can not invoke after onDestroy has been called");
        }
        this.f4613b.execute(new a(new b(jSContext), boneCall, boneCallback2));
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.invoker.BoneInvoker
    public void onDestroy() {
        this.f4612a.clear();
        ExecutorService executorService = this.f4613b;
        if (executorService != null && !executorService.isShutdown()) {
            this.f4613b.shutdown();
        }
        this.f4613b = null;
        this.f4615d = true;
    }

    class a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        JSContext f4616a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        BoneCall f4617b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        BoneCallback f4618c;

        public a(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
            this.f4616a = jSContext;
            this.f4617b = boneCall;
            this.f4618c = boneCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            ALog.d("BoneInvoker", "" + Thread.currentThread().getName());
            if (AsyncBoneInvoker.this.f4615d) {
                return;
            }
            this.f4617b.mode = BoneCallMode.SYNC;
            AsyncBoneInvoker.this.f4614c.invoke(this.f4616a, this.f4617b, this.f4618c, null);
        }
    }

    static class b implements JSContext {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private WeakReference<JSContext> f4620a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f4621b;

        private b(JSContext jSContext) {
            this.f4620a = new WeakReference<>(jSContext);
            this.f4621b = jSContext.getCurrentUrl();
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.JSContext
        public String getId() {
            if (this.f4620a.get() == null) {
                return null;
            }
            return this.f4620a.get().getId();
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.JSContext
        public Activity getCurrentActivity() {
            if (this.f4620a.get() == null) {
                return null;
            }
            return this.f4620a.get().getCurrentActivity();
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.JSContext
        public String getCurrentUrl() {
            return this.f4621b;
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.JSContext
        public void emitter(String str, JSONObject jSONObject) {
            if (this.f4620a.get() != null) {
                this.f4620a.get().emitter(str, jSONObject);
            }
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.JSContext
        public void reload() {
            final Activity currentActivity = getCurrentActivity();
            if (currentActivity == null) {
                return;
            }
            currentActivity.runOnUiThread(new Runnable() { // from class: com.aliyun.iot.aep.sdk.bridge.invoker.AsyncBoneInvoker.b.1
                @Override // java.lang.Runnable
                public void run() {
                    if (b.this.f4620a.get() == null || currentActivity == null) {
                        return;
                    }
                    ((JSContext) b.this.f4620a.get()).reload();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.OnNewIntentManager
        public void addOnNewIntentListener(OnNewIntentManager.OnNewIntentListener onNewIntentListener) {
            if (this.f4620a.get() != null) {
                this.f4620a.get().addOnNewIntentListener(onNewIntentListener);
            }
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.OnActivityResultManager
        public void addOnActivityResultListener(OnActivityResultManager.OnActivityResultListener onActivityResultListener) {
            if (this.f4620a.get() != null) {
                this.f4620a.get().addOnActivityResultListener(onActivityResultListener);
            }
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.OnNewIntentManager
        public void removeOnNewIntentListener(OnNewIntentManager.OnNewIntentListener onNewIntentListener) {
            if (this.f4620a.get() != null) {
                this.f4620a.get().removeOnNewIntentListener(onNewIntentListener);
            }
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.ActivityLifeCircleManager
        public void addActivityLifeCircleListener(ActivityLifeCircleManager.ActivityLifeCircleListener activityLifeCircleListener) {
            if (this.f4620a.get() != null) {
                this.f4620a.get().addActivityLifeCircleListener(activityLifeCircleListener);
            }
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.OnActivityResultManager
        public void removeOnActivityResultListener(OnActivityResultManager.OnActivityResultListener onActivityResultListener) {
            if (this.f4620a.get() != null) {
                this.f4620a.get().removeOnActivityResultListener(onActivityResultListener);
            }
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.context.ActivityLifeCircleManager
        public void removeActivityLifeCircleListener(ActivityLifeCircleManager.ActivityLifeCircleListener activityLifeCircleListener) {
            if (this.f4620a.get() != null) {
                this.f4620a.get().removeActivityLifeCircleListener(activityLifeCircleListener);
            }
        }
    }
}
