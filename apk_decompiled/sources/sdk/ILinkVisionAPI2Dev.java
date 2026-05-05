package sdk;

import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;

/* JADX INFO: loaded from: classes4.dex */
public interface ILinkVisionAPI2Dev {
    void addEventRecordPlan2Dev(String str, int i, IoTCallback ioTCallback);

    void capture(IoTCallback ioTCallback);

    void deleteDevPictureFile(String str, long j, IoTCallback ioTCallback);

    void deleteEventRecordPlan2Dev(int i, IoTCallback ioTCallback);

    void getDevPictureFileById(String str, int i, IoTCallback ioTCallback);

    void getEventRecordPlan2Dev(IoTCallback ioTCallback);

    void queryDevPictureFileList(long j, long j2, int i, int i2, int i3, int i4, IoTCallback ioTCallback);

    void queryEventLst(long j, long j2, Integer num, int i, int i2, IoTCallback ioTCallback);

    void queryMonthVideos(String str, IoTCallback ioTCallback);

    void queryVideoLst(int i, int i2, int i3, int i4, int i5, int i6, IoTCallback ioTCallback);

    void updateEventRecordPlan2Dev(String str, int i, IoTCallback ioTCallback);
}
