package anet.channel.entity;

import anet.channel.strategy.IConnStrategy;
import com.google.firebase.appindexing.Indexable;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final IConnStrategy f1734a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f1735b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1736c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f1737d;
    private String e;

    public a(String str, String str2, IConnStrategy iConnStrategy) {
        this.f1734a = iConnStrategy;
        this.f1737d = str;
        this.e = str2;
    }

    public String a() {
        IConnStrategy iConnStrategy = this.f1734a;
        if (iConnStrategy != null) {
            return iConnStrategy.getIp();
        }
        return null;
    }

    public int b() {
        IConnStrategy iConnStrategy = this.f1734a;
        if (iConnStrategy != null) {
            return iConnStrategy.getPort();
        }
        return 0;
    }

    public ConnType c() {
        IConnStrategy iConnStrategy = this.f1734a;
        if (iConnStrategy != null) {
            return ConnType.valueOf(iConnStrategy.getProtocol());
        }
        return ConnType.HTTP;
    }

    public int d() {
        IConnStrategy iConnStrategy = this.f1734a;
        return (iConnStrategy == null || iConnStrategy.getConnectionTimeout() == 0) ? Indexable.MAX_STRING_LENGTH : this.f1734a.getConnectionTimeout();
    }

    public int e() {
        IConnStrategy iConnStrategy = this.f1734a;
        return (iConnStrategy == null || iConnStrategy.getReadTimeout() == 0) ? Indexable.MAX_STRING_LENGTH : this.f1734a.getReadTimeout();
    }

    public String f() {
        return this.f1737d;
    }

    public int g() {
        IConnStrategy iConnStrategy = this.f1734a;
        if (iConnStrategy != null) {
            return iConnStrategy.getHeartbeat();
        }
        return 45000;
    }

    public String h() {
        return this.e;
    }

    public String toString() {
        return "ConnInfo [ip=" + a() + ",port=" + b() + ",type=" + c() + ",hb" + g() + "]";
    }
}
