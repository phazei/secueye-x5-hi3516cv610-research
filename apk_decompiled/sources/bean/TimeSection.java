package bean;

/* JADX INFO: loaded from: classes.dex */
public class TimeSection implements Comparable<TimeSection> {
    private int begin;
    private int end;
    private Integer mday;

    public Integer getMday() {
        return this.mday;
    }

    public void setMday(int i) {
        this.mday = Integer.valueOf(i);
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

    @Override // java.lang.Comparable
    public int compareTo(TimeSection timeSection) {
        return getMday().compareTo(timeSection.getMday());
    }
}
