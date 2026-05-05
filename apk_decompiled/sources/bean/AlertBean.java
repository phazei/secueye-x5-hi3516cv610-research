package bean;

/* JADX INFO: loaded from: classes.dex */
public class AlertBean {
    private String eventId;
    private String eventName;
    private boolean noticeEnabled;

    public void setNoticeEnabled(boolean z) {
        this.noticeEnabled = z;
    }

    public boolean getNoticeEnabled() {
        return this.noticeEnabled;
    }

    public void setEventId(String str) {
        this.eventId = str;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventName(String str) {
        this.eventName = str;
    }

    public String getEventName() {
        return this.eventName;
    }
}
