package bean;

/* JADX INFO: loaded from: classes.dex */
public class PalettesDialogBean {
    private String color;
    private float[] hsv;
    private String title;

    public PalettesDialogBean(String str, String str2) {
        this.title = str;
        this.color = str2;
    }

    public float[] getHsv() {
        return this.hsv;
    }

    public void setHsv(float[] fArr) {
        this.hsv = fArr;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String str) {
        this.color = str;
    }
}
