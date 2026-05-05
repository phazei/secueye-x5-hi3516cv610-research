package bean;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class AreaCodeModel implements Serializable {
    private String en;
    private String name;
    private String price;
    private String shortName;
    private String tel;

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String str) {
        this.shortName = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getEn() {
        return this.en;
    }

    public void setEn(String str) {
        this.en = str;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String str) {
        this.tel = str;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String str) {
        this.price = str;
    }
}
