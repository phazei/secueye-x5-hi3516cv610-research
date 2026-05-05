package bean;

/* JADX INFO: loaded from: classes.dex */
public class CloudInfo implements Comparable<CloudInfo> {
    public int actualBeginTime;
    public int actualEndTime;
    public int beginTime;
    public int endTime;
    public String fileName;
    public int fileSize;
    public String iotId;
    public boolean isBeginTimeLastDay;
    public boolean isEndTimeAfterDay;
    public int recordType;
    public int streamType;

    @Override // java.lang.Comparable
    public int compareTo(CloudInfo cloudInfo) {
        return Integer.compare(this.beginTime, cloudInfo.beginTime);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj == null || getClass() != obj.getClass()) ? false : false;
    }
}
