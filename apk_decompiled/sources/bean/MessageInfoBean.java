package bean;

/* JADX INFO: loaded from: classes.dex */
public class MessageInfoBean implements Comparable<MessageInfoBean> {
    public String bigPicUrl;
    public String body;
    public long gmtCreated;
    public long gmtModified;
    public long id;
    public String iotId;
    public String messageType;
    public String nickName;
    public String picUrl;
    public String pictureId;
    public String productName;
    public String title;

    public long getId() {
        return this.id;
    }

    @Override // java.lang.Comparable
    public int compareTo(MessageInfoBean messageInfoBean) {
        return Long.compare(this.gmtCreated, messageInfoBean.gmtCreated) * (-1);
    }

    public boolean equals(Object obj) {
        String str = this.picUrl;
        return str != null && str.equals(((MessageInfoBean) obj).picUrl);
    }
}
