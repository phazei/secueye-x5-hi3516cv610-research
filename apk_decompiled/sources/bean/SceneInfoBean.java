package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SceneInfoBean {
    private List<DeviceInfoBean> actions;
    private Object conditions;
    private String description;
    private boolean enable;
    private String icon;
    private String id;
    private String name;
    private int status;
    private Object triggers;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean z) {
        this.enable = z;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String str) {
        this.icon = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public Object getTriggers() {
        return this.triggers;
    }

    public void setTriggers(Object obj) {
        this.triggers = obj;
    }

    public Object getConditions() {
        return this.conditions;
    }

    public void setConditions(Object obj) {
        this.conditions = obj;
    }

    public List<DeviceInfoBean> getActions() {
        return this.actions;
    }

    public void setActions(List<DeviceInfoBean> list) {
        this.actions = list;
    }
}
