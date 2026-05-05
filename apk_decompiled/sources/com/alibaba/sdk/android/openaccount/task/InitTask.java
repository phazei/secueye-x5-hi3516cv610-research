package com.alibaba.sdk.android.openaccount.task;

import android.content.Context;
import android.os.AsyncTask;
import com.alibaba.sdk.android.oauth.OauthModule;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.OpenAccountSessionService;
import com.alibaba.sdk.android.openaccount.callback.InitResultCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.DynamicConfigInitHandler;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.executor.impl.ExecutorServiceImpl;
import com.alibaba.sdk.android.openaccount.impl.OpenAccountServiceImpl;
import com.alibaba.sdk.android.openaccount.initialization.InitializationHandler;
import com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient;
import com.alibaba.sdk.android.openaccount.initialization.impl.DefaultInitializationServiceClientImpl;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.openaccount.rpc.RpcService;
import com.alibaba.sdk.android.openaccount.rpc.cloudapi.ApiGatewayRpcServiceImpl;
import com.alibaba.sdk.android.openaccount.rpc.mtop.MtopMtopRpcServiceImpl;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.security.impl.SecurityGuardWrapper;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.session.impl.DefaultSessionManagerServiceImpl;
import com.alibaba.sdk.android.openaccount.session.impl.SessionServiceImpl;
import com.alibaba.sdk.android.openaccount.trace.ActionTraceLogger;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.trace.TraceLoggerManager;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.ut.impl.AlibabaUserTrackerService;
import com.alibaba.sdk.android.openaccount.util.ReflectionUtils;
import com.alibaba.sdk.android.openaccount.util.TraceHelper;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.meta.ModuleInfo;
import com.alibaba.sdk.android.pluto.meta.ModuleInfoBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public class InitTask implements Runnable {
    private static final ReentrantLock initLock = new ReentrantLock();
    public static volatile Boolean isInitOk;
    private InitResultCallback initResultCallback;
    private volatile boolean initialized = false;
    private CountDownLatch initializationLock = new CountDownLatch(1);

    public InitTask(Context context, final InitResultCallback initResultCallback) {
        final ActionTraceLogger actionTraceLoggerBegin = TraceLoggerManager.INSTANCE.action("init_sdk", "initTask").begin();
        this.initResultCallback = new InitResultCallback() { // from class: com.alibaba.sdk.android.openaccount.task.InitTask.1
            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                InitTask.this.sendInitHint(false, actionTraceLoggerBegin.getCaseTime(), str);
                actionTraceLoggerBegin.failed();
                InitResultCallback initResultCallback2 = initResultCallback;
                if (initResultCallback2 != null) {
                    initResultCallback2.onFailure(i, str);
                }
                InitTask.this.postInitResultCallbackEvents(i, str);
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.InitResultCallback
            public void onSuccess() {
                InitTask.this.sendInitHint(true, actionTraceLoggerBegin.getCaseTime(), null);
                actionTraceLoggerBegin.success();
                InitResultCallback initResultCallback2 = initResultCallback;
                if (initResultCallback2 != null) {
                    initResultCallback2.onSuccess();
                }
                InitTask.this.postInitResultCallbackEvents(100, null);
            }
        };
        init();
    }

    private void init() {
        try {
            Class.forName(AsyncTask.class.getName());
        } catch (Exception unused) {
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                initLock.lock();
                asyncRun();
            } catch (Throwable th) {
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, th.getMessage(), th);
                doWhenException(th);
            }
        } finally {
            initLock.unlock();
            this.initializationLock.countDown();
        }
    }

    public void await() {
        try {
            this.initializationLock.await();
        } catch (InterruptedException e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, e.getMessage(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendInitHint(boolean z, long j, String str) {
        String str2 = z ? UTConstants.TYPE_INIT_SUCCESS : UTConstants.TYPE_INIT_FAILED;
        HashMap map = new HashMap();
        if (str != null) {
            map.put("msg", str);
        }
        UserTrackerService userTrackerService = (UserTrackerService) Pluto.DEFAULT_INSTANCE.getBean(UserTrackerService.class, null);
        if (userTrackerService != null) {
            userTrackerService.sendCustomHit(UTConstants.E_SDK_INIT_RESULT, j, str2, map);
            HashMap map2 = new HashMap();
            map2.put("model", "openaccount");
            map2.put("version", ConfigManager.getInstance().getSDKVersion().toString());
            userTrackerService.sendCustomHit("7", "80001", 19999, "init", j, str2, map2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postInitResultCallbackEvents(final int i, final String str) {
        ExecutorServiceImpl.INSTANCE.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.InitTask.2
            @Override // java.lang.Runnable
            public void run() {
                InitResultCallback[] initResultCallbackArr = (InitResultCallback[]) Pluto.DEFAULT_INSTANCE.getBeans(InitResultCallback.class);
                if (initResultCallbackArr == null || initResultCallbackArr.length == 0) {
                    return;
                }
                for (InitResultCallback initResultCallback : initResultCallbackArr) {
                    try {
                        if (i == 100) {
                            initResultCallback.onSuccess();
                        } else {
                            initResultCallback.onFailure(i, str);
                        }
                    } catch (Exception e) {
                        AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to invoke the system initresultcallback", e);
                    }
                }
            }
        });
    }

    private boolean asyncRun() throws ClassNotFoundException {
        ModuleInfoBuilder moduleInfoBuilder = new ModuleInfoBuilder("system");
        moduleInfoBuilder.addBeanInfo(ConfigService.class, ConfigManager.getInstance(), "init", (Map<String, String>) null);
        moduleInfoBuilder.addBeanInfo(new Class[]{ExecutorService.class, java.util.concurrent.ExecutorService.class}, new ExecutorServiceImpl());
        moduleInfoBuilder.addBeanInfo(new Class[]{DeviceManager.class, InitializationHandler.class, EnvironmentChangeListener.class}, DeviceManager.INSTANCE, "init", (Map<String, String>) null);
        moduleInfoBuilder.addBeanInfo(new Class[]{UserTrackerService.class, EnvironmentChangeListener.class}, AlibabaUserTrackerService.class, "init", (Map<String, String>) null);
        moduleInfoBuilder.addBeanInfo(new Class[]{SessionManagerService.class, EnvironmentChangeListener.class}, DefaultSessionManagerServiceImpl.INSTANCE, "init", (Map<String, String>) null);
        moduleInfoBuilder.addBeanInfo(new Class[]{InitializationHandler.class, OpenAccountSessionService.class}, SessionServiceImpl.class, (String) null, (Map<String, String>) null);
        moduleInfoBuilder.addBeanInfo(new Class[]{SecurityGuardService.class, EnvironmentChangeListener.class}, SecurityGuardWrapper.INSTANCE, "init", (Map<String, String>) null);
        moduleInfoBuilder.addBeanInfo(InitializationServiceClient.class, DefaultInitializationServiceClientImpl.class);
        moduleInfoBuilder.addBeanInfo(InitializationHandler.class, DynamicConfigInitHandler.class, "init", (Map<String, String>) null);
        Pluto.DEFAULT_INSTANCE.registerModule(moduleInfoBuilder.build());
        regiterRpc();
        ModuleInfoBuilder moduleInfoBuilder2 = new ModuleInfoBuilder("openaccount");
        moduleInfoBuilder2.addBeanInfo(OpenAccountService.class, OpenAccountServiceImpl.class);
        Pluto.DEFAULT_INSTANCE.registerModule(moduleInfoBuilder2.build());
        Pluto.DEFAULT_INSTANCE.registerModule(OauthModule.getModuleInfo());
        ModuleInfo moduleInfo = (ModuleInfo) ReflectionUtils.invoke("com.alibaba.sdk.android.openaccount.ui.module.OpenAccountTaobaoUIModule", "getModuleInfo", (String[]) null, (Object) null, (Object[]) null);
        if (moduleInfo != null) {
            Pluto.DEFAULT_INSTANCE.registerModule(moduleInfo);
        }
        ModuleInfo moduleInfo2 = (ModuleInfo) ReflectionUtils.invoke("com.alibaba.sdk.android.openaccount.ui.module.OpenAccountUIModule", "getModuleInfo", (String[]) null, (Object) null, (Object[]) null);
        if (moduleInfo2 != null) {
            Pluto.DEFAULT_INSTANCE.registerModule(moduleInfo2);
        }
        ModuleInfo moduleInfo3 = (ModuleInfo) ReflectionUtils.invoke("com.alibaba.sdk.android.openaccount.ui.module.OpenAccountFaceModule", "getModuleInfo", (String[]) null, (Object) null, (Object[]) null);
        if (moduleInfo3 != null) {
            Pluto.DEFAULT_INSTANCE.registerModule(moduleInfo3);
        }
        final List<ResultCode> listInit = Pluto.DEFAULT_INSTANCE.init(OpenAccountSDK.getAndroidContext());
        if (listInit.size() != 0) {
            ExecutorServiceImpl.INSTANCE.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.InitTask.3
                @Override // java.lang.Runnable
                public void run() {
                    if (InitTask.this.initResultCallback != null) {
                        ResultCode resultCode = (ResultCode) listInit.get(0);
                        InitTask.this.initResultCallback.onFailure(resultCode.code, resultCode.message);
                    }
                }
            });
            for (ResultCode resultCode : listInit) {
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "init failed code = " + resultCode.code + " message = " + resultCode.message);
            }
            return false;
        }
        TraceHelper.init(OpenAccountSDK.getAndroidContext(), OpenAccountSDK.getProperty(OpenAccountConstants.APP_KEY), null, OpenAccountSDK.getVersion().toString());
        ExecutorServiceImpl.INSTANCE.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.InitTask.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ((InitializationServiceClient) Pluto.DEFAULT_INSTANCE.getBean(InitializationServiceClient.class)).request();
                } catch (Exception e) {
                    AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to do the sdk init", e);
                }
            }
        });
        ((ExecutorService) Pluto.DEFAULT_INSTANCE.getBean(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.InitTask.5
            @Override // java.lang.Runnable
            public void run() {
                if (InitTask.this.initResultCallback != null) {
                    InitTask.this.initResultCallback.onSuccess();
                }
            }
        });
        isInitOk = true;
        return true;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    private void regiterRpc() throws ClassNotFoundException {
        Class<?> cls;
        boolean z;
        ModuleInfoBuilder moduleInfoBuilder = new ModuleInfoBuilder("rpc");
        if (ConfigManager.getInstance().isAPIGateway()) {
            cls = ApiGatewayRpcServiceImpl.class;
            z = false;
        } else {
            try {
                cls = Class.forName("com.alibaba.sdk.android.openaccount.ext.rpc.OpenMtopServiceImplMtop");
                z = true;
            } catch (Throwable unused) {
                cls = MtopMtopRpcServiceImpl.class;
                z = false;
            }
        }
        ConfigManager.getInstance().setOpenMtop(z);
        moduleInfoBuilder.addBeanInfo(new Class[]{RpcService.class, EnvironmentChangeListener.class, InitResultCallback.class}, cls, "init", (Map<String, String>) null);
        Pluto.DEFAULT_INSTANCE.registerModule(moduleInfoBuilder.build());
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void doWhenException(java.lang.Throwable r3) {
        /*
            r2 = this;
            r0 = 0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            com.alibaba.sdk.android.openaccount.task.InitTask.isInitOk = r0
            boolean r0 = r3 instanceof com.alibaba.sdk.android.openaccount.OpenAccountSDKException
            if (r0 == 0) goto L1d
            r0 = r3
            com.alibaba.sdk.android.openaccount.OpenAccountSDKException r0 = (com.alibaba.sdk.android.openaccount.OpenAccountSDKException) r0
            com.alibaba.sdk.android.openaccount.message.Message r1 = r0.getSDKMessage()
            if (r1 == 0) goto L1d
            com.alibaba.sdk.android.openaccount.message.Message r3 = r0.getSDKMessage()
            int r0 = r3.code
            java.lang.String r3 = r3.message
            goto L23
        L1d:
            r0 = 10010(0x271a, float:1.4027E-41)
            java.lang.String r3 = com.alibaba.sdk.android.openaccount.util.CommonUtils.toString(r3)
        L23:
            com.alibaba.sdk.android.openaccount.callback.InitResultCallback r1 = r2.initResultCallback
            com.alibaba.sdk.android.openaccount.util.CommonUtils.onFailure(r1, r0, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.openaccount.task.InitTask.doWhenException(java.lang.Throwable):void");
    }

    public static Boolean checkInitStatus() {
        try {
            initLock.lock();
            return isInitOk;
        } finally {
            initLock.unlock();
        }
    }
}
