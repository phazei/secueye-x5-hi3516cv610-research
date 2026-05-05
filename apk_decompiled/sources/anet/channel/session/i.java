package anet.channel.session;

import anet.channel.IAuth;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class i implements IAuth.AuthCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ TnetSpdySession f1830a;

    i(TnetSpdySession tnetSpdySession) {
        this.f1830a = tnetSpdySession;
    }

    @Override // anet.channel.IAuth.AuthCallback
    public void onAuthSuccess() {
        this.f1830a.notifyStatus(4, null);
        this.f1830a.z = System.currentTimeMillis();
        if (this.f1830a.D != null) {
            this.f1830a.D.start(this.f1830a);
        }
        this.f1830a.q.ret = 1;
        ALog.d("awcn.TnetSpdySession", "spdyOnStreamResponse", this.f1830a.p, "authTime", Long.valueOf(this.f1830a.q.authTime));
        if (this.f1830a.A > 0) {
            this.f1830a.q.authTime = System.currentTimeMillis() - this.f1830a.A;
        }
    }

    @Override // anet.channel.IAuth.AuthCallback
    public void onAuthFail(int i, String str) {
        this.f1830a.notifyStatus(5, null);
        if (this.f1830a.q != null) {
            this.f1830a.q.closeReason = "Accs_Auth_Fail:" + i;
            this.f1830a.q.errorCode = (long) i;
        }
        this.f1830a.close();
    }
}
