package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send;

import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.AResponse;
import com.aliyun.alink.linksdk.channel.core.base.ASend;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.base.ISendStatus;
import com.aliyun.alink.linksdk.channel.core.persistent.BadNetworkException;
import com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttPublishRequest;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.utils.e;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/* JADX INFO: compiled from: MqttSend.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends ASend implements IMqttActionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IOnSubscribeListener f4141a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public HashMap<String, String> f4142b;

    public b(ARequest aRequest, IOnCallListener iOnCallListener) {
        super(aRequest, iOnCallListener);
        this.f4141a = null;
        this.f4142b = new HashMap<>();
        a(MqttSendStatus.waitingToSend);
    }

    public void a(MqttSendStatus mqttSendStatus) {
        this.status = mqttSendStatus;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onFailure(IMqttToken iMqttToken, Throwable th) {
        int i;
        String message = th != null ? th.getMessage() : "MqttNet send failed: unknown error";
        a(MqttSendStatus.completed);
        ARequest aRequest = this.request;
        if (aRequest instanceof MqttSubscribeRequest) {
            byte b2 = 5;
            if (th instanceof BadNetworkException) {
                b2 = 6;
                i = 4101;
            } else {
                i = 4201;
            }
            IOnSubscribeListener iOnSubscribeListener = this.f4141a;
            if (iOnSubscribeListener != null) {
                if (iOnSubscribeListener.needUISafety()) {
                    ThreadTools.runOnUiThread(new d(this, b2, message));
                } else if (b2 == 6) {
                    AError aError = new AError();
                    aError.setCode(4101);
                    this.f4141a.onFailed(((MqttSubscribeRequest) this.request).topic, aError);
                } else {
                    AError aError2 = new AError();
                    aError2.setCode(4201);
                    aError2.setMsg(message);
                    this.f4141a.onFailed(((MqttSubscribeRequest) this.request).topic, aError2);
                }
            }
        } else if (aRequest instanceof MqttPublishRequest) {
            byte b3 = 2;
            if (th instanceof BadNetworkException) {
                b3 = 3;
                i = 4101;
            } else {
                i = 4201;
            }
            IOnCallListener iOnCallListener = this.listener;
            if (iOnCallListener != null) {
                if (iOnCallListener.needUISafety()) {
                    ThreadTools.runOnUiThread(new d(this, b3, message));
                } else if (b3 == 3) {
                    AError aError3 = new AError();
                    aError3.setCode(4101);
                    this.listener.onFailed(this.request, aError3);
                } else {
                    AError aError4 = new AError();
                    aError4.setCode(4201);
                    aError4.setMsg(message);
                    this.listener.onFailed(this.request, aError4);
                }
            }
        } else {
            i = 0;
        }
        a("endTime-send", String.valueOf(System.currentTimeMillis()));
        a("errorCode", String.valueOf(i));
        e.a("mqtt-send", this.f4142b);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onSuccess(IMqttToken iMqttToken) {
        ARequest aRequest = this.request;
        if (!(aRequest instanceof MqttSubscribeRequest)) {
            if (aRequest instanceof MqttPublishRequest) {
                if (!((MqttPublishRequest) aRequest).isRPC) {
                    a(MqttSendStatus.completed);
                    IOnCallListener iOnCallListener = this.listener;
                    if (iOnCallListener != null) {
                        if (iOnCallListener.needUISafety()) {
                            ThreadTools.runOnUiThread(new d(this, (byte) 1, null));
                        } else {
                            this.listener.onSuccess(this.request, this.response);
                        }
                    }
                    a("endTime-send", String.valueOf(System.currentTimeMillis()));
                    e.a("mqtt-send", this.f4142b);
                    return;
                }
                ISendStatus iSendStatus = this.status;
                if (iSendStatus == MqttSendStatus.waitingToSubReply) {
                    a(MqttSendStatus.subReplyed);
                    a("endTime-send", String.valueOf(System.currentTimeMillis()));
                    e.a("mqtt-send", this.f4142b);
                    new c().asyncSend(this);
                    return;
                }
                if (iSendStatus == MqttSendStatus.waitingToPublish) {
                    a(MqttSendStatus.published);
                    a("endTime-send", String.valueOf(System.currentTimeMillis()));
                    e.a("mqtt-send", this.f4142b);
                    return;
                }
                return;
            }
            return;
        }
        a(MqttSendStatus.completed);
        try {
        } catch (Exception unused) {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttSend", "onSuccess(),getGrantedQos");
        }
        boolean z = iMqttToken.getGrantedQos()[0] != 128;
        IOnSubscribeListener iOnSubscribeListener = this.f4141a;
        if (iOnSubscribeListener == null) {
            a("endTime-send", String.valueOf(System.currentTimeMillis()));
            e.a("mqtt-send", this.f4142b);
            return;
        }
        if (iOnSubscribeListener.needUISafety()) {
            ThreadTools.runOnUiThread(new d(this, z ? (byte) 4 : (byte) 5, null));
            a("endTime-send", String.valueOf(System.currentTimeMillis()));
            if (!z) {
                a("errorCode", String.valueOf(4201));
            }
            e.a("mqtt-send", this.f4142b);
            return;
        }
        if (z) {
            this.f4141a.onSuccess(((MqttSubscribeRequest) this.request).topic);
            a("endTime-send", String.valueOf(System.currentTimeMillis()));
            e.a("mqtt-send", this.f4142b);
            return;
        }
        AError aError = new AError();
        aError.setCode(4103);
        aError.setMsg("subACK Failure");
        this.f4141a.onFailed(((MqttSubscribeRequest) this.request).topic, aError);
        a("endTime-send", String.valueOf(System.currentTimeMillis()));
        a("errorCode", String.valueOf(4103));
        e.a("mqtt-send", this.f4142b);
    }

    public IOnSubscribeListener a() {
        return this.f4141a;
    }

    @Override // com.aliyun.alink.linksdk.channel.core.base.ASend
    public MqttSendStatus getStatus() {
        return (MqttSendStatus) this.status;
    }

    public void a(String str, String str2) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttSend", "addTrackData() called with: key = [" + str + "], value = [" + str2 + "]");
        HashMap<String, String> map = this.f4142b;
        if (map != null) {
            map.put(str, str2);
        }
    }

    public void a(String str, MqttMessage mqttMessage) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttSend", "rpcMessageArrived(), topic =" + str + " msg =" + mqttMessage.toString());
        ARequest aRequest = this.request;
        if (aRequest instanceof MqttPublishRequest) {
            MqttPublishRequest mqttPublishRequest = (MqttPublishRequest) aRequest;
            if (mqttPublishRequest.isRPC) {
                ISendStatus iSendStatus = this.status;
                if ((iSendStatus == MqttSendStatus.published || iSendStatus == MqttSendStatus.waitingToPublish) && str.equals(mqttPublishRequest.replyTopic)) {
                    com.aliyun.alink.linksdk.channel.core.utils.a.a("MqttSend", "messageArrived(), match!");
                    a(MqttSendStatus.completed);
                    if (this.response == null) {
                        this.response = new AResponse();
                    }
                    this.response.data = mqttMessage.toString();
                    IOnCallListener iOnCallListener = this.listener;
                    if (iOnCallListener != null) {
                        if (iOnCallListener.needUISafety()) {
                            ThreadTools.runOnUiThread(new d(this, (byte) 1, null));
                        } else {
                            this.listener.onSuccess(this.request, this.response);
                        }
                    }
                }
            }
        }
    }

    public b(ARequest aRequest, IOnSubscribeListener iOnSubscribeListener) {
        super(aRequest, null);
        this.f4141a = null;
        this.f4142b = new HashMap<>();
        this.f4141a = iOnSubscribeListener;
        a(MqttSendStatus.waitingToSend);
    }
}
