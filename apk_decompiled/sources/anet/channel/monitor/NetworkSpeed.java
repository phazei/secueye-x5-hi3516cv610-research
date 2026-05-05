package anet.channel.monitor;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public enum NetworkSpeed {
    Slow("弱网络", 1),
    Fast("强网络", 5);


    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f1771a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f1772b;

    NetworkSpeed(String str, int i) {
        this.f1771a = str;
        this.f1772b = i;
    }

    public String getDesc() {
        return this.f1771a;
    }

    public int getCode() {
        return this.f1772b;
    }

    public static NetworkSpeed valueOfCode(int i) {
        return i == 1 ? Slow : Fast;
    }
}
