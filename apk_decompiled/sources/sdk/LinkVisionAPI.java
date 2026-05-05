package sdk;

import bean.DevPictureFile;
import bean.TimeSection;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import config.APIConstants;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tools.SpUtil;

/* JADX INFO: loaded from: classes4.dex */
public class LinkVisionAPI {
    private String version;

    private LinkVisionAPI() {
        this.version = "";
    }

    private static class LinkVisionAPIHolder {
        private static LinkVisionAPI linkVisionAPI = new LinkVisionAPI();

        private LinkVisionAPIHolder() {
        }
    }

    public static LinkVisionAPI getInstance() {
        return LinkVisionAPIHolder.linkVisionAPI;
    }

    public void init(String str) {
        this.version = str;
    }

    public void sendRequest(Map<String, Object> map, String str, IoTCallback ioTCallback) {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath(str).setApiVersion(this.version).setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), ioTCallback);
    }

    public void capture(String str, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        sendRequest(map, APIConstants.API_PATH_CAPTURE, ioTCallback);
    }

    public void createTimeTemplate(String str, boolean z, List<TimeSection> list, IoTCallback ioTCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", str);
        if (z) {
            map.put("allday", 1);
        } else {
            map.put("allday", 0);
        }
        LinkedList linkedList = new LinkedList();
        for (TimeSection timeSection : list) {
            HashMap map2 = new HashMap();
            map2.put("mDay", timeSection.getMday());
            map2.put("begin", Integer.valueOf(timeSection.getBegin()));
            map2.put(TtmlNode.END, Integer.valueOf(timeSection.getEnd()));
            linkedList.add(map2);
        }
        map.put("timeSectionsList", linkedList);
        sendRequest(map, APIConstants.API_PATH_TIME_TEMPLATE_SET, ioTCallback);
    }

    public void updateTimeTemplate(String str, String str2, boolean z, List<TimeSection> list, IoTCallback ioTCallback) {
        Map<String, Object> map = new HashMap<>();
        map.put("templateId", str);
        map.put("name", str2);
        if (z) {
            map.put("allday", 1);
        } else {
            map.put("allday", 0);
        }
        LinkedList linkedList = new LinkedList();
        for (TimeSection timeSection : list) {
            HashMap map2 = new HashMap();
            map2.put("mDay", timeSection.getMday());
            map2.put("begin", Integer.valueOf(timeSection.getBegin()));
            map2.put(TtmlNode.END, Integer.valueOf(timeSection.getEnd()));
            linkedList.add(map2);
        }
        map.put("timeSectionsList", linkedList);
        sendRequest(map, APIConstants.API_PATH_TIME_TEMPLATE_UPDATE, ioTCallback);
    }

    public void getTimeTemplate(String str, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("templateId", str);
        sendRequest(map, APIConstants.API_PATH_TIME_TEMPLATE_GET, ioTCallback);
    }

    public void deleteTimeTemplate(String str, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("templateId", str);
        sendRequest(map, APIConstants.API_PATH_TIME_TEMPLATE_DELETE, ioTCallback);
    }

    public void queryTimeTemplate(int i, int i2, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("pageStart", Integer.valueOf(i));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i2));
        sendRequest(map, APIConstants.API_PATH_TIME_TEMPLATE_QUERY, ioTCallback);
    }

    public void setRecordPlan(String str, String str2, int i, int i2, String str3, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("name", str);
        map.put("eventTypeList", str2);
        map.put("preRecordDuration", Integer.valueOf(i));
        map.put("recordDuration", Integer.valueOf(i2));
        map.put("templateId", str3);
        sendRequest(map, APIConstants.API_PATH_RECORD_PLAN_SET, ioTCallback);
    }

    public void updateRecordPlan(String str, String str2, String str3, int i, int i2, String str4, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("planId", str);
        map.put("name", str2);
        map.put("eventTypeList", str3);
        map.put("preRecordDuration", Integer.valueOf(i));
        map.put("recordDuration", Integer.valueOf(i2));
        map.put("templateId", str4);
        sendRequest(map, APIConstants.API_PATH_RECORD_PLAN_UPDATE, ioTCallback);
    }

    public void deleteRecordPlan(String str, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("planId", str);
        sendRequest(map, APIConstants.API_PATH_RECORD_PLAN_DELETE, ioTCallback);
    }

    public void getRecordPlan(String str, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("planId", str);
        sendRequest(map, APIConstants.API_PATH_RECORD_PLAN_GET, ioTCallback);
    }

    public void queryRecordPlan(int i, int i2, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("pageStart", Integer.valueOf(i));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i2));
        sendRequest(map, APIConstants.API_PATH_RECORD_PLAN_QUERY, ioTCallback);
    }

    public void getRecordPlan2Dev(String str, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        sendRequest(map, APIConstants.API_PATH_DEV_PLAN_GET, ioTCallback);
    }

    public void addRecordPlan2Dev(String str, String str2, int i, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("planId", str2);
        map.put("streamType", Integer.valueOf(i));
        sendRequest(map, APIConstants.API_PATH_DEV_PLAN_ADD, ioTCallback);
    }

    public void updateRecordPlan2Dev(String str, String str2, int i, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("planId", str2);
        map.put("streamType", Integer.valueOf(i));
        sendRequest(map, APIConstants.API_PATH_DEV_PLAN_UPDATE, ioTCallback);
    }

    public void deleteRecordPlan2Dev(String str, int i, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("streamType", Integer.valueOf(i));
        sendRequest(map, APIConstants.API_PATH_DEV_PLAN_DELETE, ioTCallback);
    }

    public void queryEventLst(String str, long j, long j2, Integer num, int i, int i2, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("beginTime", Long.valueOf(j));
        map.put(AUserTrack.UTKEY_END_TIME, Long.valueOf(j2));
        if (num != null) {
            map.put("eventType", num);
        }
        map.put("pageStart", Integer.valueOf(i));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i2));
        sendRequest(map, APIConstants.API_PATH_EVENT_LIST_QUERY, ioTCallback);
    }

    public void queryDevPictureFileList(String str, long j, long j2, int i, int i2, int i3, int i4, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put(AUserTrack.UTKEY_START_TIME, Long.valueOf(j));
        map.put(AUserTrack.UTKEY_END_TIME, Long.valueOf(j2));
        map.put("pageStart", Integer.valueOf(i));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i2));
        map.put("type", Integer.valueOf(i3));
        map.put("source", Integer.valueOf(i4));
        sendRequest(map, APIConstants.API_PATH_PICTURE_QUERY_FILE_LIST, ioTCallback);
    }

    public void deleteDevPictureFile(String str, String str2, long j, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("picId", str2);
        map.put("picCreateTime", Long.valueOf(j));
        sendRequest(map, APIConstants.API_PATH_PICTURE_DELETE_FILE, ioTCallback);
    }

    public void getDevPictureFileById(String str, String str2, int i, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("captureId", str2);
        map.put("type", Integer.valueOf(i));
        sendRequest(map, APIConstants.API_PATH_PICTURE_GET_BY_ID, ioTCallback);
    }

    public void batchDeleteDevPictureFile(List<DevPictureFile> list, IoTCallback ioTCallback) {
        LinkedList linkedList = new LinkedList();
        for (DevPictureFile devPictureFile : list) {
            HashMap map = new HashMap();
            map.put("iotId", devPictureFile.getIotId());
            map.put("picCreateTime", devPictureFile.getPicCreateTime());
            map.put("picId", devPictureFile.getPicId());
            linkedList.add(map);
        }
        Map<String, Object> map2 = new HashMap<>();
        map2.put("devPictureFileDTOList", linkedList);
        sendRequest(map2, APIConstants.API_PATH_PICTURE_DELETE_FILE_BATCH, ioTCallback);
    }

    public void queryVideoLst(String str, int i, int i2, int i3, int i4, int i5, int i6, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("streamType", Integer.valueOf(i));
        map.put("beginTime", Integer.valueOf(i2));
        map.put(AUserTrack.UTKEY_END_TIME, Integer.valueOf(i3));
        map.put("recordType", Integer.valueOf(i4));
        map.put("pageStart", Integer.valueOf(i5));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i6));
        sendRequest(map, APIConstants.API_PATH_VIDEO_LIST_QUERY, ioTCallback);
    }

    public void queryMonthVideos(String str, String str2, IoTCallback ioTCallback) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put(SpUtil.MONTH, str2);
        sendRequest(map, APIConstants.API_PATH_VIDEO_MONTH_QUERY, ioTCallback);
    }
}
