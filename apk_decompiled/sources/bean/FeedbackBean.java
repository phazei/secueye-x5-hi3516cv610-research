package bean;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackBean {
    public String appVersion;
    public String contact;
    public String content;
    public String devicename;
    public List<FeedBackReplyList> feedBackReplyList = new ArrayList();
    public int feedbackStatus;
    public long gmtCreate;
    public long gmtLastFeedback;
    public long gmtLastReply;
    public long gmtModified;
    public long id;
    public String iotId;
    public String isolationId;
    public String lastFeedback;
    public String lastReply;
    public int logReadableStatus;
    public String mobileModel;
    public String mobileSystem;
    public String productKey;
    public int replyStatus;
    public String tenantId;
    public String topic;
    public int type;
    public String uid;
}
