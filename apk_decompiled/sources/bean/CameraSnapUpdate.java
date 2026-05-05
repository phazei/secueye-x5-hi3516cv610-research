package bean;

/* JADX INFO: loaded from: classes.dex */
public class CameraSnapUpdate {
    private String iotId;
    private String updateName;
    private long updateTime;

    public CameraSnapUpdate(String str, String str2) {
        this.iotId = str;
        this.updateName = str2;
    }

    public CameraSnapUpdate(String str, long j) {
        this.iotId = str;
        this.updateTime = j;
    }

    public String getUpdateName() {
        return this.updateName;
    }

    public void setUpdateName(String str) {
        this.updateName = str;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long j) {
        this.updateTime = j;
    }
}
