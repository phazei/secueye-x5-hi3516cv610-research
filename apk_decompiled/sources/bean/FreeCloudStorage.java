package bean;

/* JADX INFO: loaded from: classes.dex */
public class FreeCloudStorage {
    private int consumed;
    private String endTime;
    private String endTimeUTC;
    private int expired;
    private int lifecycle;
    private int months;
    private int remainQuota;
    private String startTime;
    private String startTimeUTC;
    private int type;

    public int getLifecycle() {
        return this.lifecycle;
    }

    public void setLifecycle(int i) {
        this.lifecycle = i;
    }

    public int getConsumed() {
        return this.consumed;
    }

    public void setConsumed(int i) {
        this.consumed = i;
    }

    public String getEndTimeUTC() {
        return this.endTimeUTC;
    }

    public void setEndTimeUTC(String str) {
        this.endTimeUTC = str;
    }

    public int getMonths() {
        return this.months;
    }

    public void setMonths(int i) {
        this.months = i;
    }

    public int getExpired() {
        return this.expired;
    }

    public void setExpired(int i) {
        this.expired = i;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String str) {
        this.startTime = str;
    }

    public int getRemainQuota() {
        return this.remainQuota;
    }

    public void setRemainQuota(int i) {
        this.remainQuota = i;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String str) {
        this.endTime = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getStartTimeUTC() {
        return this.startTimeUTC;
    }

    public void setStartTimeUTC(String str) {
        this.startTimeUTC = str;
    }
}
