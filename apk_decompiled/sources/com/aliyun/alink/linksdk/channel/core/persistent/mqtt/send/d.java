package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send;

import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request.MqttSubscribeRequest;

/* JADX INFO: compiled from: MqttSendResponseRunnable.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public b f4143a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public byte f4144b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f4145c;

    public d(b bVar, byte b2, String str) {
        this.f4143a = null;
        this.f4144b = (byte) 0;
        this.f4145c = null;
        this.f4143a = bVar;
        this.f4144b = b2;
        this.f4145c = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        b bVar = this.f4143a;
        if (bVar == null) {
        }
        switch (this.f4144b) {
            case 1:
                if (bVar.getListener() != null) {
                    this.f4143a.getListener().onSuccess(this.f4143a.getRequest(), this.f4143a.getResponse());
                    break;
                }
                break;
            case 2:
            case 3:
                if (bVar.getListener() != null) {
                    AError aError = new AError();
                    if (this.f4144b == 3) {
                        aError.setCode(4101);
                    } else {
                        aError.setCode(4201);
                    }
                    aError.setMsg(this.f4145c);
                    this.f4143a.getListener().onFailed(this.f4143a.getRequest(), aError);
                    break;
                }
                break;
            case 4:
                if (bVar.a() != null) {
                    this.f4143a.a().onSuccess(((MqttSubscribeRequest) this.f4143a.getRequest()).topic);
                    break;
                }
                break;
            case 5:
            case 6:
                if (bVar.a() != null) {
                    AError aError2 = new AError();
                    if (this.f4144b == 3) {
                        aError2.setCode(4101);
                    } else {
                        aError2.setCode(4201);
                    }
                    aError2.setMsg(this.f4145c);
                    this.f4143a.a().onFailed(((MqttSubscribeRequest) this.f4143a.getRequest()).topic, aError2);
                    break;
                }
                break;
        }
    }
}
