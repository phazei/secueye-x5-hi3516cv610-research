package anet.channel;

import anet.channel.security.ISecurity;
import anet.channel.strategy.dispatch.IAmdcSign;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class d implements IAmdcSign {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f1681a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ ISecurity f1682b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ SessionCenter f1683c;

    d(SessionCenter sessionCenter, String str, ISecurity iSecurity) {
        this.f1683c = sessionCenter;
        this.f1681a = str;
        this.f1682b = iSecurity;
    }

    @Override // anet.channel.strategy.dispatch.IAmdcSign
    public String getAppkey() {
        return this.f1681a;
    }

    @Override // anet.channel.strategy.dispatch.IAmdcSign
    public String sign(String str) {
        return this.f1682b.sign(this.f1683c.f1638b, ISecurity.SIGN_ALGORITHM_HMAC_SHA1, getAppkey(), str);
    }

    @Override // anet.channel.strategy.dispatch.IAmdcSign
    public boolean useSecurityGuard() {
        return !this.f1682b.isSecOff();
    }
}
