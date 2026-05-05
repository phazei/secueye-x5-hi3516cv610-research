package bean;

/* JADX INFO: loaded from: classes.dex */
public class PresetBean {
    private boolean canDelete;
    private String iotId;
    private boolean isEdit;
    private boolean isOwner;
    private boolean isSelected;
    private int position;
    private String string;

    public boolean isCanDelete() {
        return this.canDelete;
    }

    public void setCanDelete(boolean z) {
        this.canDelete = z;
    }

    public boolean isOwner() {
        return this.isOwner;
    }

    public void setOwner(boolean z) {
        this.isOwner = z;
    }

    public boolean isEdit() {
        return this.isEdit;
    }

    public void setEdit(boolean z) {
        this.isEdit = z;
    }

    public String getString() {
        return this.string;
    }

    public void setString(String str) {
        this.string = str;
    }

    public PresetBean(int i, String str) {
        this.position = i;
        this.iotId = str;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
