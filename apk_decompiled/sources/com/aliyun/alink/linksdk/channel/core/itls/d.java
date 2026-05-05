package com.aliyun.alink.linksdk.channel.core.itls;

import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.id2.Id2Itls;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: compiled from: ITLSOutputStream.java */
/* JADX INFO: loaded from: classes2.dex */
public class d extends OutputStream {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Id2Itls f4099a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public long f4100b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public byte[] f4101c;

    public d(Id2Itls id2Itls, long j) {
        this.f4099a = null;
        this.f4101c = null;
        this.f4099a = id2Itls;
        this.f4100b = j;
        this.f4101c = new byte[1024];
    }

    @Override // java.io.OutputStream
    public synchronized void write(int i) {
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i, int i2) {
        int i3;
        if (bArr == null) {
            return;
        }
        if (i >= 0) {
            try {
                try {
                    try {
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (IndexOutOfBoundsException e2) {
                    e2.printStackTrace();
                }
            } catch (IOException e3) {
                throw e3;
            } catch (Exception e4) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("ITLSOutputStream", "itls write exception " + e4);
            }
            if (i <= bArr.length && i2 >= 0 && (i3 = i + i2) <= bArr.length && i3 >= 0) {
                if (i2 == 0) {
                    return;
                }
                com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSOutputStream", "b.len=" + bArr.length + ", off=" + i + ", len=" + i2);
                for (int i4 = 0; i4 < i2; i4 += 1024) {
                    int iMin = Math.min(1024, i2 - i4);
                    System.arraycopy(bArr, i4, this.f4101c, 0, iMin);
                    int iItlsWrite = this.f4099a.itlsWrite(this.f4100b, this.f4101c, iMin, MqttConfigure.itlsWriteTimeout);
                    com.aliyun.alink.linksdk.channel.core.utils.a.a("ITLSOutputStream", "result=" + iItlsWrite + ", length=" + iMin);
                    if (iItlsWrite < iMin) {
                        throw new IOException(String.valueOf(32109), new Throwable("itlsWriteErrorDataLen=" + iItlsWrite));
                    }
                }
                super.write(bArr, i, i2);
                return;
            }
        }
        throw new IndexOutOfBoundsException();
    }
}
