package bean;

/* JADX INFO: loaded from: classes.dex */
public class CameraNameModify {
    private String iotId;
    private String name;

    public CameraNameModify(String str, String str2) {
        this.name = str;
        this.iotId = str2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }
}
