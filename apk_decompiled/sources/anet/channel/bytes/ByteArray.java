package anet.channel.bytes;

import anet.channel.bytes.a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class ByteArray implements Comparable<ByteArray> {
    final byte[] buffer;
    int bufferLength;
    int dataLength;

    private ByteArray(byte[] bArr, int i) {
        this.buffer = bArr == null ? new byte[i] : bArr;
        this.bufferLength = this.buffer.length;
        this.dataLength = i;
    }

    public static ByteArray create(int i) {
        return new ByteArray(null, i);
    }

    public static ByteArray wrap(byte[] bArr, int i) {
        if (bArr == null || i < 0 || i > bArr.length) {
            return null;
        }
        return new ByteArray(bArr, i);
    }

    public static ByteArray wrap(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return wrap(bArr, bArr.length);
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public int getBufferLength() {
        return this.bufferLength;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public void setDataLength(int i) {
        this.dataLength = i;
    }

    public void recycle() {
        if (this.bufferLength == 0) {
            return;
        }
        a.C0170a.f1676a.a(this);
    }

    public int readFrom(InputStream inputStream) throws IOException {
        int i = inputStream.read(this.buffer, 0, this.bufferLength);
        this.dataLength = i != -1 ? i : 0;
        return i;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.buffer, 0, this.dataLength);
    }

    @Override // java.lang.Comparable
    public int compareTo(ByteArray byteArray) {
        int i = this.bufferLength;
        int i2 = byteArray.bufferLength;
        if (i != i2) {
            return i - i2;
        }
        if (this.buffer == null) {
            return -1;
        }
        if (byteArray.buffer == null) {
            return 1;
        }
        return hashCode() - byteArray.hashCode();
    }
}
