package enums;

/* JADX INFO: loaded from: classes3.dex */
public enum ActionTypeEnum {
    LEFT(0, "向左"),
    RIGHT(1, "向右"),
    UP(2, "向上"),
    DOWN(3, "向下"),
    UP_LEFT(4, "向上向左"),
    UP_RIGHT(5, "向上向右"),
    DOWN_LEFT(6, "向下向左"),
    DOWN_RIGHT(7, "向下向右"),
    ZOOM_IN(8, "放大"),
    ZOOM_OUT(9, "缩小");

    private int code;
    private String desc;

    ActionTypeEnum(int i, String str) {
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
