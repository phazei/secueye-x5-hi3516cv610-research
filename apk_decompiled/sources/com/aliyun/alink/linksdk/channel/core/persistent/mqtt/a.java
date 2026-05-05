package com.aliyun.alink.linksdk.channel.core.persistent.mqtt;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.AResponse;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnRrpcResponseHandle;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeRrpcListener;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentConnectState;
import com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttPublishRequest;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttRrpcRequest;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: compiled from: MqttDefaulCallback.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements MqttCallbackExtended {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Map<String, IOnSubscribeRrpcListener> f4117a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Map<String, IOnSubscribeRrpcListener> f4118b;

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.channel.core.persistent.mqtt.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: MqttDefaulCallback.java */
    public class RunnableC0215a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ boolean f4119a;

        public RunnableC0215a(a aVar, boolean z) {
            this.f4119a = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            IMqttAsyncClient iMqttAsyncClientB = com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().b();
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "connectComplete, reconnect=" + this.f4119a + ", client=" + iMqttAsyncClientB + ",threadId=" + Thread.currentThread());
            if (iMqttAsyncClientB == null || !com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().f()) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "connectComplete, try reconnect");
            } else {
                com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().a(PersistentConnectState.CONNECTED);
                PersistentEventDispatcher.getInstance().broadcastMessage(1, null, null, 0, "reconnect  success");
            }
        }
    }

    /* JADX INFO: compiled from: MqttDefaulCallback.java */
    public class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IOnSubscribeRrpcListener f4120a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ MqttRrpcRequest f4121b;

        public b(IOnSubscribeRrpcListener iOnSubscribeRrpcListener, MqttRrpcRequest mqttRrpcRequest) {
            this.f4120a = iOnSubscribeRrpcListener;
            this.f4121b = mqttRrpcRequest;
        }

        @Override // java.lang.Runnable
        public void run() {
            IOnSubscribeRrpcListener iOnSubscribeRrpcListener = this.f4120a;
            MqttRrpcRequest mqttRrpcRequest = this.f4121b;
            String str = mqttRrpcRequest.topic;
            iOnSubscribeRrpcListener.onReceived(str, mqttRrpcRequest, new c(a.this, str, iOnSubscribeRrpcListener));
        }
    }

    /* JADX INFO: compiled from: MqttDefaulCallback.java */
    public class c implements IOnRrpcResponseHandle {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f4123a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public IOnSubscribeRrpcListener f4124b;

        /* JADX INFO: renamed from: com.aliyun.alink.linksdk.channel.core.persistent.mqtt.a$c$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: MqttDefaulCallback.java */
        public class C0216a implements IOnCallListener {
            public C0216a() {
            }

            @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
            public boolean needUISafety() {
                return c.this.f4124b.needUISafety();
            }

            @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
            public void onFailed(ARequest aRequest, AError aError) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "onRrpcResponse(), publish fail");
                c.this.f4124b.onResponseFailed(c.this.f4123a, aError);
            }

            @Override // com.aliyun.alink.linksdk.channel.core.base.IOnCallListener
            public void onSuccess(ARequest aRequest, AResponse aResponse) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "onRrpcResponse(), publish succ");
                c.this.f4124b.onResponseSuccess(c.this.f4123a);
            }
        }

        public c(a aVar, String str, IOnSubscribeRrpcListener iOnSubscribeRrpcListener) {
            this.f4123a = str;
            this.f4124b = iOnSubscribeRrpcListener;
        }

        @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnRrpcResponseHandle
        public void onRrpcResponse(String str, AResponse aResponse) {
            Object obj;
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "onRrpcResponse(), reply topic = " + str);
            MqttPublishRequest mqttPublishRequest = new MqttPublishRequest();
            mqttPublishRequest.isRPC = false;
            if (TextUtils.isEmpty(str)) {
                mqttPublishRequest.topic = this.f4123a + TmpConstant.URI_TOPIC_REPLY_POST;
            } else {
                mqttPublishRequest.topic = str;
            }
            if (aResponse != null && (obj = aResponse.data) != null) {
                mqttPublishRequest.payloadObj = obj;
            }
            com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().asyncSend(mqttPublishRequest, new C0216a());
        }
    }

    public void a(String str, IOnSubscribeRrpcListener iOnSubscribeRrpcListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "registerRrpcListener(), topic = " + str);
        if (TextUtils.isEmpty(str) || iOnSubscribeRrpcListener == null) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "registerRrpcListener(), params error ");
            return;
        }
        if (this.f4117a == null) {
            this.f4117a = new HashMap();
        }
        if (this.f4118b == null) {
            this.f4118b = new HashMap();
        }
        if (!str.contains(MqttTopic.MULTI_LEVEL_WILDCARD) && !str.contains(MqttTopic.SINGLE_LEVEL_WILDCARD)) {
            this.f4117a.put(str, iOnSubscribeRrpcListener);
        } else {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "registerRrpcListener(), pattern topic ");
            this.f4118b.put(str, iOnSubscribeRrpcListener);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallbackExtended
    public void connectComplete(boolean z, String str) {
        com.aliyun.alink.linksdk.channel.core.utils.a.c("MqttDefaulCallback", "mqtt connectComplete,reconnect = " + z + " ," + str);
        if (z) {
            ThreadTools.submitTask(new RunnableC0215a(this, z), true, 1000);
        } else {
            com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().a(PersistentConnectState.CONNECTED);
            PersistentEventDispatcher.getInstance().broadcastMessage(1, null, null, 0, "connect success");
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void connectionLost(Throwable th) {
        com.aliyun.alink.linksdk.channel.core.utils.a.d("MqttDefaulCallback", "mqtt connectionLost,cause:" + th);
        if (th != null) {
            th.printStackTrace();
        }
        com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().a(PersistentConnectState.DISCONNECTED);
        if (!(th instanceof MqttException)) {
            PersistentEventDispatcher.getInstance().broadcastMessage(2, null, null, 4201, "connection lost " + th);
            return;
        }
        MqttException mqttException = (MqttException) th;
        PersistentEventDispatcher.getInstance().broadcastMessage(2, null, null, mqttException.getReasonCode(), mqttException.getMessage() + "，" + mqttException);
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        StringBuilder sb = new StringBuilder();
        sb.append("deliveryComplete! ");
        sb.append((iMqttDeliveryToken == null || iMqttDeliveryToken.getResponse() == null) ? TmpConstant.GROUP_ROLE_UNKNOWN : iMqttDeliveryToken.getResponse().getKey());
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", sb.toString());
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void messageArrived(String str, MqttMessage mqttMessage) {
        try {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "messageArrived,topic = [" + str + "] , msg = [" + new String(mqttMessage.getPayload(), "UTF-8") + "],  ");
            try {
                PersistentEventDispatcher.getInstance().broadcastMessage(3, str, mqttMessage.getPayload(), 0, null);
            } catch (Exception unused) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "messageArrived(), send broadcastMsg error");
            }
            if (this.f4117a != null && this.f4117a.containsKey(str)) {
                MqttRrpcRequest mqttRrpcRequest = new MqttRrpcRequest();
                mqttRrpcRequest.setTopic(str);
                mqttRrpcRequest.payloadObj = mqttMessage.getPayload();
                a(mqttRrpcRequest, this.f4117a.get(str));
            } else if (this.f4118b != null && this.f4118b.size() > 0) {
                Iterator<String> it = this.f4118b.keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String next = it.next();
                    if (a(next, str)) {
                        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "messageArrived(), match pattern");
                        MqttRrpcRequest mqttRrpcRequest2 = new MqttRrpcRequest();
                        mqttRrpcRequest2.setTopic(str);
                        mqttRrpcRequest2.payloadObj = mqttMessage.getPayload();
                        a(mqttRrpcRequest2, this.f4118b.get(next));
                        break;
                    }
                }
            }
            com.aliyun.alink.linksdk.channel.core.persistent.mqtt.b.i().a(str, mqttMessage);
        } catch (Throwable th) {
            com.aliyun.alink.linksdk.channel.core.utils.a.b("MqttDefaulCallback", "messageArrived() handle error:" + th.toString());
        }
    }

    public final void a(MqttRrpcRequest mqttRrpcRequest, IOnSubscribeRrpcListener iOnSubscribeRrpcListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "handleRrpcRequest()");
        if (iOnSubscribeRrpcListener == null || mqttRrpcRequest == null) {
            return;
        }
        if (iOnSubscribeRrpcListener.needUISafety()) {
            ThreadTools.runOnUiThread(new b(iOnSubscribeRrpcListener, mqttRrpcRequest));
        } else {
            String str = mqttRrpcRequest.topic;
            iOnSubscribeRrpcListener.onReceived(str, mqttRrpcRequest, new c(this, str, iOnSubscribeRrpcListener));
        }
    }

    public final boolean a(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                if (str.contains(MqttTopic.MULTI_LEVEL_WILDCARD) && str2.startsWith(str.split(MqttTopic.MULTI_LEVEL_WILDCARD)[0])) {
                    return true;
                }
                if (str.contains(MqttTopic.SINGLE_LEVEL_WILDCARD)) {
                    String str3 = str.split("\\+")[0];
                    String str4 = str.split("\\+", 2)[1];
                    if (str2.startsWith(str3)) {
                        if (str2.endsWith(str4)) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttDefaulCallback", "isTopicMatchForPattern(),e = " + e.toString());
            }
        }
        return false;
    }
}
