package sdk;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import anet.channel.util.HttpConstant;
import bean.NetWorkEvent;
import bean.SwitchLanguageBean;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import com.aliyun.iot.aep.component.router.IUrlHandler;
import com.aliyun.iot.aep.routerexternal.RouterExternal;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.adapter.APIGatewayHttpAdapterImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestWrapper;
import com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.bundle.BundleManager;
import com.aliyun.iot.aep.sdk.framework.bundle.IBundleRegister;
import com.aliyun.iot.aep.sdk.framework.bundle.PageConfigure;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.IRefreshSessionCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.push.PushManager;
import com.xiaomi.mipush.sdk.Constants;
import config.AppConfig;
import enums.NetworkStateEnum;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
import tools.FileUtil;
import tools.LanguageUtil;
import tools.LogEx;
import tools.NetWorkChangeListener;
import tools.NetWorkStateReceiver;
import tools.SharePreferenceManager;
import tools.WebViewUtil;

/* JADX INFO: loaded from: classes4.dex */
public class DemoApplication extends AApplication {
    private static final String TAG = "DemoApplication";
    public static int activityCount;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    NetWorkChangeListener netWorkChangeListener = new NetWorkChangeListener() { // from class: sdk.DemoApplication.4
        @Override // tools.NetWorkChangeListener
        public void stateChanged(NetworkStateEnum networkStateEnum) {
            if (networkStateEnum == NetworkStateEnum.NONE) {
                return;
            }
            EventBus.getDefault().post(new NetWorkEvent());
        }
    };
    private NetWorkStateReceiver netWorkStateReceiver;
    private SharedPreferences sharedPreferences;

