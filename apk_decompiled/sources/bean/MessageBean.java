package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MessageBean {
    private List<EventListBean> eventList;
    private boolean nextValid;

    public boolean isNextValid() {
        return this.nextValid;
    }

    public void setNextValid(boolean z) {
        this.nextValid = z;
    }

    public List<EventListBean> getEventList() {
        return this.eventList;
    }

    public void setEventList(List<EventListBean> list) {
        this.eventList = list;
    }

    public static class EventListBean {
        private String eventDesc;
        private String eventId;
        private String eventNickName;
        private String eventPicId;
        private String eventPicThumbUrl;
        private String eventPicUrl;
        private String eventTime;
        private String eventTimeUTC;
        private int eventType;

        public String getEventNickName() {
            return this.eventNickName;
        }

        public void setEventNickName(String str) {
            this.eventNickName = str;
        }

        public String getEventPicThumbUrl() {
            return this.eventPicThumbUrl;
        }

        public void setEventPicThumbUrl(String str) {
            this.eventPicThumbUrl = str;
        }

        public String getEventId() {
            return this.eventId;
        }

        public void setEventId(String str) {
            this.eventId = str;
        }

        public String getEventDesc() {
            return this.eventDesc;
        }

        public void setEventDesc(String str) {
            this.eventDesc = str;
        }

        public String getEventTime() {
            return this.eventTime;
        }

        public void setEventTime(String str) {
            this.eventTime = str;
        }

        public String getEventPicId() {
            return this.eventPicId;
        }

        public void setEventPicId(String str) {
            this.eventPicId = str;
        }

        public int getEventType() {
            return this.eventType;
        }

        public void setEventType(int i) {
            this.eventType = i;
        }

        public String getEventTimeUTC() {
            return this.eventTimeUTC;
        }

        public void setEventTimeUTC(String str) {
            this.eventTimeUTC = str;
        }

        public String getEventPicUrl() {
            return this.eventPicUrl;
        }

        public void setEventPicUrl(String str) {
            this.eventPicUrl = str;
        }
    }
}
