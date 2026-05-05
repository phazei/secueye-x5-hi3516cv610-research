package anet.channel.session;

import anet.channel.security.ISecurity;
import anet.channel.util.ALog;
import org.android.spdy.AccsSSLCallback;
import org.android.spdy.SpdyProtocol;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class j implements AccsSSLCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ TnetSpdySession f1831a;

    j(TnetSpdySession tnetSpdySession) {
        this.f1831a = tnetSpdySession;
    }

    @Override // org.android.spdy.AccsSSLCallback
    public byte[] getSSLPublicKey(int i, byte[] bArr) {
        byte[] bArrDecrypt;
        try {
            bArrDecrypt = this.f1831a.G.decrypt(this.f1831a.f1632a, ISecurity.CIPHER_ALGORITHM_AES128, SpdyProtocol.TNET_PUBKEY_SG_KEY, bArr);
            if (bArrDecrypt != null) {
                try {
                    if (ALog.isPrintLog(2)) {
                        ALog.i("getSSLPublicKey", null, "decrypt", new String(bArrDecrypt));
                    }
                } catch (Throwable th) {
                    th = th;
                    ALog.e("awcn.TnetSpdySession", "getSSLPublicKey", null, th, new Object[0]);
                }
            }
        } catch (Throwable th2) {
            th = th2;
            bArrDecrypt = null;
        }
        return bArrDecrypt;
    }
}
