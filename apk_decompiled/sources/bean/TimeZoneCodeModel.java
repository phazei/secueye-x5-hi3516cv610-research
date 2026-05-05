package bean;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class TimeZoneCodeModel implements Serializable {
    public boolean check;
    public String code;
    public String english;
    public String name;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getEnglish() {
        return this.english;
    }

    public void setEnglish(String str) {
        this.english = str;
    }

    public boolean isCheck() {
        return this.check;
    }

    public void setCheck(boolean z) {
        this.check = z;
    }
}
