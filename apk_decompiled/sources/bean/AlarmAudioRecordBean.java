package bean;

/* JADX INFO: loaded from: classes.dex */
public class AlarmAudioRecordBean {
    public String AlarmName = "123";
    public int AlarmType = 0;
    public String FileName;
    public int FileType;

    public AlarmAudioRecordBean(int i, String str) {
        this.FileName = str;
        this.FileType = i;
    }

    public int getFileType() {
        return this.FileType;
    }

    public void setFileType(int i) {
        this.FileType = i;
    }

    public String getFileName() {
        return this.FileName;
    }

    public void setFileName(String str) {
        this.FileName = str;
    }

    public int getAlarmType() {
        return this.AlarmType;
    }

    public void setAlarmType(int i) {
        this.AlarmType = i;
    }

    public String getAlarmName() {
        return this.AlarmName;
    }

    public void setAlarmName(String str) {
        this.AlarmName = str;
    }
}
