package anet.channel.statist;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class AlarmObject {
    public String arg;
    public String errorCode;
    public String errorMsg;
    public boolean isSuccess;
    public String module;
    public String modulePoint;

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("[module:");
        sb.append(this.module);
        sb.append(" modulePoint:");
        sb.append(this.modulePoint);
        sb.append(" arg:");
        sb.append(this.arg);
        sb.append(" isSuccess:");
        sb.append(this.isSuccess);
        sb.append(" errorCode:");
        sb.append(this.errorCode);
        sb.append("]");
        return sb.toString();
    }
}
