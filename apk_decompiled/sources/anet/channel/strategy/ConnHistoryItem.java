package anet.channel.strategy;

import java.io.Serializable;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class ConnHistoryItem implements Serializable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    byte f1838a = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    long f1839b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    long f1840c = 0;

    ConnHistoryItem() {
    }

    void a(boolean z) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - (z ? this.f1839b : this.f1840c) > 10000) {
            this.f1838a = (byte) ((this.f1838a << 1) | (!z ? 1 : 0));
            if (z) {
                this.f1839b = jCurrentTimeMillis;
            } else {
                this.f1840c = jCurrentTimeMillis;
            }
        }
    }

    int a() {
        int i = 0;
        for (int i2 = this.f1838a & 255; i2 > 0; i2 >>= 1) {
            i += i2 & 1;
        }
        return i;
    }

    boolean b() {
        return (this.f1838a & 1) == 1;
    }

    boolean c() {
        return a() >= 3 && System.currentTimeMillis() - this.f1840c <= 300000;
    }

    boolean d() {
        long j = this.f1839b;
        long j2 = this.f1840c;
        if (j <= j2) {
            j = j2;
        }
        return j != 0 && System.currentTimeMillis() - j > 86400000;
    }
}
