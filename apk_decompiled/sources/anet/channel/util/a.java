package anet.channel.util;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a extends InputStream {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private InputStream f1935a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f1936b = 0;

    public a(InputStream inputStream) {
        this.f1935a = null;
        if (inputStream == null) {
            throw new NullPointerException("input stream cannot be null");
        }
        this.f1935a = inputStream;
    }

    public long a() {
        return this.f1936b;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        this.f1936b++;
        return this.f1935a.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = this.f1935a.read(bArr, i, i2);
        if (i3 != -1) {
            this.f1936b += (long) i3;
        }
        return i3;
    }
}
