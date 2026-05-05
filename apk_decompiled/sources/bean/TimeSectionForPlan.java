package bean;

import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public class TimeSectionForPlan implements Comparable<TimeSectionForPlan> {
    private int begin;
    private int end;
    private boolean isCheck;
    private Integer mDay;

    public Integer getMday() {
        return this.mDay;
    }

    public void setMday(int i) {
        this.mDay = Integer.valueOf(i);
    }

    public int getBegin() {
        return this.begin;
    }

    public void setBegin(int i) {
        this.begin = i;
    }

    public int getEnd() {
        return this.end;
    }

    public void setEnd(int i) {
        this.end = i;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean z) {
        this.isCheck = z;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull TimeSectionForPlan timeSectionForPlan) {
        return getMday().compareTo(timeSectionForPlan.getMday());
    }
}
