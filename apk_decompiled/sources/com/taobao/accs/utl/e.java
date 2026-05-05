package com.taobao.accs.utl;

import java.io.ByteArrayOutputStream;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class e extends ByteArrayOutputStream {
    public e(int i) {
        super(i);
    }

    public e() {
    }

    public e a(byte b2) {
        write(b2);
        return this;
    }

    public e a(short s) {
        write(s >> 8);
        write(s);
        return this;
    }
}
