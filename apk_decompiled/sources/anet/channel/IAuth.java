package anet.channel;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface IAuth {

    /* JADX INFO: compiled from: Taobao */
    public interface AuthCallback {
        void onAuthFail(int i, String str);

        void onAuthSuccess();
    }

    void auth(Session session, AuthCallback authCallback);
}
