package bean;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class EventInfo {
    public List<Info> eventList = new ArrayList();

    public static class Info {
        public String eventData;
        public String eventDesc;
        public String eventFileName;
        public String eventId;
        public String eventPicId;
        public String eventPicThumbUrl;
        public String eventPicUrl;
        public long eventTime;
        public String eventTimeUTC;
        public int eventType;
        public String intelligentTypeList;
        public int tagList;
    }
}
