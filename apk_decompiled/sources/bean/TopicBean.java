package bean;

/* JADX INFO: loaded from: classes.dex */
public class TopicBean {
    private int icon;
    private boolean isSelect;
    private String title;

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }

    public TopicBean(int i, String str) {
        this.icon = i;
        this.title = str;
    }

    public TopicBean(int i, String str, boolean z) {
        this.icon = i;
        this.title = str;
        this.isSelect = z;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int i) {
        this.icon = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
