package com.aliyun.alink.linksdk.channel.core.itls;

import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.id2.Id2Itls;
import com.aliyun.alink.linksdk.tools.ALog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.NetworkModule;

/* JADX INFO: compiled from: ITLSNetworkModule.java */
/* JADX INFO: loaded from: classes2.dex */
public class c implements NetworkModule {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public a f4095a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Id2Itls f4096b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public long f4097c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public boolean f4098d = true;
    public AtomicBoolean e = new AtomicBoolean(false);

    public c(a aVar) {
        this.f4095a = null;
        this.f4096b = null;
        this.f4095a = aVar;
        this.f4096b = new Id2Itls();
        int i = MqttConfigure.itlsLogLevel;
        int level = (ALog.getLevel() & 255) + 2;
        com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSNetworkModule", "itlsDebugLevel = " + i + "， jniLevel=" + level);
        this.f4096b.setItlsDebugLevel(i);
        this.f4096b.setJniDebugLevel(level);
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public InputStream getInputStream() throws IOException {
        if (this.f4098d) {
            throw new IOException("ITLS Channel Closed.");
        }
        return new b(this.f4096b, this.f4097c);
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public OutputStream getOutputStream() throws IOException {
        if (this.f4098d) {
            throw new IOException("ITLS Channel Closed.");
        }
        return new d(this.f4096b, this.f4097c);
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public String getServerURI() {
        return "ssl://" + this.f4095a.f4087a + ":" + this.f4095a.f4088b;
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public void start() throws MqttException {
        com.aliyun.alink.linksdk.channel.core.utils.a.c("ITLSNetworkModule", "start");
        this.f4098d = false;
        try {
            this.f4097c = this.f4096b.establishItls(this.f4095a.f4087a, this.f4095a.f4088b, this.f4095a.f4089c, this.f4095a.f4090d);
        } catch (Exception e) {
            e.printStackTrace();
            this.f4098d = true;
        }
        this.e.set(true);
        com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSNetworkModule", "handleId=" + this.f4097c);
        if (this.f4097c != 0) {
            return;
        }
        com.aliyun.alink.linksdk.channel.core.utils.a.b("ITLSNetworkModule", "establishItls failed.");
        this.f4098d = true;
        throw new MqttException(this.f4096b.getAlertType());
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.NetworkModule
    public void stop() {
        com.aliyun.alink.linksdk.channel.core.utils.a.c("ITLSNetworkModule", "stop");
        try {
            this.f4098d = true;
            if (this.e.compareAndSet(true, false)) {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSNetworkModule", "stop itls destroy.");
                this.f4096b.destroyItls(this.f4097c);
                this.e.set(false);
            } else {
                com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSNetworkModule", "stop itls already destroyed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
