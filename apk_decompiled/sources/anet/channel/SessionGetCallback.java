package anet.channel;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface SessionGetCallback {
    void onSessionGetFail();

    void onSessionGetSuccess(Session session);
}
