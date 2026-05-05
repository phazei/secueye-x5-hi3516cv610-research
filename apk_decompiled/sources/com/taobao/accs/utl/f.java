package com.taobao.accs.utl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class f extends ByteArrayInputStream {
    public f(byte[] bArr) {
        super(bArr);
    }

    public int a() {
        return read() & 255;
    }

    public int b() {
        return (a() << 8) | a();
    }

    public String a(int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = read(bArr);
        if (i2 == i) {
            return new String(bArr, "utf-8");
        }
        throw new IOException("read len not match. ask for " + i + " but read for " + i2);
    }

    public byte[] c() throws IOException {
        byte[] bArr = new byte[available()];
        read(bArr);
        return bArr;
    }
}
