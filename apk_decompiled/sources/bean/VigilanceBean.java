package bean;

/* JADX INFO: loaded from: classes.dex */
public class VigilanceBean {
    int num;
    String content = "";
    Boolean isCheck = false;
    String type = "";
    String name = "";
    Boolean edit = false;

    public Boolean getEdit() {
        return this.edit;
    }

    public void setEdit(Boolean bool) {
        this.edit = bool;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getContent() {
        return this.content;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int i) {
        this.num = i;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public Boolean getCheck() {
        return this.isCheck;
    }

    public void setCheck(Boolean bool) {
        this.isCheck = bool;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }
}
