package anet.channel.monitor;

import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ long f1782a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ long f1783b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ long f1784c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ b f1785d;

    d(b bVar, long j, long j2, long j3) {
        this.f1785d = bVar;
        this.f1782a = j;
        this.f1783b = j2;
        this.f1784c = j3;
    }

    @Override // java.lang.Runnable
    public void run() {
        b.f1776a++;
        b.e += this.f1782a;
        if (b.f1776a == 1) {
            b.f1779d = this.f1783b - this.f1784c;
        }
        if (b.f1776a >= 2 && b.f1776a <= 3) {
            if (this.f1784c >= b.f1778c) {
                b.f1779d += this.f1783b - this.f1784c;
            } else if (this.f1784c < b.f1778c && this.f1783b >= b.f1778c) {
                b.f1779d += this.f1783b - this.f1784c;
                b.f1779d -= b.f1778c - this.f1784c;
            }
        }
        b.f1777b = this.f1784c;
        b.f1778c = this.f1783b;
        if (b.f1776a == 3) {
            b.i = (long) this.f1785d.n.a(b.e, b.f1779d);
            b.f++;
            b.b(this.f1785d);
            if (b.f > 30) {
                this.f1785d.n.a();
                b.f = 3L;
            }
            double d2 = (b.i * 0.68d) + (b.h * 0.27d) + (b.g * 0.05d);
            b.g = b.h;
            b.h = b.i;
            if (b.i < b.g * 0.65d || b.i > b.g * 2.0d) {
                b.i = d2;
            }
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.BandWidthSampler", "NetworkSpeed", null, "mKalmanDataSize", Long.valueOf(b.e), "mKalmanTimeUsed", Long.valueOf(b.f1779d), "speed", Double.valueOf(b.i), "mSpeedKalmanCount", Long.valueOf(b.f));
            }
            if (this.f1785d.m > 5 || b.f == 2) {
                a.a().a(b.i);
                this.f1785d.m = 0;
                this.f1785d.l = b.i < b.j ? 1 : 5;
                ALog.i("awcn.BandWidthSampler", "NetworkSpeed notification!", null, "Send Network quality notification.");
            }
            b.f1779d = 0L;
            b.e = 0L;
            b.f1776a = 0;
        }
    }
}
