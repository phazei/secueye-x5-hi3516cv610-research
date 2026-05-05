package com.aliyun.alink.linksdk.channel.core.persistent.mqtt;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.AResponse;
import com.aliyun.alink.linksdk.channel.core.base.ASend;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeRrpcListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentConnectState;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentInitParams;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttSubscribeRequestParams;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.PersisitentNetParams;
import com.aliyun.alink.linksdk.id2.Id2ItlsSdk;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.NetTools;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.eclipse.paho.client.mqttv3.AlarmMqttPingSender;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/* JADX INFO: compiled from: MqttNet.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements IPersisitentNet {
    public static final Object o = new Object();
    public static String[] p = {"register", "regnwl"};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f4126a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public MemoryPersistence f4127b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public IMqttAsyncClient f4128c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public SSLSocketFactory f4129d;
    public MqttConnectOptions e;
    public InputStream f;
    public AtomicBoolean g;
    public AtomicBoolean h;
    public AtomicBoolean i;
    public PersistentConnectState j;
    public com.aliyun.alink.linksdk.channel.core.persistent.mqtt.a k;
    public IOnCallListener l;
    public MqttInitParams m;
    public AtomicBoolean n;

    /* JADX INFO: compiled from: MqttNet.java */
    public class a implements IMqttActionListener {
        public a(b bVar) {
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onFailure(IMqttToken iMqttToken, Throwable th) {
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onSuccess(IMqttToken iMqttToken) {
        }
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b$b, reason: collision with other inner class name */
    /* JADX INFO: compiled from: MqttNet.java */
    public class C0217b implements IMqttActionListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IMqttActionListener f4130a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ AtomicBoolean f4131b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final /* synthetic */ CountDownLatch f4132c;

        public C0217b(IMqttActionListener iMqttActionListener, AtomicBoolean atomicBoolean, CountDownLatch countDownLatch) {
            this.f4130a = iMqttActionListener;
            this.f4131b = atomicBoolean;
            this.f4132c = countDownLatch;
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onFailure(IMqttToken iMqttToken, Throwable th) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "onFailure() called with: iMqttToken = [" + iMqttToken + "], throwable = [" + th + "], callback = [" + this.f4130a + "]");
            b bVar = b.this;
            StringBuilder sb = new StringBuilder();
            sb.append("onFailure -> closeConnect callback = ");
            sb.append(this.f4130a);
            sb.append(", hasCallback = ");
            sb.append(this.f4131b);
            bVar.b(sb.toString());
            if (this.f4131b.compareAndSet(false, true)) {
                b.this.h();
                b.this.i.set(false);
                com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "mqtt disconnect finished and callback onFailure. " + hashCode());
                IMqttActionListener iMqttActionListener = this.f4130a;
                if (iMqttActionListener != null) {
                    iMqttActionListener.onFailure(iMqttToken, th);
                }
            }
            CountDownLatch countDownLatch = this.f4132c;
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onSuccess(IMqttToken iMqttToken) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "onSuccess() called with: iMqttToken = [" + iMqttToken + "], callback = [" + this.f4130a + "]");
            b.this.b("onSuccess -> closeConnect callback = " + this.f4130a + ", hasCallback = " + this.f4131b);
            if (this.f4131b.compareAndSet(false, true)) {
                b.this.h();
                b.this.i.set(false);
                IMqttActionListener iMqttActionListener = this.f4130a;
                if (iMqttActionListener != null) {
                    iMqttActionListener.onSuccess(iMqttToken);
                }
            }
            CountDownLatch countDownLatch = this.f4132c;
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }

    /* JADX INFO: compiled from: MqttNet.java */
    public class c implements IOnSubscribeListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IOnSubscribeRrpcListener f4134a;

        public c(b bVar, IOnSubscribeRrpcListener iOnSubscribeRrpcListener) {
            this.f4134a = iOnSubscribeRrpcListener;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
        public boolean needUISafety() {
            return this.f4134a.needUISafety();
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
        public void onFailed(String str, AError aError) {
            this.f4134a.onSubscribeFailed(str, aError);
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
        public void onSuccess(String str) {
            this.f4134a.onSubscribeSuccess(str);
        }
    }

    /* JADX INFO: compiled from: MqttNet.java */
    public class d implements IMqttActionListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IOnCallListener f4135a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ Context f4136b;

        public d(b bVar, IOnCallListener iOnCallListener, Context context) {
            this.f4135a = iOnCallListener;
            this.f4136b = context;
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onFailure(IMqttToken iMqttToken, Throwable th) {
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "dynamicRegister mqtt connect onFailure, exce = " + th.toString());
            if (th instanceof MqttException) {
                MqttException mqttException = (MqttException) th;
                if (this.f4135a != null) {
                    AError aError = new AError();
                    aError.setCode(mqttException.getReasonCode());
                    aError.setMsg(mqttException.toString());
                    this.f4135a.onFailed(null, aError);
                    return;
                }
                return;
            }
            if (this.f4135a != null) {
                AError aError2 = new AError();
                if (NetTools.isAvailable(this.f4136b)) {
                    aError2.setCode(4201);
                    aError2.setMsg("dynamicRegister mqtt connect failed. " + th.toString());
                } else {
                    aError2.setCode(4101);
                    aError2.setMsg("dynamicRegister mqtt connect failed, invalid network. " + th.toString());
                }
                this.f4135a.onFailed(null, aError2);
            }
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onSuccess(IMqttToken iMqttToken) {
            com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "dynamicRegister mqtt connect onSuccess");
        }
    }

    /* JADX INFO: compiled from: MqttNet.java */
    public class e implements IMqttActionListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ Map f4137a;

        public e(Map map) {
            this.f4137a = map;
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onFailure(IMqttToken iMqttToken, Throwable th) {
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "mqtt connect onFailure, exce = " + th.toString());
            b.this.g.set(false);
            b.this.h.set(false);
            b.this.j = PersistentConnectState.CONNECTFAIL;
            this.f4137a.put("endTime-connect", String.valueOf(System.currentTimeMillis()));
            this.f4137a.put("result", "0");
            if (th instanceof MqttException) {
                MqttException mqttException = (MqttException) th;
                this.f4137a.put("errorCode", String.valueOf(mqttException.getReasonCode()));
                PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, mqttException.getReasonCode(), mqttException.toString());
            } else {
                PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4201, th.toString());
                this.f4137a.put("errorCode", String.valueOf(4201));
            }
            if (b.this.n.compareAndSet(false, true)) {
                com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils.e.a("mqtt-connect", this.f4137a);
            }
        }

        @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
        public void onSuccess(IMqttToken iMqttToken) {
            com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "mqtt connect onSuccess");
            if (b.this.g.compareAndSet(true, false)) {
                b.this.h.set(true);
                b.this.j = PersistentConnectState.CONNECTED;
                this.f4137a.put("endTime-connect", String.valueOf(System.currentTimeMillis()));
                this.f4137a.put("result", "1");
                if (b.this.n.compareAndSet(false, true)) {
                    com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils.e.a("mqtt-connect", this.f4137a);
                }
            }
        }
    }

    /* JADX INFO: compiled from: MqttNet.java */
    public static class f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final b f4139a = new b(null);
    }

    public /* synthetic */ b(a aVar) {
        this();
    }

    public static b i() {
        return f.f4139a;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.base.INet
    public ASend asyncSend(ARequest aRequest, IOnCallListener iOnCallListener) {
        com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.b bVar = new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.b(aRequest, iOnCallListener);
        new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.c().asyncSend(bVar);
        return bVar;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void destroy() {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "destroy()");
        this.n.set(false);
        try {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "before destroy with no params." + System.currentTimeMillis());
            destroy(10000L, null, new a(this));
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "after destroy with no params." + System.currentTimeMillis());
        } catch (MqttException e2) {
            com.aliyun.alink.linksdk.channel.core.utils.a.d("MqttNet", "destroy exception=" + e2);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void dynamicRegister(Context context, PersistentInitParams persistentInitParams, IOnCallListener iOnCallListener) {
        String str;
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "mqttDynamicRegister() called with: persistentInitParams = [" + persistentInitParams + "], listener = [" + iOnCallListener + "]");
        this.f4127b = new MemoryPersistence();
        this.f4126a = context;
        if (!(persistentInitParams instanceof MqttInitParams)) {
            if (iOnCallListener != null) {
                AError aError = new AError();
                aError.setMsg("init params should be instance of MqttInitParams.");
                iOnCallListener.onFailed(null, aError);
                return;
            }
            return;
        }
        MqttInitParams mqttInitParams = (MqttInitParams) persistentInitParams;
        if (!mqttInitParams.checkValid() || TextUtils.isEmpty(mqttInitParams.productSecret) || TextUtils.isEmpty(a(mqttInitParams.registerType, false))) {
            if (iOnCallListener != null) {
                AError aError2 = new AError();
                aError2.setMsg("init params invalid.");
                iOnCallListener.onFailed(null, aError2);
                return;
            }
            return;
        }
        MqttConfigure.productKey = mqttInitParams.productKey;
        MqttConfigure.deviceName = mqttInitParams.deviceName;
        MqttConfigure.productSecret = mqttInitParams.productSecret;
        String strReplace = MqttConfigure.mqttHost;
        if (TextUtils.isEmpty(strReplace)) {
            if (MqttConfigure.SECURE_MODE == 8) {
                if (iOnCallListener != null) {
                    AError aError3 = new AError();
                    aError3.setMsg("init params invalid. itls do not support dynamic register.");
                    iOnCallListener.onFailed(null, aError3);
                    return;
                }
                return;
            }
            strReplace = MqttConfigure.DEFAULT_REGISTER_TLS_HOST;
        }
        if (strReplace.contains("${productKey}")) {
            strReplace = strReplace.replace("${productKey}", MqttConfigure.productKey);
        }
        if (MqttConfigure.SECURE_MODE == 3 && !strReplace.startsWith("tcp://")) {
            strReplace = "tcp://" + strReplace;
        } else if (MqttConfigure.SECURE_MODE != 3 && !strReplace.startsWith("ssl://")) {
            strReplace = "ssl://" + strReplace;
        }
        String str2 = MqttConfigure.clientId;
        if (TextUtils.isEmpty(str2)) {
            str2 = MqttConfigure.deviceName + "&" + MqttConfigure.productKey;
        }
        String strA = com.aliyun.alink.linksdk.channel.core.utils.c.a();
        HashMap map = new HashMap();
        map.put("productKey", MqttConfigure.productKey);
        map.put("deviceName", MqttConfigure.deviceName);
        map.put(AlinkConstants.KEY_RANDOM, strA);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("|securemode=");
        sb.append(MqttConfigure.SECURE_MODE);
        sb.append(a(mqttInitParams.registerType, false));
        sb.append(",random=");
        sb.append(strA);
        sb.append(",signmethod=");
        sb.append(MqttConfigure.SIGN_METHOD);
        if (TextUtils.isEmpty(MqttConfigure.registerInstanceId)) {
            str = "";
        } else {
            str = ",instanceId=" + MqttConfigure.registerInstanceId;
        }
        sb.append(str);
        sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
        String string = sb.toString();
        String str3 = MqttConfigure.deviceName + "&" + MqttConfigure.productKey;
        String strA2 = a(map, MqttConfigure.productSecret);
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "mqttClientConnect mqttUsername:" + str3 + " mqttPassword:" + strA2 + " mqttClientId:" + string);
        if (TextUtils.isEmpty(str3) || TextUtils.isEmpty(strA2)) {
            this.g.set(false);
            this.h.set(false);
            if (iOnCallListener != null) {
                AError aError4 = new AError();
                aError4.setMsg("create mqtt client error empty username or password");
                iOnCallListener.onFailed(null, aError4);
                return;
            }
            return;
        }
        if (this.f4128c != null) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "clear mqttAndroidAsyncClient force.");
            try {
                if (this.f4128c != null) {
                    this.f4128c.disconnectForcibly();
                }
            } catch (Exception unused) {
            }
            try {
                if (this.f4128c != null) {
                    this.f4128c.close();
                }
            } catch (Exception unused2) {
            }
            this.f4128c = null;
        }
        try {
            this.f4128c = new com.aliyun.alink.linksdk.channel.core.itls.e(strReplace, string, this.f4127b);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            this.e = mqttConnectOptions;
            mqttConnectOptions.setMqttVersion(4);
            if (MqttConfigure.SECURE_MODE == 2 && MqttConfigure.isCheckRootCrt) {
                synchronized (o) {
                    e();
                    try {
                        SSLSocketFactory sSLSocketFactoryA = a();
                        this.f4129d = sSLSocketFactoryA;
                        this.e.setSocketFactory(sSLSocketFactoryA);
                    } catch (Exception e2) {
                        com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "create SSL Socket error" + e2.toString());
                        e2.printStackTrace();
                    }
                }
            }
            this.e.setAutomaticReconnect(false);
            this.e.setCleanSession(MqttConfigure.cleanSession);
            this.e.setUserName(str3);
            this.e.setPassword(strA2.toCharArray());
            this.e.setKeepAliveInterval(MqttConfigure.getKeepAliveInterval());
            this.e.setMaxInflight(MqttConfigure.maxInflight);
            this.f4128c.setCallback(this.k);
            this.l = iOnCallListener;
            try {
                com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "dynamicRegister mqtt client connect..." + strReplace);
                this.f4128c.connect(this.e, null, new d(this, iOnCallListener, context));
            } catch (MqttException e3) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", " dynamicRegister mqtt connect error,e" + e3.toString());
                e3.printStackTrace();
                if (iOnCallListener != null) {
                    AError aError5 = new AError();
                    aError5.setCode(e3.getReasonCode());
                    aError5.setMsg("dynamicRegister mqtt connect exception, " + e3.toString());
                    iOnCallListener.onFailed(null, aError5);
                }
            } catch (Exception e4) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "dynamicRegister mqtt connect error,e" + e4.toString());
                e4.printStackTrace();
                if (iOnCallListener != null) {
                    AError aError6 = new AError();
                    if (NetTools.isAvailable(context)) {
                        aError6.setCode(4201);
                        aError6.setMsg("dynamicRegister mqtt connect exception. " + e4.toString());
                    } else {
                        aError6.setCode(4101);
                        aError6.setMsg("dynamicRegister mqtt connect exception, invalid network. " + e4.toString());
                    }
                    iOnCallListener.onFailed(null, aError6);
                }
            }
        } catch (Exception e5) {
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "create mqtt client error,e" + e5.toString());
            e5.printStackTrace();
            if (iOnCallListener != null) {
                AError aError7 = new AError();
                aError7.setMsg("create mqtt client error " + e5.toString());
                iOnCallListener.onFailed(null, aError7);
            }
        }
    }

    public boolean f() {
        try {
            if (this.f4128c != null) {
                return this.f4128c.isConnected();
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public final void g() {
        String str;
        String strA;
        this.f4127b = new MemoryPersistence();
        String str2 = System.currentTimeMillis() + "";
        String strReplace = MqttConfigure.mqttHost;
        if (TextUtils.isEmpty(strReplace)) {
            strReplace = MqttConfigure.SECURE_MODE == 8 ? MqttConfigure.DEFAULT_ITLS_HOST : MqttConfigure.DEFAULT_HOST;
        }
        if (strReplace.contains("${productKey}")) {
            strReplace = strReplace.replace("${productKey}", MqttConfigure.productKey);
        }
        if (MqttConfigure.SECURE_MODE == 3 && !strReplace.startsWith("tcp://")) {
            strReplace = "tcp://" + strReplace;
        } else if (MqttConfigure.SECURE_MODE != 3 && !strReplace.startsWith("ssl://")) {
            strReplace = "ssl://" + strReplace;
        }
        String str3 = MqttConfigure.clientId;
        if (TextUtils.isEmpty(str3)) {
            str3 = MqttConfigure.deviceName + "&" + MqttConfigure.productKey;
        }
        HashMap map = new HashMap();
        map.put("productKey", MqttConfigure.productKey);
        map.put("deviceName", MqttConfigure.deviceName);
        map.put(TmpConstant.KEY_CLIENT_ID, str3);
        String strA2 = a((String) null, true);
        String str4 = MqttConfigure.uuid;
        if (str4 == null || str4.isEmpty() || (strA = a(MqttConfigure.uuid)) == null || strA.isEmpty()) {
            str = null;
        } else {
            str = ",_uuid=" + strA;
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "uuid data" + str);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str3);
        sb.append("|securemode=");
        sb.append(strA2.contains("connwl") ? -2 : MqttConfigure.SECURE_MODE);
        sb.append(",_v=");
        sb.append(PersistentNet.getInstance().getSDKVersion());
        sb.append(",lan=Android,os=");
        sb.append(Build.VERSION.RELEASE);
        sb.append(",signmethod=");
        sb.append(MqttConfigure.SIGN_METHOD);
        sb.append(strA2);
        sb.append(TextUtils.isEmpty(MqttConfigure.extraMqttClientIdItems) ? "" : MqttConfigure.extraMqttClientIdItems);
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        sb.append(str);
        sb.append(",ext=1|");
        String string = sb.toString();
        String str5 = "";
        String strA3 = "";
        if (!TextUtils.isEmpty(MqttConfigure.deviceSecret)) {
            str5 = MqttConfigure.deviceName + "&" + MqttConfigure.productKey;
            strA3 = a(map, MqttConfigure.deviceSecret);
        } else if (!TextUtils.isEmpty(MqttConfigure.mqttUserName) && !TextUtils.isEmpty(MqttConfigure.mqttPassWord)) {
            str5 = MqttConfigure.mqttUserName;
            strA3 = MqttConfigure.mqttPassWord;
            string = MqttConfigure.mqttClientId;
        }
        if (!TextUtils.isEmpty(MqttConfigure.clientId) && !TextUtils.isEmpty(MqttConfigure.deviceToken)) {
            str5 = MqttConfigure.deviceName + "&" + MqttConfigure.productKey;
            strA3 = MqttConfigure.deviceToken;
        }
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "mqttClientConnect mqttUsername:" + str5 + " mqttPassword:" + strA3 + " mqttClientId:" + string);
        if (TextUtils.isEmpty(str5) || TextUtils.isEmpty(strA3)) {
            this.g.set(false);
            this.h.set(false);
            this.j = PersistentConnectState.CONNECTFAIL;
            PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4201, "create mqtt client error empty username or password");
            return;
        }
        if (this.f4128c != null) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "clear mqttAndroidAsyncClient force.");
            try {
                if (this.f4128c != null) {
                    this.f4128c.disconnectForcibly();
                }
            } catch (Exception unused) {
            }
            try {
                if (this.f4128c != null) {
                    this.f4128c.close();
                }
            } catch (Exception unused2) {
            }
            this.f4128c = null;
        }
        try {
            if (MqttConfigure.pingSender != null) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "use user define timer ping sender.");
                this.f4128c = new com.aliyun.alink.linksdk.channel.core.itls.e(strReplace, string, this.f4127b, MqttConfigure.pingSender);
            } else if (DispatchConstants.ANDROID.equals(MqttConfigure.pingSenderType)) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "use android timer ping sender.");
                this.f4128c = new com.aliyun.alink.linksdk.channel.core.itls.e(strReplace, string, this.f4127b, new AlarmMqttPingSender(this.f4126a));
            } else {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "use java timer ping sender.");
                this.f4128c = new com.aliyun.alink.linksdk.channel.core.itls.e(strReplace, string, this.f4127b);
            }
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            this.e = mqttConnectOptions;
            mqttConnectOptions.setMqttVersion(4);
            this.e.setConnectionTimeout(10);
            if (MqttConfigure.SECURE_MODE == 2 && MqttConfigure.isCheckRootCrt) {
                synchronized (o) {
                    e();
                    try {
                        SSLSocketFactory sSLSocketFactoryA = a();
                        this.f4129d = sSLSocketFactoryA;
                        this.e.setSocketFactory(sSLSocketFactoryA);
                    } catch (Exception e2) {
                        com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "create SSL Socket error" + e2.toString());
                        e2.printStackTrace();
                    }
                }
            }
            this.e.setAutomaticReconnect(MqttConfigure.automaticReconnect);
            this.e.setCleanSession(MqttConfigure.cleanSession);
            this.e.setUserName(str5);
            this.e.setPassword(strA3.toCharArray());
            this.e.setKeepAliveInterval(MqttConfigure.getKeepAliveInterval());
            this.e.setMaxInflight(MqttConfigure.maxInflight);
            this.f4128c.setCallback(this.k);
            HashMap map2 = new HashMap();
            map2.put("startTime-connect", String.valueOf(System.currentTimeMillis()));
            try {
                this.j = PersistentConnectState.CONNECTING;
                this.f4128c.connect(this.e, null, new e(map2));
                com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "mqtt client connect..," + strReplace);
            } catch (MqttException e3) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", " mqtt client connect error,e" + e3.toString());
                e3.printStackTrace();
                this.g.set(false);
                this.h.set(false);
                this.j = PersistentConnectState.CONNECTFAIL;
                PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, e3.getReasonCode(), e3.toString());
            } catch (Exception e4) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", " mqtt client connect error,e" + e4.toString());
                e4.printStackTrace();
                this.g.set(false);
                this.h.set(false);
                this.j = PersistentConnectState.CONNECTFAIL;
                PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4201, e4.toString());
            }
        } catch (Exception e5) {
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "create mqtt client error,e" + e5.toString());
            e5.printStackTrace();
            this.h.set(false);
            this.g.set(false);
            this.j = PersistentConnectState.CONNECTFAIL;
            PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4201, "create mqtt client error,e" + e5.toString());
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public PersistentConnectState getConnectState() {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "getConnectState()");
        if (b() == null) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "getConnectState() client is empty");
            this.j = PersistentConnectState.DISCONNECTED;
        } else {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "getConnectState() paho state = " + i().f());
            this.j = i().f() ? PersistentConnectState.CONNECTED : PersistentConnectState.DISCONNECTED;
        }
        return this.j;
    }

    public final void h() {
        try {
            this.j = PersistentConnectState.DISCONNECTED;
            this.f4129d = null;
            this.m = null;
            com.aliyun.alink.linksdk.channel.core.utils.a.d("MqttNet", "connection lost disconnect by user.");
            PersistentEventDispatcher.getInstance().broadcastMessage(2, null, null, 0, "disconnect success");
        } catch (Exception e2) {
            com.aliyun.alink.linksdk.channel.core.utils.a.d("MqttNet", "destroyP(), internal error, e = " + e2.toString());
            e2.printStackTrace();
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void init(Context context, PersistentInitParams persistentInitParams) {
        PersistentConnectState persistentConnectState;
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "init()");
        if (this.g.get() || this.h.get() || (persistentConnectState = this.j) == PersistentConnectState.CONNECTING || persistentConnectState == PersistentConnectState.CONNECTED) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "init(), already init, ignore init call!");
            this.j = PersistentConnectState.CONNECTFAIL;
            PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4300, "is initing or inited.");
            return;
        }
        if (context != null && persistentInitParams != null && (persistentInitParams instanceof MqttInitParams)) {
            MqttInitParams mqttInitParams = (MqttInitParams) persistentInitParams;
            if (mqttInitParams.checkValid()) {
                if (this.i.get()) {
                    com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "is deiniting, return");
                    this.j = PersistentConnectState.CONNECTFAIL;
                    PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4302, "mqtt is deiniting");
                    return;
                }
                this.g.set(true);
                this.h.set(false);
                this.f4126a = context;
                this.m = mqttInitParams;
                LoggerFactory.setLogger(com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils.c.class.getName());
                MqttInitParams mqttInitParams2 = this.m;
                MqttConfigure.productKey = mqttInitParams2.productKey;
                MqttConfigure.productSecret = mqttInitParams2.productSecret;
                MqttConfigure.deviceName = mqttInitParams2.deviceName;
                MqttConfigure.deviceSecret = mqttInitParams2.deviceSecret;
                MqttConfigure.cleanSession = !mqttInitParams2.receiveOfflineMsg;
                int i = mqttInitParams2.secureMode;
                MqttConfigure.SECURE_MODE = i;
                if (i == 8) {
                    Id2ItlsSdk.init(context);
                }
                g();
                return;
            }
        }
        com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "init error ,params error");
        this.j = PersistentConnectState.CONNECTFAIL;
        PersistentEventDispatcher.getInstance().broadcastMessage(7, null, null, 4301, "init error ,params error");
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public boolean isDeiniting() {
        return this.i.get();
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void openLog(boolean z) {
        if (z) {
            ALog.setLevel((byte) 1);
        } else {
            ALog.setLevel((byte) 4);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void reconnect() throws MqttException {
        IMqttAsyncClient iMqttAsyncClient = this.f4128c;
        if (iMqttAsyncClient == null || !(iMqttAsyncClient instanceof MqttAsyncClient)) {
            return;
        }
        ((MqttAsyncClient) iMqttAsyncClient).reconnect();
    }

    @Override // com.aliyun.alink.linksdk.channel.core.base.INet
    public void retry(ASend aSend) {
        new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.c().asyncSend(aSend);
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribe(String str, IOnSubscribeListener iOnSubscribeListener) {
        if (TextUtils.isEmpty(str)) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "subscribe, topic is empty");
            return;
        }
        MqttSubscribeRequest mqttSubscribeRequest = new MqttSubscribeRequest();
        mqttSubscribeRequest.topic = str;
        mqttSubscribeRequest.isSubscribe = true;
        new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.c().asyncSend(new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.b(mqttSubscribeRequest, iOnSubscribeListener));
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribeRrpc(String str, IOnSubscribeRrpcListener iOnSubscribeRrpcListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "subscribeRrpc(),topic = " + str);
        if (TextUtils.isEmpty(str) || iOnSubscribeRrpcListener == null) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "subscribeRrpc(), params error");
            return;
        }
        subscribe(str, new c(this, iOnSubscribeRrpcListener));
        if (this.k != null) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "subscribeRrpc(), registerRrpcListener");
            this.k.a(str, iOnSubscribeRrpcListener);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void unSubscribe(String str, IOnSubscribeListener iOnSubscribeListener) {
        if (TextUtils.isEmpty(str)) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "unSubscribe, topic is empty");
            return;
        }
        MqttSubscribeRequest mqttSubscribeRequest = new MqttSubscribeRequest();
        mqttSubscribeRequest.topic = str;
        mqttSubscribeRequest.isSubscribe = false;
        new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.c().asyncSend(new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.b(mqttSubscribeRequest, iOnSubscribeListener));
    }

    public b() {
        this.g = new AtomicBoolean(false);
        this.h = new AtomicBoolean(false);
        this.i = new AtomicBoolean(false);
        this.j = PersistentConnectState.DISCONNECTED;
        this.k = new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.a();
        this.l = null;
        this.n = new AtomicBoolean(false);
        LoggerFactory.setLogger(com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils.c.class.getName());
    }

    public final void b(String str) {
        try {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "closeConnect " + str);
            if (this.f4128c != null) {
                this.f4128c.close();
            }
        } catch (Exception e2) {
            com.aliyun.alink.linksdk.channel.core.utils.a.d("MqttNet", "closeConnect e = " + e2.toString());
            e2.printStackTrace();
        }
        this.f4128c = null;
    }

    public Context c() {
        return this.f4126a;
    }

    public PersistentInitParams d() {
        return this.m;
    }

    public final void e() {
        if (MqttConfigure.mqttRootCrtFile != null) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "init(),custom cert file");
            this.f = MqttConfigure.mqttRootCrtFile;
            return;
        }
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "init(),default cert file");
        try {
            this.f = this.f4126a.getAssets().open(MqttConfigure.DEFAULT_ROOTCRT);
        } catch (Exception e2) {
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "setCertFile : cannot config cert file：" + e2.getMessage());
        }
    }

    public void a(PersistentConnectState persistentConnectState) {
        this.j = persistentConnectState;
    }

    public void a(String str, MqttMessage mqttMessage) {
        if ((!"/ext/regnwl".equals(str) && !"/ext/register".equals(str)) || mqttMessage == null || mqttMessage.getPayload() == null || this.l == null) {
            return;
        }
        AResponse aResponse = new AResponse();
        aResponse.data = mqttMessage.getPayload();
        this.l.onSuccess(null, aResponse);
        this.l = null;
    }

    public IMqttAsyncClient b() {
        return this.f4128c;
    }

    public final String a(String str, boolean z) {
        return (z && MqttConfigure.SECURE_MODE == 8) ? ",authtype=id2" : z ? (TextUtils.isEmpty(MqttConfigure.deviceToken) || TextUtils.isEmpty(MqttConfigure.clientId)) ? "" : ",authType=connwl" : (TextUtils.isEmpty(str) || p[0].equals(str)) ? ",authType=register" : p[1].equals(str) ? ",authType=regnwl" : "";
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void subscribe(String str, PersisitentNetParams persisitentNetParams, IOnSubscribeListener iOnSubscribeListener) {
        if (TextUtils.isEmpty(str)) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "subscribe, topic is empty");
            return;
        }
        MqttSubscribeRequest mqttSubscribeRequest = new MqttSubscribeRequest();
        mqttSubscribeRequest.topic = str;
        mqttSubscribeRequest.isSubscribe = true;
        if (persisitentNetParams != null && (persisitentNetParams instanceof MqttSubscribeRequestParams)) {
            mqttSubscribeRequest.subscribeRequestParams = (MqttSubscribeRequestParams) persisitentNetParams;
        }
        new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.c().asyncSend(new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send.b(mqttSubscribeRequest, iOnSubscribeListener));
    }

    @Override // com.aliyun.alink.linksdk.channel.core.persistent.IPersisitentNet
    public void destroy(long j, Object obj, Object obj2) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "destroyP() called with: quiesceTimeout = [" + j + "], userContext = [" + obj + "], callback = [" + obj2 + "] " + hashCode());
        if (obj2 instanceof IMqttActionListener) {
            IMqttActionListener iMqttActionListener = (IMqttActionListener) obj2;
            if (this.g.get()) {
                if (iMqttActionListener != null) {
                    iMqttActionListener.onFailure(null, new IllegalStateException("Please wait for init done."));
                    return;
                }
                return;
            }
            this.g.set(false);
            this.h.set(false);
            if (!this.i.compareAndSet(false, true)) {
                if (iMqttActionListener != null) {
                    iMqttActionListener.onFailure(null, new IllegalStateException("Please wait for last deiniting to finish."));
                    return;
                }
                return;
            }
            if (b() == null) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "destroyP(), client is null");
                b("onClientNull -> closeConnect callback=" + iMqttActionListener);
                this.i.set(false);
                com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "mqtt disconnect finished and callback=" + iMqttActionListener + " onFailure. " + hashCode());
                if (iMqttActionListener != null) {
                    iMqttActionListener.onSuccess(null);
                    return;
                }
                return;
            }
            try {
                if (this.f4127b != null) {
                    this.f4127b.close();
                }
            } catch (Exception unused) {
            }
            this.f4127b = null;
            synchronized (o) {
                try {
                    if (this.f != null) {
                        this.f.close();
                    }
                } catch (Exception unused2) {
                }
                this.f = null;
            }
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            try {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "destroyP->disconnect");
                this.f4128c.disconnect(j, obj, new C0217b(iMqttActionListener, atomicBoolean, countDownLatch));
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "destroyP->disconnected");
                try {
                    countDownLatch.await(j, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                if (atomicBoolean.compareAndSet(false, true)) {
                    b("onSuccess -> closeConnect callback = " + iMqttActionListener + ", hasCallback = " + atomicBoolean);
                    h();
                    this.i.set(false);
                    com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "mqtt disconnect finished and callback success. " + hashCode());
                    if (iMqttActionListener != null) {
                        iMqttActionListener.onSuccess(null);
                        return;
                    }
                    return;
                }
                this.i.set(false);
            } catch (Exception e3) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttNet", "destroyP(), error, e = " + e3.toString());
                e3.printStackTrace();
                b("onFailure exception -> closeConnect callback=" + iMqttActionListener + ", hasCallback = " + atomicBoolean);
                if (atomicBoolean.compareAndSet(false, true)) {
                    this.i.set(false);
                    h();
                    com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttNet", "mqtt disconnect finished and callback failure. " + hashCode());
                    if (iMqttActionListener != null) {
                        iMqttActionListener.onFailure(null, e3);
                    }
                }
                countDownLatch.countDown();
            }
        }
    }

    public final SSLSocketFactory a() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sSLContext = SSLContext.getInstance("TLSV1.2");
        sSLContext.init(null, new TrustManager[]{new com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils.d(this.f)}, null);
        return sSLContext.getSocketFactory();
    }

    public final String a(Map<String, String> map, String str) {
        if (map != null && !TextUtils.isEmpty(str)) {
            String[] strArr = (String[]) map.keySet().toArray(new String[0]);
            Arrays.sort(strArr);
            StringBuilder sb = new StringBuilder();
            for (String str2 : strArr) {
                if (!"sign".equalsIgnoreCase(str2)) {
                    sb.append(str2);
                    sb.append(map.get(str2));
                }
            }
            try {
                SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes("utf-8"), MqttConfigure.SIGN_METHOD);
                Mac mac = Mac.getInstance(secretKeySpec.getAlgorithm());
                mac.init(secretKeySpec);
                return a(mac.doFinal(sb.toString().getBytes("utf-8")));
            } catch (Exception e2) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "hmacSign error, e" + e2.toString());
                e2.printStackTrace();
            }
        }
        return null;
    }

    public static final String a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length);
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hexString.toUpperCase());
        }
        return stringBuffer.toString();
    }

    public final String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            String str2 = "";
            for (byte b2 : MessageDigest.getInstance("MD5").digest(str.getBytes())) {
                String hexString = Integer.toHexString(b2 & 255);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                str2 = str2 + hexString;
            }
            return str2.toUpperCase();
        } catch (Exception unused) {
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "md5 calc with exception");
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttNet", "md5 calc failed");
            return "";
        }
    }
}
