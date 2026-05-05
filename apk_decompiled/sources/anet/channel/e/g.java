package anet.channel.e;

import anet.channel.entity.ConnType;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.IConnStrategy;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class g implements IConnStrategy {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ IConnStrategy f1733a;

    g(IConnStrategy iConnStrategy) {
        this.f1733a = iConnStrategy;
    }

    @Override // anet.channel.strategy.IConnStrategy
    public String getIp() {
        return this.f1733a.getIp();
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getIpType() {
        return this.f1733a.getIpType();
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getIpSource() {
        return this.f1733a.getIpSource();
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getPort() {
        return this.f1733a.getPort();
    }

    @Override // anet.channel.strategy.IConnStrategy
    public ConnProtocol getProtocol() {
        this.f1733a.getProtocol();
        return ConnProtocol.valueOf(ConnType.HTTP3_1RTT, null, null);
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getConnectionTimeout() {
        return this.f1733a.getConnectionTimeout();
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getReadTimeout() {
        return this.f1733a.getReadTimeout();
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getRetryTimes() {
        return this.f1733a.getRetryTimes();
    }

    @Override // anet.channel.strategy.IConnStrategy
    public int getHeartbeat() {
        return this.f1733a.getHeartbeat();
    }
}
