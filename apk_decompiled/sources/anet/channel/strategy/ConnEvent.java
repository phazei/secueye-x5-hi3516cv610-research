package anet.channel.strategy;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class ConnEvent {
    public boolean isSuccess = false;
    public long connTime = Long.MAX_VALUE;
    public boolean isAccs = false;

    public String toString() {
        return this.isSuccess ? "ConnEvent#Success" : "ConnEvent#Fail";
    }
}
