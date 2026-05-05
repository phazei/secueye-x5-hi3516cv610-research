package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TimeTemplateDTO {
    private int isAllDay;
    private int isDefault;
    private String name;
    private List<TimeSectionForPlan> timeSectionList;

    public int getIsAllDay() {
        return this.isAllDay;
    }

    public void setIsAllDay(int i) {
        this.isAllDay = i;
    }

    public int getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(int i) {
        this.isDefault = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<TimeSectionForPlan> getTimeSectionList() {
        return this.timeSectionList;
    }

    public void setTimeSectionList(List<TimeSectionForPlan> list) {
        this.timeSectionList = list;
    }
}
