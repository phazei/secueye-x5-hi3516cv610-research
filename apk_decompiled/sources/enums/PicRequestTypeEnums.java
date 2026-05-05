package enums;

/* JADX INFO: loaded from: classes3.dex */
public enum PicRequestTypeEnums {
    THUMB(0, "缩略图"),
    ORIG(1, "原图"),
    ALL(2, "全部");

    private int code;
    private String desc;

    PicRequestTypeEnums(int i, String str) {
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
