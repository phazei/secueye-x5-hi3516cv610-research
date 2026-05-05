package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anet.channel.bytes.ByteArray;
import anetwork.channel.aidl.ParcelableInputStream;
import anetwork.channel.entity.g;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class ParcelableInputStreamImpl extends ParcelableInputStream.Stub {
    private static final ByteArray EOS = ByteArray.create(0);
    private static final String TAG = "anet.ParcelableInputStreamImpl";
    private int blockIndex;
    private int blockOffset;
    private int contentLength;
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private LinkedList<ByteArray> byteList = new LinkedList<>();
    private int rto = 10000;
    private String seqNo = "";
    final ReentrantLock lock = new ReentrantLock();
    final Condition newDataArrive = this.lock.newCondition();

    public void write(ByteArray byteArray) {
        if (this.isClosed.get()) {
            return;
        }
        this.lock.lock();
        try {
            this.byteList.add(byteArray);
            this.newDataArrive.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public void writeEnd() {
        write(EOS);
    }

    private void recycleCurrentItem() {
        this.lock.lock();
        try {
            this.byteList.set(this.blockIndex, EOS).recycle();
        } finally {
            this.lock.unlock();
        }
    }

    @Override // anetwork.channel.aidl.ParcelableInputStream
    public int available() throws RemoteException {
        if (this.isClosed.get()) {
            throw new RuntimeException("Stream is closed");
        }
        this.lock.lock();
        try {
            int dataLength = 0;
            if (this.blockIndex == this.byteList.size()) {
                return 0;
            }
            ListIterator<ByteArray> listIterator = this.byteList.listIterator(this.blockIndex);
            while (listIterator.hasNext()) {
                dataLength += listIterator.next().getDataLength();
            }
            return dataLength - this.blockOffset;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // anetwork.channel.aidl.ParcelableInputStream
    public void close() throws RemoteException {
        if (this.isClosed.compareAndSet(false, true)) {
            this.lock.lock();
            try {
                for (ByteArray byteArray : this.byteList) {
                    if (byteArray != EOS) {
                        byteArray.recycle();
                    }
                }
                this.byteList.clear();
                this.byteList = null;
                this.blockIndex = -1;
                this.blockOffset = -1;
                this.contentLength = 0;
            } finally {
                this.lock.unlock();
            }
        }
    }

    @Override // anetwork.channel.aidl.ParcelableInputStream
    public int readByte() throws RemoteException {
        byte b2;
        if (this.isClosed.get()) {
            throw new RuntimeException("Stream is closed");
        }
        this.lock.lock();
        while (true) {
            try {
                try {
                    if (this.blockIndex == this.byteList.size() && !this.newDataArrive.await(this.rto, TimeUnit.MILLISECONDS)) {
                        close();
                        throw new RuntimeException("await timeout.");
                    }
                    ByteArray byteArray = this.byteList.get(this.blockIndex);
                    if (byteArray == EOS) {
                        b2 = -1;
                        break;
                    }
                    if (this.blockOffset < byteArray.getDataLength()) {
                        b2 = byteArray.getBuffer()[this.blockOffset];
                        this.blockOffset++;
                        break;
                    }
                    recycleCurrentItem();
                    this.blockIndex++;
                    this.blockOffset = 0;
                } catch (InterruptedException unused) {
                    close();
                    throw new RuntimeException("await interrupt");
                }
            } finally {
                this.lock.unlock();
            }
        }
        return b2;
    }

    @Override // anetwork.channel.aidl.ParcelableInputStream
    public int readBytes(byte[] bArr, int i, int i2) throws RemoteException {
        int i3;
        if (this.isClosed.get()) {
            throw new RuntimeException("Stream is closed");
        }
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i < 0 || i2 < 0 || (i3 = i2 + i) > bArr.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.lock.lock();
        int i4 = i;
        while (i4 < i3) {
            try {
                try {
                    if (this.blockIndex == this.byteList.size() && !this.newDataArrive.await(this.rto, TimeUnit.MILLISECONDS)) {
                        close();
                        throw new RuntimeException("await timeout.");
                    }
                    ByteArray byteArray = this.byteList.get(this.blockIndex);
                    if (byteArray == EOS) {
                        break;
                    }
                    int dataLength = byteArray.getDataLength() - this.blockOffset;
                    int i5 = i3 - i4;
                    if (dataLength < i5) {
                        System.arraycopy(byteArray.getBuffer(), this.blockOffset, bArr, i4, dataLength);
                        i4 += dataLength;
                        recycleCurrentItem();
                        this.blockIndex++;
                        this.blockOffset = 0;
                    } else {
                        System.arraycopy(byteArray.getBuffer(), this.blockOffset, bArr, i4, i5);
                        this.blockOffset += i5;
                        i4 += i5;
                    }
                } catch (InterruptedException unused) {
                    close();
                    throw new RuntimeException("await interrupt");
                }
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }
        this.lock.unlock();
        int i6 = i4 - i;
        if (i6 > 0) {
            return i6;
        }
        return -1;
    }

    @Override // anetwork.channel.aidl.ParcelableInputStream
    public int read(byte[] bArr) throws RemoteException {
        return readBytes(bArr, 0, bArr.length);
    }

    @Override // anetwork.channel.aidl.ParcelableInputStream
    public long skip(int i) throws RemoteException {
        ByteArray byteArray;
        this.lock.lock();
        int i2 = 0;
        while (i2 < i) {
            try {
                if (this.blockIndex != this.byteList.size() && (byteArray = this.byteList.get(this.blockIndex)) != EOS) {
                    int dataLength = byteArray.getDataLength();
                    int i3 = i - i2;
                    if (dataLength - this.blockOffset < i3) {
                        i2 += dataLength - this.blockOffset;
                        recycleCurrentItem();
                        this.blockIndex++;
                        this.blockOffset = 0;
                    } else {
                        this.blockOffset += i3;
                        i2 = i;
                    }
                }
            } catch (Throwable th) {
                this.lock.unlock();
                throw th;
            }
        }
        this.lock.unlock();
        return i2;
    }

    @Override // anetwork.channel.aidl.ParcelableInputStream
    public int length() throws RemoteException {
        return this.contentLength;
    }

    public void init(g gVar, int i) {
        this.contentLength = i;
        this.seqNo = gVar.e;
        this.rto = gVar.f2047d;
    }
}
