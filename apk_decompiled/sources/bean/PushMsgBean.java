package bean;

/* JADX INFO: loaded from: classes.dex */
public class PushMsgBean {
    private String _ALIYUN_NOTIFICATION_ID_;
    private String iotId;
    private int strongReminder;

    public int getStrongReminder() {
        return this.strongReminder;
    }

    public void setStrongReminder(int i) {
        this.strongReminder = i;
    }

    public String get_ALIYUN_NOTIFICATION_ID_() {
        return this._ALIYUN_NOTIFICATION_ID_;
    }

    public void set_ALIYUN_NOTIFICATION_ID_(String str) {
        this._ALIYUN_NOTIFICATION_ID_ = str;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }
}
