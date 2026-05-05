package bean;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AlarmPlanBean {
    public String iotId;
    public boolean isEdit;
    List<bean> list = new ArrayList();

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public boolean isEdit() {
        return this.isEdit;
    }

    public void setEdit(boolean z) {
        this.isEdit = z;
    }

    public List<bean> getList() {
        return this.list;
    }

    public void setList(List<bean> list) {
        this.list = list;
    }

    public static class bean {
        public int IsAcrossDay;
        public int WeekMask;
        public int Enable = 0;
        public int BeginTime = -1;
        public int EndTime = -1;

        public int isEnable() {
            return this.Enable;
        }

        public void setEnable(int i) {
            this.Enable = i;
        }

        public int isAcrossDay() {
            return this.IsAcrossDay;
        }

        public void setAcrossDay(int i) {
            this.IsAcrossDay = i;
        }

        public int getBeginTime() {
            return this.BeginTime;
        }

        public void setBeginTime(int i) {
            this.BeginTime = i;
        }

        public int getEndTime() {
            return this.EndTime;
        }

        public void setEndTime(int i) {
            this.EndTime = i;
        }

        public int getWeekMask() {
            return this.WeekMask;
        }

        public void setWeekMask(int i) {
            this.WeekMask = i;
        }
    }
}
