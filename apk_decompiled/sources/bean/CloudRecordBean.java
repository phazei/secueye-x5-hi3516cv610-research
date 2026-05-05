package bean;

/* JADX INFO: loaded from: classes.dex */
public class CloudRecordBean {
    public String desc;
    public String eventData;
    public String eventId;
    public int eventType;
    public String iotId;
    public String picCreateTime;
    public String picId;
    public String pictureTimeUTC;
    public String place;
    public String thumbUrl;
    public int tagList = 0;
    public boolean isShow = true;
    public boolean isSelect = false;

    public String toString() {
        return "CloudRecordBean{iotId='" + this.iotId + "', thumbUrl='" + this.thumbUrl + "', picCreateTime='" + this.picCreateTime + "', picId='" + this.picId + "', place='" + this.place + "', desc='" + this.desc + "', eventId='" + this.eventId + "', tagList=" + this.tagList + ", isShow=" + this.isShow + '}';
    }
}
