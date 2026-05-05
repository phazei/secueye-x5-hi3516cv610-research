package anet.channel.detect;

import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.l;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class j implements IConnStrategy {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ l.e f1708a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ ConnProtocol f1709b;

    @Override // anet.channel.strategy.IConnStrategy
    public int getHeartbeat() {
        return 0;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getIpSource() {
        return 2;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getIpType() {
        return 1;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getRetryTimes() {
        return 0;
    }

    j(l.e eVar, ConnProtocol connProtocol) {
        this.f1708a = eVar;
        this.f1709b = connProtocol;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public String getIp() {
        return this.f1708a.f1915a;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getPort() {
        return this.f1708a.f1916b.f1901a;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public ConnProtocol getProtocol() {
        return this.f1709b;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getConnectionTimeout() {
        return this.f1708a.f1916b.f1903c;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getReadTimeout() {
        return this.f1708a.f1916b.f1904d;
    }
}
