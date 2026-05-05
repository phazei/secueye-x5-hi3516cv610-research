package enums;

/* JADX INFO: loaded from: classes3.dex */
public enum SpeedEnum {
    SLOW(0, "慢速"),
    MEDIUM(1, "中速"),
    FAST(2, "快速");

    private int code;
    private String desc;

    SpeedEnum(int i, String str) {
        this.code = i;
        this.desc = str;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }
}
