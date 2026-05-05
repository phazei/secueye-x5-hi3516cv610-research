package com.aliyun.alink.linksdk.channel.core.itls;

import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.id2.Id2Itls;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

/* JADX INFO: compiled from: ITLSInputStream.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends InputStream {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Id2Itls f4091a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public long f4092b;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public byte[] f4094d;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Object f4093c = new Object();
    public int e = -1;
    public int f = 0;

    public b(Id2Itls id2Itls, long j) {
        this.f4091a = null;
        this.f4094d = null;
        this.f4091a = id2Itls;
        this.f4092b = j;
        this.f4094d = new byte[1024];
    }

    public final void a() throws IOException {
        try {
            com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSInputStream", "read dataLen=" + this.f + ", byteIndex=" + this.e + ",handleId=" + this.f4092b);
            this.e = -1;
            this.f = 0;
            if (this.f4091a != null) {
                this.f = this.f4091a.itlsRead(this.f4092b, this.f4094d, 1024, MqttConfigure.itlsReadTimeout);
            }
            com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSInputStream", "read dataLen=" + this.f + ", byteIndex=" + this.e + ",handleId=" + this.f4092b);
        } catch (Exception e) {
            this.f = 0;
            e.printStackTrace();
        }
        if (this.f >= 0) {
            return;
        }
        throw new IOException(String.valueOf(32109), new Throwable("itlsReadErrorDataLen=" + this.f));
    }

    @Override // java.io.InputStream
    public int available() {
        return super.available();
    }

    @Override // java.io.InputStream
    public synchronized int read() {
        int i;
        synchronized (this.f4093c) {
            if (this.e < 0 || this.e >= this.f - 1) {
                this.e = -1;
                this.f = 0;
                a();
            }
            if (this.f <= 0) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new SocketTimeoutException("NoData");
            }
            int i2 = this.e + 1;
            this.e = i2;
            i = this.f4094d[i2] & 255;
        }
        return i;
    }
}
