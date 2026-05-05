package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ShareBean {
    private List<DataBean> data;
    private int maxResults;
    private int nextToken;

    public int getNextToken() {
        return this.nextToken;
    }

    public void setNextToken(int i) {
        this.nextToken = i;
    }

    public int getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(int i) {
        this.maxResults = i;
    }

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> list) {
        this.data = list;
    }

    public static class DataBean {
        private String body;
        private String deviceType;
        private long gmtCreate;
        private long gmtModified;
        private long id;
        private int isRead;
        private String keyId;
        private String messageId;
        private String messageType;
        private int tag;
        private String target;
        private String targetValue;
        private String title;
        private String type;

        public String getDeviceType() {
            return this.deviceType;
        }

        public void setDeviceType(String str) {
            this.deviceType = str;
        }

        public long getGmtModified() {
            return this.gmtModified;
        }

        public void setGmtModified(long j) {
            this.gmtModified = j;
        }

        public int getIsRead() {
            return this.isRead;
        }

        public void setIsRead(int i) {
            this.isRead = i;
        }

        public String getKeyId() {
            return this.keyId;
        }

        public void setKeyId(String str) {
            this.keyId = str;
        }

        public String getMessageId() {
            return this.messageId;
        }

        public void setMessageId(String str) {
            this.messageId = str;
        }

        public long getGmtCreate() {
            return this.gmtCreate;
        }

        public void setGmtCreate(long j) {
            this.gmtCreate = j;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public String getBody() {
            return this.body;
        }

        public void setBody(String str) {
            this.body = str;
        }

        public String getTarget() {
            return this.target;
        }

        public void setTarget(String str) {
            this.target = str;
        }

        public String getMessageType() {
            return this.messageType;
        }

        public void setMessageType(String str) {
            this.messageType = str;
        }

        public String getTargetValue() {
            return this.targetValue;
        }

        public void setTargetValue(String str) {
            this.targetValue = str;
        }

        public long getId() {
            return this.id;
        }

        public void setId(long j) {
            this.id = j;
        }

        public int getTag() {
            return this.tag;
        }

        public void setTag(int i) {
            this.tag = i;
        }
    }
}
