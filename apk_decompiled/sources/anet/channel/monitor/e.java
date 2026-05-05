package anet.channel.monitor;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private double f1787b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private double f1788c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private double f1789d;
    private double e;
    private double f;
    private double g;
    private double h;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private long f1786a = 0;
    private double i = 0.0d;
    private double j = 0.0d;
    private double k = 0.0d;

    e() {
    }

    public double a(double d2, double d3) {
        double d4 = d2 / d3;
        if (d4 < 8.0d) {
            if (this.f1786a != 0) {
                return this.k;
            }
            this.k = d4;
            return this.k;
        }
        long j = this.f1786a;
        if (j == 0) {
            this.i = d4;
            this.h = this.i;
            double d5 = this.h;
            this.f1789d = d5 * 0.1d;
            this.f1788c = 0.02d * d5;
            this.e = 0.1d * d5 * d5;
        } else if (j == 1) {
            this.j = d4;
            this.h = this.j;
        } else {
            double d6 = this.j;
            double d7 = d4 - d6;
            this.i = d6;
            this.j = d4;
            this.f1787b = d4 / 0.95d;
            this.g = this.f1787b - (this.h * 0.95d);
            char c2 = 0;
            double dSqrt = Math.sqrt(this.f1789d);
            double d8 = this.g;
            if (d8 >= 4.0d * dSqrt) {
                this.g = (d8 * 0.75d) + (dSqrt * 2.0d);
                c2 = 1;
            } else if (d8 <= (-4.0d) * dSqrt) {
                this.g = (dSqrt * (-1.0d)) + (d8 * 0.75d);
                c2 = 2;
            }
            double d9 = this.f1789d * 1.05d;
            double d10 = this.g;
            this.f1789d = Math.min(Math.max(Math.abs(d9 - ((0.0025d * d10) * d10)), this.f1789d * 0.8d), this.f1789d * 1.25d);
            double d11 = this.e;
            this.f = d11 / ((0.9025d * d11) + this.f1789d);
            this.h = this.h + (1.0526315789473684d * d7) + (this.f * this.g);
            if (c2 == 1) {
                this.h = Math.min(this.h, this.f1787b);
            } else if (c2 == 2) {
                this.h = Math.max(this.h, this.f1787b);
            }
            this.e = (1.0d - (0.95d * this.f)) * (this.e + this.f1788c);
        }
        double d12 = this.h;
        if (d12 < 0.0d) {
            this.k = this.j * 0.7d;
            this.h = this.k;
        } else {
            this.k = d12;
        }
        return this.k;
    }

    public void a() {
        this.f1786a = 0L;
        this.k = 0.0d;
    }
}
