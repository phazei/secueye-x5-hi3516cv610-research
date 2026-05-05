package com.taobao.accs.net;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.Config;
import anet.channel.SessionCenter;
import anet.channel.entity.ConnType;
import anet.channel.entity.ENV;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.StrategyTemplate;
import com.alibaba.sdk.android.error.ErrorCode;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.AccsException;
import com.taobao.accs.client.ClientManager;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UtilityImpl;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import me.jessyan.autosize.BuildConfig;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class b {
    public static final int ACCS_RECEIVE_TIMEOUT = 40000;
    public static final int INAPP = 1;
    public static final int SERVICE = 0;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f6363a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f6364b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected int f6365c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected Context f6366d;
    protected com.taobao.accs.data.d e;
    public ClientManager h;
    public AccsClientConfig i;
    protected String j;
    public String m;
    private Runnable o;
    private ScheduledFuture<?> p;
    protected int f = 0;
    private long n = 0;
    protected volatile boolean g = false;
    protected String k = null;
    protected LinkedHashMap<Integer, Message> l = new LinkedHashMap<Integer, Message>() { // from class: com.taobao.accs.net.BaseConnection$1
        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<Integer, Message> entry) {
            return size() > 10;
        }
    };

    protected String a(int i) {
        if (i == 4) {
            return "DISCONNECTING";
        }
        switch (i) {
            case 1:
                return "CONNECTED";
            case 2:
                return "CONNECTING";
            default:
                return "DISCONNECTED";
        }
    }

    public abstract void a();

    protected abstract void a(Message message, boolean z);

    protected abstract void a(String str, boolean z, String str2);

    public abstract void a(boolean z, boolean z2);

    public abstract boolean a(String str);

    public abstract void b();

    public abstract com.taobao.accs.ut.a.c c();

    protected abstract String d();

    public void e() {
    }

    protected boolean h() {
        return true;
    }

    public abstract boolean l();

    public abstract int m();

    public abstract void n();

    public abstract void o();

    protected b(Context context, int i, String str) {
        this.f6364b = "";
        this.f6365c = i;
        this.f6366d = context.getApplicationContext();
        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
        if (configByTag == null) {
            ALog.e(d(), "BaseConnection config null!!", new Object[0]);
            try {
                configByTag = new AccsClientConfig.Builder().setAppKey(ACCSManager.getDefaultAppkey(context)).setTag(str).build();
            } catch (AccsException e) {
                ALog.e(d(), "BaseConnection build config", e, new Object[0]);
            }
        }
        this.m = configByTag.getTag();
        this.f6364b = configByTag.getAppKey();
        this.i = configByTag;
        this.e = new com.taobao.accs.data.d(context, this);
        this.e.f6313b = this.f6365c;
        ALog.d(d(), "new connection", new Object[0]);
    }

    public void b(Message message, boolean z) {
        long jA = message.a() != 2 ? this.e.f6315d.a(message.H, message.V) : 0L;
        if (jA == -1) {
            ALog.e(d(), "sendMessage ready server limit high", Constants.KEY_DATA_ID, message.q);
            this.e.a(message, AccsErrorCode.SERVIER_HIGH_LIMIT);
            return;
        }
        if (jA == -1000) {
            ALog.e(d(), "sendMessage ready server limit high for brush", Constants.KEY_DATA_ID, message.q);
            this.e.a(message, AccsErrorCode.SERVIER_HIGH_LIMIT_BRUSH);
            return;
        }
        if (jA > 0) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            long j = this.n;
            if (jCurrentTimeMillis > j) {
                message.Q = jA;
            } else {
                message.Q = (j + jA) - System.currentTimeMillis();
            }
            this.n = System.currentTimeMillis() + message.Q;
            ALog.e(d(), "sendMessage ready delayed", Constants.KEY_DATA_ID, message.q, "type", Message.c.b(message.a()), "delay", Long.valueOf(message.Q));
        } else if ("accs".equals(message.H)) {
            ALog.i(d(), "sendMessage ready", Constants.KEY_DATA_ID, message.q, "type", Message.c.b(message.a()), "delay", Long.valueOf(message.Q));
        } else if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(d(), "sendMessage ready", Constants.KEY_DATA_ID, message.q, "type", Message.c.b(message.a()), "delay", Long.valueOf(message.Q));
        }
        try {
            if (TextUtils.isEmpty(this.j)) {
                this.j = UtilityImpl.getDeviceId(this.f6366d);
            }
            if (!message.g()) {
                a(message, z);
            } else {
                this.e.a(message, AccsErrorCode.REQ_TIME_OUT.copy().msg("重试或者延期时超时，不发送").detail(AccsErrorCode.getAllDetails(null)).build());
            }
        } catch (RejectedExecutionException unused) {
            int size = ThreadPoolExecutorFactory.getSendScheduledExecutor().getQueue().size();
            this.e.a(message, AccsErrorCode.MESSAGE_QUEUE_FULL.copy().detail(" " + size).build());
            ALog.e(d(), "sendMessage ready queue full", "size", Integer.valueOf(size));
        }
    }

    protected void a(String str, boolean z, long j) {
        ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new c(this, str, j, z), j, TimeUnit.MILLISECONDS);
    }

    protected boolean a(Message message, int i) {
        boolean z = true;
        try {
        } catch (Throwable th) {
            th = th;
            z = false;
        }
        if (message.R > 3) {
            return false;
        }
        message.R++;
        message.Q = i;
        ALog.e(d(), "reSend dataid:" + message.q + " retryTimes:" + message.R, new Object[0]);
        b(message, true);
        try {
            if (message.e() != null) {
                message.e().take_date = 0L;
                message.e().to_tnet_date = 0L;
                message.e().retry_times = message.R;
                if (message.R == 1) {
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, AlinkConstants.KEY_TOTAL, 0.0d);
                }
            }
        } catch (Throwable th2) {
            th = th2;
            ALog.e(d(), "reSend error", th, new Object[0]);
            this.e.a(message, AccsErrorCode.SEND_LOCAL_EXCEPTION.copy().detail(AccsErrorCode.getExceptionInfo(th)).build());
        }
        return z;
        ALog.e(d(), "reSend error", th, new Object[0]);
        this.e.a(message, AccsErrorCode.SEND_LOCAL_EXCEPTION.copy().detail(AccsErrorCode.getExceptionInfo(th)).build());
        return z;
    }

    protected void b(int i) {
        if (i < 0) {
            ALog.e(d(), "reSendAck", Constants.KEY_DATA_ID, Integer.valueOf(i));
            Message message = this.l.get(Integer.valueOf(i));
            if (message != null) {
                a(message, 5000);
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, BaseMonitor.COUNT_ACK, 0.0d);
            }
        }
    }

    protected void f() {
        if (this.o == null) {
            this.o = new d(this);
        }
        g();
        this.p = ThreadPoolExecutorFactory.getScheduledExecutor().schedule(this.o, 40000L, TimeUnit.MILLISECONDS);
    }

    protected void g() {
        ScheduledFuture<?> scheduledFuture = this.p;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    public String b(String str) {
        String inappHost = this.i.getInappHost();
        StringBuilder sb = new StringBuilder();
        sb.append("https://");
        sb.append(TextUtils.isEmpty(str) ? "" : str);
        sb.append(inappHost);
        String string = sb.toString();
        try {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://");
            if (TextUtils.isEmpty(str)) {
                str = "";
            }
            sb2.append(str);
            sb2.append(inappHost);
            return sb2.toString();
        } catch (Throwable th) {
            ALog.e("InAppConnection", "getHost", th, new Object[0]);
            return string;
        }
    }

    protected void a(Context context) {
        ALog.d(d(), "initAwcn() called with: mAppkey = [" + this.f6364b + "], authCode = [" + this.i.getAuthCode() + "], config.appKey = [" + this.i.getAppKey() + "], config = [" + this.i + "]", new Object[0]);
        try {
            this.f6364b = this.i.getAppKey();
            ENV env = ENV.ONLINE;
            if (AccsClientConfig.mEnv == 2) {
                env = ENV.TEST;
                SessionCenter.switchEnvironment(env);
            } else if (AccsClientConfig.mEnv == 1) {
                env = ENV.PREPARE;
                SessionCenter.switchEnvironment(env);
            }
            try {
                AwcnConfig.setSendConnectInfoByService(false);
            } catch (Throwable unused) {
            }
            String str = (TextUtils.isEmpty(this.i.getAppSecret()) && TextUtils.isEmpty(this.i.getAuthCode()) && !TextUtils.isEmpty(this.f6364b)) ? "0" : this.f6364b;
            SessionCenter.init(context, new Config.Builder().setAppkey(str).setAppSecret(this.i.getAppSecret()).setAuthCode(this.i.getAuthCode()).setEnv(env).setTag(str).build());
            String str2 = ConnType.PK_ACS;
            if (this.i.getInappPubKey() == 10 || this.i.getInappPubKey() == 11) {
                str2 = "open";
            }
            StrategyTemplate.getInstance().registerConnProtocol(this.i.getInappHost(), ConnProtocol.valueOf(ConnType.HTTP2, ConnType.RTT_0, str2, false));
        } catch (Throwable th) {
            ALog.e(d(), "initAwcn", th, new Object[0]);
        }
    }

    public void a(Message message, ErrorCode errorCode) {
        this.e.a(message, errorCode);
    }

    public String i() {
        return this.f6364b;
    }

    public ClientManager j() {
        if (this.h == null) {
            ALog.d(d(), "new ClientManager", Constants.KEY_CONFIG_TAG, this.m);
            this.h = new ClientManager(this.f6366d, this.m, this.i.getInappHost(), this.f6364b);
        }
        return this.h;
    }

    public void b(Context context) {
        try {
            ThreadPoolExecutorFactory.schedule(new e(this, context), 10000L, TimeUnit.MILLISECONDS);
        } catch (Throwable th) {
            ALog.w(d(), "startChannelService", th, new Object[0]);
        }
    }

    protected String c(String str) {
        String deviceId = UtilityImpl.getDeviceId(this.f6366d);
        try {
            deviceId = URLEncoder.encode(deviceId);
        } catch (Throwable th) {
            ALog.e(d(), "buildAuthUrl", th, new Object[0]);
        }
        String strA = UtilityImpl.a(this.f6366d, i(), this.i.getAppSecret(), UtilityImpl.getDeviceId(this.f6366d), this.m);
        StringBuilder sb = new StringBuilder(256);
        sb.append(str);
        sb.append("auth?1=");
        sb.append(deviceId);
        sb.append("&2=");
        sb.append(strA);
        sb.append("&3=");
        sb.append(i());
        if (this.k != null) {
            sb.append("&4=");
            sb.append(this.k);
        }
        sb.append("&5=");
        sb.append(this.f6365c);
        sb.append("&6=");
        sb.append(UtilityImpl.e(this.f6366d));
        sb.append("&7=");
        sb.append(UtilityImpl.a());
        sb.append("&8=");
        sb.append(this.f6365c == 1 ? BuildConfig.VERSION_NAME : Integer.valueOf(Constants.SDK_VERSION_CODE));
        sb.append("&9=");
        sb.append(System.currentTimeMillis());
        sb.append("&10=");
        sb.append(1);
        sb.append("&11=");
        sb.append(Build.VERSION.SDK_INT);
        sb.append("&12=");
        sb.append(this.f6366d.getPackageName());
        sb.append("&13=");
        sb.append(UtilityImpl.i(this.f6366d));
        sb.append("&14=");
        sb.append(this.f6363a);
        sb.append("&15=");
        sb.append(UtilityImpl.c(Build.MODEL));
        sb.append("&16=");
        sb.append(UtilityImpl.c(Build.BRAND));
        sb.append("&17=");
        sb.append(Constants.SDK_VERSION_CODE);
        sb.append("&19=");
        sb.append(!k() ? 1 : 0);
        sb.append("&20=");
        sb.append(this.i.getStoreId());
        return sb.toString();
    }

    public boolean k() {
        return 2 == this.i.getSecurity();
    }

    public void p() {
        ClientManager clientManager = this.h;
        if (clientManager != null) {
            clientManager.clearClients();
        }
        this.g = false;
    }
}
