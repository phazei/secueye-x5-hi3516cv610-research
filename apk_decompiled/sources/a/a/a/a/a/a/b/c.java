package a.a.a.a.a.a.b;

import a.a.a.a.a.a.a.b;
import a.a.a.a.a.a.b.b;
import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.AResponse;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentConnectState;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.event.IConnectionStateListener;
import com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttInitParams;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileSubscrbieListener;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectConfig;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectState;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: compiled from: MobileChannelImpl.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements IMobileChannel {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f1138a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IMobileConnectListener f1139b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public f f1141d;
    public HashMap<IMobileConnectListener, IConnectionStateListener> e;
    public HashMap<IMobileDownstreamListener, Boolean> f;
    public g g;
    public Queue<String> k;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public MobileConnectState f1140c = MobileConnectState.DISCONNECTED;
    public boolean h = false;
    public boolean i = false;
    public int j = 100;

    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class a implements b.InterfaceC0000b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ MobileConnectConfig f1142a;

        public a(MobileConnectConfig mobileConnectConfig) {
            this.f1142a = mobileConnectConfig;
        }

        @Override // a.a.a.a.a.a.a.b.InterfaceC0000b
        public void a() {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "DynamicHostRequest,onSuccess");
            c.this.a(this.f1142a);
        }

        @Override // a.a.a.a.a.a.a.b.InterfaceC0000b
        public void b() {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "DynamicHostRequest,onFailure");
            if (c.this.f1139b != null) {
                c.this.f1139b.onConnectStateChange(MobileConnectState.CONNECTFAIL);
            }
        }
    }

    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class b implements IOnCallListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IMobileRequestListener f1144a;

        public b(c cVar, IMobileRequestListener iMobileRequestListener) {
            this.f1144a = iMobileRequestListener;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public boolean needUISafety() {
            return true;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public void onFailed(ARequest aRequest, AError aError) {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "bindAccount(),bind, get rsp fail, error = " + aError.getMsg().toString());
            this.f1144a.onFailure(aError);
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public void onSuccess(ARequest aRequest, AResponse aResponse) {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "bindAccount(),bind,get rsp");
            try {
                JSONObject object = JSONObject.parseObject((String) aResponse.data);
                int intValue = object.getIntValue("code");
                String string = object.getString("message");
                if (200 == intValue) {
                    if (this.f1144a != null) {
                        this.f1144a.onSuccess(null);
                        return;
                    }
                    return;
                }
                AError aError = new AError();
                aError.setCode(4103);
                aError.setSubCode(intValue);
                aError.setMsg(string);
                if (this.f1144a != null) {
                    this.f1144a.onFailure(aError);
                }
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.a("MobileChannelImpl", "bindAccount(),bind,get rsp, parse error" + e.toString());
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: renamed from: a.a.a.a.a.a.b.c$c, reason: collision with other inner class name */
    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class C0002c implements IOnCallListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IMobileRequestListener f1145a;

        public C0002c(c cVar, IMobileRequestListener iMobileRequestListener) {
            this.f1145a = iMobileRequestListener;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public boolean needUISafety() {
            return true;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public void onFailed(ARequest aRequest, AError aError) {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "unBindAccount(),unbind, fail, error = " + aError.getMsg().toString());
            this.f1145a.onFailure(aError);
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public void onSuccess(ARequest aRequest, AResponse aResponse) {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "bindAccount(),unbind ,get rsp");
            try {
                JSONObject object = JSONObject.parseObject((String) aResponse.data);
                int intValue = object.getIntValue("code");
                String string = object.getString("message");
                if (200 == intValue) {
                    if (this.f1145a != null) {
                        this.f1145a.onSuccess(null);
                        return;
                    }
                    return;
                }
                AError aError = new AError();
                aError.setCode(4103);
                aError.setSubCode(intValue);
                aError.setMsg(string);
                if (this.f1145a != null) {
                    this.f1145a.onFailure(aError);
                }
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.a("MobileChannelImpl", "bindAccount(),unbind,get rsp, parse error" + e.toString());
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class e implements IMobileSubscrbieListener {
        public e() {
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
        public boolean needUISafety() {
            return false;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
        public void onFailed(String str, AError aError) {
            c.this.i = false;
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "afterConnect(),onFailed, error = " + aError.getMsg().toString());
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
        public void onSuccess(String str) {
            c.this.i = true;
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "afterConnect(),onSuccess, topic=" + str);
        }
    }

    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class f implements IConnectionStateListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public IMobileConnectListener f1148a;

        public f(IMobileConnectListener iMobileConnectListener) {
            this.f1148a = null;
            this.f1148a = iMobileConnectListener;
        }

        public void a(boolean z) {
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.event.IConnectionStateListener
        public void onConnectFail(String str) {
            c.this.f1140c = MobileConnectState.CONNECTFAIL;
            if (c.this.f1139b != null) {
                this.f1148a.onConnectStateChange(MobileConnectState.CONNECTFAIL);
            }
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.event.IConnectionStateListener
        public void onConnected() {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "onConnected() called");
            c.this.f1140c = MobileConnectState.CONNECTED;
            c.this.h = true;
            if (c.this.f1139b != null) {
                this.f1148a.onConnectStateChange(MobileConnectState.CONNECTED);
            }
            c.this.a();
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.event.IConnectionStateListener
        public void onDisconnect() {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "onDisconnect() called");
            c.this.f1140c = MobileConnectState.DISCONNECTED;
            if (c.this.f1139b != null) {
                this.f1148a.onConnectStateChange(MobileConnectState.DISCONNECTED);
            }
        }
    }

    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class g implements IOnPushListener {

        /* JADX INFO: compiled from: MobileChannelImpl.java */
        public class a implements Runnable {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final /* synthetic */ String f1151a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public final /* synthetic */ IMobileDownstreamListener f1152b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public final /* synthetic */ a.a.a.a.a.a.b.a f1153c;

            public a(String str, IMobileDownstreamListener iMobileDownstreamListener, a.a.a.a.a.a.b.a aVar) {
                this.f1151a = str;
                this.f1152b = iMobileDownstreamListener;
                this.f1153c = aVar;
            }

            @Override // java.lang.Runnable
            public void run() {
                String strB = g.this.b(this.f1151a);
                IMobileDownstreamListener iMobileDownstreamListener = this.f1152b;
                String strC = g.this.c(this.f1153c.b());
                if (strB == null) {
                    strB = this.f1151a;
                }
                iMobileDownstreamListener.onCommand(strC, strB);
            }
        }

        public g() {
        }

        public final String c(String str) {
            a.a.a.a.a.a.b.e eVarB;
            if (TextUtils.isEmpty(str) || (eVarB = a.a.a.a.a.a.b.f.b().b(null)) == null) {
                return str;
            }
            String str2 = "/sys/" + eVarB.f1161b + "/" + eVarB.f1162c;
            if (str.contains(str2)) {
                str = str.replace(str2, "");
            }
            return str.contains("/app/down") ? str.replace("/app/down", "") : str;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener
        public void onCommand(String str, byte[] bArr) {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "Common Listener,onCommand, s = " + str);
            if (c.this.f == null || c.this.f.size() == 0) {
                return;
            }
            a.a.a.a.a.a.b.a aVar = new a.a.a.a.a.a.b.a(str, bArr);
            String strA = aVar.a();
            if (TextUtils.isEmpty(strA)) {
                return;
            }
            String strA2 = a(strA);
            if (!TextUtils.isEmpty(strA2)) {
                if (c.this.k == null) {
                    c.this.k = new LinkedList();
                }
                String str2 = aVar.b() + OpenAccountUIConstants.UNDER_LINE + strA2;
                if (c.this.k.contains(str2)) {
                    return;
                }
                if (c.this.k.size() < c.this.j) {
                    c.this.k.offer(str2);
                } else {
                    c.this.k.poll();
                    c.this.k.offer(str2);
                }
            }
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "Common Listener,onCommand,loop,size = " + c.this.f.size());
            for (IMobileDownstreamListener iMobileDownstreamListener : c.this.f.keySet()) {
                if (iMobileDownstreamListener.shouldHandle(c(aVar.b()))) {
                    a.a.a.a.a.a.a.a.a("MobileChannelImpl", "Common Listener,onCommand,notify = " + iMobileDownstreamListener);
                    if (((Boolean) c.this.f.get(iMobileDownstreamListener)).booleanValue()) {
                        ThreadTools.runOnUiThread(new a(strA, iMobileDownstreamListener, aVar));
                    } else {
                        String strB = b(strA);
                        String strC = c(aVar.b());
                        if (strB == null) {
                            strB = strA;
                        }
                        iMobileDownstreamListener.onCommand(strC, strB);
                    }
                }
            }
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.event.IOnPushListener
        public boolean shouldHandle(String str) {
            return true;
        }

        public final String a(String str) {
            try {
                JSONObject object = JSONObject.parseObject(str);
                if (object == null || !object.containsKey("id")) {
                    return null;
                }
                return object.getString("id");
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.a("MobileChannelImpl", "getMsgId(),error = " + e.toString());
                return null;
            }
        }

        public final String b(String str) {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "getParams(),payload = " + str);
            try {
                JSONObject object = JSONObject.parseObject(str);
                if (object == null || !object.containsKey("params")) {
                    return null;
                }
                return object.getString("params");
            } catch (Exception e) {
                a.a.a.a.a.a.a.a.a("MobileChannelImpl", "getParams(),error = " + e.toString());
                e.printStackTrace();
                return null;
            }
        }
    }

    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class h implements IOnCallListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public IMobileRequestListener f1155a;

        public h(c cVar, IMobileRequestListener iMobileRequestListener) {
            this.f1155a = iMobileRequestListener;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public boolean needUISafety() {
            return true;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public void onFailed(ARequest aRequest, AError aError) {
            this.f1155a.onFailure(aError);
        }

        @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
        public void onSuccess(ARequest aRequest, AResponse aResponse) {
            Object obj;
            Object obj2;
            StringBuilder sb = new StringBuilder();
            sb.append("MobileOnCallListener, onSuccess, rsp = ");
            sb.append((aResponse == null || (obj2 = aResponse.data) == null) ? TmpConstant.GROUP_ROLE_UNKNOWN : obj2.toString());
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", sb.toString());
            this.f1155a.onSuccess((aResponse == null || (obj = aResponse.data) == null) ? null : obj.toString());
        }
    }

    public c() {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "MobileChannelImpl(),SDK Version = 1.5.3-cb5ea18");
        this.e = new HashMap<>();
        this.f = new HashMap<>();
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void asyncSendRequest(String str, Map<String, Object> map, Object obj, IMobileRequestListener iMobileRequestListener) {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "asyncSendRequest(), topic = " + str);
        if (b()) {
            PersistentNet.getInstance().asyncSend(new a.a.a.a.a.a.b.d(true, str, map, obj), new h(this, iMobileRequestListener));
        } else if (iMobileRequestListener != null) {
            AError aError = new AError();
            aError.setCode(4101);
            aError.setMsg("mqtt not not connected.");
            iMobileRequestListener.onFailure(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void ayncSendPublishRequest(String str, Object obj, IMobileRequestListener iMobileRequestListener) {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "ayncSendPublishRequest(), topic =" + str);
        if (b()) {
            PersistentNet.getInstance().asyncSend(new a.a.a.a.a.a.b.d(false, str, null, obj), new h(this, iMobileRequestListener));
        } else if (iMobileRequestListener != null) {
            AError aError = new AError();
            aError.setCode(4101);
            aError.setMsg("mqtt not not connected.");
            iMobileRequestListener.onFailure(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void bindAccount(String str, IMobileRequestListener iMobileRequestListener) {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "bindAccount(), iotToken = " + str);
        if (b()) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("iotToken", (Object) str);
            PersistentNet.getInstance().asyncSend(new a.a.a.a.a.a.b.d(true, "/account/bind", null, jSONObject), new b(this, iMobileRequestListener));
        } else if (iMobileRequestListener != null) {
            AError aError = new AError();
            aError.setCode(4101);
            aError.setMsg("mqtt not not connected.");
            iMobileRequestListener.onFailure(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void endConnect() {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "endConnect() called");
        this.f1140c = MobileConnectState.DISCONNECTED;
        this.h = false;
        this.i = false;
        a.a.a.a.a.a.b.f.b().a();
        PersistentNet.getInstance().destroy();
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public String getClientId() {
        a.a.a.a.a.a.b.e eVarB = a.a.a.a.a.a.b.f.b().b(this.f1138a);
        if (eVarB == null || !eVarB.a()) {
            return null;
        }
        return eVarB.f1162c + "&" + eVarB.f1161b;
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public MobileConnectState getMobileConnectState() {
        return this.f1140c;
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void registerConnectListener(boolean z, IMobileConnectListener iMobileConnectListener) {
        if (iMobileConnectListener == null || this.e.containsKey(iMobileConnectListener)) {
            return;
        }
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "registerConnectListener()");
        f fVar = new f(iMobileConnectListener);
        PersistentEventDispatcher.getInstance().registerOnTunnelStateListener(fVar, z);
        this.e.put(iMobileConnectListener, fVar);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void registerDownstreamListener(boolean z, IMobileDownstreamListener iMobileDownstreamListener) {
        if (iMobileDownstreamListener == null || this.f.containsKey(iMobileDownstreamListener)) {
            return;
        }
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "registerDownstreamListener()");
        if (this.g == null) {
            a.a.a.a.a.a.a.a.a("MobileChannelImpl", "registerDownstreamListener(), register common");
            this.g = new g();
            PersistentEventDispatcher.getInstance().registerOnPushListener(this.g, false);
        }
        this.f.put(iMobileDownstreamListener, Boolean.valueOf(z));
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void startConnect(Context context, MobileConnectConfig mobileConnectConfig, IMobileConnectListener iMobileConnectListener) {
        MobileConnectState mobileConnectState;
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "startConnect()," + mobileConnectConfig);
        if (context == null || mobileConnectConfig == null || !mobileConnectConfig.checkValid()) {
            a.a.a.a.a.a.a.a.b("MobileChannelImpl", "startConnect(), param error, config is empty");
            return;
        }
        if (this.h || (mobileConnectState = this.f1140c) == MobileConnectState.CONNECTING || mobileConnectState == MobileConnectState.CONNECTED) {
            a.a.a.a.a.a.a.a.d("MobileChannelImpl", "startConnect(), channel is connecting or connected");
            return;
        }
        if (PersistentNet.getInstance().isDeiniting()) {
            a.a.a.a.a.a.a.a.d("MobileChannelImpl", "startConnect(), channel is deiniting, please wait for deinitig to finish.");
            return;
        }
        this.f1138a = context;
        this.f1139b = iMobileConnectListener;
        if (this.k == null) {
            this.k = new LinkedList();
        }
        if (this.f1141d != null || iMobileConnectListener == null) {
            f fVar = this.f1141d;
            if (fVar != null) {
                fVar.a(true);
                PersistentEventDispatcher.getInstance().registerOnTunnelStateListener(this.f1141d, true);
                this.e.put(iMobileConnectListener, this.f1141d);
            }
        } else {
            this.f1141d = new f(iMobileConnectListener);
            this.f1141d.a(true);
            PersistentEventDispatcher.getInstance().registerOnTunnelStateListener(this.f1141d, true);
            this.e.put(iMobileConnectListener, this.f1141d);
        }
        this.i = false;
        MqttConfigure.mqttRootCrtFile = MobileConnectConfig.channelRootCrtFile;
        MqttConfigure.isCheckRootCrt = mobileConnectConfig.isCheckChannelRootCrt;
        if (!TextUtils.isEmpty(mobileConnectConfig.channelHost)) {
            MqttConfigure.mqttHost = mobileConnectConfig.channelHost;
            a(mobileConnectConfig);
        } else {
            if (!mobileConnectConfig.autoSelectChannelHost) {
                a(mobileConnectConfig);
                return;
            }
            if (!TextUtils.isEmpty(mobileConnectConfig.serverUrlForAutoSelectChannel)) {
                a.a.a.a.a.a.a.b.f1114a = mobileConnectConfig.serverUrlForAutoSelectChannel;
            }
            a.a.a.a.a.a.a.b.a(new a(mobileConnectConfig));
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void subscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener) {
        if (TextUtils.isEmpty(str)) {
            a.a.a.a.a.a.a.a.b("MobileChannelImpl", "subscrbie(), topic is Empty");
            return;
        }
        if (!b()) {
            if (iMobileSubscrbieListener != null) {
                AError aError = new AError();
                aError.setCode(4101);
                aError.setMsg("mqtt not not connected.");
                iMobileSubscrbieListener.onFailed(str, aError);
                return;
            }
            return;
        }
        a.a.a.a.a.a.b.e eVarB = a.a.a.a.a.a.b.f.b().b(null);
        if (!str.startsWith("/sys/") && eVarB != null) {
            str = ("/sys/" + eVarB.f1161b + "/" + eVarB.f1162c + "/app/down/" + str).replace("//", "/");
        }
        PersistentNet.getInstance().subscribe(str, iMobileSubscrbieListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unBindAccount(IMobileRequestListener iMobileRequestListener) {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "unBindAccount()");
        if (b()) {
            PersistentNet.getInstance().asyncSend(new a.a.a.a.a.a.b.d(true, "/account/unbind", null, null), new C0002c(this, iMobileRequestListener));
        } else if (iMobileRequestListener != null) {
            AError aError = new AError();
            aError.setCode(4101);
            aError.setMsg("mqtt not not connected.");
            iMobileRequestListener.onFailure(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unRegisterConnectListener(IMobileConnectListener iMobileConnectListener) {
        if (iMobileConnectListener == null || !this.e.containsKey(iMobileConnectListener)) {
            return;
        }
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "unRegisterConnectListener()");
        PersistentEventDispatcher.getInstance().unregisterOnTunnelStateListener(this.e.get(iMobileConnectListener));
        this.e.remove(iMobileConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unRegisterDownstreamListener(IMobileDownstreamListener iMobileDownstreamListener) {
        if (iMobileDownstreamListener == null || !this.f.containsKey(iMobileDownstreamListener)) {
            return;
        }
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "unRegisterDownstreamListener(),remove ");
        this.f.remove(iMobileDownstreamListener);
        if (this.f.size() != 0 || this.g == null) {
            return;
        }
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "unRegisterDownstreamListener(),remove common");
        PersistentEventDispatcher.getInstance().unregisterOnPushListener(this.g);
        this.g = null;
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void unSubscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener) {
        if (TextUtils.isEmpty(str)) {
            a.a.a.a.a.a.a.a.b("MobileChannelImpl", "subscrbie(), topic is Empty");
            return;
        }
        if (!b()) {
            if (iMobileSubscrbieListener != null) {
                AError aError = new AError();
                aError.setCode(4101);
                aError.setMsg("mqtt not not connected.");
                iMobileSubscrbieListener.onFailed(str, aError);
                return;
            }
            return;
        }
        a.a.a.a.a.a.b.e eVarB = a.a.a.a.a.a.b.f.b().b(null);
        if (!str.startsWith("/sys/") && eVarB != null) {
            str = ("/sys/" + eVarB.f1161b + "/" + eVarB.f1162c + "/app/down/" + str).replace("//", "/");
        }
        PersistentNet.getInstance().unSubscribe(str, iMobileSubscrbieListener);
    }

    public final boolean b() {
        return PersistentNet.getInstance().getConnectState() == PersistentConnectState.CONNECTED;
    }

    public final void a(MobileConnectConfig mobileConnectConfig) {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "getTripleValueAndConnect");
        a.a.a.a.a.a.b.e eVarB = a.a.a.a.a.a.b.f.b().b(this.f1138a);
        if (eVarB != null && eVarB.a()) {
            a(eVarB);
            return;
        }
        if (!Utils.hasSecurityGuardDep()) {
            mobileConnectConfig.securityGuardAuthcode = null;
        }
        a.a.a.a.a.a.b.b.a(this.f1138a, mobileConnectConfig, new d());
    }

    @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileChannel
    public void endConnect(long j, IMqttActionListener iMqttActionListener) {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "endConnect() called with: waitTime = [" + j + "], listener = [" + iMqttActionListener + "]");
        this.f1140c = MobileConnectState.DISCONNECTED;
        this.h = false;
        this.i = false;
        a.a.a.a.a.a.b.f.b().a();
        PersistentNet.getInstance().destroy(j, null, iMqttActionListener);
    }

    public final void a(a.a.a.a.a.a.b.e eVar) {
        MobileConnectState mobileConnectState;
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "connect(),mqttHost = " + MqttConfigure.mqttHost + ", crt = " + MqttConfigure.isCheckRootCrt);
        if (!this.h && (mobileConnectState = this.f1140c) != MobileConnectState.CONNECTING && mobileConnectState != MobileConnectState.CONNECTED) {
            MobileConnectState mobileConnectState2 = MobileConnectState.CONNECTING;
            this.f1140c = mobileConnectState2;
            IMobileConnectListener iMobileConnectListener = this.f1139b;
            if (iMobileConnectListener != null) {
                iMobileConnectListener.onConnectStateChange(mobileConnectState2);
            }
            PersistentNet.getInstance().init(this.f1138a, new MqttInitParams(eVar.f1161b, eVar.f1162c, eVar.f1163d));
            return;
        }
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "connect(), channel is connecting or connected now");
    }

    /* JADX INFO: compiled from: MobileChannelImpl.java */
    public class d implements b.InterfaceC0001b {
        public d() {
        }

        @Override // a.a.a.a.a.a.b.b.InterfaceC0001b
        public void a(a.a.a.a.a.a.b.e eVar) {
            if (eVar != null && eVar.a()) {
                if (!a.a.a.a.a.a.b.f.b().a(c.this.f1138a, eVar)) {
                    a.a.a.a.a.a.a.a.a("MobileChannelImpl", "save trilpe error");
                }
                c.this.a(eVar);
            } else {
                a.a.a.a.a.a.a.a.b("MobileChannelImpl", "mobile Auth onSuccess but value empty");
                c.this.f1140c = MobileConnectState.CONNECTFAIL;
                if (c.this.f1139b != null) {
                    c.this.f1139b.onConnectStateChange(MobileConnectState.CONNECTFAIL);
                }
            }
        }

        @Override // a.a.a.a.a.a.b.b.InterfaceC0001b
        public void a(String str) {
            a.a.a.a.a.a.a.a.b("MobileChannelImpl", "mobile Auth onFailed,msg =" + str);
            c.this.f1140c = MobileConnectState.CONNECTFAIL;
            if (c.this.f1139b != null) {
                c.this.f1139b.onConnectStateChange(MobileConnectState.CONNECTFAIL);
            }
        }
    }

    public final void a() {
        a.a.a.a.a.a.a.a.a("MobileChannelImpl", "afterConnect() isSubFlag=" + this.i);
        if (this.i) {
            return;
        }
        subscrbie(MqttTopic.MULTI_LEVEL_WILDCARD, new e());
    }
}
