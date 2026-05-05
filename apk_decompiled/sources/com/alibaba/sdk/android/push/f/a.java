package com.alibaba.sdk.android.push.f;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import anet.channel.AwcnConfig;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.AppLifecycle;
import anetwork.channel.http.NetworkSdkSetting;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.ReportProgressUtil;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.PushControlService;
import com.alibaba.sdk.android.push.common.util.AppInfoUtil;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.report.ReportManager;
import com.aliyun.alink.linksdk.securesigner.SecuritySourceContext;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.ams.emas.push.IReportPushArrive;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsException;
import com.taobao.accs.AccsState;
import com.taobao.accs.ConnectionListener;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.agoo.IRegister;
import com.taobao.agoo.TaobaoRegister;
import com.ut.device.UTDevice;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static AmsLogger f = AmsLogger.getLogger("MPS:AppRegister");
    private static a g = null;
    private static final String[] h = {"amdcopen.m.taobao.com", "amdc.wapa.taobao.com", "amdc.taobao.net"};
    private static final IntentFilter i = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private static final IntentFilter j = new IntentFilter("android.intent.action.USER_PRESENT");
    private static boolean l = false;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    volatile HandlerThreadC0202a<d> f3076a;
    private final b k = new b();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    volatile boolean f3077b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    volatile boolean f3078c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    volatile boolean f3079d = true;
    volatile boolean e = true;

    /* JADX INFO: renamed from: com.alibaba.sdk.android.push.f.a$a, reason: collision with other inner class name */
    class HandlerThreadC0202a<Token> extends HandlerThread {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        Handler f3086a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        Handler f3087b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        c<Token> f3088c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        volatile int f3089d;
        int e;
        private Token g;

        public HandlerThreadC0202a() {
            super("ConnectionWorker");
            this.f3089d = 0;
            this.e = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public e a(Token token) {
            e eVar;
            Context contextB = com.alibaba.sdk.android.ams.common.a.a.b();
            long jCurrentTimeMillis = System.currentTimeMillis();
            try {
                try {
                    try {
                        if (!com.alibaba.sdk.android.push.common.util.c.a(contextB.getApplicationContext())) {
                            this.e = 2;
                            e eVar2 = new e(com.alibaba.sdk.android.push.common.a.d.f3049a);
                            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                            try {
                                a.f.i("connState=" + this.e + ";estimatedTime=" + jCurrentTimeMillis2 + ";response{msg: " + eVar2.a().getMsg() + ", code: " + eVar2.a().getCode() + "}", null, 1);
                            } catch (Exception e) {
                                a.f.e("ut log error", e);
                            }
                            return eVar2;
                        }
                        if (this.e == 0) {
                            a.f.d("is debug：" + com.alibaba.sdk.android.push.common.a.b.e());
                            if (com.alibaba.sdk.android.push.common.a.b.e()) {
                                a.this.j();
                                a.this.i();
                            }
                            a(contextB);
                        }
                        if (com.alibaba.sdk.android.ams.common.a.a.i() && !com.alibaba.sdk.android.ams.common.a.a.d() && !com.alibaba.sdk.android.push.notification.e.a(contextB)) {
                            long jElapsedRealtime = SystemClock.elapsedRealtime();
                            while (!com.alibaba.sdk.android.push.notification.e.a(contextB) && SystemClock.elapsedRealtime() - jElapsedRealtime < 10000) {
                                Thread.sleep(1000L);
                                a.f.d("wait for app come to foreground");
                            }
                            try {
                                if (!com.alibaba.sdk.android.push.notification.e.a(contextB)) {
                                    AwcnConfig.setIpv6Enable(false);
                                    a.f.d("APP is background, disable ipv6 test");
                                }
                            } catch (Throwable unused) {
                            }
                        }
                        if (this.e == 1) {
                            a.f.d("accs init.");
                            e eVarB = b(contextB);
                            long jCurrentTimeMillis3 = System.currentTimeMillis() - jCurrentTimeMillis;
                            if (eVarB != null) {
                                try {
                                    a.f.i("connState=" + this.e + ";estimatedTime=" + jCurrentTimeMillis3 + ";response{msg: " + eVarB.a().getMsg() + ", code: " + eVarB.a().getCode() + "}", null, 1);
                                } catch (Exception e2) {
                                    a.f.e("ut log error", e2);
                                }
                            }
                            return eVarB;
                        }
                        if (this.e == 2) {
                            a.f.d("accs connected.setBindStop.");
                            eVar = null;
                        } else {
                            a.f.e("cant entry this block...");
                            eVar = new e(com.alibaba.sdk.android.push.common.a.d.l);
                        }
                        long jCurrentTimeMillis4 = System.currentTimeMillis() - jCurrentTimeMillis;
                        if (eVar != null) {
                            try {
                                a.f.i("connState=" + this.e + ";estimatedTime=" + jCurrentTimeMillis4 + ";response{msg: " + eVar.a().getMsg() + ", code: " + eVar.a().getCode() + "}", null, 1);
                            } catch (Exception e3) {
                                a.f.e("ut log error", e3);
                            }
                        }
                        return eVar;
                    } catch (com.alibaba.sdk.android.push.b.f e4) {
                        a.f.d("Catch StopProcessException: " + e4.a() + " stack:" + Log.getStackTraceString(e4));
                        e eVar3 = new e(e4.a());
                        long jCurrentTimeMillis5 = System.currentTimeMillis() - jCurrentTimeMillis;
                        try {
                            a.f.i("connState=" + this.e + ";estimatedTime=" + jCurrentTimeMillis5 + ";response{msg: " + eVar3.a().getMsg() + ", code: " + eVar3.a().getCode() + "}", null, 1);
                        } catch (Exception e5) {
                            a.f.e("ut log error", e5);
                        }
                        return eVar3;
                    }
                } catch (Throwable th) {
                    a.f.d("Catch RuntimeException: " + th.getMessage());
                    e eVar4 = new e(com.alibaba.sdk.android.push.common.a.d.k.copy().msg(th.getMessage()).detail(Log.getStackTraceString(th)).build());
                    long jCurrentTimeMillis6 = System.currentTimeMillis() - jCurrentTimeMillis;
                    try {
                        a.f.i("connState=" + this.e + ";estimatedTime=" + jCurrentTimeMillis6 + ";response{msg: " + eVar4.a().getMsg() + ", code: " + eVar4.a().getCode() + "}", null, 1);
                    } catch (Exception e6) {
                        a.f.e("ut log error", e6);
                    }
                    return eVar4;
                }
            } catch (Throwable th2) {
                System.currentTimeMillis();
                throw th2;
            }
        }

        private void a(Context context) throws com.alibaba.sdk.android.push.b.f {
            a.f.d("load utdid: " + UTDevice.getUtdid(context));
            com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
            String strC = bVarA.c();
            a.f.d("vip init.");
            String strB = bVarA.b();
            if (!StringUtil.isEmpty(strB) && !StringUtil.isBlank(strC) && strC.equals(UTDevice.getUtdid(context))) {
                AmsLogger.getImportantLogger().i("Got deviceId from preference: " + strB);
                this.e = 1;
                return;
            }
            String strC2 = c();
            AmsLogger.getImportantLogger().i("Got deviceId from remote server: " + strC2);
            if (StringUtil.isEmpty(strC2)) {
                throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.g.copy().msg("获取设备ID失败").detail("getDeviceIdFromServer").build());
            }
            bVarA.b(strC2);
            bVarA.c(UTDevice.getUtdid(context));
            this.e = 1;
            AmsLogger.getImportantLogger().i("vip init success");
        }

        private e b(Context context) {
            a.f.d("initAccsChannel...");
            NetworkSdkSetting.init(context.getApplicationContext());
            String strA = com.alibaba.sdk.android.ams.common.b.c.a().a();
            AmsLogger.getImportantLogger().i("register agoo appkey:" + strA);
            final com.alibaba.sdk.android.push.e.b bVar = new com.alibaba.sdk.android.push.e.b();
            final e[] eVarArr = {null};
            a.this.b();
            try {
                a.f.d("init aliyun accs. context:" + context.getPackageName() + " -- appkey:" + strA);
                ACCSClient.getAccsClient("AliyunPush").cleanLocalBindInfo();
                AppLifecycle.onForeground();
                TaobaoRegister.register(context.getApplicationContext(), "AliyunPush", strA, "", "aliyun", new IRegister() { // from class: com.alibaba.sdk.android.push.f.a.a.2
                    @Override // com.taobao.agoo.IRegister, com.taobao.agoo.ICallback
                    public void onFailure(String str, String str2) {
                        AmsLogger.getImportantLogger().i("agoo errorcode:" + str + ";errorMsg:" + str2);
                        eVarArr[0] = new e(com.alibaba.sdk.android.push.common.a.d.a(str, str2).detail("register").build());
                        bVar.a();
                    }

                    @Override // com.taobao.agoo.IRegister
                    public void onSuccess(String str) {
                        AmsLogger.getImportantLogger().i("agoo init success.");
                        HandlerThreadC0202a.this.e = 2;
                        eVarArr[0] = new e(com.alibaba.sdk.android.push.common.a.d.f3049a);
                        bVar.a();
                    }
                });
            } catch (Throwable th) {
                a.f.e("accs config failed", th);
                eVarArr[0] = new e(com.alibaba.sdk.android.push.common.a.d.k.copy().msg(th.getMessage()).detail(Log.getStackTraceString(th)).build());
                bVar.a();
            }
            if (!com.alibaba.sdk.android.push.common.util.c.a(context.getApplicationContext())) {
                a.this.f3079d = true;
                a.f.d("not main process");
                return new e(com.alibaba.sdk.android.push.common.a.d.n);
            }
            a.f.d("lock" + bVar.toString());
            bVar.a(150);
            if (eVarArr[0] == null) {
                String stateByKey = "accs time out";
                try {
                    stateByKey = AccsState.getInstance().getStateByKey(AccsState.RECENT_ERRORS);
                } catch (Exception unused) {
                }
                eVarArr[0] = new e(com.alibaba.sdk.android.push.common.a.d.o.copy().msg(stateByKey).detail("connected:" + a.this.c()).build());
            }
            AmsLogger.getImportantLogger().d("register agoo result " + eVarArr[0].a());
            return eVarArr[0];
        }

        private String c() {
            PushServiceFactory.getCloudPushService().getDeviceId();
            com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
            String strJ = com.alibaba.sdk.android.ams.common.a.a.j();
            Context contextB = com.alibaba.sdk.android.ams.common.a.a.b();
            HttpURLConnection httpURLConnection = null;
            try {
                try {
                    HashMap map = new HashMap();
                    map.put("appKey", bVarA.a());
                    map.put("deviceId", UUID.randomUUID().toString());
                    map.put("version", "-SNAPSHOT");
                    map.put("utdid", UTDevice.getUtdid(contextB));
                    map.put(Constants.KEY_OS_VERSION, "2");
                    map.put("package", com.alibaba.sdk.android.ams.common.a.a.l());
                    try {
                        HttpURLConnection httpURLConnectionA = com.alibaba.sdk.android.ams.common.util.b.a(strJ, com.alibaba.sdk.android.ams.common.util.d.a(map), "POST");
                        if (httpURLConnectionA == null) {
                            a.f.e("failed to loadConfigFromRemote!");
                            throw new com.alibaba.sdk.android.push.common.util.a.a(com.alibaba.sdk.android.push.common.a.d.p.copy().msg("getDeviceId创建请求连接失败").build());
                        }
                        String strA = i.a(com.alibaba.sdk.android.push.common.util.a.d.CONFIG.a(), httpURLConnectionA);
                        if (httpURLConnectionA != null) {
                            httpURLConnectionA.disconnect();
                        }
                        return strA;
                    } catch (IOException e) {
                        throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.p.copy().msg(e.getMessage()).detail(Log.getStackTraceString(e)).build());
                    }
                } catch (Throwable th) {
                    if (0 != 0) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            } catch (com.alibaba.sdk.android.push.b.f e2) {
                throw e2;
            } catch (Throwable th2) {
                a.f.w("loadConfigFromRemote failed! error:", th2);
                throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.k.copy().msg(th2.getMessage()).detail(Log.getStackTraceString(th2)).build());
            }
        }

        public synchronized void a() {
            this.f3089d = 0;
            if ((!a.this.f3079d || this.e != 2) && this.f3086a != null) {
                this.f3086a.sendMessage(this.f3086a.obtainMessage(1, this.g));
            }
        }

        public void a(c<Token> cVar) {
            this.f3088c = cVar;
        }

        public synchronized boolean a(e eVar) {
            if (this.e == 2 || this.f3089d >= 5) {
                return false;
            }
            a.f.d("init retry:" + this.f3089d);
            this.f3089d = this.f3089d + 1;
            if (this.f3086a != null) {
                this.f3086a.sendMessageDelayed(this.f3086a.obtainMessage(2, this.g), ((int) Math.pow(3.0d, this.f3089d)) * 5000);
            }
            return true;
        }

        public void b() {
            Handler handler = this.f3086a;
            if (handler != null) {
                handler.removeMessages(1);
                this.f3086a.removeMessages(2);
            }
        }

        @Override // android.os.HandlerThread
        @SuppressLint({"HandlerLeak"})
        protected void onLooperPrepared() {
            this.f3087b = new Handler(Looper.getMainLooper());
            this.f3086a = new Handler() { // from class: com.alibaba.sdk.android.push.f.a.a.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    final e eVarA;
                    if (message.what == 1 || message.what == 2) {
                        final Object obj = message.obj;
                        a.f.d("Looping handleMessage: " + message.what);
                        if (message.what == 1) {
                            removeMessages(2);
                        }
                        if (a.this.f3079d || (eVarA = HandlerThreadC0202a.this.a(obj)) == null) {
                            return;
                        }
                        if (!HandlerThreadC0202a.this.a(eVarA) || HandlerThreadC0202a.this.f3089d <= 1) {
                            HandlerThreadC0202a.this.f3087b.post(new Runnable() { // from class: com.alibaba.sdk.android.push.f.a.a.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    HandlerThreadC0202a.this.f3088c.a((Token) obj, eVarA);
                                }
                            });
                        }
                    }
                }
            };
            a.f.d("Looping Prepared.");
            a.this.f3077b = true;
            a();
        }
    }

    class b extends BroadcastReceiver {
        b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                    if (intent.getBooleanExtra("noConnectivity", false)) {
                        a.f.e("Network has lost");
                        return;
                    } else if (a.this.f3079d || !a.this.f3077b) {
                        return;
                    }
                } else if (!"android.intent.action.USER_PRESENT".equals(intent.getAction()) || !com.alibaba.sdk.android.push.common.util.a.a(context) || a.this.f3079d || !a.this.f3077b) {
                    return;
                }
                a.this.f3076a.a();
            }
        }
    }

    private a() {
    }

    public static a a() {
        if (g == null) {
            synchronized (a.class) {
                if (g == null) {
                    g = new a();
                }
            }
        }
        return g;
    }

    private void b(boolean z, long j2) {
        Context contextB = com.alibaba.sdk.android.ams.common.a.a.b();
        final com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
        String strA = bVarA.a();
        AmsLogger.getImportantLogger().i("init agoo config appkey:" + strA);
        DispatchConstants.setAmdcServerDomain(!TextUtils.isEmpty(com.alibaba.sdk.android.ams.common.a.a.h()) ? new String[]{com.alibaba.sdk.android.ams.common.a.a.h(), com.alibaba.sdk.android.ams.common.a.a.h(), com.alibaba.sdk.android.ams.common.a.a.h()} : h);
        if (com.alibaba.sdk.android.ams.common.a.a.i()) {
            try {
                AwcnConfig.setWifiInfoEnable(false);
            } catch (Throwable unused) {
            }
            try {
                AwcnConfig.setCarrierInfoEnable(false);
            } catch (Throwable unused2) {
            }
            try {
                AmdcRuntimeInfo.setForceHttps(true);
            } catch (Throwable unused3) {
            }
        }
        try {
            AwcnConfig.setAccsSessionCreateForbiddenInBg(false);
        } catch (Throwable unused4) {
        }
        try {
            TaobaoRegister.setEnv(contextB, 0);
            AccsClientConfig.Builder builderLoopChannelInterval = new AccsClientConfig.Builder().setAppKey(strA).setTag("AliyunPush").setInappHost(com.alibaba.sdk.android.ams.common.a.a.f()).setChannelHost(com.alibaba.sdk.android.ams.common.a.a.g()).setDisableChannel(this.e).setAccsHeartbeatEnable(this.e).setConfigEnv(0).loopChannelStart(z).loopChannelInterval(j2);
            if (Utils.hasSecurityGuardDep()) {
                builderLoopChannelInterval.setAutoCode(com.alibaba.sdk.android.ams.common.a.a.c());
            } else {
                builderLoopChannelInterval.setAppSecret(SecuritySourceContext.getInstance().getAppSecretKey());
            }
            AccsClientConfig accsClientConfigBuild = builderLoopChannelInterval.build();
            TaobaoRegister.setAccsConfigTag(contextB, "AliyunPush");
            ACCSClient.init(contextB, accsClientConfigBuild);
            TaobaoRegister.setReportPushArrive(new IReportPushArrive() { // from class: com.alibaba.sdk.android.push.f.a.1
                @Override // com.aliyun.ams.emas.push.IReportPushArrive
                public void reportPushArrive(Context context, String str, int i2) {
                    ReportManager reportManager = ReportManager.getInstance(context);
                    if (reportManager != null) {
                        reportManager.reportPushArrive(bVarA.b(), str, i2);
                    }
                }
            });
        } catch (AccsException e) {
            e.printStackTrace();
        }
    }

    private void h() {
        Context contextB = com.alibaba.sdk.android.ams.common.a.a.b();
        if (com.alibaba.sdk.android.push.common.util.c.a(contextB)) {
            try {
                contextB.registerReceiver(this.k, i);
                contextB.registerReceiver(this.k, j);
            } catch (Exception e) {
                f.e("Fail to register broad", e);
            }
        }
        if (AdapterUtilityImpl.isChannelProcess(contextB)) {
            com.alibaba.sdk.android.push.c.a.a(contextB);
            com.alibaba.sdk.android.push.c.a.a().b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() throws com.alibaba.sdk.android.push.b.f {
        String strE = com.alibaba.sdk.android.ams.common.b.c.a().e();
        if (StringUtil.isEmpty(strE) || strE.length() > 32) {
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.r);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() throws com.alibaba.sdk.android.push.b.f {
        for (com.alibaba.sdk.android.push.common.a.a aVar : com.alibaba.sdk.android.push.common.a.a.values()) {
            if (!AppInfoUtil.isComponentExists(com.alibaba.sdk.android.ams.common.a.a.b(), aVar.a(), aVar.b())) {
                if (aVar.c()) {
                    throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.s.copy().msg(aVar.a() + "未配置").build());
                }
                f.w("未配置" + aVar.a() + "; 建议配置,可有效提高推送到达率");
            }
        }
    }

    public synchronized void a(final CommonCallback commonCallback) {
        if (this.f3078c) {
            AmsLogger.getImportantLogger().d("Already startReg, skip.");
            if (commonCallback != null) {
                commonCallback.onFailed(com.alibaba.sdk.android.push.common.a.d.w.getCode(), com.alibaba.sdk.android.push.common.a.d.w.getMsg());
            }
            return;
        }
        this.f3078c = true;
        l = true;
        h();
        this.f3079d = false;
        if (this.f3076a != null) {
            try {
                if (Build.VERSION.SDK_INT >= 18) {
                    this.f3076a.quitSafely();
                } else {
                    this.f3076a.quit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.f3076a = new HandlerThreadC0202a<>();
        this.f3076a.a(new c<d>() { // from class: com.alibaba.sdk.android.push.f.a.2
            @Override // com.alibaba.sdk.android.push.f.c
            public void a(d dVar, e eVar) {
                if (eVar.a().getCode().equals(com.alibaba.sdk.android.push.common.a.d.f3049a.getCode())) {
                    synchronized (a.class) {
                        a.this.f3079d = true;
                        a.this.f3077b = false;
                        if (a.this.f3076a != null) {
                            a.this.f3076a.b();
                            a.this.f3076a.quit();
                        }
                    }
                }
                com.alibaba.sdk.android.push.f.b.a(commonCallback, eVar);
            }
        });
        this.f3076a.start();
        this.f3076a.getLooper();
        f.d("getLooper called.");
    }

    public void a(final PushControlService.ConnectionChangeListener connectionChangeListener) {
        try {
            ACCSClient.getAccsClient("AliyunPush").addConnectionListener(new ConnectionListener() { // from class: com.alibaba.sdk.android.push.f.a.3
                @Override // com.taobao.accs.ConnectionListener
                public void onConnect() {
                    connectionChangeListener.onConnect();
                }

                @Override // com.taobao.accs.ConnectionListener
                public void onDisconnect(int i2, String str) {
                    ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.a(i2, str).build();
                    connectionChangeListener.onDisconnect(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                }
            });
        } catch (AccsException e) {
            e.printStackTrace();
        }
    }

    public void a(boolean z, long j2) {
        b(z, j2);
    }

    public void b() {
        b(false, 0L);
    }

    public synchronized void b(CommonCallback commonCallback) {
        try {
            this.f3078c = false;
            l = false;
            this.f3079d = true;
            this.f3077b = false;
            if (this.f3076a != null) {
                this.f3076a.b();
                this.f3076a.quit();
            }
            com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
            bVarA.b(null);
            bVarA.c(null);
            f.d("endReg success");
            if (commonCallback != null) {
                commonCallback.onSuccess(ReportProgressUtil.CODE_OK);
            }
        } catch (Exception e) {
            if (commonCallback != null) {
                commonCallback.onSuccess(ReportProgressUtil.CODE_OK + e);
            }
        }
    }

    public boolean c() {
        try {
            return ACCSClient.getAccsClient("AliyunPush").isConnected();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public void d() {
        try {
            ACCSClient.getAccsClient("AliyunPush").reconnect();
        } catch (AccsException e) {
            e.printStackTrace();
        }
    }

    public void e() {
        TaobaoRegister.reset();
        this.f3078c = false;
    }

    public void f() {
        try {
            ACCSClient.getAccsClient("AliyunPush").disconnect();
        } catch (AccsException e) {
            e.printStackTrace();
        }
    }
}
