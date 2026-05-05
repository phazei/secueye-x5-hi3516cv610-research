package anet.channel.strategy.dispatch;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class DispatchEvent {
    public static final int DNSFAIL = 0;
    public static final int DNSSUCCESS = 1;
    public final int eventType;
    public final Object extraObject;

    public DispatchEvent(int i, Object obj) {
        this.eventType = i;
        this.extraObject = obj;
    }
}
