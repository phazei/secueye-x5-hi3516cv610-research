package bean;

/* JADX INFO: loaded from: classes.dex */
public class PicInfo {
    private String iotId;
    private Long picCreateTime;
    private String picId;
    private String picUrl;
    private String thumbUrl;

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public Long getPicCreateTime() {
        return this.picCreateTime;
    }

    public void setPicCreateTime(Long l) {
        this.picCreateTime = l;
    }

    public String getPicId() {
        return this.picId;
    }

    public void setPicId(String str) {
        this.picId = str;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String str) {
        this.picUrl = str;
    }

    public String getThumbUrl() {
        return this.thumbUrl;
    }

    public void setThumbUrl(String str) {
        this.thumbUrl = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PicInfo picInfo = (PicInfo) obj;
        String str = this.iotId;
        if (str == null ? picInfo.iotId != null : !str.equals(picInfo.iotId)) {
            return false;
        }
        Long l = this.picCreateTime;
        if (l == null ? picInfo.picCreateTime != null : !l.equals(picInfo.picCreateTime)) {
            return false;
        }
        String str2 = this.picId;
        return str2 != null ? str2.equals(picInfo.picId) : picInfo.picId == null;
    }

    public int hashCode() {
        String str = this.iotId;
        int iHashCode = (str != null ? str.hashCode() : 0) * 31;
        Long l = this.picCreateTime;
        int iHashCode2 = (iHashCode + (l != null ? l.hashCode() : 0)) * 31;
        String str2 = this.picId;
        return iHashCode2 + (str2 != null ? str2.hashCode() : 0);
    }
}
