package anet.channel.heartbeat;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class HeartbeatManager {
    public static IHeartbeat getDefaultHeartbeat() {
        return new b();
    }

    public static IHeartbeat getDefaultBackgroundAccsHeartbeat() {
        return new a();
    }
}
