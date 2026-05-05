package bean;

import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public class VideoInfo implements Comparable<VideoInfo> {
    public String beginTime;
    public long dayTime;
    public String endTime;
    public String eventId;
    public String fileName;
    public int fileSize;
    public String iotId;
    public int recordType;
    public int streamType;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj != null && getClass() == obj.getClass() && this.iotId.equals(((VideoInfo) obj).iotId)) ? false : false;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull VideoInfo videoInfo) {
        return Long.compare(Long.parseLong(this.beginTime), Long.parseLong(videoInfo.beginTime));
    }

    public String toString() {
        return "VideoInfo{iotId='" + this.iotId + "', fileName='" + this.fileName + "', fileSize=" + this.fileSize + ", streamType=" + this.streamType + ", recordType=" + this.recordType + ", beginTime='" + this.beginTime + "', endTime='" + this.endTime + "', dayTime=" + this.dayTime + ", eventId='" + this.eventId + "'}";
    }
}
