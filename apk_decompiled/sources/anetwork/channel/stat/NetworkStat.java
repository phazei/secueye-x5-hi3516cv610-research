package anetwork.channel.stat;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class NetworkStat {
    public static INetworkStat getNetworkStat() {
        return NetworkStatCache.getInstance();
    }
}
