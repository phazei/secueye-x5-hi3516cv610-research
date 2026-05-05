package anet.channel.heartbeat;

import anet.channel.Session;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface IHeartbeat {
    void reSchedule();

    void start(Session session);

    void stop();
}
