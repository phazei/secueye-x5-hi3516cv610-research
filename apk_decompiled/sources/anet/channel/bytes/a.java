package anet.channel.bytes;

import android.support.v4.media.session.PlaybackStateCompat;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {
    public static final int MAX_POOL_SIZE = 524288;
    public static final String TAG = "awcn.ByteArrayPool";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final TreeSet<ByteArray> f1672a = new TreeSet<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ByteArray f1673b = ByteArray.create(0);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Random f1674c = new Random();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f1675d = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: anet.channel.bytes.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: Taobao */
    public static class C0170a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static a f1676a = new a();

        C0170a() {
        }
    }

    public synchronized void a(ByteArray byteArray) {
        ByteArray byteArrayPollLast;
        if (byteArray != null) {
            if (byteArray.bufferLength < 524288) {
                this.f1675d += (long) byteArray.bufferLength;
                this.f1672a.add(byteArray);
                while (this.f1675d > PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED) {
                    if (this.f1674c.nextBoolean()) {
                        byteArrayPollLast = this.f1672a.pollFirst();
                    } else {
                        byteArrayPollLast = this.f1672a.pollLast();
                    }
                    this.f1675d -= (long) byteArrayPollLast.bufferLength;
                }
            }
        }
    }

    public synchronized ByteArray a(int i) {
        if (i >= 524288) {
            return ByteArray.create(i);
        }
        this.f1673b.bufferLength = i;
        ByteArray byteArrayCeiling = this.f1672a.ceiling(this.f1673b);
        if (byteArrayCeiling == null) {
            byteArrayCeiling = ByteArray.create(i);
        } else {
            Arrays.fill(byteArrayCeiling.buffer, (byte) 0);
            byteArrayCeiling.dataLength = 0;
            this.f1672a.remove(byteArrayCeiling);
            this.f1675d -= (long) byteArrayCeiling.bufferLength;
        }
        return byteArrayCeiling;
    }

    public ByteArray a(byte[] bArr, int i) {
        ByteArray byteArrayA = a(i);
        System.arraycopy(bArr, 0, byteArrayA.buffer, 0, i);
        byteArrayA.dataLength = i;
        return byteArrayA;
    }
}