    @Override // com.aliyun.iot.aep.sdk.framework.AApplication, android.app.Application
    public void onCreate() {
        super.onCreate();
        SharePreferenceManager.getInstance().init(this);
        AppConfig.time = System.currentTimeMillis();
        WebViewUtil.handleWebViewDir(this);
        this.sharedPreferences = getSharedPreferences("FirstRun", 0);
        boolean z = this.sharedPreferences.getBoolean("First", true);
        if (!z) {
            SDKInitHelper.init(this);
            ALog.setLevel((byte) 2);
            com.aliyun.alink.linksdk.tools.ALog.setLevel((byte) 1);
        }
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance().setExcludeFontScale(true).setOnAdaptListener(new onAdaptListener() { // from class: sdk.DemoApplication.1
            @Override // me.jessyan.autosize.onAdaptListener
            public void onAdaptBefore(Object obj, Activity activity2) {
                LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", obj.getClass().getName()));
            }

            @Override // me.jessyan.autosize.onAdaptListener
            public void onAdaptAfter(Object obj, Activity activity2) {
                LogUtils.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", obj.getClass().getName()));
            }
        });
        if (getPackageName().equals(ThreadTools.getProcessName(this, Process.myPid()))) {
            BundleManager.init(this, new IBundleRegister() { // from class: sdk.-$$Lambda$DemoApplication$QERF9-4_-74Q6NLB2O3aI7JxvQs
                @Override // com.aliyun.iot.aep.sdk.framework.bundle.IBundleRegister
                public final void registerPage(Application application, PageConfigure pageConfigure) {
                    DemoApplication.lambda$onCreate$0(application, pageConfigure);
                }
            });
            registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: sdk.DemoApplication.2
                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStarted(Activity activity2) {
                    if (DemoApplication.activityCount == 0) {
                        LoginBusiness.refreshSession(true, new IRefreshSessionCallback() { // from class: sdk.DemoApplication.2.1
                            @Override // com.aliyun.iot.aep.sdk.login.IRefreshSessionCallback
                            public void onRefreshFailed() {
                            }

                            @Override // com.aliyun.iot.aep.sdk.login.IRefreshSessionCallback
                            public void onRefreshSuccess() {
                            }
                        });
                    }
                    DemoApplication.activityCount++;
                    Log.d(DemoApplication.TAG, "onActivityStarted: " + activity2);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStopped(Activity activity2) {
                    DemoApplication.activityCount--;
                    int i = DemoApplication.activityCount;
                    Log.d(DemoApplication.TAG, "onActivityStopped: " + activity2);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivitySaveInstanceState(Activity activity2, Bundle bundle) {
                    Log.d(DemoApplication.TAG, "onActivitySaveInstanceState: " + activity2);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity2) {
                    Log.d(DemoApplication.TAG, "onActivityDestroyed: " + activity2);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityCreated(Activity activity2, Bundle bundle) {
                    Log.d(DemoApplication.TAG, "onActivityCreated: " + activity2);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityResumed(Activity activity2) {
                    Log.d(DemoApplication.TAG, "onActivityResumed: " + activity2);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityPaused(Activity activity2) {
                    Log.d(DemoApplication.TAG, "onActivityPaused: " + activity2);
                }
            });
            if (!z) {
                IoTSmart.setDebug(true);
                IoTAPIClientImpl.getInstance().registerTracker(new Tracker() { // from class: sdk.DemoApplication.3
                    final String TAG = "APIGatewaySDKDelegate$Tracker";

                    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
                    public void onSend(IoTRequest ioTRequest) {
                        ALog.i("APIGatewaySDKDelegate$Tracker", "onSend:\r\n" + toString(ioTRequest));
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
                    public void onRealSend(IoTRequestWrapper ioTRequestWrapper) {
                        ALog.d("APIGatewaySDKDelegate$Tracker", "onRealSend:\r\n" + toString(ioTRequestWrapper));
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
                    public void onRawFailure(IoTRequestWrapper ioTRequestWrapper, Exception exc) {
                        ALog.d("APIGatewaySDKDelegate$Tracker", "onRawFailure:\r\n" + toString(ioTRequestWrapper) + "ERROR-MESSAGE:" + exc.getMessage());
                        exc.printStackTrace();
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                        ALog.i("APIGatewaySDKDelegate$Tracker", "onFailure:\r\n" + toString(ioTRequest) + "ERROR-MESSAGE:" + exc.getMessage());
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
                    public void onRawResponse(IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse) {
                        ALog.d("APIGatewaySDKDelegate$Tracker", "onRawResponse:\r\n" + toString(ioTRequestWrapper) + toString(ioTResponse));
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        ALog.i("APIGatewaySDKDelegate$Tracker", "onResponse:\r\n" + toString(ioTRequest) + toString(ioTResponse));
                    }

                    private String toString(IoTRequest ioTRequest) {
                        StringBuilder sb = new StringBuilder("Request:");
                        sb.append("\r\n");
                        sb.append("url:");
                        sb.append(ioTRequest.getScheme());
                        sb.append(HttpConstant.SCHEME_SPLIT);
                        sb.append(ioTRequest.getHost() == null ? "" : ioTRequest.getHost());
                        sb.append(ioTRequest.getPath());
                        sb.append("\r\n");
                        sb.append("apiVersion:");
                        sb.append(ioTRequest.getAPIVersion());
                        sb.append("\r\n");
                        sb.append("params:");
                        sb.append(ioTRequest.getParams() == null ? "" : JSON.toJSONString(ioTRequest.getParams()));
                        sb.append("\r\n");
                        return sb.toString();
                    }

                    private String toString(IoTRequestWrapper ioTRequestWrapper) {
                        IoTRequest ioTRequest = ioTRequestWrapper.request;
                        StringBuilder sb = new StringBuilder("Request:");
                        sb.append("\r\n");
                        sb.append("id:");
                        sb.append(ioTRequestWrapper.payload.getId());
                        sb.append("\r\n");
                        sb.append("url:");
                        sb.append(ioTRequest.getScheme());
                        sb.append(HttpConstant.SCHEME_SPLIT);
                        sb.append(TextUtils.isEmpty(ioTRequestWrapper.request.getHost()) ? "" : ioTRequestWrapper.request.getHost());
                        sb.append(ioTRequest.getPath());
                        sb.append("\r\n");
                        sb.append("apiVersion:");
                        sb.append(ioTRequest.getAPIVersion());
                        sb.append("\r\n");
                        sb.append("params:");
                        sb.append(ioTRequest.getParams() == null ? "" : JSON.toJSONString(ioTRequest.getParams()));
                        sb.append("\r\n");
                        sb.append("payload:");
                        sb.append(JSON.toJSONString(ioTRequestWrapper.payload));
                        sb.append("\r\n");
                        return sb.toString();
                    }

                    private String toString(IoTResponse ioTResponse) {
                        StringBuilder sb = new StringBuilder("Response:");
                        sb.append("\r\n");
                        sb.append("id:");
                        sb.append(ioTResponse.getId());
                        sb.append("\r\n");
                        sb.append("code:");
                        sb.append(ioTResponse.getCode());
                        sb.append("\r\n");
                        sb.append("message:");
                        sb.append(ioTResponse.getMessage());
                        sb.append("\r\n");
                        sb.append("localizedMsg:");
                        sb.append(ioTResponse.getLocalizedMsg());
                        sb.append("\r\n");
                        sb.append("data:");
                        sb.append(ioTResponse.getData() == null ? "" : ioTResponse.getData().toString());
                        sb.append("\r\n");
                        return sb.toString();
                    }
                });
            }
            if (this.netWorkStateReceiver == null) {
                this.netWorkStateReceiver = new NetWorkStateReceiver(this.netWorkChangeListener);
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(this.netWorkStateReceiver, intentFilter);
            LanguageUtil.setLocale(Locale.getDefault());
            setLanguageCode(LanguageUtil.getRealLanguage(this, Locale.getDefault().getLanguage() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + Locale.getDefault().getCountry()));
        }
    }

    static /* synthetic */ void lambda$onCreate$0(Application application, PageConfigure pageConfigure) {
        if (pageConfigure == null || pageConfigure.navigationConfigures == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (PageConfigure.NavigationConfigure navigationConfigure : pageConfigure.navigationConfigures) {
            if (navigationConfigure.navigationCode != null && !navigationConfigure.navigationCode.isEmpty() && navigationConfigure.navigationIntentUrl != null && !navigationConfigure.navigationIntentUrl.isEmpty()) {
                PageConfigure.NavigationConfigure navigationConfigure2 = new PageConfigure.NavigationConfigure();
                navigationConfigure2.navigationCode = navigationConfigure.navigationCode;
                navigationConfigure2.navigationIntentUrl = navigationConfigure.navigationIntentUrl;
                navigationConfigure2.navigationIntentAction = navigationConfigure.navigationIntentAction;
                navigationConfigure2.navigationIntentCategory = navigationConfigure.navigationIntentCategory;
                arrayList2.add(navigationConfigure2);
                arrayList.add(navigationConfigure2.navigationIntentUrl);
                ALog.d("BundleManager", "register-native-page: " + navigationConfigure.navigationCode + ", " + navigationConfigure.navigationIntentUrl);
                RouterExternal.getInstance().registerNativeCodeUrl(navigationConfigure2.navigationCode, navigationConfigure2.navigationIntentUrl);
                RouterExternal.getInstance().registerNativePages(arrayList, new NativeUrlHandler(navigationConfigure2));
            }
        }
    }

    public void setLanguageCode(String str) {
        IoTSmart.setLanguage(str);
        final IoTSmart.Country country = IoTSmart.getCountry();
        if (country != null) {
            EventBus.getDefault().post(new SwitchLanguageBean(true));
            IoTSmart.getCountryList(new IoTSmart.ICountryListGetCallBack() { // from class: sdk.DemoApplication.5
                @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
                public void onSucess(List<IoTSmart.Country> list) {
                    if (list != null) {
                        for (IoTSmart.Country country2 : list) {
                            if (country2 != null && country2.code.equals(country.code)) {
                                IoTSmart.setCountry(country2, (IoTSmart.ICountrySetCallBack) null);
                            }
                        }
                    }
                    EventBus.getDefault().post(new SwitchLanguageBean(false));
                }

                @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
                public void onFail(String str2, int i, String str3) {
                    EventBus.getDefault().post(new SwitchLanguageBean(false));
                }
            });
        }
    }

    public void startPushAndConnect() {
        if (LoginBusiness.isLogin()) {
            PushManager.getInstance().bindUser();
            ChannelManager.getInstance().init(this, APIGatewayHttpAdapterImpl.getAppKey(this, "product_china"));
        }
    }

    public void stopPushAndDisconnect() {
        if (LoginBusiness.isLogin()) {
            PushManager.getInstance().unbindUser();
            ChannelManager.getInstance().disconnect();
        }
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        try {
            setLanguageCode(LanguageUtil.getRealLanguage(this, configuration.locale.getLanguage() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + configuration.locale.getCountry()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class NativeUrlHandler implements IUrlHandler {
        private final String TAG = "ApplicationHelper$NativeUrlHandler";
        private final PageConfigure.NavigationConfigure navigationConfigure;

        NativeUrlHandler(PageConfigure.NavigationConfigure navigationConfigure) {
            this.navigationConfigure = navigationConfigure;
        }

        @Override // com.aliyun.iot.aep.component.router.IUrlHandler
        public void onUrlHandle(Context context, String str, Bundle bundle, boolean z, int i) {
            LogEx.d(true, "ApplicationHelper$NativeUrlHandler", "onUrlHandle: url: " + str);
            if (context == null || str == null || str.isEmpty()) {
                return;
            }
            Intent intent = new Intent();
            intent.setData(Uri.parse(str));
            if (this.navigationConfigure.navigationIntentAction != null) {
                intent.setAction(this.navigationConfigure.navigationIntentAction);
            }
            if (this.navigationConfigure.navigationIntentCategory != null) {
                intent.addCategory(this.navigationConfigure.navigationIntentCategory);
            }
            if (Build.VERSION.SDK_INT >= 26) {
                intent.setPackage(context.getPackageName());
            }
            LogEx.d(true, "ApplicationHelper$NativeUrlHandler", "startActivity(): url: " + this.navigationConfigure.navigationIntentUrl + ", startActForResult: " + z + ", reqCode: " + i);
            startActivity(context, intent, bundle, z, i);
        }

        private void startActivity(Context context, Intent intent, Bundle bundle, boolean z, int i) {
            if (context == null || intent == null) {
                return;
            }
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (z) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, i);
                }
            } else if (context instanceof Application) {
                intent.addFlags(268435456);
                context.startActivity(intent);
            } else if ((context instanceof Activity) || (context instanceof Service)) {
                context.startActivity(intent);
            }
        }
    }

    public class crashHandler implements Thread.UncaughtExceptionHandler {
        public crashHandler() {
        }

        /* JADX WARN: Type inference failed for: r1v6, types: [sdk.DemoApplication$crashHandler$1] */
        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, final Throwable th) {
            if (th == null) {
                DemoApplication.this.mDefaultHandler.uncaughtException(thread, th);
                return;
            }
            final String str = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DemoApplication.this.getPackageName() + "/crash_Log";
            new Thread() { // from class: sdk.DemoApplication.crashHandler.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    FileUtil.saveCrashInfo2File(th, str);
                }
            }.start();
            DemoApplication.this.mDefaultHandler.uncaughtException(thread, th);
        }
    }

    public static int getActivityCount() {
        return activityCount;
    }
}
